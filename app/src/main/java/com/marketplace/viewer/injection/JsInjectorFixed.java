package com.marketplace.viewer.injection;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.webkit.WebView;

import com.marketplace.viewer.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class JsInjectorFixed {
    private static final String TAG = "JsInjector";
    private final WebView webView;
    private final boolean isOverlay;

    public JsInjectorFixed(WebView webView) {
        this(webView, false);
    }

    public JsInjectorFixed(WebView webView, boolean isOverlay) {
        this.webView = webView;
        this.isOverlay = isOverlay;
    }

    public void injectMarketplaceEnhancements() {
        Log.d(TAG, "Injecting marketplace enhancements (overlay=" + isOverlay + ")");
        runAsset("js/preconnect.js");
        runAsset("js/enhancements.js");
        runAsset("js/feed-grid-fix.js");
        runAsset("js/scroll-fix.js");
        runAsset("js/image-viewer.js");
        runAsset("js/textarea-fix.js");
        if (isOverlay) return;
        runAsset("js/saved-button.js");
        runAsset("js/search-enter-key.js");
        runAsset("js/scroll-to-top.js");
        runAsset("js/side-nav.js");
        runAsset("js/scroll-persistence.js");
        runAsset("js/listing-interceptor.js");
    }

    public void injectOverlayEnhancements() {
        injectMarketplaceEnhancements();
    }

    public void injectAntiDetection() {
        runAsset("js/anti-detection.js");
    }

    public void injectCustomScript(String script) {
        Log.d(TAG, "Injecting custom script");
        webView.evaluateJavascript(script, null);
    }

    private void runAsset(String path) {
        String script = readAsset(path);
        if (script == null) return;
        if (path.endsWith("enhancements.js")) {
            script = script.replace("__DEBUG__", BuildConfig.DEBUG ? "true" : "false");
        }
        webView.evaluateJavascript(script, null);
    }

    private String readAsset(String path) {
        Context context = webView.getContext();
        AssetManager assets = context.getAssets();
        try (InputStream in = assets.open(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        } catch (IOException e) {
            Log.e(TAG, "Failed to read asset " + path, e);
            return null;
        }
    }
}
