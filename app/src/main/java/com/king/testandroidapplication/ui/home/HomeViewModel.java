package com.king.testandroidapplication.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.king.testandroidapplication.R;
import com.king.testandroidapplication.pojo.DiaryDataDO;
import com.king.testandroidapplication.utils.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<DiaryDataDO>> mDataList;

    public HomeViewModel() {
        mDataList = new MutableLiveData<>();
        List<DiaryDataDO> dataList = SharePreferenceUtils.getAllDataBySort();

        mDataList.setValue(dataList);
    }

    public MutableLiveData<List<DiaryDataDO>> getDataList() {
        return mDataList;
    }
}