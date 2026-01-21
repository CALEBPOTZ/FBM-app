package com.marketplace.viewer;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.marketplace.viewer.auth.AuthManager;
import com.marketplace.viewer.auth.CustomTabsAuthLauncher;
import com.marketplace.viewer.config.UrlConfig;
import com.marketplace.viewer.databinding.ActivityMainBinding;
import com.marketplace.viewer.injection.JsInjectorFixed;
import com.marketplace.viewer.messenger.MessengerDeepLinker;
import com.marketplace.viewer.webview.MarketplaceWebChromeClient;
import com.marketplace.viewer.webview.MarketplaceWebView;
import com.marketplace.viewer.webview.MarketplaceWebViewClient;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* compiled from: MainActivity.kt */
@Metadata(d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0015\n\u0002\b\b\u0018\u0000 92\u00020\u0001:\u00019B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\u0010\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u000fH\u0002J3\u0010\u0019\u001a\u00020\u001a2\u0014\u0010\u001b\u001a\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b\u0018\u00010\n2\u000e\u0010\u001c\u001a\n\u0012\u0004\u0012\u00020\u000f\u0018\u00010\u000bH\u0002¢\u0006\u0002\u0010\u001dJ\b\u0010\u001e\u001a\u00020\u0016H\u0002J\u0010\u0010\u001f\u001a\u00020\u00162\u0006\u0010 \u001a\u00020\u000fH\u0002J\u0010\u0010!\u001a\u00020\u00162\u0006\u0010 \u001a\u00020\u000fH\u0002J\u0010\u0010\"\u001a\u00020\u00162\u0006\u0010#\u001a\u00020$H\u0002J\b\u0010%\u001a\u00020\u0016H\u0002J\b\u0010&\u001a\u00020\u0016H\u0002J\b\u0010'\u001a\u00020\u0016H\u0002J\b\u0010(\u001a\u00020\u0016H\u0016J\u0012\u0010)\u001a\u00020\u00162\b\u0010*\u001a\u0004\u0018\u00010+H\u0014J\b\u0010,\u001a\u00020\u0016H\u0014J\b\u0010-\u001a\u00020\u0016H\u0014J-\u0010.\u001a\u00020\u00162\u0006\u0010/\u001a\u00020$2\u000e\u00100\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u000f0\u000b2\u0006\u00101\u001a\u000202H\u0016¢\u0006\u0002\u00103J\b\u00104\u001a\u00020\u0016H\u0014J\b\u00105\u001a\u00020\u0016H\u0002J\b\u00106\u001a\u00020\u0016H\u0002J\u0010\u00107\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u000fH\u0002J\b\u00108\u001a\u00020\u0016H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.¢\u0006\u0002\n\u0000R\u001c\u0010\t\u001a\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\r\u001a\u0010\u0012\f\u0012\n \u0010*\u0004\u0018\u00010\u000f0\u000f0\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.¢\u0006\u0002\n\u0000¨\u0006:"}, d2 = {"Lcom/marketplace/viewer/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "authManager", "Lcom/marketplace/viewer/auth/AuthManager;", "binding", "Lcom/marketplace/viewer/databinding/ActivityMainBinding;", "customTabsLauncher", "Lcom/marketplace/viewer/auth/CustomTabsAuthLauncher;", "filePathCallback", "Landroid/webkit/ValueCallback;", "", "Landroid/net/Uri;", "filePickerLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "kotlin.jvm.PlatformType", "jsInjector", "Lcom/marketplace/viewer/injection/JsInjector;", "messengerDeepLinker", "Lcom/marketplace/viewer/messenger/MessengerDeepLinker;", "checkAuthAndLoad", "", "handleError", "error", "handleFileChooser", "", "callback", "acceptTypes", "(Landroid/webkit/ValueCallback;[Ljava/lang/String;)Z", "handleLoginRequired", "handleMessengerLink", "url", "handlePageLoadComplete", "handleProgressChanged", "progress", "", "hideLoading", "launchLogin", "loadMarketplace", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onPause", "onRequestPermissionsResult", "requestCode", "permissions", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onResume", "requestLocationPermissionIfNeeded", "setupWebView", "showError", "showLoading", "Companion", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes8.dex */
public final class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 1001;
    private static final String TAG = "MainActivity";
    private AuthManager authManager;
    private ActivityMainBinding binding;
    private CustomTabsAuthLauncher customTabsLauncher;
    private ValueCallback<Uri[]> filePathCallback;
    private final ActivityResultLauncher<String> filePickerLauncher;
    private JsInjectorFixed jsInjector;
    private MessengerDeepLinker messengerDeepLinker;

    public MainActivity() {
        ActivityResultLauncher<String> activityResultLauncherRegisterForActivityResult = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), new ActivityResultCallback() { // from class: com.marketplace.viewer.MainActivity$$ExternalSyntheticLambda1
            @Override // androidx.activity.result.ActivityResultCallback
            public final void onActivityResult(Object obj) {
                MainActivity.filePickerLauncher$lambda$0(this.f$0, (List) obj);
            }
        });
        Intrinsics.checkNotNullExpressionValue(activityResultLauncherRegisterForActivityResult, "registerForActivityResult(...)");
        this.filePickerLauncher = activityResultLauncherRegisterForActivityResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void filePickerLauncher$lambda$0(MainActivity this$0, List uris) {
        Uri[] results;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (uris == null) {
            results = null;
        } else {
            List $this$toTypedArray$iv = uris;
            results = (Uri[]) $this$toTypedArray$iv.toArray(new Uri[0]);
        }
        ValueCallback<Uri[]> valueCallback = this$0.filePathCallback;
        if (valueCallback != null) {
            valueCallback.onReceiveValue(results == null ? new Uri[0] : results);
        }
        this$0.filePathCallback = null;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBindingInflate = ActivityMainBinding.inflate(getLayoutInflater());
        Intrinsics.checkNotNullExpressionValue(activityMainBindingInflate, "inflate(...)");
        this.binding = activityMainBindingInflate;
        ActivityMainBinding activityMainBinding = this.binding;
        ActivityMainBinding activityMainBinding2 = null;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        setContentView(activityMainBinding.getRoot());
        Log.d(TAG, "MainActivity created");
        this.authManager = new AuthManager(this);
        this.customTabsLauncher = new CustomTabsAuthLauncher(this);
        this.messengerDeepLinker = new MessengerDeepLinker(this);
        ActivityMainBinding activityMainBinding3 = this.binding;
        if (activityMainBinding3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding3 = null;
        }
        MarketplaceWebView webView = activityMainBinding3.webView;
        Intrinsics.checkNotNullExpressionValue(webView, "webView");
        this.jsInjector = new JsInjectorFixed(webView);
        setupWebView();
        ActivityMainBinding activityMainBinding4 = this.binding;
        if (activityMainBinding4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
        } else {
            activityMainBinding2 = activityMainBinding4;
        }
        activityMainBinding2.retryButton.setOnClickListener(new View.OnClickListener() { // from class: com.marketplace.viewer.MainActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.onCreate$lambda$1(this.f$0, view);
            }
        });
        checkAuthAndLoad();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$lambda$1(MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.loadMarketplace();
    }

    private final void setupWebView() {
        ActivityMainBinding activityMainBinding = this.binding;
        ActivityMainBinding activityMainBinding2 = null;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        activityMainBinding.webView.setWebViewClient(new MarketplaceWebViewClient(new Function0<Unit>() { // from class: com.marketplace.viewer.MainActivity.setupWebView.1
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
                MainActivity.this.handleLoginRequired();
            }
        }, new Function1<String, Unit>() { // from class: com.marketplace.viewer.MainActivity.setupWebView.2
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(String str) {
                invoke2(str);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(String url) {
                Intrinsics.checkNotNullParameter(url, "url");
                MainActivity.this.handleMessengerLink(url);
            }
        }, new Function1<String, Unit>() { // from class: com.marketplace.viewer.MainActivity.setupWebView.3
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(String str) {
                invoke2(str);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(String url) {
                Intrinsics.checkNotNullParameter(url, "url");
                MainActivity.this.handlePageLoadComplete(url);
            }
        }, new Function1<String, Unit>() { // from class: com.marketplace.viewer.MainActivity.setupWebView.4
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(String str) {
                invoke2(str);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(String error) {
                Intrinsics.checkNotNullParameter(error, "error");
                MainActivity.this.handleError(error);
            }
        }));
        ActivityMainBinding activityMainBinding3 = this.binding;
        if (activityMainBinding3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
        } else {
            activityMainBinding2 = activityMainBinding3;
        }
        activityMainBinding2.webView.setWebChromeClient(new MarketplaceWebChromeClient(new Function1<Integer, Unit>() { // from class: com.marketplace.viewer.MainActivity.setupWebView.5
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Integer num) {
                invoke(num.intValue());
                return Unit.INSTANCE;
            }

            public final void invoke(int progress) {
                MainActivity.this.handleProgressChanged(progress);
            }
        }, new Function2<ValueCallback<Uri[]>, String[], Boolean>() { // from class: com.marketplace.viewer.MainActivity.setupWebView.6
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            public final Boolean invoke(ValueCallback<Uri[]> valueCallback, String[] acceptTypes) {
                return Boolean.valueOf(MainActivity.this.handleFileChooser(valueCallback, acceptTypes));
            }
        }));
        requestLocationPermissionIfNeeded();
    }

    private final void requestLocationPermissionIfNeeded() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            Log.d(TAG, "Requesting location permission");
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 1001);
        } else {
            Log.d(TAG, "Location permission already granted");
        }
    }

    private final void checkAuthAndLoad() {
        Log.d(TAG, "Loading Marketplace directly");
        loadMarketplace();
    }

    private final void loadMarketplace() {
        Log.d(TAG, "Loading Marketplace");
        showLoading();
        ActivityMainBinding activityMainBinding = this.binding;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        activityMainBinding.webView.loadUrl(UrlConfig.MARKETPLACE_URL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void handleLoginRequired() {
        Log.d(TAG, "Login page detected - user can login in WebView");
    }

    private final void launchLogin() {
        Log.d(TAG, "Login detected in WebView - user will login there");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void handleMessengerLink(final String url) {
        Log.d(TAG, "Handling Messenger link: " + url);
        runOnUiThread(new Runnable() { // from class: com.marketplace.viewer.MainActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.handleMessengerLink$lambda$2(this.f$0, url);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void handleMessengerLink$lambda$2(MainActivity this$0, String url) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(url, "$url");
        MessengerDeepLinker messengerDeepLinker = this$0.messengerDeepLinker;
        if (messengerDeepLinker == null) {
            Intrinsics.throwUninitializedPropertyAccessException("messengerDeepLinker");
            messengerDeepLinker = null;
        }
        messengerDeepLinker.openMessengerLink(url);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void handlePageLoadComplete(final String url) {
        Log.d(TAG, "Page load complete: " + url);
        runOnUiThread(new Runnable() { // from class: com.marketplace.viewer.MainActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.handlePageLoadComplete$lambda$3(this.f$0, url);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void handlePageLoadComplete$lambda$3(MainActivity this$0, String url) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(url, "$url");
        this$0.hideLoading();
        AuthManager authManager = null;
        if (UrlConfig.INSTANCE.isMarketplaceUrl(url) || StringsKt.contains$default((CharSequence) url, (CharSequence) "facebook.com", false, 2, (Object) null)) {
            JsInjectorFixed jsInjector = this$0.jsInjector;
            if (jsInjector == null) {
                Intrinsics.throwUninitializedPropertyAccessException("jsInjector");
                jsInjector = null;
            }
            jsInjector.injectAntiDetection();
            if (UrlConfig.INSTANCE.isMarketplaceUrl(url)) {
                JsInjectorFixed jsInjector2 = this$0.jsInjector;
                if (jsInjector2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("jsInjector");
                    jsInjector2 = null;
                }
                jsInjector2.injectMarketplaceEnhancements();
                AuthManager authManager2 = this$0.authManager;
                if (authManager2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("authManager");
                } else {
                    authManager = authManager2;
                }
                authManager.setLoggedIn(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void handleProgressChanged(final int progress) {
        runOnUiThread(new Runnable() { // from class: com.marketplace.viewer.MainActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.handleProgressChanged$lambda$4(this.f$0, progress);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void handleProgressChanged$lambda$4(MainActivity this$0, int $progress) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        ActivityMainBinding activityMainBinding = this$0.binding;
        ActivityMainBinding activityMainBinding2 = null;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        activityMainBinding.progressBar.setProgress($progress);
        ActivityMainBinding activityMainBinding3 = this$0.binding;
        if (activityMainBinding3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
        } else {
            activityMainBinding2 = activityMainBinding3;
        }
        activityMainBinding2.progressBar.setVisibility($progress < 100 ? 0 : 8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void handleError(final String error) {
        Log.e(TAG, "Error: " + error);
        runOnUiThread(new Runnable() { // from class: com.marketplace.viewer.MainActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.handleError$lambda$5(this.f$0, error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void handleError$lambda$5(MainActivity this$0, String error) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(error, "$error");
        this$0.showError(error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean handleFileChooser(ValueCallback<Uri[]> callback, String[] acceptTypes) {
        String mimeType;
        Log.d(TAG, "File chooser requested");
        ValueCallback<Uri[]> valueCallback = this.filePathCallback;
        if (valueCallback != null) {
            valueCallback.onReceiveValue(null);
        }
        this.filePathCallback = callback;
        if (acceptTypes == null || (mimeType = (String) ArraysKt.firstOrNull(acceptTypes)) == null) {
            mimeType = "image/*";
        }
        this.filePickerLauncher.launch(mimeType);
        return true;
    }

    private final void showLoading() {
        ActivityMainBinding activityMainBinding = this.binding;
        ActivityMainBinding activityMainBinding2 = null;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        activityMainBinding.loadingLayout.setVisibility(0);
        ActivityMainBinding activityMainBinding3 = this.binding;
        if (activityMainBinding3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding3 = null;
        }
        activityMainBinding3.errorLayout.setVisibility(8);
        ActivityMainBinding activityMainBinding4 = this.binding;
        if (activityMainBinding4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
        } else {
            activityMainBinding2 = activityMainBinding4;
        }
        activityMainBinding2.webView.setVisibility(8);
    }

    private final void hideLoading() {
        ActivityMainBinding activityMainBinding = this.binding;
        ActivityMainBinding activityMainBinding2 = null;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        activityMainBinding.loadingLayout.setVisibility(8);
        ActivityMainBinding activityMainBinding3 = this.binding;
        if (activityMainBinding3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
        } else {
            activityMainBinding2 = activityMainBinding3;
        }
        activityMainBinding2.webView.setVisibility(0);
    }

    private final void showError(String error) {
        ActivityMainBinding activityMainBinding = this.binding;
        ActivityMainBinding activityMainBinding2 = null;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        activityMainBinding.errorLayout.setVisibility(0);
        ActivityMainBinding activityMainBinding3 = this.binding;
        if (activityMainBinding3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding3 = null;
        }
        activityMainBinding3.loadingLayout.setVisibility(8);
        ActivityMainBinding activityMainBinding4 = this.binding;
        if (activityMainBinding4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding4 = null;
        }
        activityMainBinding4.webView.setVisibility(8);
        ActivityMainBinding activityMainBinding5 = this.binding;
        if (activityMainBinding5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
        } else {
            activityMainBinding2 = activityMainBinding5;
        }
        activityMainBinding2.errorMessage.setText(error);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        ActivityMainBinding activityMainBinding = this.binding;
        ActivityMainBinding activityMainBinding2 = null;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        if (activityMainBinding.webView.canGoBack()) {
            ActivityMainBinding activityMainBinding3 = this.binding;
            if (activityMainBinding3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding3;
            }
            activityMainBinding2.webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        ActivityMainBinding activityMainBinding = this.binding;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        activityMainBinding.webView.onResume();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        ActivityMainBinding activityMainBinding = this.binding;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        activityMainBinding.webView.onPause();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        ActivityMainBinding activityMainBinding = this.binding;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        activityMainBinding.webView.destroy();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Intrinsics.checkNotNullParameter(permissions, "permissions");
        Intrinsics.checkNotNullParameter(grantResults, "grantResults");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (!(grantResults.length == 0) && grantResults[0] == 0) {
                Log.d(TAG, "Location permission granted");
                ActivityMainBinding activityMainBinding = this.binding;
                if (activityMainBinding == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("binding");
                    activityMainBinding = null;
                }
                activityMainBinding.webView.reload();
                return;
            }
            Log.d(TAG, "Location permission denied");
            Toast.makeText(this, "Location permission is needed for local Marketplace listings", 0).show();
        }
    }
}
