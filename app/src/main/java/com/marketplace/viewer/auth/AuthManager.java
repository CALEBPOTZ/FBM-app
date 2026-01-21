package com.marketplace.viewer.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;

import com.marketplace.viewer.config.UrlConfig;

/**
 * Manages authentication state for the Facebook Marketplace viewer.
 * Tracks login status via SharedPreferences and Facebook cookies.
 */
public final class AuthManager {
    
    private static final String TAG = "AuthManager";
    private static final String PREFS_NAME = "marketplace_auth";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_LAST_LOGIN_TIME = "last_login_time";
    
    private final SharedPreferences prefs;

    public AuthManager(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Checks if the user is currently logged in.
     * Returns true only if both the login flag is set and Facebook cookies exist.
     */
    public boolean isLoggedIn() {
        boolean hasLoginFlag = prefs.getBoolean(KEY_IS_LOGGED_IN, false);
        boolean hasCookies = checkFacebookCookies();
        boolean loggedIn = hasLoginFlag && hasCookies;
        Log.d(TAG, "Login status: " + loggedIn + " (flag=" + hasLoginFlag + ", cookies=" + hasCookies + ")");
        return loggedIn;
    }

    /**
     * Sets the logged in state.
     * If logging in, also records the current time as the last login time.
     */
    public void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        if (isLoggedIn) {
            editor.putLong(KEY_LAST_LOGIN_TIME, System.currentTimeMillis());
        }
        editor.apply();
        Log.d(TAG, "Login status updated: " + isLoggedIn);
    }

    /**
     * Checks if Facebook authentication cookies are present.
     */
    private boolean checkFacebookCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(UrlConfig.FACEBOOK_BASE);
        if (cookies == null) {
            cookies = "";
        }
        return cookies.contains("c_user") || cookies.contains("xs") || cookies.contains("datr");
    }

    /**
     * Logs out the user by clearing preferences and cookies.
     */
    public void logout() {
        prefs.edit().clear().apply();
        
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean success) {
                Log.d(TAG, "Cookies cleared: " + success);
            }
        });
        cookieManager.flush();
        
        Log.d(TAG, "User logged out");
    }

    /**
     * Returns the time in milliseconds since the last login.
     * Returns Long.MAX_VALUE if the user has never logged in.
     */
    public long getTimeSinceLastLogin() {
        long lastLoginTime = prefs.getLong(KEY_LAST_LOGIN_TIME, 0L);
        if (lastLoginTime > 0) {
            return System.currentTimeMillis() - lastLoginTime;
        }
        return Long.MAX_VALUE;
    }
}
