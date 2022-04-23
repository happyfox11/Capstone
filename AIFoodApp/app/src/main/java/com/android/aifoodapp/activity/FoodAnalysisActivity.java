package com.android.aifoodapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.RecyclerView.FoodInfo;
import com.android.aifoodapp.RecyclerView.FoodItem;
import com.android.aifoodapp.RecyclerView.FoodItemAdapter;
import com.android.aifoodapp.RecyclerView.FoodInfoAdapter;
import com.android.aifoodapp.domain.fooddata;
import com.android.aifoodapp.domain.user;

import java.util.ArrayList;

public class FoodAnalysisActivity extends AppCompatActivity {

    Activity activity;

    ArrayList<FoodItem> foodItemList = new ArrayList<>();
    ArrayList<FoodInfo> foodInfoList = new ArrayList<>();

    FoodItemAdapter foodItemAdapter;
    FoodInfoAdapter foodInfoAdapter;
    RecyclerView recyclerView ,recyclerView2;
    ImageView iv_plusBtn;
    user user;
    fooddata addFoodData;
    ArrayList<fooddata> foodList=new ArrayList<>(); //담은 식단 목록

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_analysis);
        initialize();

        Intent intent = getIntent();
        foodList=intent.getParcelableArrayListExtra("foodList");
        //addFoodData=intent.getParcelableExtra("addFoodData"); //수기입력에서 넘어온 값 -> 음식 하나
        /*
        if(addFoodData!=null) {
            foodList.add(addFoodData);
        }*/

        if(foodList!=null){
            for(fooddata repo : foodList){
                String img = String.valueOf(R.drawable.ic_launcher_background); //기본 사진
                foodItemList.add(new FoodItem(img,R.drawable.minusbtn,repo.getName()));
                foodInfoList.add(new FoodInfo(repo,img,1.0)); //음식객체, 이미지, 인분
            }
        }
        else{
            foodList=new ArrayList<>();
        }

        //어댑터 연결
        foodItemAdapter = new FoodItemAdapter(foodItemList);
        recyclerView.setAdapter(foodItemAdapter);

        //2번째 어댑터 연결
        foodInfoAdapter = new FoodInfoAdapter(foodInfoList);
        recyclerView2.setAdapter(foodInfoAdapter);

        foodItemAdapter.setItemClickListener(new FoodItemAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(FoodItem item) {

            }
            @Override
            public void onRemoveButtonClicked(int position) {
                foodInfoAdapter.removeById(position);
                foodList.remove(position);
            }
        });

        iv_plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FoodInputActivity.class);
                intent.putExtra("user",user);
                intent.putExtra("foodList",foodList);
                startActivity(intent);
            }
        });

    }

    //변수 초기화
    private void initialize(){
        activity = this;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView2);
        iv_plusBtn=findViewById(R.id.iv_plusBtn);
    }
}
