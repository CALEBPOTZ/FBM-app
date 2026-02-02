package com.marketplace.viewer.webview;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.marketplace.viewer.config.UrlConfig;

public class MarketplaceWebViewClient extends WebViewClient {
    private static final String TAG = "MarketplaceWebViewClient";
    
    private final Callbacks callbacks;

    public interface Callbacks {
        void onLoginRequired();
        void onMessengerLink(String url);
        void onPageLoadComplete(String url);
        void onError(String error);
    }

    public MarketplaceWebViewClient(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        Log.d(TAG, "shouldOverrideUrlLoading: " + url);
        
        if (UrlConfig.isFacebookScheme(url)) {
            Log.d(TAG, "Facebook scheme detected: " + url);
            if (callbacks != null) {
                callbacks.onMessengerLink(url);
            }
            return true;
        }
        
        if (UrlConfig.isLoginUrl(url)) {
            Log.d(TAG, "Login URL detected, loading in WebView");
            return false;
        }
        
        if (UrlConfig.isMessengerUrl(url)) {
            Log.d(TAG, "Messenger URL detected: " + url);
            if (callbacks != null) {
                callbacks.onMessengerLink(url);
            }
            return true;
        }
        
        if (UrlConfig.isMarketplaceUrl(url)) {
            Log.d(TAG, "Marketplace URL, loading internally");
            return false;
        }
        
        if (url.contains("facebook.com")) {
            Log.d(TAG, "Facebook URL, loading internally");
            return false;
        }
        
        Log.d(TAG, "External URL blocked: " + url);
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Log.d(TAG, "Page started loading: " + url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.d(TAG, "Page finished loading: " + url);
        
        if (callbacks != null) {
            callbacks.onPageLoadComplete(url);
        }
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        
        if (request.isForMainFrame()) {
            String errorMessage = "Error loading page";
            if (error != null) {
                errorMessage = "Error " + error.getErrorCode() + ": " + error.getDescription();
            }
            Log.e(TAG, "Page load error: " + errorMessage);
            
            if (callbacks != null) {
                callbacks.onError(errorMessage);
            }
        }
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
        Log.d(TAG, "History updated: " + url + " (reload=" + isReload + ")");
        
        if (UrlConfig.isMessengerUrl(url)) {
            Log.d(TAG, "Messenger URL detected in history update: " + url);
            if (callbacks != null) {
                callbacks.onMessengerLink(url);
            }
            // Go back to close the messenger view in WebView if possible
            // This prevents the user from being stuck in the web messenger
            if (view.canGoBack()) {
                view.goBack();
            }
        }
    }
}
