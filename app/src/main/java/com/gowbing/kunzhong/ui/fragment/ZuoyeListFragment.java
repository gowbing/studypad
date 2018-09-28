package com.gowbing.kunzhong.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.gowbing.kunzhong.AppApplication;
import com.gowbing.kunzhong.R;
import com.gowbing.kunzhong.adapter.ListAdapter;
import com.gowbing.kunzhong.model.Homework;
import com.gowbing.kunzhong.model.list.HomeworkList;
import com.gowbing.kunzhong.net.ServerUrl;
import com.gowbing.kunzhong.ui.BaseFragment;
import com.gowbing.kunzhong.util.StringUtils;
import com.gowbing.kunzhong.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-8-24.
 */

public class ZuoyeListFragment extends BaseFragment {

    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.refresh)
    MaterialRefreshLayout refresh;

    private int subject_id;
    private String search;
    private int type;

    public void setSearch(String search) {
        this.search = search;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    private int page = 1;
    private List<Homework> list = new ArrayList<>();
    private ListAdapter adapter;

    @Override
    public void bindListener() {
    }

    @Override
    public void initData() {

        Log.i("ZuoyeListFragment", "initData");
        adapter = new ListAdapter(mContext);
        if (type == 2) {
            adapter.setType(1);
        }
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (type == 2) {
                    LearnDetailFragment fragment = new LearnDetailFragment();
                    fragment.setId(list.get(position).getId());
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.main_ll_3, fragment).commit();
                } else {
                    ZuoyeInfoFragment fragment = new ZuoyeInfoFragment();
                    fragment.setType(type);
                    fragment.setId(list.get(position).getId());
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.beginTransaction().replace(type == 0 ? R.id.main_ll_1 : R.id.main_ll_2, fragment).commit();
                }
            }
        });

        refresh.setLoadMore(true);
//		contentView=new SearchListView(mContext);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener()
        {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout)
            {
                page = 1;
                try {
                    getinfo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout)
            {
                page++;
                try {
                    getinfo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            getinfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getinfo();
    }

    public void getinfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageSize", 10 + "");
        params.put("pageNum", page + "");
        params.put("class_id",AppApplication.getInstance().getUserinfo().getClass_id());
        params.put("student id", AppApplication.getInstance().getUserinfo().getId() + "");
        if (subject_id != 0) {
            params.put("subject_id", subject_id + "");
        }
        if (!StringUtils.isEmpty(search)) {
            params.put("keyword", search);
        }
        getDataFromServer(Request.Method.POST, type == 0 ? ServerUrl.GET_LAST_HOMEWORK : (type == 1 ? ServerUrl.GET_PREVIOUS_HOMEWORK : ServerUrl.GET_LEARN_INFO)
                , params, HomeworkList.class, new Response.Listener<HomeworkList>() {
                    @Override
                    public void onResponse(HomeworkList response) {
                        if(response.getStatus() == 1){
                            if(page == 1){
                                list = response.getResults();
                                refresh.finishRefresh();
                            }else{
                                list.addAll(response.getResults());
                                refresh.finishRefreshLoadMore();
                            }
                            adapter.setList(list);
                            adapter.notifyDataSetChanged();
                        }else{
                            ToastUtils.showToast(response.getMessage(),mContext);
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
        return R.layout.fragment_zuoye_list;
    }
}
