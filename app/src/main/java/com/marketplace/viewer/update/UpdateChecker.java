package com.marketplace.viewer.update;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.marketplace.viewer.BuildConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
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

    public UpdateChecker(Context context) {
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void checkForUpdates() {
        // Check if we should skip this check (too recent)
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long lastCheck = prefs.getLong(KEY_LAST_CHECK, 0);
        if (System.currentTimeMillis() - lastCheck < CHECK_INTERVAL) {
            Log.d(TAG, "Skipping update check - too recent");
            return;
        }

        executor.execute(() -> {
            try {
                String response = fetchLatestRelease();
                if (response != null) {
                    parseAndCheckVersion(response, prefs);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error checking for updates", e);
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

    private void parseAndCheckVersion(String response, SharedPreferences prefs) {
        try {
            JSONObject release = new JSONObject(response);
            String tagName = release.getString("tag_name"); // e.g., "v1.0.0-20260121-090833"
            String releaseName = release.getString("name");
            String htmlUrl = release.getString("html_url");
            
            // Get download URL for APK
            String downloadUrl = null;
            JSONArray assets = release.getJSONArray("assets");
            for (int i = 0; i < assets.length(); i++) {
                JSONObject asset = assets.getJSONObject(i);
                String assetName = asset.getString("name");
                if (assetName.endsWith(".apk")) {
                    downloadUrl = asset.getString("browser_download_url");
                    break;
                }
            }

            // Compare versions
            String currentVersion = BuildConfig.VERSION_NAME;
            Log.d(TAG, "Current version: " + currentVersion + ", Latest: " + tagName);

            // Extract version numbers for comparison
            // Tag format: v1.0.0-20260121-090833
            // Current format: 1.0.0-build123 or just 1.0.0
            String latestVersion = tagName.startsWith("v") ? tagName.substring(1) : tagName;
            
            // Check if this version was skipped
            String skippedVersion = prefs.getString(KEY_SKIPPED_VERSION, "");
            if (latestVersion.equals(skippedVersion)) {
                Log.d(TAG, "Version " + latestVersion + " was skipped by user");
                return;
            }

            // Compare - if latest contains a timestamp that's different, it's newer
            if (!latestVersion.equals(currentVersion) && isNewer(latestVersion, currentVersion)) {
                String finalDownloadUrl = downloadUrl;
                mainHandler.post(() -> showUpdateDialog(latestVersion, releaseName, htmlUrl, finalDownloadUrl, prefs));
            }

            // Save last check time
            prefs.edit().putLong(KEY_LAST_CHECK, System.currentTimeMillis()).apply();

        } catch (Exception e) {
            Log.e(TAG, "Error parsing release", e);
        }
    }

    private boolean isNewer(String latest, String current) {
        // Simple comparison - if they're different and latest has a timestamp, assume newer
        // Format: 1.0.0-20260121-090833
        try {
            // Extract timestamp from latest (if present)
            if (latest.contains("-")) {
                String[] latestParts = latest.split("-");
                if (latestParts.length >= 2) {
                    String latestTimestamp = latestParts[1];
                    
                    // If current also has a timestamp, compare them
                    if (current.contains("-")) {
                        String[] currentParts = current.split("-");
                        if (currentParts.length >= 2) {
                            String currentTimestamp = currentParts[1];
                            return latestTimestamp.compareTo(currentTimestamp) > 0;
                        }
                    }
                    // Current doesn't have timestamp, so latest with timestamp is newer
                    return true;
                }
            }
            
            // Fall back to simple string comparison
            return !latest.equals(current);
        } catch (Exception e) {
            Log.e(TAG, "Error comparing versions", e);
            return false;
        }
    }

    private void showUpdateDialog(String version, String releaseName, String htmlUrl, String downloadUrl, SharedPreferences prefs) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Available");
        builder.setMessage("A new version of Facebook Marketplace Viewer is available!\n\n" +
                "Version: " + version + "\n\n" +
                "Would you like to download it now?");
        
        builder.setPositiveButton("Download", (dialog, which) -> {
            // Open download URL in browser
            String url = downloadUrl != null ? downloadUrl : htmlUrl;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        });
        
        builder.setNegativeButton("Later", (dialog, which) -> {
            dialog.dismiss();
        });
        
        builder.setNeutralButton("Skip Version", (dialog, which) -> {
            // Save this version as skipped
            prefs.edit().putString(KEY_SKIPPED_VERSION, version).apply();
            dialog.dismiss();
        });
        
        builder.setCancelable(true);
        builder.show();
    }
}
