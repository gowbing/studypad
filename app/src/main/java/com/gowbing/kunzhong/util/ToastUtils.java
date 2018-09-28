package com.gowbing.kunzhong.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018-1-28.
 */

public class ToastUtils {
    public static void showToast(String content, Context context){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
