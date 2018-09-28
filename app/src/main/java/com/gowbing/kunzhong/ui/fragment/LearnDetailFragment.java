package com.gowbing.kunzhong.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.gowbing.kunzhong.R;
import com.gowbing.kunzhong.adapter.DownloadAdapter;
import com.gowbing.kunzhong.constants.PreferenceConstants;
import com.gowbing.kunzhong.model.DownloadPercent;
import com.gowbing.kunzhong.model.Learn;
import com.gowbing.kunzhong.model.detail.LearnDetail;
import com.gowbing.kunzhong.net.ServerUrl;
import com.gowbing.kunzhong.ui.BaseFragment;
import com.gowbing.kunzhong.util.PreferenceUtils;
import com.gowbing.kunzhong.util.ToastUtils;
import com.gowbing.kunzhong.util.Utility;
import com.gowbing.kunzhong.util.download.FileDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-8-27.
 */

public class LearnDetailFragment extends BaseFragment {


    @Bind(R.id.subject_tv)
    TextView subjectTv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.time_tv)
    TextView timeTv;
    @Bind(R.id.teacher_tv)
    TextView teacherTv;
    @Bind(R.id.content_wv)
    WebView contentWv;
    @Bind(R.id.ziliao_lv)
    ListView ziliaoLv;
    private int id;
    private Learn item;

    private DownloadAdapter adapter;

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void bindListener() {

    }

    @Override
    public void initData() {
        adapter = new DownloadAdapter(mContext);
        webViewSetting(contentWv);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id + "");
        getDataFromServer(Request.Method.POST, ServerUrl.GET_LEARN_DETAIL, params, LearnDetail.class, new Response.Listener<LearnDetail>() {
            @Override
            public void onResponse(LearnDetail response) {
                item = response.getResults();
                subjectTv.setText(item.getSubject_name());
                titleTv.setText(item.getTitle());
                timeTv.setText(item.getPublish_date());
                contentWv.loadDataWithBaseURL(null,item.getContent(),"text/html","UTF-8",null);
                teacherTv.setText(item.getTeacher_name());
                adapter.setId(id);
                adapter.setList(item.getPics());
                ziliaoLv.setAdapter(adapter);
                Utility.setListViewHeightBasedOnChildren(ziliaoLv);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_learn_detail;
    }



}
