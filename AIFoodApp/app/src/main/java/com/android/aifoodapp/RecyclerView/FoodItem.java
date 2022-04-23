package com.android.aifoodapp.RecyclerView;

public class FoodItem {

    int id;
    int fl_foodInfo;
    int minusBtn;
    String fl_foodName;


    public FoodItem (int id, int fl_foodInfo , int minusBtn, String fl_foodName){
        this.id= id;
        this.fl_foodInfo = fl_foodInfo;
        this.minusBtn = minusBtn;
        this.fl_foodName = fl_foodName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFl_foodInfo() {
        return fl_foodInfo;
    }

    public void setFl_foodInfo(int circularImage) {
        this.fl_foodInfo = fl_foodInfo;
    }

    public int getMinusBtn() {
        return minusBtn;
    }

    public void setMinusBtn(int minusBtn) {
        this.minusBtn = minusBtn;
    }

    public String getFl_foodName() {
        return fl_foodName;
    }

    public void setFl_foodName(String fl_foodName) {
        this.fl_foodName = fl_foodName;
    }
}


