package com.gowbing.kunzhong.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gowbing.kunzhong.R;
import com.gowbing.kunzhong.ui.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-8-30.
 */

public class ImageActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.iv)
    SimpleDraweeView iv;

    @Override
    public void bindListener() {
        iv.setOnClickListener(this);
    }

    @Override
    public void initData() {
        iv.setImageURI(Uri.parse(getIntent().getStringExtra("url")));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv:{
                finish();
                break;
            }
        }
    }

}
