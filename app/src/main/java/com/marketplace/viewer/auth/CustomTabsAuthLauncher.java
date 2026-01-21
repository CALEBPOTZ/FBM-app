package com.marketplace.viewer.auth;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.marketplace.viewer.config.UrlConfig;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CustomTabsAuthLauncher.kt */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u0000 \n2\u00020\u0001:\u0001\nB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0006\u0010\t\u001a\u00020\u0006R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lcom/marketplace/viewer/auth/CustomTabsAuthLauncher;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "fallbackToBrowser", "", "url", "", "launchFacebookLogin", "Companion", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes6.dex */
public final class CustomTabsAuthLauncher {
    private static final String TAG = "CustomTabsAuthLauncher";
    private final Context context;

    public CustomTabsAuthLauncher(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
    }

    public final void launchFacebookLogin() {
        Log.d(TAG, "Launching Facebook login via Chrome Custom Tabs");
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().setShowTitle(true).setUrlBarHidingEnabled(false).build();
        Intrinsics.checkNotNullExpressionValue(customTabsIntent, "build(...)");
        try {
            customTabsIntent.launchUrl(this.context, Uri.parse(UrlConfig.LOGIN_URL));
        } catch (Exception e) {
            Log.e(TAG, "Failed to launch Custom Tabs, falling back to browser", e);
            fallbackToBrowser(UrlConfig.LOGIN_URL);
        }
    }

    private final void fallbackToBrowser(String url) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            intent.addFlags(268435456);
            this.context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Failed to open browser", e);
        }
    }
}
