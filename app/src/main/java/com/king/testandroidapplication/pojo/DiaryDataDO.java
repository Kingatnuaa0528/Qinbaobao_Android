package com.king.testandroidapplication.pojo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class DiaryDataDO implements Serializable {

    private long timeStamp;
    private String[] imgPathArray;
    private String text;

    public DiaryDataDO() {
    }

    public DiaryDataDO(JSONObject dataJson) {
        this.text = dataJson.getString("text");
        this.timeStamp = dataJson.getLong("timeStamp");
        JSONArray imgArray = dataJson.getJSONArray("imgList");
        this.imgPathArray = new String[imgArray.size()];
        for(int index = 0; index < imgArray.size(); index++) {
            this.imgPathArray[index] = imgArray.getString(index);
        }
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String[] getImgPathArray() {
        return imgPathArray;
    }

    public void setImgPathArray(String[] imgPathArray) {
        this.imgPathArray = imgPathArray;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
