package com.king.testandroidapplication.pojo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ImageInfoDO implements Serializable {

    private static final long serialVersionUID = -6298516694275121291L;

    private int id;
    private String imgPath;
    private int width;
    private int height;
    private transient Bitmap bitmap;
    private boolean isChecked;

    public ImageInfoDO(String imgPath, int width, int height) {
        this.imgPath = imgPath;
        this.width = width;
        this.height = height;
    }

    public ImageInfoDO() {
    }

//    protected ImageInfoDO(Parcel in) {
//        id = in.readInt();
//        imgPath = in.readString();
//        width = in.readInt();
//        height = in.readInt();
//        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
//        isChecked = in.readByte() != 0;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(id);
//        dest.writeString(imgPath);
//        dest.writeInt(width);
//        dest.writeInt(height);
//        bitmap.writeToParcel(dest, 0);
//        dest.writeByte((byte) (isChecked ? 1 : 0));
//    }
//
//    public static final Creator<ImageInfoDO> CREATOR = new Creator<ImageInfoDO>() {
//        @Override
//        public ImageInfoDO createFromParcel(Parcel in) {
//            return new ImageInfoDO(in);
//        }
//
//        @Override
//        public ImageInfoDO[] newArray(int size) {
//            return new ImageInfoDO[size];
//        }
//    };

    public void setId(int id) {
        this.id = id;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getId() {
        return id;
    }

    public String getImgPath() {
        return imgPath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
