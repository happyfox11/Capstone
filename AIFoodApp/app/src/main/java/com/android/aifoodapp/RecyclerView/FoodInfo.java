package com.android.aifoodapp.RecyclerView;


public class FoodInfo {

    int id;
    int cl_foodInfo;
    String cl_foodName;
    String cl_caloriesInfo;
    // int iv_scrollUpBtn;
    String cl_intake;
    // int iv_scrollDownBtn;

    // int iv_arrowBtn;

    String food_list_name;

    //int modifyBtn;

    public FoodInfo(int id, int cl_foodInfo, String cl_foodName, String cl_caloriesInfo, String cl_intake, String food_list_name){
        this.id = id;
        this.cl_foodInfo=cl_foodInfo;
        this.cl_foodName = cl_foodName;
        this.cl_caloriesInfo = cl_caloriesInfo;
        this.cl_intake = cl_intake;
        this.food_list_name = food_list_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCl_foodInfo() {
        return cl_foodInfo;
    }

    public void setCl_foodInfo(int cl_foodInfo) {
        this.cl_foodInfo = cl_foodInfo;
    }

    public String getCl_foodName() {
        return cl_foodName;
    }

    public void setCl_foodName(String cl_foodName) {
        this.cl_foodName = cl_foodName;
    }

    public String getCl_caloriesInfo() {
        return cl_caloriesInfo;
    }

    public void setCl_caloriesInfo(String cl_caloriesInfo) {
        this.cl_caloriesInfo = cl_caloriesInfo;
    }




    public String getCl_intake() {
        return cl_intake;
    }

    public void setCl_intake(String cl_intake) {
        this.cl_intake = cl_intake;
    }



    public String getFood_list_name() {
        return food_list_name;
    }

    public void setFood_list_name(String food_list_name) {
        this.food_list_name = food_list_name;
    }


}
