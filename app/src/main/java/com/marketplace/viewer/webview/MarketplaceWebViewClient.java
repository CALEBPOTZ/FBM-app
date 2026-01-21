package com.marketplace.viewer.webview;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.marketplace.viewer.config.UrlConfig;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* compiled from: MarketplaceWebViewClient.kt */
@Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bBO\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00040\u0006\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00040\u0006\u0012\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00040\u0006¢\u0006\u0002\u0010\nJ$\u0010\u000b\u001a\u00020\u00042\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0018\u0010\u0011\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0007H\u0016J&\u0010\u0012\u001a\u00020\u00042\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u00072\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016J \u0010\u0015\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u0018\u0010\u001a\u001a\u00020\u00102\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0016\u001a\u00020\u0017H\u0016R\u001a\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00040\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00040\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00040\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001c"}, d2 = {"Lcom/marketplace/viewer/webview/MarketplaceWebViewClient;", "Landroid/webkit/WebViewClient;", "onLoginRequired", "Lkotlin/Function0;", "", "onMessengerLink", "Lkotlin/Function1;", "", "onPageLoadComplete", "onError", "(Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "doUpdateVisitedHistory", "view", "Landroid/webkit/WebView;", "url", "isReload", "", "onPageFinished", "onPageStarted", "favicon", "Landroid/graphics/Bitmap;", "onReceivedError", "request", "Landroid/webkit/WebResourceRequest;", "error", "Landroid/webkit/WebResourceError;", "shouldOverrideUrlLoading", "Companion", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes7.dex */
public final class MarketplaceWebViewClient extends WebViewClient {
    private static final String TAG = "MarketplaceWebViewClient";
    private final Function1<String, Unit> onError;
    private final Function0<Unit> onLoginRequired;
    private final Function1<String, Unit> onMessengerLink;
    private final Function1<String, Unit> onPageLoadComplete;

    /* JADX WARN: Multi-variable type inference failed */
    public MarketplaceWebViewClient(Function0<Unit> onLoginRequired, Function1<? super String, Unit> onMessengerLink, Function1<? super String, Unit> onPageLoadComplete, Function1<? super String, Unit> onError) {
        Intrinsics.checkNotNullParameter(onLoginRequired, "onLoginRequired");
        Intrinsics.checkNotNullParameter(onMessengerLink, "onMessengerLink");
        Intrinsics.checkNotNullParameter(onPageLoadComplete, "onPageLoadComplete");
        Intrinsics.checkNotNullParameter(onError, "onError");
        this.onLoginRequired = onLoginRequired;
        this.onMessengerLink = onMessengerLink;
        this.onPageLoadComplete = onPageLoadComplete;
        this.onError = onError;
    }

    @Override // android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(request, "request");
        String url = request.getUrl().toString();
        Intrinsics.checkNotNullExpressionValue(url, "toString(...)");
        Log.d(TAG, "shouldOverrideUrlLoading: " + url);
        if (UrlConfig.INSTANCE.isFacebookScheme(url)) {
            Log.d(TAG, "Facebook scheme detected: " + url);
            this.onMessengerLink.invoke(url);
            return true;
        }
        if (UrlConfig.INSTANCE.isLoginUrl(url)) {
            Log.d(TAG, "Login URL detected, loading in WebView");
            return false;
        }
        if (UrlConfig.INSTANCE.isMessagesUrl(url)) {
            Log.d(TAG, "Messenger URL detected: " + url);
            this.onMessengerLink.invoke(url);
            return true;
        }
        if (UrlConfig.INSTANCE.isMarketplaceUrl(url)) {
            Log.d(TAG, "Marketplace URL, loading internally");
            return false;
        }
        if (StringsKt.contains$default((CharSequence) url, (CharSequence) "facebook.com", false, 2, (Object) null)) {
            Log.d(TAG, "Facebook URL, loading internally");
            return false;
        }
        Log.d(TAG, "External URL blocked: " + url);
        return true;
    }

    @Override // android.webkit.WebViewClient
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Log.d(TAG, "Page started loading: " + url);
    }

    @Override // android.webkit.WebViewClient
    public void onPageFinished(WebView view, String url) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(url, "url");
        super.onPageFinished(view, url);
        Log.d(TAG, "Page finished loading: " + url);
        this.onPageLoadComplete.invoke(url);
    }

    @Override // android.webkit.WebViewClient
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(request, "request");
        Intrinsics.checkNotNullParameter(error, "error");
        super.onReceivedError(view, request, error);
        if (request.isForMainFrame()) {
            String errorMessage = "Error " + error.getErrorCode() + ": " + ((Object) error.getDescription());
            Log.e(TAG, "Page load error: " + errorMessage);
            this.onError.invoke(errorMessage);
        }
    }

    @Override // android.webkit.WebViewClient
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
        Log.d(TAG, "History updated: " + url + " (reload=" + isReload + ")");
    }
}
