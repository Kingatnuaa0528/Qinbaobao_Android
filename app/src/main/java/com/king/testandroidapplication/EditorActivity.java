package com.king.testandroidapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.king.testandroidapplication.pojo.ChosenParcelable;
import com.king.testandroidapplication.pojo.DeviceScaleDO;
import com.king.testandroidapplication.pojo.DiaryDataDO;
import com.king.testandroidapplication.pojo.ImageInfoDO;
import com.king.testandroidapplication.utils.AppUtils;
import com.king.testandroidapplication.utils.PhotoUtils;
import com.king.testandroidapplication.utils.SharePreferenceUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditorActivity extends AppCompatActivity {

    private String inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_editor);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.editor_title);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        List<ImageInfoDO> imageInfoDOList = (ArrayList<ImageInfoDO>)getIntent().getSerializableExtra("imageList");

        LayoutInflater.from(EditorActivity.this).inflate(R.layout.editor_title, getWindow().getDecorView().findViewById(android.R.id.content), false);
        TextView leftTitleBtn = findViewById(R.id.editor_title_cancel);
        TextView rightTitleBtn = findViewById(R.id.editor_title_save);

        leftTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rightTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 将本条日志写storage
                SharePreferenceUtils.initSharedPreference(getApplicationContext());
                DiaryDataDO diaryDataDO = new DiaryDataDO();
                diaryDataDO.setText(inputText);
                diaryDataDO.setTimeStamp(System.currentTimeMillis());
                String[] imgPathArray = new String[imageInfoDOList.size()];
                for(int index = 0; index < imageInfoDOList.size(); index++) {
                    imgPathArray[index] = imageInfoDOList.get(index).getImgPath();
                }
                diaryDataDO.setImgPathArray(imgPathArray);
                SharePreferenceUtils.writeData(diaryDataDO);

                Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NO_HISTORY); //当离开该Activity后，该Activity将被从任务栈中移除
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK); //清除任务栈中的所有activity

//                intent.putExtra("data", (Serializable) diaryDataDO);
                startActivity(intent);
            }
        });

        EditText inputView = (EditText) findViewById(R.id.editor_input);
        inputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputText = inputView.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        for(ImageInfoDO imageInfoDO : imageInfoDOList) {
            Bitmap imageBitmap = PhotoUtils.openLocalImage(imageInfoDO);
            imageInfoDO.setBitmap(imageBitmap);
        }

        LinearLayout imageListView = findViewById(R.id.editor_image_list);
        for(ImageInfoDO imageInfoDO : imageInfoDOList) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageBitmap(imageInfoDO.getBitmap());
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(230, 230);
            params.setMargins(6, 0, 6, 0);
            imageListView.addView(imageView, params);
        }
    }
}