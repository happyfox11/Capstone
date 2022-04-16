package com.android.aifoodapp.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class meal implements Parcelable {
    @SerializedName("userid")
    private String userid;
    @SerializedName("dailymealid")
    private long dailymealid;
    @SerializedName("calorie")
    private char calorie;
    @SerializedName("protein")
    private int protein;
    @SerializedName("carbohydrate")
    private int carbohydrate;
    @SerializedName("fat")
    private int fat;
    @SerializedName("mealname")
    private String mealname;

    public meal(){

    }

    public meal(String userid, long dailymealid, char calorie, int protein, int carbohydrate, int fat, String mealname) {
        this.userid = userid;
        this.dailymealid = dailymealid;
        this.calorie = calorie;
        this.protein = protein;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.mealname = mealname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public long getDailymealid() {
        return dailymealid;
    }

    public void setDailymealid(long dailymealid) {
        this.dailymealid = dailymealid;
    }

    public char getCalorie() {
        return calorie;
    }

    public void setCalorie(char calorie) {
        this.calorie = calorie;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public String getMealname() {
        return mealname;
    }

    public void setMealname(String mealname) {
        this.mealname = mealname;
    }


    protected meal(Parcel in) {
        userid = in.readString();
        dailymealid = in.readLong();
        calorie = (char) in.readInt();
        protein = in.readInt();
        carbohydrate = in.readInt();
        fat = in.readInt();
        mealname = in.readString();
    }

    public static final Creator<meal> CREATOR = new Creator<meal>() {
        @Override
        public meal createFromParcel(Parcel in) {
            return new meal(in);
        }

        @Override
        public meal[] newArray(int size) {
            return new meal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userid);
        parcel.writeLong(dailymealid);
        parcel.writeInt((int) calorie);
        parcel.writeInt(protein);
        parcel.writeInt(carbohydrate);
        parcel.writeInt(fat);
        parcel.writeString(mealname);
    }
}
