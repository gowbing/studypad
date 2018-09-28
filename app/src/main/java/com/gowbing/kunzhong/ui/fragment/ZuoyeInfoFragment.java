package com.gowbing.kunzhong.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.gowbing.kunzhong.AppApplication;
import com.gowbing.kunzhong.R;
import com.gowbing.kunzhong.adapter.PicAdapter;
import com.gowbing.kunzhong.constants.PreferenceConstants;
import com.gowbing.kunzhong.listener.DetailListener;
import com.gowbing.kunzhong.model.DownloadPercent;
import com.gowbing.kunzhong.model.Homework;
import com.gowbing.kunzhong.model.detail.HomeworkDetail;
import com.gowbing.kunzhong.net.ServerUrl;
import com.gowbing.kunzhong.ui.BaseFragment;
import com.gowbing.kunzhong.ui.activity.ImageActivity;
import com.gowbing.kunzhong.util.Base64BitmapUtil;
import com.gowbing.kunzhong.util.FileUtils;
import com.gowbing.kunzhong.util.FileUtils2;
import com.gowbing.kunzhong.util.PreferenceUtils;
import com.gowbing.kunzhong.util.StringUtils;
import com.gowbing.kunzhong.util.ToastUtils;
import com.gowbing.kunzhong.util.download.FileDownloader;
import com.gowbing.kunzhong.util.download.FileUtil;
import com.gowbing.kunzhong.util.uploadhead.ImageUtils;
import com.gowbing.kunzhong.view.gridview.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Administrator on 2018-8-25.
 */

