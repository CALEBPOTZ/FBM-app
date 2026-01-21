package com.marketplace.viewer.config;

public class UrlConfig {
    public static final String FACEBOOK_BASE = "https://www.facebook.com";
    public static final String LOGIN_URL = "https://www.facebook.com/login";
    public static final String MARKETPLACE_URL = "https://www.facebook.com/marketplace";
    public static final String MESSENGER_URL = "https://www.facebook.com/messages";
    
    public static final String PATTERN_LOGIN = "facebook.com/login";
    public static final String PATTERN_MARKETPLACE = "facebook.com/marketplace";
    public static final String PATTERN_MESSAGES = "facebook.com/messages";
    public static final String PATTERN_MESSENGER = "messenger.com";
    
    public static final String SCHEME_FACEBOOK = "fb://";
    public static final String SCHEME_MESSENGER = "fb-messenger://";

    public static boolean isMarketplaceUrl(String url) {
        if (url == null) return false;
        return url.toLowerCase().contains(PATTERN_MARKETPLACE);
    }

    public static boolean isMessengerUrl(String url) {
        if (url == null) return false;
        String lowerUrl = url.toLowerCase();
        return lowerUrl.contains(PATTERN_MESSAGES) || lowerUrl.contains(PATTERN_MESSENGER);
    }

    public static boolean isLoginUrl(String url) {
        if (url == null) return false;
        return url.toLowerCase().contains(PATTERN_LOGIN);
    }

    public static boolean isFacebookScheme(String url) {
        if (url == null) return false;
        return url.startsWith(SCHEME_FACEBOOK) || url.startsWith(SCHEME_MESSENGER);
    }
}
