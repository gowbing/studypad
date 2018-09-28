package com.gowbing.kunzhong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gowbing.kunzhong.R;
import com.gowbing.kunzhong.model.Subject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-8-25.
 */

public class TabAdapter extends BaseAdapter {

    private List<Subject> list = new ArrayList<>();
    private Context mContext;

    public TabAdapter(Context context) {
        mContext = context;
    }

    public void setList(List<Subject> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tab, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tabTv.setText(list.get(position).getSubject_name());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tab_tv)
        TextView tabTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
