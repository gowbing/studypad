package com.gowbing.kunzhong.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gowbing.kunzhong.R;
import com.gowbing.kunzhong.adapter.SectionsPagerAdapter;
import com.gowbing.kunzhong.model.Subject;
import com.gowbing.kunzhong.model.list.SubjectList;
import com.gowbing.kunzhong.net.ServerUrl;
import com.gowbing.kunzhong.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.xiaopan.psts.PagerSlidingTabStrip;

/**
 * Created by Administrator on 2018-8-21.
 */

public class ZuoyeFragment extends BaseFragment {
    @Bind(R.id.tab_strip)
    PagerSlidingTabStrip tabStrip;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.type_tv)
    TextView typeTv;
    @Bind(R.id.tab_ll)
    LinearLayout tabLl;
    @Bind(R.id.search_et)
    EditText searchEt;

    private int type;  //0:最新作业，1：往期作业，2：学习资料
    private List<ZuoyeListFragment> fragmentList = new ArrayList<>();

    public void setType(int type) {
        this.type = type;
    }

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    public void bindListener() {
        searchEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //这里注意要作判断处理，ActionDown、ActionUp都会回调到这里，不作处理的话就会调用两次
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
                    //处理事件
                    for(ZuoyeListFragment fragment:fragmentList){
                        fragment.setSearch(searchEt.getText().toString());
                        fragment.getinfo();
                    }
                    return true;
                }
                return false;
            }
        });
//        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if(actionId == EditorInfo.IME_ACTION_SEARCH){
//                    for(ZuoyeListFragment fragment:fragmentList){
//                        fragment.setSearch(searchEt.getText().toString());
//                        fragment.getinfo();
//                    }
//                }
//                return false;
//            }
//        });
    }

    @Override
    public void initData() {
        if (type == 0) {
            typeTv.setText("最新作业");
            searchEt.setVisibility(View.GONE);
        } else if (type == 1) {
            typeTv.setText("往期作业");
            searchEt.setVisibility(View.GONE);
        } else {
            typeTv.setText("学习资料");
            searchEt.setVisibility(View.VISIBLE);
        }

        getDataFromServer(Request.Method.POST, ServerUrl.GET_SUBJECT_LIST, SubjectList.class, new Response.Listener<SubjectList>() {
            @Override
            public void onResponse(SubjectList response) {
                if (response.getStatus() == 1) {

                    Log.i("GET_SUBJECT_LIST", response.getMessage());

                    View baseView = LayoutInflater.from(mContext).inflate(R.layout.item_tab, null);
                    TextView baseTabTv = (TextView) baseView.findViewById(R.id.tab_tv);
                    baseTabTv.setText("全部");
                    tabLl.addView(baseView);
                    ZuoyeListFragment baseFragment = new ZuoyeListFragment();
                    baseFragment.setType(type);
                    fragmentList.add(baseFragment);
                    for (Subject item : response.getResults()) {
                        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tab, null);
                        TextView tabTv = (TextView) view.findViewById(R.id.tab_tv);
                        tabTv.setText(item.getSubject_name());
                        tabLl.addView(view);
                        ZuoyeListFragment fragment = new ZuoyeListFragment();
                        fragment.setType(type);
                        fragment.setSubject_id(item.getId());
                        fragmentList.add(fragment);
                    }
                    mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), fragmentList);
                    viewPager.setAdapter(mSectionsPagerAdapter);
                    viewPager.setOffscreenPageLimit(fragmentList.size() - 1);
                    tabStrip.setViewPager(viewPager);
                    tabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        }

                        @Override
                        public void onPageSelected(int position) {
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                } else {
                    showToast(response.getMessage());
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
        return R.layout.fragment_zuoye_main;
    }

}
