package com.king.testandroidapplication.utils;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Size;

import com.king.testandroidapplication.pojo.ImageInfoDO;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class PhotoUtils {

    private static final int IMG_DEFAULT_WIDTH = 640;

    public static Bitmap openLocalImage(ImageInfoDO imageInfoDO) {

        Bitmap bitmap = null;
        try{
            int imgWidth = imageInfoDO.getWidth();
            int imgHeight = imageInfoDO.getHeight();
            int thumbnailHeight = imgHeight * IMG_DEFAULT_WIDTH / imgWidth;
            bitmap = getImageThumbnail(imageInfoDO.getImgPath(), IMG_DEFAULT_WIDTH, thumbnailHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static Bitmap getImageThumbnail(String imagePath, int width, int height)
    {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();   //缩放图片，width、height按相同比例缩放图片
        options.inJustDecodeBounds = true;     //options为true时，构造出的bitmap没有图片，只有一些长宽等配置信息，但是速度快；也就是说，当inJustDecodeBounds为true时，bitmap并不加载到内存，这样效率很高。options为false时，才有图片。
        bitmap = BitmapFactory.decodeFile(imagePath, options);    //获取这个图像的宽和高，这里的bitmap为null
        options.inJustDecodeBounds = false;        //此处一定要设为false，真正解码图片
        //计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight)
        {
            be = beWidth;
        }
        else {
            be = beHeight;
        }
        if (be <= 0)
        {
            be = 1;
        }
        options.inSampleSize = be;   //inSampleSize是缩放比，如options.inSampleSize =8，就是原图的八分之一
        bitmap = BitmapFactory.decodeFile(imagePath, options);   //重新读入图片，读取缩放后的bitmap
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);   //利用ThumbnailUtils来创建缩略图，这里要指定缩放哪个Bitmap对象

        return bitmap;    //返回生成的缩略图
    }
}
