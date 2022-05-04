package com.android.aifoodapp.vo;

import android.graphics.Bitmap;

import java.util.List;

public class MealMemberVo {
    private String name;
    private Bitmap mealImg;
    private List<SubItem> subItemList;

    public MealMemberVo() {

    }

    public MealMemberVo(String name, List<SubItem> subItemList) {
        this.name = name;
        this.subItemList = subItemList;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getMealImg() {
        return mealImg;
    }

    public void setMealImg(Bitmap mealImg) {
        this.mealImg = mealImg;
    }

    public List<SubItem> getSubItemList() {
        return subItemList;
    }

    public void setSubItemList(List<SubItem> subItemList) {
        this.subItemList = subItemList;
    }
}

