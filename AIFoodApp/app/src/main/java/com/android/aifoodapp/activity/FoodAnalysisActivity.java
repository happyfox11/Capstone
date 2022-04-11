package com.android.aifoodapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.aifoodapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodAnalysisActivity extends AppCompatActivity {

    Activity activity;
    ImageView iv_foodAnalysis,iv_minusBtn,iv_plusBtn,cl_foodInfo,
            iv_scrollUpBtn,iv_scrollDownBtn,iv_arrowBtn,iv_infoBtn;
    TextView tv_foodName,fl_foodName,tv_calories,cl_foodName,cl_caloriesInfo,cl_intake;
    CircleImageView fl_foodInfo;

    Button  modifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_analysis);
        initialize();


        iv_arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FoodDetailInfoActivity.class);
                startActivity(intent);
            }
        });

    }

    //변수 초기화
    private void initialize(){
        activity = this;
        iv_foodAnalysis = findViewById(R.id.iv_foodAnalysis);
        tv_foodName = findViewById(R.id.tv_foodName);
        fl_foodInfo = findViewById(R.id.fl_foodInfo);
        iv_minusBtn = findViewById(R.id.iv_minusBtn);
        fl_foodName = findViewById(R.id.fl_foodName);
        iv_plusBtn = findViewById(R.id.iv_plusBtn);
        tv_calories = findViewById(R.id.tv_calories);
        cl_foodInfo = findViewById(R.id.cl_foodInfo);
        cl_foodName = findViewById(R.id.cl_foodName);
        cl_caloriesInfo = findViewById(R.id.cl_caloriesInfo);
        cl_intake = findViewById(R.id.cl_intake);
        iv_scrollUpBtn = findViewById(R.id.iv_scorllUpBtn);
        iv_scrollDownBtn = findViewById(R.id.iv_scorllDownBtn);
        iv_arrowBtn = findViewById(R.id.iv_arrowBtn);
        iv_infoBtn = findViewById(R.id.iv_infoBtn);
        modifyBtn = findViewById(R.id.modifyBtn);

    }
}