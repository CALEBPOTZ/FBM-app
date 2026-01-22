package com.marketplace.viewer.update;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.marketplace.viewer.BuildConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateChecker {
    private static final String TAG = "UpdateChecker";
    private static final String GITHUB_API_URL = "https://api.github.com/repos/CALEBPOTZ/FBM-app/releases/latest";
    private static final String PREFS_NAME = "update_prefs";
    private static final String KEY_SKIPPED_VERSION = "skipped_version";
    private static final String KEY_LAST_CHECK = "last_check";
    private static final long CHECK_INTERVAL = 6 * 60 * 60 * 1000; // 6 hours

    private final Context context;
    private final ExecutorService executor;
    private final Handler mainHandler;
    private ProgressDialog progressDialog;
    private UpdateCallback callback;

    public interface UpdateCallback {
        void onUpdateCheckComplete(boolean updateAvailable, String version);
        void onUpdateDownloadProgress(int progress);
        void onUpdateDownloadComplete(boolean success);
        void onNoUpdateAvailable();
    }

    public UpdateChecker(Context context) {
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void setCallback(UpdateCallback callback) {
        this.callback = callback;
    }

    public void checkForUpdates() {
        checkForUpdates(false);
    }
    
    public void checkForUpdates(boolean force) {
        // Check if we should skip this check (too recent)
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long lastCheck = prefs.getLong(KEY_LAST_CHECK, 0);
        
        if (!force && System.currentTimeMillis() - lastCheck < CHECK_INTERVAL) {
            Log.d(TAG, "Skipping update check - too recent");
            if (callback != null) {
                mainHandler.post(() -> callback.onNoUpdateAvailable());
            }
            return;
        }
        
        Log.d(TAG, "Checking for updates... Current version: " + BuildConfig.VERSION_NAME);

        executor.execute(() -> {
            try {
                String response = fetchLatestRelease();
                if (response != null) {
                    parseAndCheckVersion(response, prefs, force);
                } else {
                    Log.e(TAG, "Failed to fetch latest release");
                    if (force) {
                        mainHandler.post(() -> Toast.makeText(context, "Failed to check for updates", Toast.LENGTH_SHORT).show());
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error checking for updates", e);
                if (force) {
                    mainHandler.post(() -> Toast.makeText(context, "Error checking for updates", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    // Manual check triggered from UI - always shows result
    public void checkForUpdatesManually() {
        Log.d(TAG, "Manual update check triggered");
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        
        mainHandler.post(() -> Toast.makeText(context, "Checking for updates...", Toast.LENGTH_SHORT).show());

        executor.execute(() -> {
            try {
                String response = fetchLatestRelease();
                if (response != null) {
                    parseAndCheckVersion(response, prefs, true);
                } else {
                    Log.e(TAG, "Failed to fetch latest release");
                    mainHandler.post(() -> Toast.makeText(context, "Failed to check for updates. Please try again.", Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error checking for updates", e);
                mainHandler.post(() -> Toast.makeText(context, "Error checking for updates: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    private String fetchLatestRelease() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(GITHUB_API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching release", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    private void parseAndCheckVersion(String response, SharedPreferences prefs, boolean isManualCheck) {
        try {
            JSONObject release = new JSONObject(response);
            String tagName = release.getString("tag_name");
            String releaseName = release.getString("name");
            String htmlUrl = release.getString("html_url");
            String releaseNotes = release.optString("body", "");
            
            // Get download URL for APK
            String downloadUrl = null;
            long fileSize = 0;
            JSONArray assets = release.getJSONArray("assets");
            for (int i = 0; i < assets.length(); i++) {
                JSONObject asset = assets.getJSONObject(i);
                String assetName = asset.getString("name");
                if (assetName.endsWith(".apk")) {
                    downloadUrl = asset.getString("browser_download_url");
                    fileSize = asset.getLong("size");
                    break;
                }
            }

            // Compare versions
            String currentVersion = BuildConfig.VERSION_NAME;
            Log.d(TAG, "Current version: " + currentVersion + ", Latest tag: " + tagName);

            String latestVersion = tagName.startsWith("v") ? tagName.substring(1) : tagName;
            Log.d(TAG, "Parsed latest version: " + latestVersion);
            
            // Check if this version was skipped (only for automatic checks)
            String skippedVersion = prefs.getString(KEY_SKIPPED_VERSION, "");
            if (!isManualCheck && latestVersion.equals(skippedVersion)) {
                Log.d(TAG, "Version " + latestVersion + " was skipped by user");
                return;
            }

            boolean isUpdateAvailable = !latestVersion.equals(currentVersion) && isNewer(latestVersion, currentVersion);
            Log.d(TAG, "Update available: " + isUpdateAvailable);

            // Save last check time
            prefs.edit().putLong(KEY_LAST_CHECK, System.currentTimeMillis()).apply();
            
            if (isUpdateAvailable) {
                String finalDownloadUrl = downloadUrl;
                long finalFileSize = fileSize;
                mainHandler.post(() -> {
                    if (callback != null) {
                        callback.onUpdateCheckComplete(true, latestVersion);
                    }
                    showUpdateDialog(latestVersion, releaseName, releaseNotes, htmlUrl, finalDownloadUrl, finalFileSize, prefs);
                });
            } else {
                mainHandler.post(() -> {
                    if (callback != null) {
                        callback.onNoUpdateAvailable();
                    }
                    if (isManualCheck) {
                        Toast.makeText(context, "You're running the latest version (" + currentVersion + ")", Toast.LENGTH_LONG).show();
                    }
                });
            }

        } catch (Exception e) {
            Log.e(TAG, "Error parsing release", e);
        }
    }

    private boolean isNewer(String latest, String current) {
        Log.d(TAG, "Comparing versions - Latest: " + latest + ", Current: " + current);
        
        if (latest.equals(current)) {
            Log.d(TAG, "Versions are equal");
            return false;
        }
        
        try {
            if (latest.contains("-")) {
                String[] latestParts = latest.split("-");
                if (latestParts.length >= 2) {
                    String latestTimestamp = latestParts[1];
                    
                    if (current.contains("-")) {
                        String[] currentParts = current.split("-");
                        if (currentParts.length >= 2) {
                            String currentTimestamp = currentParts[1];
                            boolean newer = latestTimestamp.compareTo(currentTimestamp) > 0;
                            Log.d(TAG, "Timestamp comparison: " + latestTimestamp + " vs " + currentTimestamp + " = " + newer);
                            return newer;
                        }
                    }
                    Log.d(TAG, "Current has no timestamp, latest is newer");
                    return true;
                }
            }
            
            Log.d(TAG, "Falling back to string comparison");
            return !latest.equals(current);
        } catch (Exception e) {
            Log.e(TAG, "Error comparing versions", e);
            return false;
        }
    }

    private void showUpdateDialog(String version, String releaseName, String releaseNotes, String htmlUrl, String downloadUrl, long fileSize, SharedPreferences prefs) {
        String fileSizeStr = fileSize > 0 ? String.format(" (%.1f MB)", fileSize / (1024.0 * 1024.0)) : "";
        
        String message = "A new version is available!\n\n" +
                "Version: " + version + fileSizeStr + "\n";
        
        if (releaseNotes != null && !releaseNotes.isEmpty()) {
            // Truncate release notes if too long
            String notes = releaseNotes.length() > 200 ? releaseNotes.substring(0, 200) + "..." : releaseNotes;
            message += "\nWhat's new:\n" + notes;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Available");
        builder.setMessage(message);
        
        if (downloadUrl != null) {
            builder.setPositiveButton("Download & Install", (dialog, which) -> {
                if (canInstallApks()) {
                    downloadAndInstallApk(downloadUrl, version);
                } else {
                    requestInstallPermission();
                }
            });
        }
        
        builder.setNegativeButton("Later", (dialog, which) -> {
            dialog.dismiss();
        });
        
        builder.setNeutralButton("Skip Version", (dialog, which) -> {
            prefs.edit().putString(KEY_SKIPPED_VERSION, version).apply();
            Toast.makeText(context, "Version " + version + " will be skipped", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        
        builder.setCancelable(true);
        builder.show();
    }

    private boolean canInstallApks() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return context.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    private void requestInstallPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Permission Required");
            builder.setMessage("To install updates, please allow this app to install unknown apps.\n\nYou'll be taken to settings to enable this permission.");
            builder.setPositiveButton("Open Settings", (dialog, which) -> {
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        }
    }

    private void downloadAndInstallApk(String downloadUrl, String version) {
        // Show progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Downloading Update");
        progressDialog.setMessage("Downloading version " + version + "...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> {
            dialog.dismiss();
        });
        progressDialog.show();

        executor.execute(() -> {
            HttpURLConnection connection = null;
            InputStream input = null;
            FileOutputStream output = null;
            
            try {
                URL url = new URL(downloadUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(30000);
                connection.connect();

                int responseCode = connection.getResponseCode();
                
                // Handle redirect
                if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || 
                    responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
                    responseCode == 302 || responseCode == 307) {
                    String redirectUrl = connection.getHeaderField("Location");
                    connection.disconnect();
                    url = new URL(redirectUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(30000);
                    connection.setReadTimeout(30000);
                    connection.connect();
                }

                int fileLength = connection.getContentLength();
                
                // Create download directory
                File downloadDir = new File(context.getExternalFilesDir(null), "downloads");
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs();
                }
                
                // Delete old APK files
                File[] oldFiles = downloadDir.listFiles((dir, name) -> name.endsWith(".apk"));
                if (oldFiles != null) {
                    for (File oldFile : oldFiles) {
                        oldFile.delete();
                    }
                }
                
                File apkFile = new File(downloadDir, "update-" + version + ".apk");
                
                input = new BufferedInputStream(connection.getInputStream());
                output = new FileOutputStream(apkFile);

                byte[] buffer = new byte[8192];
                long total = 0;
                int count;
                
                while ((count = input.read(buffer)) != -1) {
                    if (progressDialog == null || !progressDialog.isShowing()) {
                        // Download cancelled
                        throw new Exception("Download cancelled");
                    }
                    
                    total += count;
                    output.write(buffer, 0, count);
                    
                    if (fileLength > 0) {
                        final int progress = (int) (total * 100 / fileLength);
                        mainHandler.post(() -> {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.setProgress(progress);
                                progressDialog.setMessage("Downloading... " + progress + "%");
                            }
                            if (callback != null) {
                                callback.onUpdateDownloadProgress(progress);
                            }
                        });
                    }
                }
                
                output.flush();
                output.close();
                input.close();
                
                Log.d(TAG, "Download complete: " + apkFile.getAbsolutePath());
                
                mainHandler.post(() -> {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (callback != null) {
                        callback.onUpdateDownloadComplete(true);
                    }
                    installApk(apkFile);
                });
                
            } catch (Exception e) {
                Log.e(TAG, "Error downloading APK", e);
                mainHandler.post(() -> {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (callback != null) {
                        callback.onUpdateDownloadComplete(false);
                    }
                    if (!e.getMessage().equals("Download cancelled")) {
                        Toast.makeText(context, "Download failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } finally {
                try {
                    if (output != null) output.close();
                    if (input != null) input.close();
                } catch (Exception ignored) {}
                if (connection != null) connection.disconnect();
            }
        });
    }

    private void installApk(File apkFile) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri apkUri;
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                apkUri = FileProvider.getUriForFile(context, 
                    context.getPackageName() + ".fileprovider", 
                    apkFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                apkUri = Uri.fromFile(apkFile);
            }
            
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            context.startActivity(intent);
            
        } catch (Exception e) {
            Log.e(TAG, "Error installing APK", e);
            Toast.makeText(context, "Failed to install update: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public String getCurrentVersion() {
        return BuildConfig.VERSION_NAME;
    }
}
