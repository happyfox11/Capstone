package com.android.aifoodapp.activity;

import static com.android.aifoodapp.interfaceh.baseURL.url;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.RecyclerView.FoodInfo;
import com.android.aifoodapp.RecyclerView.FoodItem;
import com.android.aifoodapp.RecyclerView.FoodItemAdapter;
import com.android.aifoodapp.RecyclerView.FoodInfoAdapter;
import com.android.aifoodapp.domain.dailymeal;
import com.android.aifoodapp.domain.fooddata;
import com.android.aifoodapp.domain.meal;
import com.android.aifoodapp.domain.user;
import com.android.aifoodapp.domain.dailymeal;
import com.android.aifoodapp.interfaceh.RetrofitAPI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodAnalysisActivity extends AppCompatActivity {

    public static Activity _FoodAnalysis_Activity;
    Activity activity;

    ArrayList<FoodItem> foodItemList = new ArrayList<>();
    ArrayList<FoodInfo> foodInfoList = new ArrayList<>();

    FoodItemAdapter foodItemAdapter;
    FoodInfoAdapter foodInfoAdapter;
    RecyclerView recyclerView ,recyclerView2;
    TextView tv_foodName;
    ImageView iv_plusBtn;
    Button btn_insert_dailymeal;
    user user;
    dailymeal dailymeal;
    fooddata addFoodData;
    ArrayList<fooddata> foodList=new ArrayList<>(); //담은 식단 목록
    //List<meal> mealList=new ArrayList<>();
    //HashMap<String, List<meal>> map = new HashMap<>();
    HashMap<String, Object> map = new HashMap<>();
    HashMap<String, Object> dailyMap = new HashMap<>();

    String userid, mealname, mealphoto;
    long dailymealid, mealid;
    int calorie, protein, carbohydrate, fat, timeflag, sumCalorie=0, sumProtein=0,sumCarbohydrate=0, sumFat=0;
    String savetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_analysis);
        initialize();
        _FoodAnalysis_Activity = FoodAnalysisActivity.this;

        Intent intent = getIntent();
        foodList=intent.getParcelableArrayListExtra("foodList");
        dailymeal=intent.getParcelableExtra("dailymeal");
        Log.e("dailymeal- userid",dailymeal.getUserid());
        //addFoodData=intent.getParcelableExtra("addFoodData"); //수기입력에서 넘어온 값 -> 음식 하나
        /*
        if(addFoodData!=null) {
            foodList.add(addFoodData);
        }*/

        if(foodList!=null){
            for(fooddata repo : foodList){
                //String img = String.valueOf(R.drawable.ic_launcher_background); //기본 사진
                String img=String.valueOf(R.drawable.icon);
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
                tv_foodName.setText(item.getFl_foodName());
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
                intent.putExtra("dailymeal",dailymeal);
                intent.putExtra("foodList",foodList);
                startActivity(intent);
            }
        });

        //meal과 dailymeal에 입력
        btn_insert_dailymeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //데이트 포맷(sql)

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();

                RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

                int cnt=0;
                for(fooddata repo : foodList){
                    userid = dailymeal.getUserid();
                    dailymealid = dailymeal.getDailymealid();
                    mealid=cnt+1;//repo.getId();
                    calorie=(int)repo.getCalorie();
                    protein=(int)repo.getProtein();
                    carbohydrate=(int)repo.getCarbohydrate();
                    fat=(int)repo.getFat();
                    mealname=repo.getName();
                    mealphoto="";
                    savetime=dailymeal.getDatekey();//해당 달력 날짜(과거날짜에서 데이터 추가하는 경우도 있기 때문)
                    //savetime = dateFormat.format(now); //날짜가 string으로 저장
                    //savetime=now;//형식없이 괜찮나?
                    timeflag=dailymeal.getStepcount()+1;//끼니별 식단 구분용으로?

                    meal meal = new meal(userid,dailymealid,mealid,calorie,protein,carbohydrate,fat,mealname,mealphoto,savetime,timeflag);
                    map.put("userid",meal.getUserid());
                    map.put("dailymealid",meal.getDailymealid());
                    map.put("mealid",meal.getMealid());
                    map.put("calorie",meal.getCalorie());
                    map.put("protein",meal.getProtein());
                    map.put("carbohydrate",meal.getCarbohydrate());
                    map.put("fat",meal.getFat());
                    map.put("mealname",meal.getMealname());
                    map.put("mealphoto",meal.getMealphoto());
                    map.put("savetime",meal.getSavetime());
                    map.put("timeflag",meal.getTimeflag());

                    cnt++;
                    sumCalorie+=calorie;
                    sumCarbohydrate+=carbohydrate;
                    sumProtein+=protein;
                    sumFat+=fat;

                    //mealList.add(meal);
                    retrofitAPI.postSaveMeal(map).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()) {
                                Log.e("★","추가완료");
                            }
                            else{
                                Log.e("★","!response.isSuccessful()");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("★","call 실패"+t);
                        }
                    });
                }

                //map.put("mealList",mealList);//서버 측에서 받는 key값 : mealList

                /*dailymeal update*/
//                dailymeal updatedailymeal = new dailymeal(userid,savetime,timeflag,sumCalorie, sumProtein,sumCarbohydrate, sumFat,dailymealid);
//                dailyMap.put("userid",updatedailymeal.getUserid());
//                dailyMap.put("datekey",updatedailymeal.getDatekey());
//                dailyMap.put("stepcount",updatedailymeal.getStepcount());
//                dailyMap.put("calorie",updatedailymeal.getCalorie());
//                dailyMap.put("protein",updatedailymeal.getProtein());
//                dailyMap.put("carbohydrate",updatedailymeal.getCarbohydrate());
//                dailyMap.put("fat",updatedailymeal.getFat());
//                dailyMap.put("dailymealid",updatedailymeal.getDailymealid());
//
//                retrofitAPI.postUpdateDailyMeal(dailyMap).enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        if(response.isSuccessful()) {
//                            Log.e("★","dailymealUpdate");
//                        }
//                        else{
//                            Log.e("★","!response.isSuccessful()");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Log.e("★","call 실패"+t);
//                    }
//                });

                Intent intent = new Intent(activity, LoginActivity.class);
                intent.putExtra("load",true);//로딩화면(call받기위해?)
                startActivity(intent);
                finish();
            }
        });


    }

    //https://whereisusb.tistory.com/30
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
        alert_ex.setMessage("작성을 그만두시겠습니까?");

        alert_ex.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.putExtra("load",true);
                startActivity(intent);
                finish();
            }
        });
        alert_ex.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert_ex.setTitle("알림");
        AlertDialog alert = alert_ex.create();
        alert.show();

    }

    //변수 초기화
    private void initialize(){
        activity = this;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView2);
        iv_plusBtn=findViewById(R.id.iv_plusBtn);
        tv_foodName = findViewById(R.id.tv_foodName);
        btn_insert_dailymeal = findViewById(R.id.btn_insert_dailymeal);
    }
}
