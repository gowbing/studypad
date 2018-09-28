package com.gowbing.kunzhong.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gowbing.kunzhong.AppApplication;
import com.gowbing.kunzhong.R;
import com.gowbing.kunzhong.net.ServerUrl;
import com.gowbing.kunzhong.ui.BaseFragment;
import com.gowbing.kunzhong.util.StringUtils;
import com.gowbing.kunzhong.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-8-22.
 */

public class UpdatePasswordFragment extends BaseFragment implements View.OnClickListener{

    @Bind(R.id.old_password_et)
    EditText oldPasswordEt;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.new_password_et)
    EditText newPasswordEt;
    @Bind(R.id.comfirm_password_et)
    EditText comfirmPasswordEt;
    @Bind(R.id.submit_bt)
    Button submitBt;

    @Override
    public void bindListener() {
        submitBt.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_update_password;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_bt:{
                if(StringUtils.isEmpty(oldPasswordEt.getText().toString().trim())){
                    ToastUtils.showToast("请输入旧密码",mContext);
                    break;
                }
                if(StringUtils.isEmpty(newPasswordEt.getText().toString().trim())){
                    ToastUtils.showToast("请输入新密码",mContext);
                    break;
                }
                if(!newPasswordEt.getText().toString().trim().equals(comfirmPasswordEt.getText().toString().trim())){
                    ToastUtils.showToast("两次新密码输入不正确",mContext);
                    break;
                }

                HashMap<String,String> params = new HashMap<>();
                params.put("old_password",oldPasswordEt.getText().toString().trim());
                params.put("new_password",newPasswordEt.getText().toString().trim());
                params.put("id", AppApplication.getInstance().getUserinfo().getId() + "");
                getDataFromServer(Request.Method.POST, ServerUrl.CHANGE_PASSWORD, params, JSONObject.class, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ToastUtils.showToast(response.getString("message"),mContext);
                            if(response.optInt("status") == 1){
                                oldPasswordEt.setText("");
                                newPasswordEt.setText("");
                                comfirmPasswordEt.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
}
