package com.king.testandroidapplication.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.king.testandroidapplication.pojo.ImageInfoDO;
import com.king.testandroidapplication.pojo.ImagePageDO;
import com.king.testandroidapplication.utils.PhotoUtils;

import java.util.ArrayList;
import java.util.List;

public class PhotoService extends Service {
    private static String TAG = "PhotoService";

    private List<ImageInfoDO> imageList;
    private onDataChangeListener onDataChangeListener;

    public void setOnDataChangeListener(PhotoService.onDataChangeListener onDataChangeListener) {
        this.onDataChangeListener = onDataChangeListener;
    }

    public List<ImageInfoDO> getImageList() {
        return imageList;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        imageList = new ArrayList<>();
        return new PhotoBinder();
    }

    public void fetchAlbumByPage(int cursor, int pageSize) {
        Log.d("PhotoService", "fetchPhoto cursor=" + cursor);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPhotosByPage(cursor, pageSize);
            }
        }).start();
    }

    private void getPhotosByPage(int cursor, int size) {
        imageList = new ArrayList<>();
        Uri mImageUrl = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = this.getContentResolver();

        String[] mediaColumns = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.TITLE
        };
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//        } else {
            try{
                String selection = MediaStore.Images.Media.MIME_TYPE + "=?";
                String[] selectionArgs = {"image/jpeg"};
                String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC limit " + size + " offset " + cursor;

                Cursor mCursor = mContentResolver.query(mImageUrl, mediaColumns, selection, selectionArgs, sortOrder);
                if(mCursor == null) {
                    Log.e(TAG, "Cursor is null!");
                    return;
                }

                int num = 0;

                while(mCursor.moveToNext()) {
                    String path = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    int id = mCursor.getInt(mCursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                    String title = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE));
                    int width = mCursor.getInt(mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH));
                    int height = mCursor.getInt(mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT));

                    ImageInfoDO imageInfoDO = new ImageInfoDO();
                    imageInfoDO.setImgPath(path);
                    imageInfoDO.setWidth(width);
                    imageInfoDO.setHeight(height);
                    imageInfoDO.setId(id);
                    Bitmap imageBitmap = PhotoUtils.openLocalImage(imageInfoDO);
                    imageInfoDO.setBitmap(imageBitmap);
                    imageInfoDO.setChecked(false);

                    imageList.add(imageInfoDO);
                    num += 1;
//                    ImagePageDO imagePageDO = new ImagePageDO();
//                    imagePageDO.setCursor(cursor+num);
//                    imagePageDO.setHasNext(true);
//                    imagePageDO.setImageList(imageList);
//                    onDataChangeListener.onDataChange(imagePageDO);
                }
                mCursor.close();
                ImagePageDO imagePageDO = new ImagePageDO();
                imagePageDO.setQueryCursor(cursor);
                imagePageDO.setCursor(cursor+num);
                imagePageDO.setHasNext(num < size ? false : true);
                imagePageDO.setImageList(imageList);
                onDataChangeListener.onDataChange(imagePageDO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}
    }

    public class PhotoBinder extends Binder {
        public PhotoService getService() {
            return PhotoService.this;
        }
    }

    public interface onDataChangeListener {
        void onDataChange(ImagePageDO imagePageDO);
    }
}
