package com.marketplace.viewer.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.marketplace.viewer.BuildConfig;
import com.marketplace.viewer.config.UserAgentConfig;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: MarketplaceWebView.kt */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0006\u0010\t\u001a\u00020\nJ\b\u0010\u000b\u001a\u00020\nH\u0002J\b\u0010\f\u001a\u00020\nH\u0003¨\u0006\r"}, d2 = {"Lcom/marketplace/viewer/webview/MarketplaceWebView;", "Landroid/webkit/WebView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "clearAllData", "", "setupCookies", "setupWebView", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes7.dex */
public final class MarketplaceWebView extends WebView {
    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public MarketplaceWebView(Context context) {
        this(context, null, 0, 6, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public MarketplaceWebView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ MarketplaceWebView(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MarketplaceWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Intrinsics.checkNotNullParameter(context, "context");
        setupWebView();
        setupCookies();
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    private final void setupWebView() {
        WebSettings $this$setupWebView_u24lambda_u240 = getSettings();
        $this$setupWebView_u24lambda_u240.setJavaScriptEnabled(true);
        $this$setupWebView_u24lambda_u240.setDomStorageEnabled(true);
        $this$setupWebView_u24lambda_u240.setDatabaseEnabled(true);
        $this$setupWebView_u24lambda_u240.setUserAgentString(UserAgentConfig.INSTANCE.getUserAgent());
        $this$setupWebView_u24lambda_u240.setCacheMode(-1);
        $this$setupWebView_u24lambda_u240.setMixedContentMode(2);
        $this$setupWebView_u24lambda_u240.setMediaPlaybackRequiresUserGesture(false);
        $this$setupWebView_u24lambda_u240.setSupportMultipleWindows(true);
        $this$setupWebView_u24lambda_u240.setSupportZoom(false);
        $this$setupWebView_u24lambda_u240.setBuiltInZoomControls(false);
        $this$setupWebView_u24lambda_u240.setDisplayZoomControls(false);
        $this$setupWebView_u24lambda_u240.setSaveFormData(true);
        $this$setupWebView_u24lambda_u240.setLoadsImagesAutomatically(true);
        $this$setupWebView_u24lambda_u240.setBlockNetworkImage(false);
        $this$setupWebView_u24lambda_u240.setBlockNetworkLoads(false);
        $this$setupWebView_u24lambda_u240.setGeolocationEnabled(true);
        $this$setupWebView_u24lambda_u240.setAllowFileAccess(false);
        $this$setupWebView_u24lambda_u240.setAllowContentAccess(true);
        $this$setupWebView_u24lambda_u240.setAllowFileAccessFromFileURLs(false);
        $this$setupWebView_u24lambda_u240.setAllowUniversalAccessFromFileURLs(false);
        setLayerType(2, null);
        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    private final void setupCookies() {
        CookieManager $this$setupCookies_u24lambda_u241 = CookieManager.getInstance();
        $this$setupCookies_u24lambda_u241.setAcceptCookie(true);
        $this$setupCookies_u24lambda_u241.setAcceptThirdPartyCookies(this, true);
        $this$setupCookies_u24lambda_u241.flush();
    }

    public final void clearAllData() {
        clearCache(true);
        clearFormData();
        clearHistory();
        CookieManager $this$clearAllData_u24lambda_u242 = CookieManager.getInstance();
        $this$clearAllData_u24lambda_u242.removeAllCookies(null);
        $this$clearAllData_u24lambda_u242.flush();
    }
}
