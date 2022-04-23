package com.android.aifoodapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.RecyclerView.FoodInfo;
import com.android.aifoodapp.RecyclerView.FoodItem;
import com.android.aifoodapp.RecyclerView.FoodItemAdapter;
import com.android.aifoodapp.RecyclerView.FoodInfoAdapter;
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
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_analysis);
        initialize();

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");

        //테스트

        for (int id = 1; id <= 5; id++) {
            int foodInfo = R.drawable.ic_launcher_background;
            String foodName = "음식" + id;

            foodItemList.add(new FoodItem(id, foodInfo, R.drawable.minusbtn, foodName));
            foodInfoList.add(new FoodInfo(id, foodInfo, foodName, "칼로리", "0", "음식" + id));
        }

        //어댑터 연결
        foodItemAdapter = new FoodItemAdapter(foodItemList);
        foodItemAdapter.setItemClickListener(new FoodItemAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(FoodItem item) {
            }

            @Override
            public void onRemoveButtonClicked(FoodItem item) {
                foodInfoAdapter.removeById(item.getId());
            }
        });

        recyclerView.setAdapter(foodItemAdapter);

        //2번째 어댑터 연결
        foodInfoAdapter = new FoodInfoAdapter(foodInfoList);
        recyclerView2.setAdapter(foodInfoAdapter);


        iv_plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FoodInputActivity.class);
                intent.putExtra("user",user);
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
