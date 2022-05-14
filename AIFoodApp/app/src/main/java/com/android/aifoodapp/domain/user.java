package com.android.aifoodapp.domain;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class user implements Parcelable {
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
    @SerializedName("profile")
    private String profile;
    @SerializedName("email")
    private String email;

    public user(String id, String nickname, char sex, int age, int height, int weight, int activity_index, int target_calories, String profile, String email) {
        this.id = id;
        this.nickname = nickname;
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.activity_index = activity_index;
        this.target_calories = target_calories;
        this.profile=profile;
        this.email=email;
    }

    public user() {

    }


    public static final Creator<user> CREATOR = new Creator<user>() {
        @Override
        public user createFromParcel(Parcel in) {
            return new user(in);
        }

        @Override
        public user[] newArray(int size) {
            return new user[size];
        }
    };

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

    public void setProfile(String profile){
        this.profile = profile;
    }
    public String getProfile(){
        return profile;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }

    //테스트용
    public String pringStirng(){
        return  "user"+id+"  "+nickname+"   "+sex + "   " + getActivity_index()+profile;
    }

    protected user(Parcel in){
        id=in.readString();
        nickname=in.readString();
        //sex = (in.readString()!=null)?in.readString().charAt(0):'N';
        sex=(char)in.readInt();
        age=in.readInt();
        height= in.readInt();
        weight=in.readInt();
        activity_index= in.readInt();
        target_calories=in.readInt();
        //이미지 저장부분 일단 살려둠..
        profile=in.readString();
        email=in.readString();
    }
/*
    public static final Creator<user> USER_CREATOR = new Creator<user>() {
        @Override
        public user createFromParcel(Parcel parcel) {
            return new user(parcel);
        }

        @Override
        public user[] newArray(int size) {
            return new user[size];
        }
    };
*/
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(nickname);
        parcel.writeInt((int) sex);
        parcel.writeInt(age);
        parcel.writeInt(height);
        parcel.writeInt(weight);
        parcel.writeInt(activity_index);
        parcel.writeInt(target_calories);
        parcel.writeString(profile);
        parcel.writeString(email);
    }
}
