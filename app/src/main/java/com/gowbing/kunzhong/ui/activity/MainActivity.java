package com.gowbing.kunzhong.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gowbing.kunzhong.AppApplication;
import com.gowbing.kunzhong.R;
import com.gowbing.kunzhong.listener.MainListener;
import com.gowbing.kunzhong.model.User;
import com.gowbing.kunzhong.net.ServerUrl;
import com.gowbing.kunzhong.ui.BaseActivity;
import com.gowbing.kunzhong.ui.fragment.UpdatePasswordFragment;
import com.gowbing.kunzhong.ui.fragment.ZuoyeFragment;
import com.gowbing.kunzhong.view.radiobutton.MyRadioButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-8-20.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener,MainListener{
    @Bind(R.id.banji_ll)
    LinearLayout banjiLl;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.rb_zuixinzuoye)
    MyRadioButton rbZuixinzuoye;
    @Bind(R.id.rb_wangqizuoye)
    MyRadioButton rbWangqizuoye;
    @Bind(R.id.rb_xuexiziliao)
    RadioButton rbXuexiziliao;
    @Bind(R.id.rb_xiugaimima)
    RadioButton rbXiugaimima;
    @Bind(R.id.rg_menu)
    RadioGroup rgMenu;
    @Bind(R.id.main_ll_1)
    LinearLayout mainLl1;
    @Bind(R.id.main_ll_2)
    LinearLayout mainLl2;
    @Bind(R.id.main_ll_3)
    LinearLayout mainLl3;
    @Bind(R.id.main_ll_4)
    LinearLayout mainLl4;

    FragmentManager manager = null;
    @Bind(R.id.class_tv)
    TextView classTv;
    @Bind(R.id.topname_tv)
    TextView topnameTv;
    @Bind(R.id.name_tv)
    TextView nameTv;
    @Bind(R.id.exit_tv)
    TextView exitTv;
    @Bind(R.id.exit_ll)
    LinearLayout exitLl;

    private int zuixinNum;
    private int wangqiNum;

    @Override
    public void bindListener() {
        exitLl.setOnClickListener(this);
        exitTv.setOnClickListener(this);
        banjiLl.setOnClickListener(this);
        rbZuixinzuoye.setOnClickListener(this);
        rbWangqizuoye.setOnClickListener(this);
        rbXuexiziliao.setOnClickListener(this);
        rbXiugaimima.setOnClickListener(this);
        rgMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_zuixinzuoye: {

                        rbZuixinzuoye.setBackgroundColor(getResources().getColor(R.color.lightblue));
                        rbWangqizuoye.setBackgroundColor(getResources().getColor(R.color.white));
                        rbXuexiziliao.setBackgroundColor(getResources().getColor(R.color.white));
                        rbXiugaimima.setBackgroundColor(getResources().getColor(R.color.white));

                        mainLl1.setVisibility(View.VISIBLE);
                        mainLl2.setVisibility(View.GONE);
                        mainLl3.setVisibility(View.GONE);
                        mainLl4.setVisibility(View.GONE);
                        break;
                    }
                    case R.id.rb_wangqizuoye: {
                        rbZuixinzuoye.setBackgroundColor(getResources().getColor(R.color.white));
                        rbWangqizuoye.setBackgroundColor(getResources().getColor(R.color.lightblue));
                        rbXuexiziliao.setBackgroundColor(getResources().getColor(R.color.white));
                        rbXiugaimima.setBackgroundColor(getResources().getColor(R.color.white));

                        mainLl1.setVisibility(View.GONE);
                        mainLl2.setVisibility(View.VISIBLE);
                        mainLl3.setVisibility(View.GONE);
                        mainLl4.setVisibility(View.GONE);
                        break;
                    }
                    case R.id.rb_xuexiziliao: {
                        rbZuixinzuoye.setBackgroundColor(getResources().getColor(R.color.white));
                        rbWangqizuoye.setBackgroundColor(getResources().getColor(R.color.white));
                        rbXuexiziliao.setBackgroundColor(getResources().getColor(R.color.lightblue));
                        rbXiugaimima.setBackgroundColor(getResources().getColor(R.color.white));

                        mainLl1.setVisibility(View.GONE);
                        mainLl2.setVisibility(View.GONE);
                        mainLl3.setVisibility(View.VISIBLE);
                        mainLl4.setVisibility(View.GONE);
                        break;
                    }
                    case R.id.rb_xiugaimima:
                        rbZuixinzuoye.setBackgroundColor(getResources().getColor(R.color.white));
                        rbWangqizuoye.setBackgroundColor(getResources().getColor(R.color.white));
                        rbXuexiziliao.setBackgroundColor(getResources().getColor(R.color.white));
                        rbXiugaimima.setBackgroundColor(getResources().getColor(R.color.lightblue));

                        mainLl1.setVisibility(View.GONE);
                        mainLl2.setVisibility(View.GONE);
                        mainLl3.setVisibility(View.GONE);
                        mainLl4.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        AppApplication.getInstance().setMainListener(this);
        mainLl1.setVisibility(View.VISIBLE);
        manager = getSupportFragmentManager();
        ZuoyeFragment fragment = new ZuoyeFragment();
        FragmentTransaction ft1 = manager.beginTransaction();
        ft1.add(R.id.main_ll_1, fragment);
        ft1.commit();

        User user = AppApplication.getInstance().getUserinfo();
        topnameTv.setText(user.getStudent_name().substring(0, 1));
        nameTv.setText(user.getStudent_name());
        classTv.setText(user.getClass_name());

        HashMap<String, String> params = new HashMap<>();
        params.put("class_id",AppApplication.getInstance().getUserinfo().getClass_id());
        params.put("student id", AppApplication.getInstance().getUserinfo().getId() + "");
        getDataFromServer(Request.Method.POST, ServerUrl.GET_LAST_HOMEWORK_NUM, params, JSONObject.class, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (Integer.parseInt(response.getJSONObject("results").getString("num")) == 0) {
                        rbZuixinzuoye.setNumberDot(false,"0");
                        return;
                    }
                    zuixinNum = response.getJSONObject("results").getInt("num");
                    rbZuixinzuoye.setNumberDot(true, response.getJSONObject("results").getString("num"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        getDataFromServer(Request.Method.POST, ServerUrl.GET_PREVIOUS_HOMEWORK_NUM, params, JSONObject.class, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (Integer.parseInt(response.getJSONObject("results").getString("num")) == 0) {
                        rbWangqizuoye.setNumberDot(false,"0");
                        return;
                    }
                    wangqiNum = response.getJSONObject("results").getInt("num");
                    rbWangqizuoye.setNumberDot(true, response.getJSONObject("results").getString("num"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banji_ll:{
                exitLl.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.exit_ll:{
                exitLl.setVisibility(View.GONE);
                break;
            }
            case R.id.exit_tv:{
                if(AppApplication.getInstance().getListener() != null){
                   AlertDialog alert = new AlertDialog.Builder(this).setTitle("取消").setMessage("是否需要保存编辑内容？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(mContext,LoginActivity.class));
                            finish();
                            AppApplication.getInstance().setListener(null);
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppApplication.getInstance().getListener().onSave();
                            startActivity(new Intent(mContext,LoginActivity.class));
                            finish();
                            AppApplication.getInstance().setListener(null);
                        }
                    }).create();
                    alert.setCanceledOnTouchOutside(false);
                    alert.show();
                }else{
                    startActivity(new Intent(mContext,LoginActivity.class));
                    finish();
                }
                break;
            }
            case R.id.rb_zuixinzuoye:{
                if(AppApplication.getInstance().getListener() != null){
                    AlertDialog alert = new AlertDialog.Builder(this).setTitle("取消").setMessage("是否需要保存编辑内容？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppApplication.getInstance().setListener(null);
                            ZuoyeFragment fragment = new ZuoyeFragment();
                            FragmentTransaction ft1 = manager.beginTransaction();
                            ft1.replace(R.id.main_ll_1, fragment);
                            ft1.commit();
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppApplication.getInstance().getListener().onSave();
                            ZuoyeFragment fragment = new ZuoyeFragment();
                            FragmentTransaction ft1 = manager.beginTransaction();
                            ft1.replace(R.id.main_ll_1, fragment);
                            ft1.commit();
                            AppApplication.getInstance().setListener(null);
                        }
                    }).create();
                    alert.setCanceledOnTouchOutside(false);
                    alert.show();
                }else{
                    ZuoyeFragment fragment = new ZuoyeFragment();
                    FragmentTransaction ft1 = manager.beginTransaction();
                    ft1.replace(R.id.main_ll_1, fragment);
                    ft1.commit();
                }

                break;
            }
            case R.id.rb_wangqizuoye:{
                if(AppApplication.getInstance().getListener() != null){
                    AlertDialog alert = new AlertDialog.Builder(this).setTitle("取消").setMessage("是否需要保存编辑内容？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppApplication.getInstance().setListener(null);
                            ZuoyeFragment fragment = new ZuoyeFragment();
                            fragment.setType(1);
                            FragmentTransaction ft1 = manager.beginTransaction();
                            ft1.replace(R.id.main_ll_2, fragment);
                            ft1.commit();
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppApplication.getInstance().getListener().onSave();
                            ZuoyeFragment fragment = new ZuoyeFragment();
                            fragment.setType(1);
                            FragmentTransaction ft1 = manager.beginTransaction();
                            ft1.replace(R.id.main_ll_2, fragment);
                            ft1.commit();
                            AppApplication.getInstance().setListener(null);
                        }
                    }).create();
                    alert.setCanceledOnTouchOutside(false);
                    alert.show();
                }else{
                    ZuoyeFragment fragment = new ZuoyeFragment();
                    fragment.setType(1);
                    FragmentTransaction ft1 = manager.beginTransaction();
                    ft1.replace(R.id.main_ll_2, fragment);
                    ft1.commit();
                }

                break;
            }
            case R.id.rb_xuexiziliao:{
                if(AppApplication.getInstance().getListener() != null){
                    AlertDialog alert = new AlertDialog.Builder(this).setTitle("取消").setMessage("是否需要保存编辑内容？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppApplication.getInstance().setListener(null);
                            ZuoyeFragment fragment = new ZuoyeFragment();
                            fragment.setType(2);
                            FragmentTransaction ft1 = manager.beginTransaction();
                            ft1.replace(R.id.main_ll_3, fragment);
                            ft1.commit();
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppApplication.getInstance().getListener().onSave();
                            ZuoyeFragment fragment = new ZuoyeFragment();
                            fragment.setType(2);
                            FragmentTransaction ft1 = manager.beginTransaction();
                            ft1.replace(R.id.main_ll_3, fragment);
                            ft1.commit();
                            AppApplication.getInstance().setListener(null);
                        }
                    }).create();
                    alert.setCanceledOnTouchOutside(false);
                    alert.show();
                }else{
                    ZuoyeFragment fragment = new ZuoyeFragment();
                    fragment.setType(2);
                    FragmentTransaction ft1 = manager.beginTransaction();
                    ft1.replace(R.id.main_ll_3, fragment);
                    ft1.commit();
                }

                break;
            }
            case R.id.rb_xiugaimima:{
                if(AppApplication.getInstance().getListener() != null){
                    AlertDialog alert = new AlertDialog.Builder(this).setTitle("取消").setMessage("是否需要保存编辑内容？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppApplication.getInstance().setListener(null);
                            UpdatePasswordFragment fragment = new UpdatePasswordFragment();
                            FragmentTransaction ft1 = manager.beginTransaction();
                            ft1.replace(R.id.main_ll_4, fragment);
                            ft1.commit();
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppApplication.getInstance().getListener().onSave();
                            UpdatePasswordFragment fragment = new UpdatePasswordFragment();
                            FragmentTransaction ft1 = manager.beginTransaction();
                            ft1.replace(R.id.main_ll_4, fragment);
                            ft1.commit();
                            AppApplication.getInstance().setListener(null);
                        }
                    }).create();
                    alert.setCanceledOnTouchOutside(false);
                    alert.show();
                }else{
                    UpdatePasswordFragment fragment = new UpdatePasswordFragment();
                    FragmentTransaction ft1 = manager.beginTransaction();
                    ft1.replace(R.id.main_ll_4, fragment);
                    ft1.commit();
                }

                break;
            }
        }
    }

    @Override
    public void onHomeworkSubmit(int type) {
        if(type == 0){
            zuixinNum--;
            if(zuixinNum <= 0){
                rbZuixinzuoye.setNumberDot(false, "0");
            }else{
                rbZuixinzuoye.setNumberDot(true,zuixinNum + "");
            }
        }
        if(type == 1){
            wangqiNum--;
            if(wangqiNum <= 0){
                rbWangqizuoye.setNumberDot(false, "0");
            }else{
                rbWangqizuoye.setNumberDot(true,wangqiNum + "");
            }
        }
    }

    @Override
    public void onBackPressed() {
        onExit();
    }

    /**
     * @author Selpro
     * @desc 用户点击返回跳出 退出的弹窗
     */
    private void onExit() {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle("退出")
                .setMessage("您要现在退出应用吗？")
                .setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                startActivity(intent);
                            }
                        })
                .setNegativeButton("否",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //不退出
                            }
                        }).create().show();
    }
}
