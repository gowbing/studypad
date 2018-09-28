package com.gowbing.kunzhong.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.gowbing.kunzhong.AppApplication;
import com.gowbing.kunzhong.R;
import com.gowbing.kunzhong.model.list.UserResponse;
import com.gowbing.kunzhong.net.ServerUrl;
import com.gowbing.kunzhong.net.http.ATTJsonRequest;
import com.gowbing.kunzhong.ui.BaseActivity;
import com.gowbing.kunzhong.ui.action.InitViews;
import com.gowbing.kunzhong.util.PreferenceUtils;
import com.gowbing.kunzhong.util.StatusBarUtil;
import com.gowbing.kunzhong.widget.LoadingProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.logging.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,InitViews {
    @Bind(R.id.username_et)
    EditText usernameEt;
    @Bind(R.id.password_et)
    EditText passwordEt;
    @Bind(R.id.login_bt)
    Button loginBt;
    @Bind(R.id.activity_login)
    LinearLayout activityLogin;
    protected Context mContext = null;
    private LoadingProgressDialog mProgressDialog;

    @Override
    public void bindListener() {
        loginBt.setOnClickListener(this);
    }

    @Override
    public void initData() {
        initPermission();

        usernameEt.setText(PreferenceUtils.getPrefString(mContext,"username",""));
        passwordEt.setText(PreferenceUtils.getPrefString(mContext,"password",""));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bt: {
                HashMap<String,String> params = new HashMap<>();
                params.put("username",usernameEt.getText().toString().trim());
                params.put("password",passwordEt.getText().toString().trim());
                getDataFromServer(Request.Method.POST,ServerUrl.LOGIN, params, UserResponse.class, new Response.Listener<UserResponse>() {
                    @Override
                    public void onResponse(UserResponse response) {
                        if(response.getStatus() == 1){
                            showToast("登录成功");
                            PreferenceUtils.setPrefString(mContext,"username",usernameEt.getText().toString().trim());
                            PreferenceUtils.setPrefString(mContext,"password",passwordEt.getText().toString().trim());
                            PreferenceUtils.setPrefString(mContext,"islogin","1");
                            AppApplication.getInstance().setUserinfo(response.getResults());
                            startActivity(new Intent(mContext,MainActivity.class));
                            finish();
                        }else{
                            showToast(response.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                break;
            }
        }
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                    this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义)
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, BAIDU_PERMISSION);
            }
        }
    }
    private static final int BAIDU_PERMISSION = 100;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case BAIDU_PERMISSION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] != PackageManager.PERMISSION_GRANTED
                        || grantResults[2] != PackageManager.PERMISSION_GRANTED) {
//                    showToast("获取权限失败！");
//                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

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

    public <T> void getDataFromServer(int method, String url, Class<T> clazz, Response.Listener<T> listener,
                                      Response.ErrorListener errorListener) {
        ATTJsonRequest<T> qBaoJsonRequest = new ATTJsonRequest<T>(method, url, clazz, listener, errorListener);
        executeRequest(qBaoJsonRequest);
    }

    public <T> void getDataFromServer(int method, String url, HashMap<String, String> params,
                                      Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        ATTJsonRequest<T> qBaoJsonRequest = new ATTJsonRequest<T>(method, url, clazz, listener, errorListener);
        if (params != null) {
            qBaoJsonRequest.addParams(params);
        }
        Log.i("http",url);
        executeRequest(qBaoJsonRequest);
    }

    public <T> void getDataFromServer(String url, JSONObject jsonRequest,
                                      Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        ATTJsonRequest<T> attJsonRequest = new ATTJsonRequest<T>(url, clazz, jsonRequest, listener, errorListener);
        executeRequest(attJsonRequest);
    }

    public void getImageFromServer(String url, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener) {
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
