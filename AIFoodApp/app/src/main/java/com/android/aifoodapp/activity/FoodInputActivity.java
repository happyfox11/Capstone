package com.android.aifoodapp.activity;


import static com.android.aifoodapp.interfaceh.baseURL.url;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.adapter.itemSearchAdapter;
import com.android.aifoodapp.domain.dailymeal;
import com.android.aifoodapp.domain.fooddata;
import com.android.aifoodapp.domain.meal;
import com.android.aifoodapp.interfaceh.RetrofitAPI;
import com.android.aifoodapp.domain.dailymeal;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodInputActivity extends AppCompatActivity {
    ArrayList<fooddata> arrayList;
    ArrayList<fooddata> foodList;//foodList : 지금까지 식단으로 담아놓은 음식리스트
    ArrayList<Double> intakeList=new ArrayList<>();
    List<fooddata> list;//검색 결과로 음식 데이터 전체를 가지고 있는 list
    Activity activity;
    RecyclerView recyclerView;
    EditText et_search;
    Button btn_search;
    dailymeal dailymeal;
    int pos, modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_input);
        initialize();

        Intent intent = getIntent();
        foodList=intent.getParcelableArrayListExtra("foodList");
        intakeList=(ArrayList<Double>) intent.getSerializableExtra("intakeList");
        dailymeal=intent.getParcelableExtra("dailymeal");
        pos=intent.getIntExtra("position",0);
        modify=intent.getIntExtra("modify",-1);//값이 없다면 -1

        itemSearchAdapter adapter=new itemSearchAdapter(arrayList,activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search=et_search.getText().toString();
                if(!search.equals("")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(url)
                            .addConverterFactory(GsonConverterFactory.create()).build();

                    RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

                    retrofitAPI.getFood(search).enqueue(new Callback<List<fooddata>>() {
                        @Override
                        public void onResponse(Call<List<fooddata>> call, Response<List<fooddata>> response) {
                            arrayList.clear();
                            list= response.body();
                            //Log.e("fooddata 목록",list.toString());
                            if(list.isEmpty()) {
                                Toast.makeText(getApplicationContext(),"검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                            for (fooddata repo : list) {
                                arrayList.add(repo);
                            }
                            adapter.notifyDataSetChanged(); //list값이 변경되었을때 화면에 보여지는 값들 refresh
                        }
                        @Override
                        public void onFailure(Call<List<fooddata>> call, Throwable t) {
                            Log.e("food search","실패");
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"검색어를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapter.setItemClickListener(new itemSearchAdapter.ItemClickListener() {
            @Override
            public void addFoodList(fooddata food) {
                Log.e("%%%%%%%%%",food.getName());
                if(foodList.isEmpty()) foodList=new ArrayList<fooddata>();
                if(modify!=-1) {
                    foodList.set(modify,food);//food 음식 수정하기
                    intakeList.set(modify,1.0);
                }
                else {
                    foodList.add(food);//선택한 food
                    intakeList.add(1.0);
                }
                FoodAnalysisActivity FA = (FoodAnalysisActivity)FoodAnalysisActivity._FoodAnalysis_Activity; // https://itun.tistory.com/357 [Bino]
                FA.finish();

                Intent intent = new Intent(activity, FoodAnalysisActivity.class);
                intent.putParcelableArrayListExtra("foodList",foodList); //선택한 음식데이터 넘기기
                intent.putExtra("intakeList",intakeList);
                intent.putExtra("dailymeal",dailymeal);
                intent.putExtra("position",pos);
                startActivity(intent);
                finish();
            }

        });

    }

    private void initialize(){
        activity = this;

        recyclerView = findViewById(R.id.recycleFoodSearch);
        et_search=findViewById(R.id.et_search);
        //et_search.setText("");
        btn_search=findViewById(R.id.btn_search);
        arrayList=new ArrayList<fooddata>();
    }
}