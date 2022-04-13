package com.android.aifoodapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.RecyclerView.FoodInfo;
import com.android.aifoodapp.RecyclerView.FoodItem;
import com.android.aifoodapp.RecyclerView.myRecyclerViewAdapter;
import com.android.aifoodapp.RecyclerView.myRecyclerViewAdapter2;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodAnalysisActivity extends AppCompatActivity {

    Activity activity;

    ArrayList<FoodItem> arrayFoodList;
    LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    com.android.aifoodapp.RecyclerView.myRecyclerViewAdapter myRecyclerViewAdapter;
    myRecyclerViewAdapter2 myRecyclerViewAdapter2;
    RecyclerView recyclerView ,recyclerView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_analysis);
        initialize();


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false); //가로 방향으로 저장
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayFoodList = new ArrayList<>();

        /*for( int i= 1; i<=4;i++){
            arrayList.add(new FoodLIst(R.drawable.ic_launcher_background,R.drawable.minusbtn,"음식"+i));
        }*/

        //어댑터 연결
        myRecyclerViewAdapter = new myRecyclerViewAdapter(arrayFoodList);
        recyclerView.setAdapter(myRecyclerViewAdapter);

        //테스트
        for ( int i=1; i<=4;i++) {
            myRecyclerViewAdapter.addItem(new FoodItem(R.drawable.ic_launcher_background, R.drawable.minusbtn, "음식" + i));

        }


        //2번째 어댑터
        recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView2);
        linearLayoutManager2 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(linearLayoutManager2);
        myRecyclerViewAdapter2 = new myRecyclerViewAdapter2();



        //테스트
        for( int i=1; i<=4; i++){
            myRecyclerViewAdapter2.addItem(new FoodInfo(R.drawable.ic_launcher_background,"음식"+i,"칼로리","0.5","음식"+i));

        }

        recyclerView2.setAdapter(myRecyclerViewAdapter2);




    }

    //변수 초기화
    private void initialize(){
        activity = this;


    }
}
