package com.marketplace.viewer.databinding;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import com.marketplace.viewer.R;
import com.marketplace.viewer.webview.MarketplaceWebView;

/* loaded from: classes4.dex */
public final class ActivityMainBinding implements ViewBinding {
    public final LinearLayout errorLayout;
    public final TextView errorMessage;
    public final TextView errorTitle;
    public final LinearLayout loadingLayout;
    public final ProgressBar loadingSpinner;
    public final TextView loadingText;
    public final ProgressBar progressBar;
    public final MaterialButton retryButton;
    private final ConstraintLayout rootView;
    public final MarketplaceWebView webView;

    private ActivityMainBinding(ConstraintLayout rootView, LinearLayout errorLayout, TextView errorMessage, TextView errorTitle, LinearLayout loadingLayout, ProgressBar loadingSpinner, TextView loadingText, ProgressBar progressBar, MaterialButton retryButton, MarketplaceWebView webView) {
        this.rootView = rootView;
        this.errorLayout = errorLayout;
        this.errorMessage = errorMessage;
        this.errorTitle = errorTitle;
        this.loadingLayout = loadingLayout;
        this.loadingSpinner = loadingSpinner;
        this.loadingText = loadingText;
        this.progressBar = progressBar;
        this.retryButton = retryButton;
        this.webView = webView;
    }

    @Override // androidx.viewbinding.ViewBinding
    public ConstraintLayout getRoot() {
        return this.rootView;
    }

    public static ActivityMainBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ActivityMainBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.activity_main, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ActivityMainBinding bind(View rootView) throws Resources.NotFoundException {
        int id = R.id.errorLayout;
        LinearLayout errorLayout = (LinearLayout) ViewBindings.findChildViewById(rootView, id);
        if (errorLayout != null) {
            id = R.id.errorMessage;
            TextView errorMessage = (TextView) ViewBindings.findChildViewById(rootView, id);
            if (errorMessage != null) {
                id = R.id.errorTitle;
                TextView errorTitle = (TextView) ViewBindings.findChildViewById(rootView, id);
                if (errorTitle != null) {
                    id = R.id.loadingLayout;
                    LinearLayout loadingLayout = (LinearLayout) ViewBindings.findChildViewById(rootView, id);
                    if (loadingLayout != null) {
                        id = R.id.loadingSpinner;
                        ProgressBar loadingSpinner = (ProgressBar) ViewBindings.findChildViewById(rootView, id);
                        if (loadingSpinner != null) {
                            id = R.id.loadingText;
                            TextView loadingText = (TextView) ViewBindings.findChildViewById(rootView, id);
                            if (loadingText != null) {
                                id = R.id.progressBar;
                                ProgressBar progressBar = (ProgressBar) ViewBindings.findChildViewById(rootView, id);
                                if (progressBar != null) {
                                    id = R.id.retryButton;
                                    MaterialButton retryButton = (MaterialButton) ViewBindings.findChildViewById(rootView, id);
                                    if (retryButton != null) {
                                        id = R.id.webView;
                                        MarketplaceWebView webView = (MarketplaceWebView) ViewBindings.findChildViewById(rootView, id);
                                        if (webView != null) {
                                            return new ActivityMainBinding((ConstraintLayout) rootView, errorLayout, errorMessage, errorTitle, loadingLayout, loadingSpinner, loadingText, progressBar, retryButton, webView);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
