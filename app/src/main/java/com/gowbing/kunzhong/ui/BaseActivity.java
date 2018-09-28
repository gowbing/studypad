package com.gowbing.kunzhong.ui;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageRequest;
import com.gowbing.kunzhong.AppApplication;
import com.gowbing.kunzhong.R;
import com.gowbing.kunzhong.net.http.ATTJsonRequest;
import com.gowbing.kunzhong.ui.action.InitViews;
import com.gowbing.kunzhong.util.StatusBarUtil;
import com.gowbing.kunzhong.widget.LoadingProgressDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by 刘 on 2016/11/18.
 */
public abstract class BaseActivity extends AppCompatActivity implements InitViews {
    protected Context mContext = null;
    private LoadingProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(getLayoutId());

        //添加activity到堆栈
        AppApplication.getInstance().addActivity(this);
        StatusBarUtil.darkMode(this);
        ButterKnife.bind(this);
        initData();
        bindListener();
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        //将activity从堆栈中移除
        AppApplication.getInstance().getRequestQueue().cancelAll("VolleyRequest");
        AppApplication.getInstance().finishActivity(this);
        super.onDestroy();
    }

    /**
     * 显示进度条
     *
     * @param msg 进度条文字
     * @return
     */
    protected LoadingProgressDialog showDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new LoadingProgressDialog(mContext, R.style.LoadingProgressTheme);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);
        return mProgressDialog;
    }

    /**
     * 隐藏进度条
     */
    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public <T> void getDataFromServer(int method, String url, Class<T> clazz, Listener<T> listener,
                                      ErrorListener errorListener) {
        ATTJsonRequest<T> qBaoJsonRequest = new ATTJsonRequest<T>(method, url, clazz, listener, errorListener);
        executeRequest(qBaoJsonRequest);
    }

    public <T> void getDataFromServer(int method, String url, HashMap<String, String> params,
                                      Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        ATTJsonRequest<T> qBaoJsonRequest = new ATTJsonRequest<T>(method, url, clazz, listener, errorListener);
        if (params != null) {
            qBaoJsonRequest.addParams(params);
        }
        Log.i("http",url);
        executeRequest(qBaoJsonRequest);
    }

    public <T> void getDataFromServer(String url, JSONObject jsonRequest,
                                      Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        ATTJsonRequest<T> attJsonRequest = new ATTJsonRequest<T>(url, clazz, jsonRequest, listener, errorListener);
        executeRequest(attJsonRequest);
    }

    public void getImageFromServer(String url, Listener<Bitmap> listener, ErrorListener errorListener) {
        ImageRequest imageRequest = new ImageRequest(
                url, listener, 0, 0, Bitmap.Config.RGB_565, errorListener);
        executeRequest(imageRequest);
    }


    protected void executeRequest(Request<?> request) {
        request.setTag(this);
        AppApplication.getInstance().getRequestQueue().add(request);
    }

    protected void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String msg, int time) {
        Toast.makeText(mContext, msg, time).show();
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected void setTranslucentStatusColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


}