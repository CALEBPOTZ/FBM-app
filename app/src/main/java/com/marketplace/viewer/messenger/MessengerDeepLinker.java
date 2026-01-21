package com.marketplace.viewer.messenger;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.marketplace.viewer.R;
import com.marketplace.viewer.config.UrlConfig;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* compiled from: MessengerDeepLinker.kt */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0007\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0002J\b\u0010\b\u001a\u00020\tH\u0002J\b\u0010\n\u001a\u00020\tH\u0002J\u0010\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u0006H\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\u0006H\u0002J\u0010\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\u0006H\u0002J\u0010\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\u0006H\u0002J\u000e\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\u0006J\u0010\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\u0006H\u0002J\b\u0010\u0013\u001a\u00020\u000eH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lcom/marketplace/viewer/messenger/MessengerDeepLinker;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "convertToMessengerScheme", "", "url", "isFacebookInstalled", "", "isMessengerInstalled", "isPackageInstalled", "packageName", "openInBrowser", "", "openInFacebookApp", "openInMessengerApp", "openMessengerLink", "openMessengerWeb", "showMessengerNotInstalledMessage", "Companion", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes9.dex */
public final class MessengerDeepLinker {
    private static final String FACEBOOK_PACKAGE = "com.facebook.katana";
    private static final String MESSENGER_PACKAGE = "com.facebook.orca";
    private static final String TAG = "MessengerDeepLinker";
    private final Context context;

    public MessengerDeepLinker(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
    }

    public final void openMessengerLink(String url) {
        Intrinsics.checkNotNullParameter(url, "url");
        Log.d(TAG, "Opening Messenger link: " + url);
        if (StringsKt.startsWith$default(url, UrlConfig.SCHEME_MESSENGER, false, 2, (Object) null)) {
            openInMessengerApp(url);
            return;
        }
        if (StringsKt.startsWith$default(url, UrlConfig.SCHEME_FACEBOOK, false, 2, (Object) null)) {
            openInFacebookApp(url);
        } else if (StringsKt.contains$default((CharSequence) url, (CharSequence) UrlConfig.PATTERN_MESSENGER, false, 2, (Object) null) || StringsKt.contains$default((CharSequence) url, (CharSequence) UrlConfig.PATTERN_MESSAGES, false, 2, (Object) null)) {
            openMessengerWeb(url);
        } else {
            Log.w(TAG, "Unknown messenger URL format: " + url);
            openInBrowser(url);
        }
    }

    private final void openInMessengerApp(String url) {
        if (isMessengerInstalled()) {
            try {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                intent.setPackage(MESSENGER_PACKAGE);
                intent.addFlags(268435456);
                this.context.startActivity(intent);
                Log.d(TAG, "Opened in Messenger app");
                return;
            } catch (Exception e) {
                Log.e(TAG, "Failed to open in Messenger app", e);
                showMessengerNotInstalledMessage();
                return;
            }
        }
        showMessengerNotInstalledMessage();
    }

    private final void openInFacebookApp(String url) {
        if (isFacebookInstalled()) {
            try {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                intent.setPackage(FACEBOOK_PACKAGE);
                intent.addFlags(268435456);
                this.context.startActivity(intent);
                Log.d(TAG, "Opened in Facebook app");
                return;
            } catch (Exception e) {
                Log.e(TAG, "Failed to open in Facebook app", e);
                openInBrowser(url);
                return;
            }
        }
        openInBrowser(url);
    }

    private final void openMessengerWeb(String url) {
        if (isMessengerInstalled()) {
            String messengerUrl = convertToMessengerScheme(url);
            openInMessengerApp(messengerUrl);
        } else {
            openInBrowser(url);
        }
    }

    private final String convertToMessengerScheme(String url) {
        return StringsKt.replace$default(url, "https://", UrlConfig.SCHEME_MESSENGER, false, 4, (Object) null);
    }

    private final void openInBrowser(String url) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(url));
            intent.addFlags(268435456);
            this.context.startActivity(intent);
            Log.d(TAG, "Opened in browser");
        } catch (Exception e) {
            Log.e(TAG, "Failed to open in browser", e);
        }
    }

    private final boolean isMessengerInstalled() {
        return isPackageInstalled(MESSENGER_PACKAGE);
    }

    private final boolean isFacebookInstalled() {
        return isPackageInstalled(FACEBOOK_PACKAGE);
    }

    private final boolean isPackageInstalled(String packageName) throws PackageManager.NameNotFoundException {
        try {
            this.context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private final void showMessengerNotInstalledMessage() {
        Toast.makeText(this.context, this.context.getString(R.string.messenger_not_installed), 0).show();
    }
}
