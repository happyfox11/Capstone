package com.android.aifoodapp.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class fooddata implements Parcelable {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("one_serving")
    private double one_serving;
    @SerializedName("total_content")
    private double total_content;
    @SerializedName("calorie")
    private double calorie;
    @SerializedName("moisture")
    private double moisture;
    @SerializedName("protein")
    private double protein;
    @SerializedName("fat")
    private double fat;
    @SerializedName("carbohydrate")
    private double carbohydrate;
    @SerializedName("total_sugar")
    private double total_sugar;
    @SerializedName("sucrose")
    private double sucrose;
    @SerializedName("glucose")
    private double glucose;
    @SerializedName("fructose")
    private double fructose;
    @SerializedName("lactose")
    private double lactose;
    @SerializedName("maltose")
    private double maltose;
    @SerializedName("total_dietary_fiber")
    private double total_dietary_fiber;
    @SerializedName("calcium")
    private double calcium;
    @SerializedName("iron")
    private double iron;
    @SerializedName("magnesium")
    private double magnesium;
    @SerializedName("in")
    private double in;
    @SerializedName("potassium")
    private double potassium;
    @SerializedName("sodium")
    private double sodium;
    @SerializedName("zinc")
    private double zinc;
    @SerializedName("copper")
    private double copper;
    @SerializedName("manganese")
    private double manganese;
    @SerializedName("vitamin_b1")
    private double vitamin_b1;
    @SerializedName("vitamin_b2")
    private double vitamin_b2;
    @SerializedName("vitamin_c")
    private double vitamin_c;
    @SerializedName("cholesterol")
    private double cholesterol;
    @SerializedName("total_saturated_fatty_acids")
    private double total_saturated_fatty_acids;
    @SerializedName("trans_fatty_acids")
    private double trans_fatty_acids;
    @SerializedName("several_times")
    private double several_times;
    @SerializedName("caffeine")
    private double caffeine;

    public fooddata(){

    }

    public fooddata(long id, String name, double one_serving, double total_content, double calorie,
            double moisture, double protein, double fat, double carbohydrate, double total_sugar, double sucrose,
            double glucose, double fructose, double lactose, double maltose, double total_dietary_fiber,
            double calcium, double iron, double magnesium, double in, double potassium, double sodium, double zinc,
            double copper, double manganese, double vitamin_b1, double vitamin_b2, double vitamin_c, double cholesterol,
            double total_saturated_fatty_acids, double trans_fatty_acids, double several_times, double caffeine) {
        this.id=id;
        this.name=name;
        this.one_serving=one_serving;
        this.total_content=total_content;
        this.calorie=calorie;
        this.moisture=moisture;
        this.protein=protein;
        this.fat=fat;
        this.carbohydrate=carbohydrate;
        this.total_sugar=total_sugar;
        this.sucrose=sucrose;
        this.glucose=glucose;
        this.fructose=fructose;
        this.lactose=lactose;
        this.maltose=maltose;
        this.total_dietary_fiber=total_dietary_fiber;
        this.calcium=calcium;
        this.iron=iron;
        this.magnesium=magnesium;
        this.in=in;
        this.potassium=potassium;
        this.sodium=sodium;
        this.zinc=zinc;
        this.copper=copper;
        this.manganese=manganese;
        this.vitamin_b1=vitamin_b1;
        this.vitamin_b2=vitamin_b2;
        this.vitamin_c=vitamin_c;
        this.cholesterol=cholesterol;
        this.total_saturated_fatty_acids=total_saturated_fatty_acids;
        this.trans_fatty_acids=trans_fatty_acids;
        this.several_times=several_times;
        this.caffeine=caffeine;
    }

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public double getOne_serving(){
        return one_serving;
    }
    public void setOne_serving(double one_serving){
        this.one_serving=one_serving;
    }

    public double getTotal_content(){
        return total_content;
    }
    public void setTotal_content(double total_content){
        this.total_content=total_content;
    }

    public double getCalorie(){
        return calorie;
    }
    public void setCalorie(double calorie){
        this.calorie=calorie;
    }

    public double getMoisture(){
        return moisture;
    }
    public void setMoisture(double moisture){
        this.moisture=moisture;
    }

    public double getProtein(){
        return protein;
    }
    public void setProtein(double protein){
        this.protein=protein;
    }

    public double getFat(){
        return fat;
    }
    public void setFat(double fat){
        this.fat=fat;
    }

    public double getCarbohydrate(){
        return carbohydrate;
    }
    public void setCarbohydrate(double carbohydrate){
        this.carbohydrate=carbohydrate;
    }

    public double getTotal_sugar(){
        return total_sugar;
    }
    public void setTotal_sugar(double total_sugar){
        this.total_sugar=total_sugar;
    }

    public double getSucrose(){
        return sucrose;
    }
    public void setSucrose(double sucrose){
        this.sucrose=sucrose;
    }

    public double getGlucose(){
        return glucose;
    }
    public void setGlucose(double glucose){
        this.glucose=glucose;
    }

    public double getFructose(){
        return fructose;
    }
    public void setFructose(double fructose){
        this.fructose=fructose;
    }

    public double getLactose(){
        return lactose;
    }
    public void setLactose(double lactose){
        this.lactose=lactose;
    }

    public double getMaltose(){
        return maltose;
    }
    public void setMaltose(double maltose){
        this.maltose=maltose;
    }

    public double getTotal_dietary_fiber(){
        return total_dietary_fiber;
    }
    public void setTotal_dietary_fiber(double total_dietary_fiber){
        this.total_dietary_fiber=total_dietary_fiber;
    }

    public double getCalcium(){
        return calcium;
    }
    public void setCalcium(double calcium){
        this.calcium=calcium;
    }

    public double getIron(){
        return iron;
    }
    public void setIron(double iron){
        this.iron=iron;
    }

    public double getMagnesium(){
        return magnesium;
    }
    public void setMagnesium(double magnesium){
        this.magnesium=magnesium;
    }

    public double getIn(){
        return in;
    }
    public void setIn(double in){
        this.in=in;
    }

    public double getPotassium(){
        return potassium;
    }
    public void setPotassium(double potassium){
        this.potassium=potassium;
    }

    public double getSodium(){
        return sodium;
    }
    public void setSodium(double sodium){
        this.sodium=sodium;
    }

    public double getZinc(){
        return zinc;
    }
    public void setZinc(double zinc){
        this.zinc=zinc;
    }

    public double getCopper(){
        return copper;
    }
    public void setCopper(double copper){
        this.copper=copper;
    }

    public double getManganese(){
        return manganese;
    }
    public void setManganese(double manganese){
        this.manganese=manganese;
    }

    public double getVitamin_b1(){
        return vitamin_b1;
    }
    public void setVitamin_b1(double vitamin_b1){
        this.vitamin_b1=vitamin_b1;
    }

    public double getVitamin_b2(){
        return vitamin_b2;
    }
    public void setVitamin_b2(double vitamin_b2){
        this.vitamin_b2=vitamin_b2;
    }

    public double getVitamin_c(){
        return vitamin_c;
    }
    public void setVitamin_c(double vitamin_c){
        this.vitamin_c=vitamin_c;
    }

    public double getCholesterol(){
        return cholesterol;
    }
    public void setCholesterol(double cholesterol){
        this.cholesterol=cholesterol;
    }

    public double getTotal_saturated_fatty_acids(){
        return total_saturated_fatty_acids;
    }
    public void setTotal_saturated_fatty_acids(double saturated_fatty_acids){
        this.total_saturated_fatty_acids=total_saturated_fatty_acids;
    }

    public double getTrans_fatty_acids(){
        return trans_fatty_acids;
    }
    public void setTrans_fatty_acids(double trans_fatty_acids){
        this.trans_fatty_acids=trans_fatty_acids;
    }

    public double getSeveral_times(){
        return several_times;
    }
    public void setSeveral_times(double several_times){
        this.several_times=several_times;
    }

    public double getCaffeine(){
        return caffeine;
    }
    public void setCaffeine(double caffeine){
        this.caffeine=caffeine;
    }

    public static final Creator<fooddata> CREATOR = new Creator<fooddata>() {
        @Override
        public fooddata createFromParcel(Parcel in2) {
            return new fooddata(in2);
        }

        @Override
        public fooddata[] newArray(int size) {
            return new fooddata[size];
        }
    };

    protected fooddata(Parcel in2) {

        id=in2.readLong();
        name=in2.readString();
        one_serving=in2.readDouble();
        total_content=in2.readDouble();
        calorie=in2.readDouble();
        moisture=in2.readDouble();
        protein=in2.readDouble();
        fat=in2.readDouble();
        carbohydrate=in2.readDouble();
        total_sugar=in2.readDouble();
        sucrose=in2.readDouble();
        glucose=in2.readDouble();
        fructose=in2.readDouble();
        lactose=in2.readDouble();
        maltose=in2.readDouble();
        total_dietary_fiber=in2.readDouble();
        calcium=in2.readDouble();
        iron=in2.readDouble();
        magnesium=in2.readDouble();
        in=in2.readDouble();
        potassium=in2.readDouble();
        sodium=in2.readDouble();
        zinc=in2.readDouble();
        copper=in2.readDouble();
        manganese=in2.readDouble();
        vitamin_b1=in2.readDouble();
        vitamin_b2=in2.readDouble();
        vitamin_c=in2.readDouble();
        cholesterol=in2.readDouble();
        total_saturated_fatty_acids=in2.readDouble();
        trans_fatty_acids=in2.readDouble();
        several_times=in2.readDouble();
        caffeine=in2.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeDouble(one_serving);
        parcel.writeDouble(total_content);
        parcel.writeDouble(calorie);
        parcel.writeDouble(moisture);
        parcel.writeDouble(protein);
        parcel.writeDouble(fat);
        parcel.writeDouble(carbohydrate);
        parcel.writeDouble(total_sugar);
        parcel.writeDouble(sucrose);
        parcel.writeDouble(glucose);
        parcel.writeDouble(fructose);
        parcel.writeDouble(lactose);
        parcel.writeDouble(maltose);
        parcel.writeDouble(total_dietary_fiber);
        parcel.writeDouble(calcium);
        parcel.writeDouble(iron);
        parcel.writeDouble(magnesium);
        parcel.writeDouble(in);
        parcel.writeDouble(potassium);
        parcel.writeDouble(sodium);
        parcel.writeDouble(zinc);
        parcel.writeDouble(copper);
        parcel.writeDouble(manganese);
        parcel.writeDouble(vitamin_b1);
        parcel.writeDouble(vitamin_b2);
        parcel.writeDouble(vitamin_c);
        parcel.writeDouble(cholesterol);
        parcel.writeDouble(total_saturated_fatty_acids);
        parcel.writeDouble(trans_fatty_acids);
        parcel.writeDouble(several_times);
        parcel.writeDouble(caffeine);
    }

}
