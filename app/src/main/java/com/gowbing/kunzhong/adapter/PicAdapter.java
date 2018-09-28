package com.gowbing.kunzhong.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gowbing.kunzhong.R;
import com.gowbing.kunzhong.util.FileUtils2;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-8-27.
 */

public class PicAdapter extends BaseAdapter {
    private List<String> list = new ArrayList<>();
    private int type;//0:浏览,1:编辑

    public void setType(int type) {
        this.type = type;
    }

    private Context mContext;

    public PicAdapter(Context context) {
        mContext = context;
    }

    public void setList(List<String> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pic, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ("upload".equals(list.get(position))) {
            holder.picRl.setVisibility(View.GONE);
            holder.picIv.setVisibility(View.GONE);
            holder.uploadLl.setVisibility(View.VISIBLE);
        } else {
            holder.picRl.setVisibility(View.VISIBLE);
            holder.picIv.setVisibility(View.VISIBLE);
            holder.uploadLl.setVisibility(View.GONE);

            if ("application".equals(FileUtils2.getMIMEType(list.get(position)).split("/")[0])) {
                holder.picIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_doc));
            } else if ("video".equals(FileUtils2.getMIMEType(list.get(position)).split("/")[0])) {
                holder.picIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_video));
            } else if ("audio".equals(FileUtils2.getMIMEType(list.get(position)).split("/")[0])) {
                holder.picIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_media));
            } else if ("image".equals(FileUtils2.getMIMEType(list.get(position)).split("/")[0])) {
                holder.picIv.setImageURI(Uri.parse(list.get(position)));
            } else {
                holder.picIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_doc));
            }
            if (type == 1) {
                holder.delIv.setVisibility(View.VISIBLE);
                holder.delIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });
            } else {
                holder.delIv.setVisibility(View.GONE);
            }
        }

//        holder.picIv.setImageURI(Uri.parse(list.get(position)));
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.pic_iv)
        SimpleDraweeView picIv;
        @Bind(R.id.del_iv)
        ImageView delIv;
        @Bind(R.id.pic_rl)
        RelativeLayout picRl;
        @Bind(R.id.upload_ll)
        LinearLayout uploadLl;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
