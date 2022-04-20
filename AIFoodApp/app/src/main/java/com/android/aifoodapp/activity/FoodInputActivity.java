package com.android.aifoodapp.activity;


import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.adapter.itemSearchAdapter;

import java.util.ArrayList;

public class FoodInputActivity extends AppCompatActivity {
    ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_input);

        list=new ArrayList<String>();

        list.add("text");
        list.add("text2");

        itemSearchAdapter adapter=new itemSearchAdapter(list);
        RecyclerView recyclerView = findViewById(R.id.recycleFoodSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged(); //btn_search을 눌러 list값이 변경되었을때 화면에 보여지는 값들 refresh
    }
}