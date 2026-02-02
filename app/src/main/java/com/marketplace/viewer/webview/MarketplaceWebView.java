package com.marketplace.viewer.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.marketplace.viewer.config.UserAgentConfig;

/**
 * Custom WebView configured for the Facebook Marketplace viewer.
 * Sets up JavaScript, cookies, and other web settings.
 */
public final class MarketplaceWebView extends WebView {

    public MarketplaceWebView(Context context) {
        this(context, null, 0);
    }

    public MarketplaceWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarketplaceWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        setupWebView();
        setupCookies();
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    /**
     * Configures WebView settings for optimal Marketplace experience.
     */
    private void setupWebView() {
        WebSettings settings = getSettings();
        
        // Enable JavaScript and DOM storage
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        
        // Set user agent
        settings.setUserAgentString(UserAgentConfig.getUserAgent());
        
        // Cache settings
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        
        // Security settings
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        
        // Media settings
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setSupportMultipleWindows(true);
        
        // Zoom settings (disable zoom for better Facebook experience)
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        
        // Form and image settings
        settings.setSaveFormData(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setBlockNetworkImage(false);
        settings.setBlockNetworkLoads(false);
        
        // Geolocation
        settings.setGeolocationEnabled(true);
        
        // File access (restricted for security)
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        
        // Hardware acceleration
        setLayerType(LAYER_TYPE_HARDWARE, null);
        
        // Enable WebView debugging (allows Chrome DevTools inspection)
        WebView.setWebContentsDebuggingEnabled(true);
    }

    /**
     * Configures cookie settings for Facebook authentication.
     */
    private void setupCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(this, true);
        cookieManager.flush();
    }

    /**
     * Clears all WebView data including cache, cookies, and history.
     */
    public void clearAllData() {
        clearCache(true);
        clearFormData();
        clearHistory();
        
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(null);
        cookieManager.flush();
    }
    
    /**
     * Enables WebView debugging (for development builds).
     */
    public static void enableDebugging(boolean enable) {
        WebView.setWebContentsDebuggingEnabled(enable);
    }
}
