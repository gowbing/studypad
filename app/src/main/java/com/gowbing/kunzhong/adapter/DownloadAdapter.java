package com.gowbing.kunzhong.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.gowbing.kunzhong.R;
import com.gowbing.kunzhong.constants.PreferenceConstants;
import com.gowbing.kunzhong.model.DownloadPercent;
import com.gowbing.kunzhong.model.Pic;
import com.gowbing.kunzhong.util.FileUtils;
import com.gowbing.kunzhong.util.FileUtils2;
import com.gowbing.kunzhong.util.PreferenceUtils;
import com.gowbing.kunzhong.util.ToastUtils;
import com.gowbing.kunzhong.util.download.FileDownloader;
import com.gowbing.kunzhong.util.download.FileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018-8-28.
 */

public class DownloadAdapter extends BaseAdapter {

    private Context mContext;
    private List<Pic> list = new ArrayList<>();
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public void setList(List<Pic> list) {
        this.list = list;
    }

    public DownloadAdapter(Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_download, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Pic pic = list.get(position);
        String[] picpoint = pic.getImg().split("\\.");
        if("application".equals(FileUtils2.getMIMEType(list.get(position).getImg()).split("/")[0])){
            holder.picIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_doc));
            holder.typeTv.setText(equals(picpoint[picpoint.length - 1]) + "文档文件");
        }else if("video".equals(FileUtils2.getMIMEType(list.get(position).getImg()).split("/")[0])){
            holder.picIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_video));
            holder.typeTv.setText(equals(picpoint[picpoint.length - 1]) + "视频文件");
        }else if("audio".equals(FileUtils2.getMIMEType(list.get(position).getImg()).split("/")[0])){
            holder.picIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_media));
            holder.typeTv.setText(equals(picpoint[picpoint.length - 1]) + "音频文件");
        }else if("image".equals(FileUtils2.getMIMEType(list.get(position).getImg()).split("/")[0])){
            holder.typeTv.setText("." + picpoint[picpoint.length - 1] + "图片文件");
            holder.picIv.setBackground(null);
            holder.picIv.setImageURI(Uri.parse(pic.getImg()));
        }else{
            holder.picIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_doc));
            holder.typeTv.setText(equals(picpoint[picpoint.length - 1]) + "文件");
        }
        holder.titleTv.setText(pic.getTitle());
        holder.downloadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download(list.get(position).getImg());
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil fileUtil = new FileUtil(mContext,"", 0);
                String name = getBac(list.get(position).getImg());
                if (fileUtil.isExist("kunzhong/file/", name)) {
//                    FileUtils.openFile(mContext,"kunzhong/file/", name);
                    FileUtils2.show(mContext,FileUtil.SDPATH + "kunzhong/file/" + name);
                }else{
                    download(list.get(position).getImg());
                }

            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.pic_iv)
        SimpleDraweeView picIv;
        @Bind(R.id.title_tv)
        TextView titleTv;
        @Bind(R.id.type_tv)
        TextView typeTv;
        @Bind(R.id.download_iv)
        ImageView downloadIv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private String getBac(String s) {
        String[] results = s.split("/");
        String result = results[results.length - 1];
        return result;
    }

    private void download( final String videoUrl) {
        String name = getBac(videoUrl);
        FileUtil fileUtil = new FileUtil(mContext,"", 0);
        if (!fileUtil.isExist("kunzhong/file/", name)) {
            ToastUtils.showToast("开始下载", mContext);
        }
        new Thread(new Runnable() {
            public void run() {
                Gson gson = new Gson();
                JSONArray object = new JSONArray();
                Map<String, Object> map1 = new HashMap<>();
                map1.put("url", "kunzhong/file/" + getBac(videoUrl));
                map1.put("percent", 0);
                JSONObject obj1 = new JSONObject(map1);
                object.put(obj1);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("item", object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                PreferenceUtils.setPrefString(mContext,
                        PreferenceConstants.KEY_VIDEO_DOWN_LOAD_URL,
                        jsonObject.toString());



                String name = getBac(videoUrl);
                int result = new FileDownloader(mContext, "", 0).downloadFile("kunzhong/file/", name, videoUrl);
                Message message;
                switch (result) {
                    case 0:
                    {
                        message = new Message();
                        message.what = 2;
                        message.obj = "下载成功,您可以在pad文件管理中查看文件";
                        mHandler.sendMessage(message);
                        break;
                    }

                    case 1:
                    {
                        message = new Message();
                        message.what = 2;
                        message.obj = "文件已存在";
                        mHandler.sendMessage(message);

                        DownloadPercent downloadPercent = gson.fromJson(
                                PreferenceUtils.getPrefString(mContext, PreferenceConstants.KEY_VIDEO_DOWN_LOAD_URL , ""),
                                DownloadPercent.class);
                        JSONObject obj = new JSONObject();
                        JSONArray array = new JSONArray();
                        int i=0;
                        for (int j = 0; j < downloadPercent.getItem().size(); j++) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("url", downloadPercent.getItem().get(i).getUrl());
                            if (i == j) {
                                map.put("percent", 100);
                            } else {
                                map.put("percent", downloadPercent.getItem().get(i).getPercent());
                            }
                            JSONObject o = new JSONObject(map);
                            array.put(o);
                        }
                        try {
                            obj.put("item", array);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        PreferenceUtils.setPrefString(mContext,
                                PreferenceConstants.KEY_VIDEO_DOWN_LOAD_URL,
                                obj.toString());
                        break;
                    }
                    case 2:
                    {
                        message = new Message();
                        message.what = 2;
                        message.obj = "下载出错";
                        mHandler.sendMessage(message);
                        break;
                    }
                }
            }
        }).start();

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    notifyDataSetChanged();
                    mHandler.sendEmptyMessageDelayed(1, 1000);
                    break;
                }
                case 2:
                    ToastUtils.showToast((String) msg.obj,mContext);
                    break;
            }
        }
    };
}