public class ZuoyeInfoFragment extends BaseFragment implements View.OnClickListener, DetailListener {
    @Bind(R.id.subject_tv)
    TextView subjectTv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.imageView2)
    ImageView imageView2;
    @Bind(R.id.time_tv)
    TextView timeTv;
    @Bind(R.id.teacher_tv)
    TextView teacherTv;
    @Bind(R.id.zuoye_zhankai_tv)
    TextView zuoyeZhankaiTv;
    @Bind(R.id.stu_name_tv)
    TextView stuNameTv;
    @Bind(R.id.stu_class_tv)
    TextView stuClassTv;
    @Bind(R.id.stu_time_tv)
    TextView stuTimeTv;
    @Bind(R.id.pic_gv)
    MyGridView picGv;
    @Bind(R.id.answer_zhankai_tv)
    TextView answerZhankaiTv;
    @Bind(R.id.info_ll)
    LinearLayout infoLl;
    @Bind(R.id.comment_tv)
    TextView commentTv;
    @Bind(R.id.comment_ll)
    LinearLayout commentLl;
    @Bind(R.id.editor_wb)
    WebView editorWb;
    @Bind(R.id.upload_pic_gv)
    MyGridView uploadPicGv;
    @Bind(R.id.submit_ll)
    LinearLayout submitLl;
    @Bind(R.id.edit_ll)
    LinearLayout editLl;
    @Bind(R.id.detail_ll)
    LinearLayout detailLl;
    @Bind(R.id.content_wv)
    WebView contentWv;
    @Bind(R.id.answer_info_wv)
    WebView answerInfoWv;
    @Bind(R.id.question_pic_gv)
    MyGridView questionPicGv;

    private List<String> picList = new ArrayList<>();
    private PicAdapter adapter;
    private PicAdapter contentAdapter;
    private PicAdapter questionAdapter;

    private int id;
    private int type;
    private Homework item;

    public void setId(int id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void bindListener() {
        answerZhankaiTv.setOnClickListener(this);
        zuoyeZhankaiTv.setOnClickListener(this);
        submitLl.setOnClickListener(this);
    }

    @JavascriptInterface
    @Override
    public void initData() {
        adapter = new PicAdapter(mContext);
        adapter.setList(picList);
        adapter.setType(1);

        contentAdapter = new PicAdapter(mContext);
        contentAdapter.setList(new ArrayList<String>());

        questionAdapter = new PicAdapter(mContext);
        questionAdapter.setList(new ArrayList<String>());

        HashMap<String, String> params = new HashMap<>();
        params.put("id", id + "");
        params.put("student_id", AppApplication.getInstance().getUserinfo().getId() + "");
        getDataFromServer(Request.Method.POST, ServerUrl.GET_HOMEWORK_DETAIL, params, HomeworkDetail.class, new Response.Listener<HomeworkDetail>() {

            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(HomeworkDetail response) {
                if (response.getStatus() != 1) {
                    ToastUtils.showToast(response.getMessage(), mContext);
                    return;
                }
                item = response.getResults();
                if (item.getAnswer_status() != 0) {//详情
                    infoLl.setVisibility(View.VISIBLE);
                    commentLl.setVisibility(View.VISIBLE);
                    detailLl.setVisibility(View.GONE);
                    editLl.setVisibility(View.GONE);
                    commentTv.setText(item.getTeacher_comment());
                    stuNameTv.setText(item.getStudent_name());
//                    answerInfo.setText(item.getAnswer_content());
                    webViewSetting(answerInfoWv);
                    answerInfoWv.loadUrl("file:///android_asset/test.html");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    stuTimeTv.setText(sdf.format(new Date(item.getAnswer_create_time() * 1000L)));
                    if(!StringUtils.isEmpty(item.getAnswer_pics())){
                        contentAdapter.setList(Arrays.asList(item.getAnswer_pics().split(",")));
                    }
                    picGv.setAdapter(contentAdapter);

                    picGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if("image".equals(FileUtils2.getMIMEType(item.getAnswer_pics().split(",")[position]).split("/")[0])){
                                Intent intent = new Intent(mContext, ImageActivity.class);
                                intent.putExtra("url",item.getAnswer_pics().split(",")[position]);
                                startActivity(intent);
                            }else{
                                FileUtil fileUtil = new FileUtil(mContext,"", 0);
                                String name = getBac(item.getAnswer_pics().split(",")[position]);
                                if (fileUtil.isExist("kunzhong/file/", name)) {
                                    FileUtils2.show(mContext,FileUtil.SDPATH + "kunzhong/file/" + name);
                                }else{
                                    download(item.getAnswer_pics().split(",")[position]);
                                }
                            }
                        }
                    });
                } else {//写作业
                    AppApplication.getInstance().setListener(ZuoyeInfoFragment.this);
                    infoLl.setVisibility(View.GONE);
                    commentLl.setVisibility(View.GONE);
                    editLl.setVisibility(View.VISIBLE);
                    detailLl.setVisibility(View.VISIBLE);
                    webViewSetting(editorWb);
                    editorWb.loadUrl("file:///android_asset/editor/index.html");
//                    editorWb.loadUrl("file:///android_asset/up.html");

                    zuoyeZhankaiTv.setVisibility(View.GONE);

                    if(!StringUtils.isEmpty(item.getAnswer_pics())){
                        picList.addAll(Arrays.asList(item.getAnswer_pics().split(",")));
                    }
                    picList.add("upload");
                    adapter.setList(picList);
                    uploadPicGv.setAdapter(adapter);
                    uploadPicGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if ("upload".equals(picList.get(position))) {//上传图片
                                heedImageClick();
                            }else{
                                if("image".equals(FileUtils2.getMIMEType(picList.get(position)).split("/")[0])){
                                    Intent intent = new Intent(mContext, ImageActivity.class);
                                    intent.putExtra("url",picList.get(position));
                                    startActivity(intent);
                                }else{
                                    FileUtil fileUtil = new FileUtil(mContext,"", 0);
                                    String name = getBac(picList.get(position));
                                    if (fileUtil.isExist("kunzhong/file/", name)) {
                                        FileUtils2.show(mContext,FileUtil.SDPATH + "kunzhong/file/" + name);
                                    }else{
                                        download(picList.get(position));
                                    }
                                }

                            }
                        }
                    });
                }
                subjectTv.setText(item.getSubject_name());
                titleTv.setText(item.getTitle());
                teacherTv.setText(item.getTeacher_name());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                timeTv.setText(item.getPublish_date() + "到" + sdf.format(new Date(item.getEnd_time() * 1000L)));
                if(!StringUtils.isEmpty(item.getPics())){
                    questionAdapter.setList(Arrays.asList(item.getPics().split(",")));
                }
                questionPicGv.setAdapter(questionAdapter);
                questionPicGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if("image".equals(FileUtils2.getMIMEType(item.getPics().split(",")[position]).split("/")[0])){
                            Intent intent = new Intent(mContext, ImageActivity.class);
                            intent.putExtra("url",item.getPics().split(",")[position]);
                            startActivity(intent);
                        }else{
                            FileUtil fileUtil = new FileUtil(mContext,"", 0);
                            String name = getBac(item.getPics().split(",")[position]);
                            if (fileUtil.isExist("kunzhong/file/", name)) {
                                FileUtils2.show(mContext,FileUtil.SDPATH + "kunzhong/file/" + name);
                            }else{
                                download(item.getPics().split(",")[position]);
                            }
                        }
                    }
                });
                webViewSetting(contentWv);
                contentWv.loadUrl("file:///android_asset/test.html");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
    private String getBac(String s) {
        String[] results = s.split("/");
        String result = results[results.length - 1];
        return result;
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:{
                    String name = getBac((String) msg.obj);
                    FileUtils2.show(mContext,FileUtil.SDPATH + "kunzhong/file/" + name);
                    break;
                }
                case 2:
                    ToastUtils.showToast((String) msg.obj,mContext);
                    break;
            }
        }
    };
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
                        message.what = 1;
                        message.obj = videoUrl;
                        mHandler.sendMessage(message);
                        break;
                    }

                    case 1:
                    {
                        message = new Message();
                        message.what = 1;
                        message.obj = videoUrl;
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

    @SuppressLint("JavascriptInterface")
    protected void webViewSetting(WebView webView) {
        //基本设置
        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (item.getAnswer_status() == 0 && !StringUtils.isEmpty(item.getAnswer_content())) {
                    String content = item.getAnswer_content();
                    content.replace("\\", "\\\\");
                    content.replace("\"", "\\\"");
                    editorWb.loadUrl("javascript:intoHtml('" + content + "')");
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 调用拨号程序
                if (url.startsWith("mailto:") || url.startsWith("geo:")
                        || url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(url));
                    startActivity(intent);
                } else {
                    // 当有新连接时，使用当前的 WebView
                    view.loadUrl(url);
                }
                return true;
            }
        });
        editorWb.addJavascriptInterface(this, "study");

        contentWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String content = item.getContent();
                content.replace("\\", "\\\\");
                content.replace("\"", "\\\"");
                contentWv.loadUrl("javascript:loadHtml('" + content + "')");
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_zuoye_info;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.answer_zhankai_tv: {
//                answerInfo.setMaxLines(Integer.MAX_VALUE);
                answerInfoWv.setVisibility(View.VISIBLE);
                String content = item.getAnswer_content();
                content.replace("\\", "\\\\");
                content.replace("\"", "\\\"");
                answerInfoWv.loadUrl("javascript:loadHtml('" + content + "')");
                answerZhankaiTv.setVisibility(View.GONE);
                break;
            }
            case R.id.zuoye_zhankai_tv: {
                zuoyeZhankaiTv.setVisibility(View.GONE);
                detailLl.setVisibility(View.VISIBLE);
                String content = item.getContent();
                content.replace("\\", "\\\\");
                content.replace("\"", "\\\"");
                contentWv.loadUrl("javascript:loadHtml('" + content + "')");
                break;
            }
            case R.id.tv_camera: {
                if (mDialogSelectImage != null) {
                    mDialogSelectImage.dismiss();
                }
                Intent intent;
                String outPath = getCameraTempFile(null);
                File outFile = new File(outPath);
                PreferenceUtils.setPrefString(mContext, PreferenceConstants.KEY_HEAD_IMAGE_OUT_PATH, outPath);
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
                startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
                break;
            }
            case R.id.tv_album: {
                Intent intent;
                String outPath = getCameraTempFile(null);
                File outFile = new File(outPath);
                if (mDialogSelectImage != null) {
                    mDialogSelectImage.dismiss();
                }
                PreferenceUtils.setPrefString(mContext, PreferenceConstants.KEY_HEAD_IMAGE_OUT_PATH, outPath);
                intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
                startActivityForResult(Intent.createChooser(intent, "选择图片"), ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
                break;
            }
            case R.id.tv_file:{
                if (mDialogSelectImage != null) {
                    mDialogSelectImage.dismiss();
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,ImageUtils.FILE_SELECT_CODE);
                break;
            }
            case R.id.tv_cancel: {
                mDialogSelectImage.dismiss();
                break;
            }
            case R.id.submit_ll: {
                editorWb.evaluateJavascript("javascript:outputHtml()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
//                        Log.i("value", value);
//                        value.replaceAll("\u003C","<");
//                        value.replaceAll("\\u003C","<");
//                        value.replaceAll("\\\u003C","<");
//                        value.replaceAll("\\\\\\u003C","<");
//                        value.replaceAll("\\\\","");
//                        value.replaceAll("\\\\\\\\","");
//                        value.replaceAll("\\\\\\\\\\\\","");
//                        value = StringUtils.unicodetoString(value);
                        submit(value, 1);
                    }
                });
                break;
            }
        }
    }

    private void submit(String value, final int state) {
        HashMap<String, String> params = new HashMap<>();
        params.put("student_id", AppApplication.getInstance().getUserinfo().getId() + "");
        params.put("question_id", id + "");
        params.put("content", value);
        if (item.getAnswer_id() != 0) {
            params.put("answer_id", item.getId() + "");
        }
        String pics = "";
        for (String pic : picList) {
            if ("upload".equals(pic)) {
                continue;
            }
            if (StringUtils.isEmpty(pics)) {
                pics = pic;
            } else {
                pics = pics + "," + pic;
            }
        }
        params.put("pics", pics);
        params.put("status", state + "");

        getDataFromServer(Request.Method.POST, ServerUrl.SAVE_HOMEWORK, params, JSONObject.class, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("status") == 1) {
                        if(state == 0){
                            ToastUtils.showToast("保存成功", mContext);
                        }else{
                            ToastUtils.showToast("提交成功", mContext);
                            if(AppApplication.getInstance().getMainListener() != null){
                                AppApplication.getInstance().getMainListener().onHomeworkSubmit(type);
                            }
                            ZuoyeFragment fragment = new ZuoyeFragment();
                            fragment.setType(type);
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            fm.beginTransaction().replace(type==0?R.id.main_ll_1:R.id.main_ll_2,fragment).commit();
                        }
                        AppApplication.getInstance().setListener(null);



                        mContext.getSupportFragmentManager().popBackStack();
                    }else{
                        ToastUtils.showToast(response.getString("message"), mContext);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:// 如果是直接从相册获取
                {
                    if (data == null) {
                        return;
                    }
//                    Uri imageUri = data.getData();
//                    Log.i("upload",data.getData().getPath());
//                    Bitmap bitmap = BitmapFactory.decodeFile("content://media/" + data.getData().getPath());
//                    uploadImg(ServerUrl.UPLOAD_IMG, bitmap, new ResponseListener() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject resp = new JSONObject(response);
//                                if(resp.getInt("status") == 1){
//                                    picList.remove(picList.size() - 1);
//                                    picList.add(resp.getJSONObject("results").getString("path"));
//                                    picList.add("upload");
//                                    adapter.setList(picList);
//                                    adapter.notifyDataSetChanged();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                        }
//                    });

                    Uri selectedImage = data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor c = mContext.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    String imagePath = c.getString(columnIndex);
                    Bitmap bm = BitmapFactory.decodeFile(imagePath);
                    c.close();

                    HashMap<String, String> params = new HashMap<>();

                    showDialog("正在上传图片");
                    params.put("img", "data:image/png;base64," + Base64BitmapUtil.bitmapToBase64(bm));
                    getDataFromServer(Request.Method.POST, ServerUrl.UPLOAD_IMG, params, JSONObject.class, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideProgress();
                            try {
                                if (response.getInt("status") == 1) {
                                    picList.remove(picList.size() - 1);
                                    picList.add(response.getJSONObject("results").getString("path"));
                                    picList.add("upload");
                                    adapter.setList(picList);
                                    adapter.notifyDataSetChanged();
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
//                    startActionCrop(imageUri);// 选图后裁剪
                    break;
                }

                case ImageUtils.HTML_REQUEST_CODE_GETIMAGE_BYSDCARD:// 如果是直接从相册获取
                {
                    if (data == null) {
                        return;
                    }
//                    Uri imageUri = data.getData();
                    HashMap<String, String> params = new HashMap<>();

                    Uri selectedImage = data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA, MediaStore.Video.Media.DATA, MediaStore.Audio.Media.DATA};
                    Cursor c = mContext.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    String imagePath = c.getString(columnIndex);
//                    Bitmap bm = BitmapFactory.decodeFile(imagePath);
//                    File file = new File();
                    c.close();

                    showDialog("正在上传图片");
                    Log.i("UPload", imagePath);
                    params.put("img", (htmluploadtype == 0 ? "data:image/png;base64," : (htmluploadtype == 1 ? "data:video/mp4;base64," : "data:audio/mp3;base64,")) + Base64BitmapUtil.fileToBase64(imagePath));
                    getDataFromServer(Request.Method.POST, ServerUrl.UPLOAD_IMG, params, JSONObject.class, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideProgress();
                            try {
                                putfile(response.getJSONObject("results").getString("path"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
//                    startActionCrop(imageUri);// 选图后裁剪
                    break;
                }
                case ImageUtils.FILE_SELECT_CODE:{
                    if (data == null) {
                        return;
                    }
//                    Uri imageUri = data.getData();
                    HashMap<String, String> params = new HashMap<>();

                    String imagePath = FileUtils2.getPath(mContext,data.getData());
                    Log.i("filePath",imagePath);

                    showDialog("正在上传文件");
                    Log.i("UPload", imagePath);
                    String[] picpoint = imagePath.split("\\.");
                    params.put("img", "data:" + FileUtils2.getMIMEType(imagePath).split("/")[0] + "/" + picpoint[picpoint.length - 1] + ";base64," + Base64BitmapUtil.fileToBase64(imagePath));
                    getDataFromServer(Request.Method.POST, ServerUrl.UPLOAD_IMG, params, JSONObject.class, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideProgress();
                            try {
                                if (response.getInt("status") == 1) {
                                    picList.remove(picList.size() - 1);
                                    picList.add(response.getJSONObject("results").getString("path"));
                                    picList.add("upload");
                                    adapter.setList(picList);
                                    adapter.notifyDataSetChanged();
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
                case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:// 如果是调用相机拍照时
                {
                    String imagePath = PreferenceUtils.getPrefString(mContext, PreferenceConstants.KEY_HEAD_IMAGE_OUT_PATH, "");
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//                    uploadImg(ServerUrl.UPLOAD_IMG, bitmap, new ResponseListener() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject resp = new JSONObject(response);
//                                if(resp.getInt("status") == 1){
//                                    picList.remove(picList.size() - 1);
//                                    picList.add(resp.getJSONObject("results").getString("path"));
//                                    picList.add("upload");
//                                    adapter.setList(picList);
//                                    adapter.notifyDataSetChanged();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                        }
//                    });
                    showDialog("正在上传图片");
                    HashMap<String, String> params = new HashMap<>();
                    params.put("img", "data:image/png;base64," + Base64BitmapUtil.bitmapToBase64(bitmap));
                    getDataFromServer(Request.Method.POST, ServerUrl.UPLOAD_IMG, params, JSONObject.class, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideProgress();
                            try {
                                if (response.getInt("status") == 1) {
                                    picList.remove(picList.size() - 1);
                                    picList.add(response.getJSONObject("results").getString("path"));
                                    picList.add("upload");
                                    adapter.setList(picList);
                                    adapter.notifyDataSetChanged();
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
//                    File outFile = new File(imagePath);
//                    startActionCrop(Uri.fromFile(outFile));// 拍照后裁剪
                    break;
                }
                case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:// 图片裁剪
                {
                    String outPath = PreferenceUtils.getPrefString(mContext, PreferenceConstants.KEY_HEAD_IMAGE_OUT_PATH, "");
                    Bitmap bitmap = BitmapFactory.decodeFile(outPath);
//                    uploadImg(ServerUrl.UPLOAD_IMG, bitmap, new ResponseListener() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject resp = new JSONObject(response);
//                                if(resp.getInt("status") == 1){
//                                    picList.remove(picList.size() - 1);
//                                    picList.add(resp.getJSONObject("results").getString("path"));
//                                    picList.add("upload");
//                                    adapter.setList(picList);
//                                    adapter.notifyDataSetChanged();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                        }
//                    });
                    HashMap<String, String> params = new HashMap<>();
                    params.put("img", "data:image/png;base64," + Base64BitmapUtil.bitmapToBase64(bitmap));
                    getDataFromServer(Request.Method.POST, ServerUrl.UPLOAD_IMG, params, JSONObject.class, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getInt("status") == 1) {
                                    picList.remove(picList.size() - 1);
                                    picList.add(response.getJSONObject("results").getString("path"));
                                    picList.add("upload");
                                    adapter.setList(picList);
                                    adapter.notifyDataSetChanged();
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

    @SuppressLint("SetJavaScriptEnabled")
    public void putfile(String path) {
        editorWb.loadUrl("javascript:uploadresponse('" + path.replace("/", "\\/") + "')");
    }

    /**
     * 图片上传
     */
    //////////////////////////////////////////////////////////

    Dialog mDialogSelectImage;
    private static final String PHOTO_PATH = getPictureDir();// 拍照存放照片路径
    /**
     * 图片对应目录文件名
     */
    private final static String PICTURE_PATH = "picture";
    /**
     * 资源文件地址
     */
    public final static String ROOT_PATH = "chat";
    private final static int CROP = 100;

    public void heedImageClick() {
        if (mDialogSelectImage == null) {
            mDialogSelectImage = new Dialog(mContext, R.style.ExitAppDialogStyle);
            LinearLayout localObject = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.view_select_image_dialog, null);
            localObject.setMinimumWidth(10000);
            TextView tvCamera = (TextView) localObject.findViewById(R.id.tv_camera);
            TextView tvAlbum = (TextView) localObject.findViewById(R.id.tv_album);
            TextView tvFile = (TextView) localObject.findViewById(R.id.tv_file);
            TextView tvCancel = (TextView) localObject.findViewById(R.id.tv_cancel);
            tvCamera.setText("拍照");
            tvCamera.setOnClickListener(this);
            tvAlbum.setText("从相册中选择");
            tvAlbum.setOnClickListener(this);
            tvFile.setText("附件");
            tvFile.setOnClickListener(this);
            tvCancel.setText("取消");
            tvCancel.setOnClickListener(this);

            WindowManager.LayoutParams localLayoutParams = mDialogSelectImage.getWindow().getAttributes();
            localLayoutParams.x = 0;
            localLayoutParams.y = -1000;
            localLayoutParams.gravity = 80;
            mDialogSelectImage.onWindowAttributesChanged(localLayoutParams);
            mDialogSelectImage.setCanceledOnTouchOutside(true);
            mDialogSelectImage.setContentView(localObject);
        }
        mDialogSelectImage.show();
    }

    /**
     * 拍照后保存相片，获得绝对路径
     *
     * @return
     */
    private String getCameraTempFile(String uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(PHOTO_PATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            showToast("无法保存上传的头像，请检查SD卡是否挂载");
        }
        // 照片命名
        String cropFileName = System.currentTimeMillis() + ".png";
        // 裁剪头像的绝对路径
        cropFileName = PHOTO_PATH + cropFileName;
        return cropFileName;
    }

    /**
     * 获取图片文件存放路径
     *
     * @return
     */
    public static String getPictureDir() {
        StringBuffer sb = new StringBuffer();
        if (hasSdcard()) {
            sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        } else {
            sb.append(AppApplication.getInstance().getFilesDir());
        }
        sb.append(File.separator);
        sb.append(ROOT_PATH);
        sb.append(File.separator);
        sb.append(PICTURE_PATH);
        sb.append(File.separator);
        return sb.toString();
    }

    /**
     * 是否存在SD卡
     *
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 拍照后裁剪
     *
     * @param data 原始图片
     */
    private void startActionCrop(Uri data) {
        String outPath = PreferenceUtils.getPrefString(mContext, PreferenceConstants.KEY_HEAD_IMAGE_OUT_PATH, "");
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output", Uri.fromFile(new File(outPath)));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP);// 输出图片大小
        intent.putExtra("outputY", CROP);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        intent.putExtra("noFaceDetection", true);// 是否去除面部检测， 如果你需要特定的比例去裁剪图片，那么这个一定要去掉，因为它会破坏掉特定的比例。
        intent.putExtra("return-data", false);// 若为false则表示不返回数据
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
    }

    //////////////////////////////////////////////////////////////////


    private int htmluploadtype;

    /**
     * @param type 0:图片，1：视频，2：音频
     */
    @JavascriptInterface
    public void htmlUpload(int type) {
        htmluploadtype = type;
        Log.i("upload", "htmlUpload");
        Intent intent;
        String outPath = getCameraTempFile(null);
        File outFile = new File(outPath);
        if (mDialogSelectImage != null) {
            mDialogSelectImage.dismiss();
        }
        PreferenceUtils.setPrefString(mContext, PreferenceConstants.KEY_HEAD_IMAGE_OUT_PATH, outPath);
        intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, type == 0 ? "image/*" : (type == 1 ? "vedio/*" : "audio/*"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
        startActivityForResult(Intent.createChooser(intent, type == 0 ? "选择图片" : (type == 1 ? "选择视频" : "选择音频")), ImageUtils.HTML_REQUEST_CODE_GETIMAGE_BYSDCARD);
    }

    @Override
    public void onSave() {
        editorWb.evaluateJavascript("javascript:outputHtml()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Log.i("ZuoyeInfoFragment",value);
                //此处为 js 返回的结果
//                value = StringUtils.unicodetoString(value);
//                Log.i("ZuoyeInfoFragment",value.substring(1));
//                submit(value.substring(1), 0);
                submit(value, 0);
            }
        });
    }

}
