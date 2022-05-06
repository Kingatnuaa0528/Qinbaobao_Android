package com.king.testandroidapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.king.testandroidapplication.pojo.DiaryDataDO;
import com.king.testandroidapplication.pojo.ImageInfoDO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SharePreferenceUtils {

    private static final String SP_NAME = "test_data";

    private static SharedPreferences sharedPreferences;

    public static SharedPreferences initSharedPreference(Context context) {
        if(sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static void writeData(DiaryDataDO diaryDataDO) {
        if(sharedPreferences == null) {
            return;
        }
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("text", diaryDataDO.getText());
        dataMap.put("timeStamp", diaryDataDO.getTimeStamp());

        String[] imagePathArray = diaryDataDO.getImgPathArray();
        dataMap.put("imgList", imagePathArray);
        Log.d("SharePreferenceUtils", "SP write data: " + JSON.toJSONString(dataMap));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(String.valueOf(diaryDataDO.getTimeStamp()), JSON.toJSONString(dataMap));
        editor.commit();
    }

    public static List<DiaryDataDO> getAllDataBySort() {
        if(sharedPreferences == null) {
            return new ArrayList<>();
        }
        Map<String, ?> allSPDataMap = sharedPreferences.getAll();
        List<DiaryDataDO> resultList = new ArrayList<>();
        Set<String> keySet = allSPDataMap.keySet();
        String[] keys = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keys);
        for(String key : keys) {
            Object values = allSPDataMap.get(key);
            if(values instanceof String) {
                try{
                    JSONObject valueJSON = JSON.parseObject((String)values);
                    DiaryDataDO diaryDataDO = new DiaryDataDO(valueJSON);
                    resultList.add(diaryDataDO);
                } catch (Exception e) {
                    Log.e("SharePreferenceUtils", "parseSPData error! values: " + (String)values, e);
                }
            }
        }
        return resultList;
    }
}
