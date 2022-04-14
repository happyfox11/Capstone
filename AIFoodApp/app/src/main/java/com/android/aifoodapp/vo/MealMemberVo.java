package com.android.aifoodapp.vo;

import android.widget.ImageView;

public class MealMemberVo {
    private String name;
    private ImageView mealImg;

    public MealMemberVo() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageView getMealImg() {
        return mealImg;
    }

    public void setMealImg(ImageView mealImg) {
        this.mealImg = mealImg;
    }

}

