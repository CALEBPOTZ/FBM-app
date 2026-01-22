package com.marketplace.viewer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.marketplace.viewer.auth.AuthManager;
import com.marketplace.viewer.auth.CustomTabsAuthLauncher;
import com.marketplace.viewer.config.UrlConfig;
import com.marketplace.viewer.databinding.ActivityMainBinding;
import com.marketplace.viewer.injection.JsInjectorFixed;
import com.marketplace.viewer.messenger.MessengerDeepLinker;
import com.marketplace.viewer.update.UpdateChecker;
import com.marketplace.viewer.webview.MarketplaceWebChromeClient;
import com.marketplace.viewer.webview.MarketplaceWebViewClient;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_LOCATION_PERMISSION = 1001;

    private ActivityMainBinding binding;
    private AuthManager authManager;
    private CustomTabsAuthLauncher customTabsLauncher;
    private MessengerDeepLinker messengerDeepLinker;
    private JsInjectorFixed jsInjector;
    private UpdateChecker updateChecker;
    private ValueCallback<Uri[]> filePathCallback;
    private ActivityResultLauncher<String> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        Log.d(TAG, "MainActivity created");
        
        // Initialize components
        authManager = new AuthManager(this);
        customTabsLauncher = new CustomTabsAuthLauncher(this);
        messengerDeepLinker = new MessengerDeepLinker(this);
        jsInjector = new JsInjectorFixed(binding.webView);
        
        // Initialize update checker
        updateChecker = new UpdateChecker(this);
        
        // Setup file picker
        filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetMultipleContents(),
            this::handleFilePickerResult
        );
        
        // Setup WebView
        setupWebView();
        
        // Setup retry button
        binding.retryButton.setOnClickListener(v -> loadMarketplace());
        
        // Load marketplace
        checkAuthAndLoad();
        
        // Check for updates (automatic check)
        updateChecker.checkForUpdates();
    }

    private void handleFilePickerResult(List<Uri> uris) {
        Uri[] results = null;
        if (uris != null && !uris.isEmpty()) {
            results = uris.toArray(new Uri[0]);
        }
        if (filePathCallback != null) {
            filePathCallback.onReceiveValue(results != null ? results : new Uri[0]);
            filePathCallback = null;
        }
    }

    private void setupWebView() {
        // Create callbacks for WebViewClient
        MarketplaceWebViewClient.Callbacks webViewCallbacks = new MarketplaceWebViewClient.Callbacks() {
            @Override
            public void onLoginRequired() {
                handleLoginRequired();
            }

            @Override
            public void onMessengerLink(String url) {
                handleMessengerLink(url);
            }

            @Override
            public void onPageLoadComplete(String url) {
                handlePageLoadComplete(url);
            }

            @Override
            public void onError(String error) {
                handleError(error);
            }
        };

        // Create callbacks for WebChromeClient
        MarketplaceWebChromeClient.Callbacks chromeCallbacks = new MarketplaceWebChromeClient.Callbacks() {
            @Override
            public void onProgressChanged(int progress) {
                handleProgressChanged(progress);
            }

            @Override
            public boolean onFileChooser(ValueCallback<Uri[]> callback, String[] acceptTypes) {
                return handleFileChooser(callback, acceptTypes);
            }
        };

        binding.webView.setWebViewClient(new MarketplaceWebViewClient(webViewCallbacks));
        binding.webView.setWebChromeClient(new MarketplaceWebChromeClient(chromeCallbacks));
        
        // Add JavaScript interface for native features
        binding.webView.addJavascriptInterface(new AppInterface(), "MarketplaceApp");
        
        requestLocationPermissionIfNeeded();
    }

    /**
     * JavaScript interface to expose native functionality to WebView
     */
    public class AppInterface {
        @JavascriptInterface
        public void checkForUpdates() {
            Log.d(TAG, "Update check triggered from JavaScript");
            runOnUiThread(() -> {
                if (updateChecker != null) {
                    updateChecker.checkForUpdatesManually();
                }
            });
        }

        @JavascriptInterface
        public String getAppVersion() {
            return updateChecker != null ? updateChecker.getCurrentVersion() : "Unknown";
        }
    }

    private void requestLocationPermissionIfNeeded() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Requesting location permission");
            ActivityCompat.requestPermissions(this,
                new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                },
                REQUEST_LOCATION_PERMISSION);
        } else {
            Log.d(TAG, "Location permission already granted");
        }
    }

    private void checkAuthAndLoad() {
        Log.d(TAG, "Loading Marketplace directly");
        loadMarketplace();
    }

    private void loadMarketplace() {
        Log.d(TAG, "Loading Marketplace");
        showLoading();
        binding.webView.loadUrl(UrlConfig.MARKETPLACE_URL);
    }

    private void handleLoginRequired() {
        Log.d(TAG, "Login page detected - user can login in WebView");
    }

    private void handleMessengerLink(String url) {
        Log.d(TAG, "Handling Messenger link: " + url);
        runOnUiThread(() -> {
            if (messengerDeepLinker != null) {
                messengerDeepLinker.openMessengerLink(url);
            }
        });
    }

    private void handlePageLoadComplete(String url) {
        Log.d(TAG, "Page load complete: " + url);
        runOnUiThread(() -> {
            hideLoading();
            
            if (UrlConfig.isMarketplaceUrl(url) || url.contains("facebook.com")) {
                if (jsInjector != null) {
                    jsInjector.injectAntiDetection();
                }
                
                if (UrlConfig.isMarketplaceUrl(url)) {
                    if (jsInjector != null) {
                        jsInjector.injectMarketplaceEnhancements();
                    }
                    if (authManager != null) {
                        authManager.setLoggedIn(true);
                    }
                }
            }
        });
    }

    private void handleProgressChanged(int progress) {
        runOnUiThread(() -> {
            binding.progressBar.setProgress(progress);
            binding.progressBar.setVisibility(progress < 100 ? View.VISIBLE : View.GONE);
        });
    }

    private void handleError(String error) {
        Log.e(TAG, "Error: " + error);
        runOnUiThread(() -> showError(error));
    }

    private boolean handleFileChooser(ValueCallback<Uri[]> callback, String[] acceptTypes) {
        Log.d(TAG, "File chooser requested");
        
        if (filePathCallback != null) {
            filePathCallback.onReceiveValue(null);
        }
        filePathCallback = callback;
        
        String mimeType = "image/*";
        if (acceptTypes != null && acceptTypes.length > 0 && acceptTypes[0] != null) {
            mimeType = acceptTypes[0];
        }
        
        filePickerLauncher.launch(mimeType);
        return true;
    }

    private void showLoading() {
        binding.loadingLayout.setVisibility(View.VISIBLE);
        binding.errorLayout.setVisibility(View.GONE);
        binding.webView.setVisibility(View.GONE);
    }

    private void hideLoading() {
        binding.loadingLayout.setVisibility(View.GONE);
        binding.webView.setVisibility(View.VISIBLE);
    }

    private void showError(String error) {
        binding.errorLayout.setVisibility(View.VISIBLE);
        binding.loadingLayout.setVisibility(View.GONE);
        binding.webView.setVisibility(View.GONE);
        binding.errorMessage.setText(error);
    }

    @Override
    public void onBackPressed() {
        // First, try to close the image viewer if it's open
        binding.webView.evaluateJavascript(
            "(function() { " +
            "if (window._marketplaceImageViewer) { " +
            "window._marketplaceImageViewer.remove(); " +
            "window._marketplaceImageViewer = null; " +
            "return 'closed'; " +
            "} return 'not_open'; " +
            "})()",
            result -> {
                if (result != null && result.contains("closed")) {
                    // Image viewer was closed, don't go back
                    return;
                }
                // No image viewer, handle normal back navigation
                runOnUiThread(() -> {
                    if (binding.webView.canGoBack()) {
                        binding.webView.goBack();
                    } else {
                        super.onBackPressed();
                    }
                });
            }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.webView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.webView.destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Location permission granted");
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "Location permission denied");
            }
        }
    }
}
