package com.king.testandroidapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ChosenParcelable implements Parcelable {
    private List<ImageInfoDO> chosenImageList;

    public ChosenParcelable() {
        chosenImageList = new ArrayList<>();
    }

    protected ChosenParcelable(Parcel in) {
        if(chosenImageList == null) {
            chosenImageList = new ArrayList<>();
        }
        in.readList(chosenImageList, ImageInfoDO.class.getClassLoader());
    }

    public static final Creator<ChosenParcelable> CREATOR = new Creator<ChosenParcelable>() {
        @Override
        public ChosenParcelable createFromParcel(Parcel in) {
//            ChosenParcelable chosenParcelable = new ChosenParcelable();
//            chosenParcelable.setChosenImageList(in.readArrayList());
            return new ChosenParcelable(in);
        }

        @Override
        public ChosenParcelable[] newArray(int size) {
            return new ChosenParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(chosenImageList);
    }

    public List<ImageInfoDO> getChosenImageList() {
        return chosenImageList;
    }

    public void setChosenImageList(List<ImageInfoDO> chosenImageList) {
        this.chosenImageList = chosenImageList;
    }
}
