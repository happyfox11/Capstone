package com.android.aifoodapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.aifoodapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodDetailInfoActivity extends AppCompatActivity {

    Activity activity;
    Intent intent;
    ImageView iv_foodDetail;
    TextView tv_foodDetailInfoCal,tv_foodDetailInfoName,tv_sugars,tv_natrium,
            tv_cholesterol,tv_saturatedFattyAcid,tv_transFat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail_info);
        initialize();

        tv_foodDetailInfoName = findViewById(R.id.tv_foodDetailInfoName);

        intent = getIntent();

        String foodName = intent.getStringExtra("foodName");
        tv_foodDetailInfoName.setText(foodName);


    }

    //변수 초기화
    private void initialize(){
        activity = this;

        iv_foodDetail =findViewById(R.id.iv_foodDetail);
        tv_foodDetailInfoName = findViewById(R.id.tv_foodDetailInfoName);
        tv_foodDetailInfoCal = findViewById(R.id.tv_foodDetailInfoCal);
        tv_sugars = findViewById(R.id.tv_sugars);
        tv_natrium = findViewById(R.id.tv_natrium);
        tv_cholesterol = findViewById(R.id.tv_cholesterol);
        tv_saturatedFattyAcid = findViewById(R.id.tv_saturatedFattyAcid);
        tv_transFat = findViewById(R.id.tv_transFat);

    }
}
