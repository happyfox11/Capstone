package com.android.aifoodapp.RecyclerView;

public class FoodItem {

    //int id;
    String fl_image;
    int minusBtn;
    String fl_foodName;


    public FoodItem (String fl_image, int minusBtn, String fl_foodName){
        //this.id= id;
        this.fl_image = fl_image;
        this.minusBtn = minusBtn;
        this.fl_foodName = fl_foodName;
    }

    /*
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
*/
    public String getFl_image() {
        return fl_image;
    }

    public void setFl_image(String fl_image) {
        this.fl_image = fl_image;
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


