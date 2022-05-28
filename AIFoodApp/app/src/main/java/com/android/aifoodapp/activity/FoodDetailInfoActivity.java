package com.android.aifoodapp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.aifoodapp.R;
import com.android.aifoodapp.domain.fooddata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodDetailInfoActivity extends AppCompatActivity {

    Activity activity;
    Bitmap bitmap = null, compressedBitmap;
    ImageView iv_foodDetail;
    TextView tv_calorie,tv_foodDetailInfoName, tv_carbohydrate, tv_protein, tv_fat,
            tv_moisture, tv_total_sugar, tv_sucrose, tv_glucose, tv_fructose, tv_lactose, tv_maltose, tv_total_dietary_fiber,
            tv_calcium, tv_iron, tv_magnesium, tv_in, tv_potassium, tv_sodium, tv_zinc,
            tv_copper, tv_manganese, tv_vitamin_b1, tv_vitamin_b2, tv_vitamin_c, tv_cholesterol,
            tv_total_saturated_fatty_acids, tv_trans_fatty_acids, tv_caffeine;
    fooddata food;
    ImageButton btn_back_detail;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail_info);
        initialize();

        Intent intent = getIntent();
        food=intent.getParcelableExtra("foodDetail");
        String img=intent.getStringExtra("foodImg");

        btn_back_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(img.equals("")){
            Drawable drawable = getResources().getDrawable(R.drawable.icon);
            compressedBitmap = ((BitmapDrawable)drawable).getBitmap();
        }
        else{
            String temp="";
            try{
                temp= URLDecoder.decode(img,"UTF-8");
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }

            byte[] decodeByte = Base64.decode(temp, Base64.DEFAULT);
            compressedBitmap = BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
        }

        iv_foodDetail.setImageBitmap(compressedBitmap);
        tv_foodDetailInfoName.setText(food.getName());

       /* tv_calorie.setText(String.valueOf(food.getCalorie()));
        tv_carbohydrate.setText(String.valueOf(food.getCarbohydrate()));
        tv_protein.setText(String.valueOf(food.getProtein()));
        tv_fat.setText(String.valueOf(food.getFat()));

        tv_moisture.setText(String.valueOf(food.getMoisture()));
        tv_total_sugar.setText(String.valueOf(food.getTotal_sugar()));
        tv_sucrose.setText(String.valueOf(food.getSucrose()));
        tv_glucose.setText(String.valueOf(food.getGlucose()));
        tv_fructose.setText(String.valueOf(food.getFructose()));
        tv_lactose.setText(String.valueOf(food.getLactose()));
        tv_maltose.setText(String.valueOf(food.getMaltose()));
        tv_total_dietary_fiber.setText(String.valueOf(food.getTotal_dietary_fiber()));
        tv_calcium.setText(String.valueOf(food.getCalcium()));
        tv_iron.setText(String.valueOf(food.getIron()));
        tv_magnesium.setText(String.valueOf(food.getMagnesium()));
        tv_in.setText(String.valueOf(food.getIn()));
        tv_potassium.setText(String.valueOf(food.getPotassium()));
        tv_sodium.setText(String.valueOf(food.getSodium()));
        tv_zinc.setText(String.valueOf(food.getZinc()));
        tv_copper.setText(String.valueOf(food.getCopper()));
        tv_manganese.setText(String.valueOf(food.getManganese()));
        tv_vitamin_b1.setText(String.valueOf(food.getVitamin_b1()));
        tv_vitamin_b2.setText(String.valueOf(food.getVitamin_b2()));
        tv_vitamin_c.setText(String.valueOf(food.getVitamin_c()));
        tv_cholesterol.setText(String.valueOf(food.getCholesterol()));
        tv_total_saturated_fatty_acids.setText(String.valueOf(food.getTotal_saturated_fatty_acids()));
        tv_trans_fatty_acids.setText(String.valueOf(food.getTrans_fatty_acids()));
        tv_caffeine.setText(String.valueOf(food.getCaffeine()));*/

        tv_calorie.setText(String.format("%.2f", food.getCalorie()));
        tv_calorie.setText(String.format("%.2f", food.getCalorie()));
        tv_carbohydrate.setText(String.format("%.2f",food.getCarbohydrate()));
        tv_protein.setText(String.format("%.2f",food.getProtein()));
        tv_fat.setText(String.format("%.2f",food.getFat()));

        tv_moisture.setText(String.format("%.3f",food.getMoisture()));
        tv_total_sugar.setText(String.format("%.3f",food.getTotal_sugar()));
        tv_sucrose.setText(String.format("%.3f",food.getSucrose()));
        tv_glucose.setText(String.format("%.3f",food.getGlucose()));
        tv_fructose.setText(String.format("%.3f",food.getFructose()));
        tv_lactose.setText(String.format("%.3f",food.getLactose()));
        tv_maltose.setText(String.format("%.3f",food.getMaltose()));
        tv_total_dietary_fiber.setText(String.format("%.3f",food.getTotal_dietary_fiber()));
        tv_calcium.setText(String.format("%.3f",food.getCalcium()));
        tv_iron.setText(String.format("%.3f",food.getIron()));
        tv_magnesium.setText(String.format("%.3f",food.getMagnesium()));
        tv_in.setText(String.format("%.3f",food.getIn()));
        tv_potassium.setText(String.format("%.3f",food.getPotassium()));
        tv_sodium.setText(String.format("%.3f",food.getSodium()));
        tv_zinc.setText(String.format("%.3f",food.getZinc()));
        tv_copper.setText(String.format("%.3f",food.getCopper()));
        tv_manganese.setText(String.format("%.3f",food.getManganese()));
        tv_vitamin_b1.setText(String.format("%.3f",food.getVitamin_b1()));
        tv_vitamin_b2.setText(String.format("%.3f",food.getVitamin_b2()));
        tv_vitamin_c.setText(String.format("%.3f",food.getVitamin_c()));
        tv_cholesterol.setText(String.format("%.3f",food.getCholesterol()));
        tv_total_saturated_fatty_acids.setText(String.format("%.3f",food.getTotal_saturated_fatty_acids()));
        tv_trans_fatty_acids.setText(String.format("%.3f",food.getTrans_fatty_acids()));
        tv_caffeine.setText(String.format("%.3f",food.getCaffeine()));

    }

    //변수 초기화
    private void initialize(){
        activity = this;

        iv_foodDetail =findViewById(R.id.iv_foodDetail);
        tv_foodDetailInfoName = findViewById(R.id.tv_foodDetailInfoName);

        tv_calorie = findViewById(R.id.tv_calorie);
        tv_carbohydrate = findViewById(R.id.tv_carbohydrate);
        tv_protein = findViewById(R.id.tv_protein);
        tv_fat = findViewById(R.id.tv_fat);

        tv_moisture = findViewById(R.id.tv_moisture);
        tv_total_sugar = findViewById(R.id.tv_total_sugar);
        tv_sucrose = findViewById(R.id.tv_sucrose);
        tv_glucose = findViewById(R.id.tv_glucose);
        tv_fructose = findViewById(R.id.tv_fructose);
        tv_lactose = findViewById(R.id.tv_lactose);
        tv_maltose = findViewById(R.id.tv_maltose);
        tv_total_dietary_fiber = findViewById(R.id.tv_total_dietary_fiber);
        tv_calcium = findViewById(R.id.tv_calcium);
        tv_iron = findViewById(R.id.tv_iron);
        tv_magnesium = findViewById(R.id.tv_magnesium);
        tv_in = findViewById(R.id.tv_in);
        tv_potassium = findViewById(R.id.tv_potassium);
        tv_sodium = findViewById(R.id.tv_sodium);
        tv_zinc = findViewById(R.id.tv_zinc);
        tv_copper = findViewById(R.id.tv_copper);
        tv_manganese = findViewById(R.id.tv_magnesium);
        tv_vitamin_b1 = findViewById(R.id.tv_vitamin_b1);
        tv_vitamin_b2 = findViewById(R.id.tv_vitamin_b2);
        tv_vitamin_c = findViewById(R.id.tv_vitamin_c);
        tv_cholesterol = findViewById(R.id.tv_cholesterol);
        tv_total_saturated_fatty_acids = findViewById(R.id.tv_total_saturated_fatty_acids);
        tv_trans_fatty_acids = findViewById(R.id.tv_trans_fatty_acids);
        tv_caffeine = findViewById(R.id.tv_caffeine);
        btn_back_detail = findViewById(R.id.btn_back_detail);

    }
    private Bitmap getCompressedBitmap(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80, stream);
        byte[] byteArray = stream.toByteArray();
        compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        return compressedBitmap;
    }
}
