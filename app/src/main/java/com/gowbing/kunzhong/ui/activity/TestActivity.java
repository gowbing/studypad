package com.gowbing.kunzhong.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.gowbing.kunzhong.R;
import com.gowbing.kunzhong.ui.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-8-24.
 */

public class TestActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.url_et)
    EditText urlEt;
    @Bind(R.id.submit_bt)
    Button submitBt;
    @Bind(R.id.toweb_bt)
    Button towebBt;
    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.tohtml_tv)
    EditText tohtmlTv;
    @Bind(R.id.toapp_bt)
    Button toappBt;

    @Override
    public void bindListener() {
        submitBt.setOnClickListener(this);
        towebBt.setOnClickListener(this);
        toappBt.setOnClickListener(this);
    }

    @JavascriptInterface
    @Override
    public void initData() {
        webViewSetting(webview);
        webview.addJavascriptInterface(this,"study");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_bt: {
                webview.loadUrl(urlEt.getText().toString());
                break;
            }
            case R.id.toweb_bt: {
                loadUrl();
                break;
            }
            case R.id.toapp_bt:{
                loadUrl();
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void loadUrl() {
        webview.loadUrl("javascript:intoHtml(" + tohtmlTv.getText().toString() + ")");
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void putfile(String path) {
        webview.loadUrl("javascript:uploadresponse(" + path + ")");
    }

    protected void webViewSetting(WebView webView) {

        //基本设置
        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 调用拨号程序
                if (url.startsWith("mailto:") || url.startsWith("geo:")
                        || url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(url));
                    startActivity(intent);
                } else {
                    // 当有新连接时，使用当前的 WebView
                    view.loadUrl(url);
                }
                return true;
            }
        });
    }

}
