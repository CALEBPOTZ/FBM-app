package com.marketplace.viewer.auth;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.browser.customtabs.CustomTabsIntent;

import com.marketplace.viewer.config.UrlConfig;

/**
 * Handles launching Facebook login via Chrome Custom Tabs.
 * Falls back to the default browser if Custom Tabs are not available.
 */
public final class CustomTabsAuthLauncher {
    
    private static final String TAG = "CustomTabsAuthLauncher";
    
    private final Context context;

    public CustomTabsAuthLauncher(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.context = context;
    }

    /**
     * Launches the Facebook login page in Chrome Custom Tabs.
     */
    public void launchFacebookLogin() {
        Log.d(TAG, "Launching Facebook login via Chrome Custom Tabs");
        
        try {
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .setUrlBarHidingEnabled(false)
                    .build();
            
            customTabsIntent.launchUrl(context, Uri.parse(UrlConfig.LOGIN_URL));
        } catch (Exception e) {
            Log.e(TAG, "Failed to launch Custom Tabs, falling back to browser", e);
            fallbackToBrowser(UrlConfig.LOGIN_URL);
        }
    }

    /**
     * Opens the URL in the default browser as a fallback.
     */
    private void fallbackToBrowser(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Failed to open browser", e);
        }
    }
}
