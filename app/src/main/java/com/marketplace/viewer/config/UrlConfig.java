package com.marketplace.viewer.config;

import androidx.constraintlayout.widget.ConstraintLayout;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* compiled from: UrlConfig.kt */
@Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0004J\u000e\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0004J\u000e\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0004J\u000e\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lcom/marketplace/viewer/config/UrlConfig;", "", "()V", "FACEBOOK_BASE", "", "LOGIN_URL", "MARKETPLACE_URL", "MESSENGER_URL", "PATTERN_LOGIN", "PATTERN_MARKETPLACE", "PATTERN_MESSAGES", "PATTERN_MESSENGER", "SCHEME_FACEBOOK", "SCHEME_MESSENGER", "isFacebookScheme", "", "url", "isLoginUrl", "isMarketplaceUrl", "isMessagesUrl", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final class UrlConfig {
    public static final String FACEBOOK_BASE = "https://www.facebook.com";
    public static final UrlConfig INSTANCE = new UrlConfig();
    public static final String LOGIN_URL = "https://www.facebook.com/login";
    public static final String MARKETPLACE_URL = "https://www.facebook.com/marketplace";
    public static final String MESSENGER_URL = "https://www.facebook.com/messages";
    public static final String PATTERN_LOGIN = "facebook.com/login";
    public static final String PATTERN_MARKETPLACE = "facebook.com/marketplace";
    public static final String PATTERN_MESSAGES = "facebook.com/messages";
    public static final String PATTERN_MESSENGER = "messenger.com";
    public static final String SCHEME_FACEBOOK = "fb://";
    public static final String SCHEME_MESSENGER = "fb-messenger://";

    private UrlConfig() {
    }

    public final boolean isMarketplaceUrl(String url) {
        Intrinsics.checkNotNullParameter(url, "url");
        return StringsKt.contains((CharSequence) url, (CharSequence) PATTERN_MARKETPLACE, true);
    }

    public final boolean isMessagesUrl(String url) {
        Intrinsics.checkNotNullParameter(url, "url");
        return StringsKt.contains((CharSequence) url, (CharSequence) PATTERN_MESSAGES, true) || StringsKt.contains((CharSequence) url, (CharSequence) PATTERN_MESSENGER, true);
    }

    public final boolean isLoginUrl(String url) {
        Intrinsics.checkNotNullParameter(url, "url");
        return StringsKt.contains((CharSequence) url, (CharSequence) PATTERN_LOGIN, true);
    }

    public final boolean isFacebookScheme(String url) {
        Intrinsics.checkNotNullParameter(url, "url");
        return StringsKt.startsWith$default(url, SCHEME_FACEBOOK, false, 2, (Object) null) || StringsKt.startsWith$default(url, SCHEME_MESSENGER, false, 2, (Object) null);
    }
}
