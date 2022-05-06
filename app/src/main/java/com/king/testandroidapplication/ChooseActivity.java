package com.king.testandroidapplication;


import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.king.testandroidapplication.adapter.RecyclerViewAdapter;
import com.king.testandroidapplication.pojo.ChosenParcelable;
import com.king.testandroidapplication.pojo.ImageInfoDO;
import com.king.testandroidapplication.pojo.ImagePageDO;
import com.king.testandroidapplication.service.PhotoService;
import com.king.testandroidapplication.utils.AppUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseActivity extends AppCompatActivity {

    private static String TAG = "ChooseActivity";
    private static int PAGE_SIZE = 10;
    private static String[] needPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private PhotoService photoService;
    private Activity activity;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<ImageInfoDO> imageInfoDOList;

    private int dataCursor = 0;
    private boolean dataHasNext = true;
    private Map<Integer, Boolean> cursorStatusMap = new HashMap<>();
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        Intent intent = new Intent("com.king.testandroidapplication.PHOTO_SERVICE");
        intent.setPackage(getPackageName());
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        activity = this;
        imageInfoDOList = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), imageInfoDOList);
        recyclerView.setAdapter(recyclerViewAdapter);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), recyclerViewAdapter.COLUMN_NUMS);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editor_title_menu, menu);
        MenuItem item = menu.getItem(0);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try {
                    Intent intent = new Intent("com.king.testandroidapplication.Editor");
                    List<ImageInfoDO> chosenImageList = new ArrayList<>();
                    for (ImageInfoDO imageInfoDO : imageInfoDOList) {
                        if (imageInfoDO.getChecked()) {

                            chosenImageList.add(imageInfoDO);
                        }
                    }

                    intent.putExtra("imageList", (Serializable) chosenImageList);
                    startActivity(intent);
                }catch (Throwable e) {
                    Log.e("onMenuItemClick error!","error!", e);
                }
                return false;
            }
        });
        return true;
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            photoService = ((PhotoService.PhotoBinder)iBinder).getService();
            photoService.setOnDataChangeListener(new PhotoService.onDataChangeListener() {
                @Override
                public void onDataChange(ImagePageDO imagePageDO) {
                    Log.d(TAG, "onDataChange! data=" + JSON.toJSONString(imagePageDO));
                    imageInfoDOList.addAll(imagePageDO.getImageList());
                    dataCursor = imagePageDO.getCursor();
                    dataHasNext = imagePageDO.isHasNext();
                    int queryCursor = imagePageDO.getQueryCursor();
                    cursorStatusMap.put(queryCursor, true);
                    isLoading = false;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerViewAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
            List<String> needRequestList = AppUtils.checkPermission(ChooseActivity.this, needPermissionList);
            if(!needRequestList.isEmpty()) {
                AppUtils.requestPermission(ChooseActivity.this, needRequestList.toArray(new String[needRequestList.size()]));
            } else {
                loadMoreItems();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            dataCursor = 0;
            dataHasNext = true;
            cursorStatusMap = new HashMap<>();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isAllpermissionGranted = true;
        if(requestCode == 200) {
            for(int index = 0; index < permissions.length; index++) {
                if(PackageManager.PERMISSION_GRANTED != grantResults[index]) {
                    Log.e(TAG, permissions[index] + " is not granted!");
                    isAllpermissionGranted = false;
                }
            }
        }
        if(isAllpermissionGranted) {
            loadMoreItems();
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }

    private void loadMoreItems() {
        //photoService.fetchAlbumByPage(dataCursor, PAGE_SIZE);
        if(dataHasNext) {
            Object value = cursorStatusMap.get(dataCursor);
            if(value == null) {
                Log.d(TAG, "load more data! dataCursor=" + dataCursor);
                cursorStatusMap.put(dataCursor, false);
                isLoading = true;
                photoService.fetchAlbumByPage(dataCursor, PAGE_SIZE);
            }
        }
    }

    class PaginationScrollListener extends RecyclerView.OnScrollListener {
        GridLayoutManager gridLayoutManager;

        public PaginationScrollListener(RecyclerView.LayoutManager layoutManager) {
            this.gridLayoutManager = (GridLayoutManager) layoutManager;
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Log.d("ScrollListener", String.format("visibleItemCount: %s  totalItemCount: %s  firstVisibleItemPosition: %s", gridLayoutManager.getChildCount(), gridLayoutManager.getItemCount(), gridLayoutManager.findLastVisibleItemPosition()));
            int visibleItemCount = gridLayoutManager.getChildCount();
            int totalItemCount = gridLayoutManager.getItemCount();
            int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
            if(!isLoading) {
                if((visibleItemCount+firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreItems();
                }
            }
        }
    }

}