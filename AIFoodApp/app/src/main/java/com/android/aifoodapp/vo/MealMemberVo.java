package com.android.aifoodapp.vo;

import android.graphics.Bitmap;

public class MealMemberVo {
    private String name;
    private Bitmap mealImg;

    public MealMemberVo() {

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

}

