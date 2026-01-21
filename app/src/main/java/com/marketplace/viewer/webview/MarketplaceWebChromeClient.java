package com.marketplace.viewer.webview;

import android.net.Uri;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import androidx.constraintlayout.widget.ConstraintLayout;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: MarketplaceWebChromeClient.kt */
@Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 #2\u00020\u0001:\u0001#BI\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003\u0012.\u0010\u0006\u001a*\u0012\u0012\u0012\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t\u0018\u00010\b\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\t\u0012\u0004\u0012\u00020\f0\u0007¢\u0006\u0002\u0010\rJ$\u0010\u000e\u001a\u00020\u00052\b\u0010\u000f\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0010\u001a\u00020\u00042\b\u0010\u0011\u001a\u0004\u0018\u00010\u000bH\u0016J\u001c\u0010\u0012\u001a\u00020\u00052\b\u0010\u0013\u001a\u0004\u0018\u00010\u000b2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J0\u0010\u0016\u001a\u00020\f2\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u000b2\b\u0010\u000f\u001a\u0004\u0018\u00010\u000b2\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016J0\u0010\u001c\u001a\u00020\f2\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u000b2\b\u0010\u000f\u001a\u0004\u0018\u00010\u000b2\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016J\u001a\u0010\u0002\u001a\u00020\u00052\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u001d\u001a\u00020\u0004H\u0016J2\u0010\u001e\u001a\u00020\f2\b\u0010\u001f\u001a\u0004\u0018\u00010\u00182\u0014\u0010 \u001a\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t\u0018\u00010\b2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016R6\u0010\u0006\u001a*\u0012\u0012\u0012\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t\u0018\u00010\b\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\t\u0012\u0004\u0012\u00020\f0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006$"}, d2 = {"Lcom/marketplace/viewer/webview/MarketplaceWebChromeClient;", "Landroid/webkit/WebChromeClient;", "onProgressChanged", "Lkotlin/Function1;", "", "", "onFileChooser", "Lkotlin/Function2;", "Landroid/webkit/ValueCallback;", "", "Landroid/net/Uri;", "", "", "(Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)V", "onConsoleMessage", "message", "lineNumber", "sourceID", "onGeolocationPermissionsShowPrompt", "origin", "callback", "Landroid/webkit/GeolocationPermissions$Callback;", "onJsAlert", "view", "Landroid/webkit/WebView;", "url", "result", "Landroid/webkit/JsResult;", "onJsConfirm", "newProgress", "onShowFileChooser", "webView", "filePathCallback", "fileChooserParams", "Landroid/webkit/WebChromeClient$FileChooserParams;", "Companion", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes7.dex */
public final class MarketplaceWebChromeClient extends WebChromeClient {
    private static final String TAG = "MarketplaceWebChromeClient";
    private final Function2<ValueCallback<Uri[]>, String[], Boolean> onFileChooser;
    private final Function1<Integer, Unit> onProgressChanged;

    /* JADX WARN: Multi-variable type inference failed */
    public MarketplaceWebChromeClient(Function1<? super Integer, Unit> onProgressChanged, Function2<? super ValueCallback<Uri[]>, ? super String[], Boolean> onFileChooser) {
        Intrinsics.checkNotNullParameter(onProgressChanged, "onProgressChanged");
        Intrinsics.checkNotNullParameter(onFileChooser, "onFileChooser");
        this.onProgressChanged = onProgressChanged;
        this.onFileChooser = onFileChooser;
    }

    @Override // android.webkit.WebChromeClient
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        this.onProgressChanged.invoke(Integer.valueOf(newProgress));
    }

    @Override // android.webkit.WebChromeClient
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        Log.d(TAG, "JS Alert: " + message);
        return super.onJsAlert(view, url, message, result);
    }

    @Override // android.webkit.WebChromeClient
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        Log.d(TAG, "JS Confirm: " + message);
        return super.onJsConfirm(view, url, message, result);
    }

    @Override // android.webkit.WebChromeClient
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        Log.d(TAG, "Geolocation permission requested for: " + origin);
        if (callback != null) {
            callback.invoke(origin, true, false);
        }
    }

    @Override // android.webkit.WebChromeClient
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        Log.d(TAG, "File chooser requested");
        String[] acceptTypes = fileChooserParams != null ? fileChooserParams.getAcceptTypes() : null;
        if (acceptTypes == null) {
            acceptTypes = new String[]{"image/*"};
        }
        return this.onFileChooser.invoke(filePathCallback, acceptTypes).booleanValue();
    }

    @Override // android.webkit.WebChromeClient
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        Log.d(TAG, "Console: " + message + " (line " + lineNumber + ")");
        super.onConsoleMessage(message, lineNumber, sourceID);
    }
}
