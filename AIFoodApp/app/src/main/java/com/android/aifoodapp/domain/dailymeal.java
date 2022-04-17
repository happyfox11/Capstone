package com.android.aifoodapp.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class dailymeal implements Parcelable {
    @SerializedName("userid")
    private String userid;
    @SerializedName("datekey")
    private Date datekey;
    @SerializedName("stepcount")
    private int stepcount;
    @SerializedName("calorie")
    private int calorie;
    @SerializedName("protein")
    private int protein;
    @SerializedName("carbohydrate")
    private int carbohydrate;
    @SerializedName("fat")
    private int fat;
    @SerializedName("dailymealid")
    private long dailymealid;

    public dailymeal(){

    }

    public dailymeal(String userid, Date datekey, int stepcount, int calorie, int protein, int carbohydrate, int fat, long dailymealid) {
        this.userid = userid;
        this.datekey = datekey;
        this.stepcount = stepcount;
        this.calorie = calorie;
        this.protein = protein;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.dailymealid = dailymealid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Date getDatekey() {
        return datekey;
    }

    public void setDatekey(Date datekey) {
        this.datekey = datekey;
    }

    public int getStepcount() {
        return stepcount;
    }

    public void setStepcount(int stepcount) {
        this.stepcount = stepcount;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
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

    public long getDailymealid() {
        return dailymealid;
    }

    public void setDailymealid(long dailymealid) {
        this.dailymealid = dailymealid;
    }

    protected dailymeal(Parcel in) {
        userid = in.readString();
        stepcount = in.readInt();
        calorie = in.readInt();
        protein = in.readInt();
        carbohydrate = in.readInt();
        fat = in.readInt();
        dailymealid = in.readLong();
    }

    public static final Creator<dailymeal> CREATOR = new Creator<dailymeal>() {
        @Override
        public dailymeal createFromParcel(Parcel in) {
            return new dailymeal(in);
        }

        @Override
        public dailymeal[] newArray(int size) {
            return new dailymeal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userid);
        parcel.writeInt(stepcount);
        parcel.writeInt(calorie);
        parcel.writeInt(protein);
        parcel.writeInt(carbohydrate);
        parcel.writeInt(fat);
        parcel.writeLong(dailymealid);
    }
}
