package com.king.testandroidapplication.pojo;

import java.io.Serializable;
import java.util.List;

public class ImagePageDO implements Serializable {

    private static final long serialVersionUID = 12345L;

    private List<ImageInfoDO> imageList;
    private boolean hasNext;
    private int cursor;
    private int queryCursor;

    public ImagePageDO() {
    }

    public void setImageList(List<ImageInfoDO> imageList) {
        this.imageList = imageList;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public List<ImageInfoDO> getImageList() {
        return imageList;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public int getCursor() {
        return cursor;
    }

    public int getQueryCursor() {
        return queryCursor;
    }

    public void setQueryCursor(int queryCursor) {
        this.queryCursor = queryCursor;
    }
}
