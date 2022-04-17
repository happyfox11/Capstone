package com.android.aifoodapp.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class meal implements Parcelable {
    @SerializedName("userid")
    private String userid;
    @SerializedName("dailymealid")
    private long dailymealid;
    @SerializedName("mealid")
    private long mealid;
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
    @SerializedName("mealphoto")
    private String mealphoto;
    @SerializedName("savetime")
    private Date savetime;
    @SerializedName("timeflag")
    private int timeflag;

    public meal(){

    }

    public meal(String userid, long dailymealid, long mealid, char calorie, int protein, int carbohydrate, int fat, String mealname, String mealphoto, Date savetime, int timeflag) {
        this.userid = userid;
        this.dailymealid = dailymealid;
        this.mealid = mealid;
        this.calorie = calorie;
        this.protein = protein;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.mealname = mealname;
        this.mealphoto = mealphoto;
        this.savetime = savetime;
        this.timeflag = timeflag;
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

    public long getMealid() {
        return mealid;
    }

    public void setMealid(long mealid) {
        this.mealid = mealid;
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

    public String getMealphoto() {
        return mealphoto;
    }

    public void setMealphoto(String mealphoto) {
        this.mealphoto = mealphoto;
    }

    public Date getSavetime() {
        return savetime;
    }

    public void setSavetime(Date savetime) {
        this.savetime = savetime;
    }

    public int getTimeflag() {
        return timeflag;
    }

    public void setTimeflag(int timeflag) {
        this.timeflag = timeflag;
    }

    protected meal(Parcel in) {
        userid = in.readString();
        dailymealid = in.readLong();
        mealid = in.readLong();
        calorie = (char) in.readInt();
        protein = in.readInt();
        carbohydrate = in.readInt();
        fat = in.readInt();
        mealname = in.readString();
        mealphoto = in.readString();
        timeflag = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeLong(dailymealid);
        dest.writeLong(mealid);
        dest.writeInt((int) calorie);
        dest.writeInt(protein);
        dest.writeInt(carbohydrate);
        dest.writeInt(fat);
        dest.writeString(mealname);
        dest.writeString(mealphoto);
        dest.writeInt(timeflag);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
