package com.king.testandroidapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.king.testandroidapplication.pojo.DeviceScaleDO;

import java.util.ArrayList;
import java.util.List;

public class AppUtils {
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static DisplayMetrics getDisplayMetrics(Context context, View view) {
        return context.getResources().getDisplayMetrics();
    }

    public static List<String> checkPermission(Context context, String[] checkList) {
        List<String> resultList = new ArrayList<>();
        for(String permission : checkList) {
            if(PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context, permission)) {
                resultList.add(permission);
            }
        }
        return resultList;
    }

    public static void requestPermission(Activity activity, String[] permissionArray) {
        ActivityCompat.requestPermissions(activity, permissionArray, 200);
    }

    public static DeviceScaleDO calculateImgScaleByDM(Context context, View containerView, Bitmap[] imgBitmapArray) {
        DeviceScaleDO deviceScaleDO = new DeviceScaleDO();

        DisplayMetrics displayMetrics = AppUtils.getDisplayMetrics(context, containerView);
        int width = displayMetrics.widthPixels;
        int imgWidth = width/imgBitmapArray.length - 20;
        deviceScaleDO.setWidth(imgWidth);
        deviceScaleDO.setHeight(imgWidth);
        return deviceScaleDO;
    }
}
