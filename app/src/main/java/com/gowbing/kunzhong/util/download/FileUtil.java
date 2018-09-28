package com.gowbing.kunzhong.util.download;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.gowbing.kunzhong.constants.PreferenceConstants;
import com.gowbing.kunzhong.model.DownloadPercent;
import com.gowbing.kunzhong.util.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hss on 2017/1/16.
 */

public class FileUtil {
    public final static String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    public final static String VIDEOPATH = SDPATH + "kunzhong/file/";
    private double length;
    private double current = 0;
    private String add;
    private Context mContext;
    private int position;

    public FileUtil(Context context, String add, int i) {
        this.add = add;
        this.mContext = context;
        this.position = i;
        //得到SD卡的目录，如：“sdcard/”
    }

    /**
     * 在SD卡的指定目录上创建文件
     *
     * @param fileName
     */
    public File createFile(String fileName) {
        File file = new File(SDPATH + fileName);
        Log.i("FileUtil", SDPATH + fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 在SD卡上创建指定名称的目录
     *
     * @param dirName
     */
    public File createDir(String dirName) {
        File file = new File(SDPATH + dirName);

        Log.i("FileUtil", "dir:" + SDPATH + dirName);
        file.mkdirs();
        return file;
    }

    /**
     * 判断指定名称的文件在SD卡上是否存在
     *
     * @param fileName
     * @return
     */
    public boolean isExist(String dirName, String fileName) {
        File file = new File(SDPATH + dirName + fileName);
        return file.exists();
    }

    /**
     * 通过URL得到HttpURLConnection，通过HttpURLConnection得到InputStream
     *
     * @param urlStr
     * @return
     */
    public InputStream getIS(String urlStr) {
        URL url = null;
        HttpURLConnection urlConn = null;

        InputStream is = null;
        try {
            url = new URL(urlStr);
            urlConn = (HttpURLConnection) url.openConnection();
            length = urlConn.getContentLength();
            is = urlConn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    /**
     * 由得到的输入流把下载的文件写入到SD卡的指定位置
     *
     * @param is
     * @param dirName
     * @param fileName
     * @return
     */
    public File IS2SD(InputStream is, String dirName, String fileName) {
        OutputStream os = null;
        File file = null;
        try {
            createDir(dirName);
            file = createFile(dirName + fileName);
            os = new FileOutputStream(file);
            byte buffer[] = new byte[1024 * 4];
            int temp = 0;
            while ((temp = is.read(buffer)) != -1) {
                os.write(buffer, 0, temp);
                current += temp;
                int percent = (int) (current / length * 100);
                if (percent % 10 == 0) {
                    Gson gson = new Gson();
                    Log.i("---2----" , PreferenceUtils.getPrefString(mContext, PreferenceConstants.KEY_VIDEO_DOWN_LOAD_URL + add, ""));
                    DownloadPercent downloadPercent = gson.fromJson(
                            PreferenceUtils.getPrefString(mContext, PreferenceConstants.KEY_VIDEO_DOWN_LOAD_URL + add, ""),
                            DownloadPercent.class);
                    JSONObject object = new JSONObject();
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < downloadPercent.getItem().size(); i++) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("url", downloadPercent.getItem().get(i).getUrl());
                        if (i < position) {
                            map.put("percent", 100);
                        } else if (i == position) {
                            map.put("percent", percent);
                        } else {
                            map.put("percent", 0);
                        }
                        JSONObject obj = new JSONObject(map);
                        array.put(obj);
                    }
                    object.put("item", array);
                    Log.d("FileUtil","--------" + object.toString() + "---------------");
                    PreferenceUtils.setPrefString(mContext,
                            PreferenceConstants.KEY_VIDEO_DOWN_LOAD_URL + add,
                            object.toString());
                    Log.i("FileUtil","----preference_key----" + PreferenceConstants.KEY_VIDEO_DOWN_LOAD_URL + add);
                }
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();

            String s = "";
            for (StackTraceElement traceElement : e.getStackTrace()) {
                s += "\n" + traceElement.toString();
            }

            Map<String, String> stringErrorMap = new HashMap<>();
            stringErrorMap.put("type", "download");
            stringErrorMap.put("error_message", e.getMessage());
            stringErrorMap.put("error_line", s);

            JSONObject object = new JSONObject(stringErrorMap);

        } finally {
            try {
                os.close();
            } catch (Exception e) {
                e.printStackTrace();

                String s = "";
                for (StackTraceElement traceElement : e.getStackTrace()) {
                    s += "\n" + traceElement.toString();
                }

                Map<String, String> stringErrorMap = new HashMap<>();
                stringErrorMap.put("type", "download");
                stringErrorMap.put("error_message", e.getMessage());
                stringErrorMap.put("error_line", s);

                JSONObject object = new JSONObject(stringErrorMap);

            }
        }
        return file;
    }

}
