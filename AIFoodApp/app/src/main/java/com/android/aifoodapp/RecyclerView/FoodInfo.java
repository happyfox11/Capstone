package com.android.aifoodapp.RecyclerView;


import com.android.aifoodapp.domain.fooddata;

public class FoodInfo {

    fooddata food;
    String cl_img;
    double cl_intake;

    public FoodInfo(fooddata food, String cl_img, double cl_intake){
        this.food=food;
        this.cl_img=cl_img;
        this.cl_intake=cl_intake;
    }

    public void setFood(fooddata food) {
        this.food = food;
    }
    public fooddata getFood() {
        return food;
    }

    public void setCl_img(String cl_img){
        this.cl_img=cl_img;
    }
    public String getCl_img(){
        return cl_img;
    }

    public void setCl_intake(int cl_intake){
        this.cl_intake=cl_intake;
    }
    public double getCl_intake(){
        return cl_intake;
    }




}
