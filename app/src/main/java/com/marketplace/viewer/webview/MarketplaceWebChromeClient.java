package com.marketplace.viewer.webview;

import android.net.Uri;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MarketplaceWebChromeClient extends WebChromeClient {
    private static final String TAG = "MarketplaceWebChromeClient";
    
    private final Callbacks callbacks;

    public interface Callbacks {
        void onProgressChanged(int progress);
        boolean onFileChooser(ValueCallback<Uri[]> callback, String[] acceptTypes);
    }

    public MarketplaceWebChromeClient(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (callbacks != null) {
            callbacks.onProgressChanged(newProgress);
        }
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        Log.d(TAG, "JS Alert: " + message);
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        Log.d(TAG, "JS Confirm: " + message);
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        Log.d(TAG, "Geolocation permission requested for: " + origin);
        if (callback != null) {
            callback.invoke(origin, true, false);
        }
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        Log.d(TAG, "File chooser requested");
        
        String[] acceptTypes = null;
        if (fileChooserParams != null) {
            acceptTypes = fileChooserParams.getAcceptTypes();
        }
        if (acceptTypes == null || acceptTypes.length == 0) {
            acceptTypes = new String[]{"image/*"};
        }
        
        if (callbacks != null) {
            return callbacks.onFileChooser(filePathCallback, acceptTypes);
        }
        return false;
    }

    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        Log.d(TAG, "Console: " + message + " (line " + lineNumber + ")");
        super.onConsoleMessage(message, lineNumber, sourceID);
    }
}
