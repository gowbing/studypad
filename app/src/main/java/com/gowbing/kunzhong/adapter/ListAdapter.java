package com.gowbing.kunzhong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gowbing.kunzhong.R;
import com.gowbing.kunzhong.model.Homework;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-8-24.
 */

public class ListAdapter extends BaseAdapter {
    private List<Homework> list = new ArrayList<>();
    private Context mContext;

    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public void setList(List<Homework> list) {
        this.list = list;
    }

    public ListAdapter(Context context) {
        mContext = context;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_zuoye, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.titleTv.setText(list.get(position).getTitle());
        holder.subjectTv.setText(list.get(position).getSubject_name());
        if(type == 0){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            holder.timeTv.setText("作业截止时间：" + sdf.format(new Date(list.get(position).getEnd_time()*1000L)));
            holder.teacherTv.setText(list.get(position).getTeacher_name() + "   发布于:" + list.get(position).getPublish_date());
        }else{
            holder.timeTv.setText(list.get(position).getPublish_date());
            holder.teacherTv.setText(list.get(position).getTeacher_name());
        }

        if (type == 0 && list.get(position).getIs_done() == 0) {
            holder.circleView.setVisibility(View.VISIBLE);
        } else {
            holder.circleView.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.subject_tv)
        TextView subjectTv;
        @Bind(R.id.title_tv)
        TextView titleTv;
        @Bind(R.id.circle_view)
        View circleView;
        @Bind(R.id.time_tv)
        TextView timeTv;
        @Bind(R.id.teacher_tv)
        TextView teacherTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
