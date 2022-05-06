package com.king.testandroidapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.king.testandroidapplication.R;
import com.king.testandroidapplication.pojo.DeviceScaleDO;
import com.king.testandroidapplication.utils.AppUtils;

import java.util.List;
import java.util.Map;

public class ListAdapter extends ArrayAdapter<Map> {

    private static String TAG = "ListAdapter";

    private int resourceId;

    public ListAdapter(@NonNull Context context, int resource, @NonNull List<Map> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Map<String, Object> dataMap = getItem(position);
        //Log.i(TAG, "start getView() title = " + (String)dataMap.get("title"));
        View view;
        ViewHolder viewHolder;

        if(convertView == null) {
            // 说明此时为第一屏，没有视图缓存
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            //viewHolder.itemImage = view.findViewById(R.id.list_item_image);
            viewHolder.itemText = view.findViewById(R.id.list_item_text);
            viewHolder.imgContainer = view.findViewById(R.id.img_container);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
            Log.i(TAG, "currentView's position = " + position + "   title = " + (String)dataMap.get("title") + "   viewHolder's itemText = " + viewHolder.itemText.getText());
        }
        viewHolder.itemText.setText((String)dataMap.get("title"));
        viewHolder.imgContainer.removeAllViews();

        Object imgArrayObj = dataMap.get("imgArray");
        if(imgArrayObj.getClass().isArray()) {
            Integer[] imgResArray = (Integer[]) imgArrayObj;
            Bitmap[] imgBitmapArray = new Bitmap[imgResArray.length];
            for(int index = 0; index < imgResArray.length; index++) {
                int imgResId = imgResArray[index];
                Bitmap imgBitmap = BitmapFactory.decodeResource(getContext().getResources(), imgResId);
                imgBitmapArray[index] = imgBitmap;
            }
            DeviceScaleDO deviceScaleDO = calculateImgScaleByDM(imgBitmapArray);
            for(Bitmap bitmap : imgBitmapArray) {
                ImageView image = new ImageView(getContext());
                image.setImageBitmap(bitmap);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, deviceScaleDO.getHeight(), 1.0f);
                viewHolder.imgContainer.addView(image, params);
            }

        }
        return view;
    }

    private DeviceScaleDO calculateImgScaleByDM(Bitmap[] imgBitmapArray) {
        DeviceScaleDO deviceScaleDO = new DeviceScaleDO();

        DisplayMetrics displayMetrics = AppUtils.getDisplayMetrics(getContext());
        int width = displayMetrics.widthPixels;
        int imgWidth = width/imgBitmapArray.length - 20;
        deviceScaleDO.setWidth(imgWidth);
        deviceScaleDO.setHeight(imgWidth);
        return deviceScaleDO;
    }

    class ViewHolder{
        ImageView itemImage;
        TextView itemText;
        LinearLayout imgContainer;
    }
}
