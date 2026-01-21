package com.marketplace.viewer.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.marketplace.viewer.config.UrlConfig;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* compiled from: AuthManager.kt */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0002J\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\bJ\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000b\u001a\u00020\bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lcom/marketplace/viewer/auth/AuthManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "prefs", "Landroid/content/SharedPreferences;", "checkFacebookCookies", "", "getTimeSinceLastLogin", "", "isLoggedIn", "logout", "", "setLoggedIn", "Companion", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes6.dex */
public final class AuthManager {
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_LAST_LOGIN_TIME = "last_login_time";
    private static final String PREFS_NAME = "marketplace_auth";
    private static final String TAG = "AuthManager";
    private final SharedPreferences prefs;

    public AuthManager(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0);
        Intrinsics.checkNotNullExpressionValue(sharedPreferences, "getSharedPreferences(...)");
        this.prefs = sharedPreferences;
    }

    public final boolean isLoggedIn() {
        boolean loggedIn = false;
        boolean hasLoginFlag = this.prefs.getBoolean(KEY_IS_LOGGED_IN, false);
        boolean hasCookies = checkFacebookCookies();
        if (hasLoginFlag && hasCookies) {
            loggedIn = true;
        }
        Log.d(TAG, "Login status: " + loggedIn + " (flag=" + hasLoginFlag + ", cookies=" + hasCookies + ")");
        return loggedIn;
    }

    public final void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor $this$setLoggedIn_u24lambda_u240 = this.prefs.edit();
        $this$setLoggedIn_u24lambda_u240.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        if (isLoggedIn) {
            $this$setLoggedIn_u24lambda_u240.putLong(KEY_LAST_LOGIN_TIME, System.currentTimeMillis());
        }
        $this$setLoggedIn_u24lambda_u240.apply();
        Log.d(TAG, "Login status updated: " + isLoggedIn);
    }

    private final boolean checkFacebookCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(UrlConfig.FACEBOOK_BASE);
        if (cookies == null) {
            cookies = "";
        }
        return StringsKt.contains$default((CharSequence) cookies, (CharSequence) "c_user", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) cookies, (CharSequence) "xs", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) cookies, (CharSequence) "datr", false, 2, (Object) null);
    }

    public final void logout() {
        this.prefs.edit().clear().apply();
        CookieManager $this$logout_u24lambda_u242 = CookieManager.getInstance();
        $this$logout_u24lambda_u242.removeAllCookies(new ValueCallback() { // from class: com.marketplace.viewer.auth.AuthManager$$ExternalSyntheticLambda0
            @Override // android.webkit.ValueCallback
            public final void onReceiveValue(Object obj) {
                AuthManager.logout$lambda$2$lambda$1((Boolean) obj);
            }
        });
        $this$logout_u24lambda_u242.flush();
        Log.d(TAG, "User logged out");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void logout$lambda$2$lambda$1(Boolean success) {
        Log.d(TAG, "Cookies cleared: " + success);
    }

    public final long getTimeSinceLastLogin() {
        long lastLoginTime = this.prefs.getLong(KEY_LAST_LOGIN_TIME, 0L);
        if (lastLoginTime > 0) {
            return System.currentTimeMillis() - lastLoginTime;
        }
        return Long.MAX_VALUE;
    }
}
