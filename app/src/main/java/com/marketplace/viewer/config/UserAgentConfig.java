package com.marketplace.viewer.config;

/**
 * Configuration for user agent strings used by the WebView.
 */
public final class UserAgentConfig {
    
    public static final String CHROME_DESKTOP = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    public static final String CHROME_MOBILE = "Mozilla/5.0 (Linux; Android 14; Pixel 8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36";

    private UserAgentConfig() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns the user agent string to use for the WebView.
     * Uses the desktop Chrome user agent by default.
     */
    public static String getUserAgent() {
        return CHROME_DESKTOP;
    }
}
