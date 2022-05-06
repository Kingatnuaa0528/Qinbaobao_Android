package com.king.testandroidapplication.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.king.testandroidapplication.R;
import com.king.testandroidapplication.pojo.ImageInfoDO;
import com.king.testandroidapplication.utils.AppUtils;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    public final int COLUMN_NUMS = 3;

    private Context context;
    private List<ImageInfoDO> imageList;

    public RecyclerViewAdapter(Context context, List<ImageInfoDO> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_item, parent, false);
        DisplayMetrics currentViewMetrics = AppUtils.getDisplayMetrics(context, currentView);
        int viewWidth = currentViewMetrics.widthPixels / COLUMN_NUMS;
        ViewGroup.LayoutParams layoutParams = currentView.getLayoutParams();
        layoutParams.height = viewWidth;
        layoutParams.width = viewWidth;
        currentView.setLayoutParams(layoutParams);

        RecycleViewHolder viewHolder = new RecycleViewHolder(currentView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageInfoDO imageInfoDO = this.imageList.get(position);

        ((RecycleViewHolder)holder).checkBoxView.setChecked(imageInfoDO.getChecked());
        ((RecycleViewHolder)holder).imageView.setImageBitmap(imageInfoDO.getBitmap());
        View containerView = ((RecycleViewHolder)holder).containerView;

        if(position%3  < 2) {
            containerView.setPadding(0, 0, 5, 5);
        } else {
            containerView.setPadding(0, 0, 0, 5);
        }

        ((RecycleViewHolder)holder).checkBoxView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageInfoDO.setChecked(!imageInfoDO.getChecked());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
//        ((RecycleViewHolder)holder).imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageInfoDO.setChecked(!imageInfoDO.isChecked());
//                notifyItemChanged(holder.getAdapterPosition());
//            }
//        });
    }



    @Override
    public int getItemCount() {
        return this.imageList.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CheckBox checkBoxView;
        View containerView;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.grid_img);
            checkBoxView = (CheckBox) itemView.findViewById(R.id.grid_checkbox);
            containerView = itemView.findViewById(R.id.grid_item_container);
        }
    }



    public class GridItemDecoration extends RecyclerView.ItemDecoration {

        public GridItemDecoration() {
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }
}
