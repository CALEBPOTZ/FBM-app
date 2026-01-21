package com.marketplace.viewer.messenger;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.marketplace.viewer.R;
import com.marketplace.viewer.config.UrlConfig;

/**
 * Handles deep linking to Messenger and Facebook apps for messaging functionality.
 */
public final class MessengerDeepLinker {
    
    private static final String TAG = "MessengerDeepLinker";
    private static final String MESSENGER_PACKAGE = "com.facebook.orca";
    private static final String FACEBOOK_PACKAGE = "com.facebook.katana";
    
    private final Context context;

    public MessengerDeepLinker(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.context = context;
    }

    /**
     * Opens a Messenger-related link in the appropriate app or browser.
     */
    public void openMessengerLink(String url) {
        if (url == null) {
            Log.w(TAG, "URL is null");
            return;
        }
        
        Log.d(TAG, "Opening Messenger link: " + url);
        
        if (url.startsWith(UrlConfig.SCHEME_MESSENGER)) {
            openInMessengerApp(url);
        } else if (url.startsWith(UrlConfig.SCHEME_FACEBOOK)) {
            openInFacebookApp(url);
        } else if (url.contains(UrlConfig.PATTERN_MESSENGER) || url.contains(UrlConfig.PATTERN_MESSAGES)) {
            openMessengerWeb(url);
        } else {
            Log.w(TAG, "Unknown messenger URL format: " + url);
            openInBrowser(url);
        }
    }

    /**
     * Opens the URL in the Messenger app.
     */
    private void openInMessengerApp(String url) {
        if (isMessengerInstalled()) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                intent.setPackage(MESSENGER_PACKAGE);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Log.d(TAG, "Opened in Messenger app");
            } catch (Exception e) {
                Log.e(TAG, "Failed to open in Messenger app", e);
                showMessengerNotInstalledMessage();
            }
        } else {
            showMessengerNotInstalledMessage();
        }
    }

    /**
     * Opens the URL in the Facebook app.
     */
    private void openInFacebookApp(String url) {
        if (isFacebookInstalled()) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                intent.setPackage(FACEBOOK_PACKAGE);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Log.d(TAG, "Opened in Facebook app");
            } catch (Exception e) {
                Log.e(TAG, "Failed to open in Facebook app", e);
                openInBrowser(url);
            }
        } else {
            openInBrowser(url);
        }
    }

    /**
     * Opens a messenger web URL, trying the Messenger app first.
     */
    private void openMessengerWeb(String url) {
        if (isMessengerInstalled()) {
            String messengerUrl = convertToMessengerScheme(url);
            openInMessengerApp(messengerUrl);
        } else {
            openInBrowser(url);
        }
    }

    /**
     * Converts an https URL to a messenger scheme URL.
     */
    private String convertToMessengerScheme(String url) {
        return url.replace("https://", UrlConfig.SCHEME_MESSENGER);
    }

    /**
     * Opens the URL in the default browser.
     */
    private void openInBrowser(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            Log.d(TAG, "Opened in browser");
        } catch (Exception e) {
            Log.e(TAG, "Failed to open in browser", e);
        }
    }

    /**
     * Checks if the Messenger app is installed.
     */
    private boolean isMessengerInstalled() {
        return isPackageInstalled(MESSENGER_PACKAGE);
    }

    /**
     * Checks if the Facebook app is installed.
     */
    private boolean isFacebookInstalled() {
        return isPackageInstalled(FACEBOOK_PACKAGE);
    }

    /**
     * Checks if a package is installed on the device.
     */
    private boolean isPackageInstalled(String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Shows a toast message indicating Messenger is not installed.
     */
    private void showMessengerNotInstalledMessage() {
        Toast.makeText(context, context.getString(R.string.messenger_not_installed), Toast.LENGTH_SHORT).show();
    }
}
