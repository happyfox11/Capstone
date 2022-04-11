package com.android.aifoodapp.domain;

import com.google.gson.annotations.SerializedName;

public class user {
    @SerializedName("id")
    private String id;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("sex")
    private char sex;
    @SerializedName("age")
    private int age;
    @SerializedName("height")
    private int height;
    @SerializedName("weight")
    private int weight;
    @SerializedName("activity_index")
    private int activity_index;
    @SerializedName("target_calories")
    private int target_calories;
    //@SerializedName("profile")
    //private

    public user(String id, String nickname, char sex, int age, int height, int weight, int activity_index, int target_calories) {
        this.id = id;
        this.nickname = nickname;
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.activity_index = activity_index;
        this.target_calories = target_calories;
    }

    public user() {

    }


    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getNickname(){
        return nickname;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public char getSex(){
        return sex;
    }

    public void setSex(char sex){
        this.sex = sex;
    }

    public int getAge(){
        return age;
    }

    public void setAge(int age){
        this.age = age;
    }

    public int getHeight(){
        return height;
    }

    public void setHeight(int height){
        this.height =height;
    }


    public int getWeight(){
        return weight;
    }

    public void setWeight(int weight){
        this.weight =weight;
    }



    public int getActivity_index(){
        return activity_index;
    }

    public void setActivity_index(int activity_index){
        this.activity_index =activity_index;
    }

    public int getTarget_calories(){
        return target_calories;
    }

    public void setTarget_calories(int target_calories){
        this.target_calories =target_calories;
    }


    public String pringStirng(){
        return  "user"+id+"  "+nickname+"   "+sex + "   " + getActivity_index();
    }




}
