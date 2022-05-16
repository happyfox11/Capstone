package com.android.aifoodapp.activity;

import static com.android.aifoodapp.interfaceh.baseURL.url;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.RecyclerView.FoodItem;
import com.android.aifoodapp.domain.dailymeal;
import com.android.aifoodapp.domain.fooddata;
import com.android.aifoodapp.domain.meal;
import com.android.aifoodapp.domain.user;
import com.android.aifoodapp.interfaceh.RetrofitAPI;
import com.android.aifoodapp.vo.MealMemberVo;
import com.android.aifoodapp.vo.ReportDaySubItemVo;
import com.android.aifoodapp.vo.SubItem;
import com.google.android.gms.common.util.CollectionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecommendFoodActivity extends AppCompatActivity {

    private Activity activity;
    private ImageButton btn_back_recommend_food;

    private Button btn_top_1_recipe;
    private Button btn_top_2_recipe;
    private Button btn_top_3_recipe;

    private TextView tv_top_1_name;
    private TextView tv_top_2_name;
    private TextView tv_top_3_name;

    private Button btn_top_1_shop;
    private Button btn_top_2_shop;
    private Button btn_top_3_shop;

    List<fooddata> recommendeList;
    com.android.aifoodapp.domain.user user;
    com.android.aifoodapp.domain.dailymeal dailymeal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        dailymeal = intent.getParcelableExtra("dailymeal");

        setContentView(R.layout.activity_recommend_food);


        initialize();
        setting();
        addListener();


    }

    private void initialize(){
        activity = this;
        btn_back_recommend_food = findViewById(R.id.btn_back_recommend_food);

        btn_top_1_recipe = findViewById(R.id.btn_top_1_recipe);
        btn_top_2_recipe = findViewById(R.id.btn_top_2_recipe);
        btn_top_3_recipe = findViewById(R.id.btn_top_3_recipe);

        tv_top_1_name  = findViewById(R.id.tv_top_1_name);
        tv_top_2_name  = findViewById(R.id.tv_top_2_name);
        tv_top_3_name  = findViewById(R.id.tv_top_3_name);

        btn_top_1_shop = findViewById(R.id.btn_top_1_shop);
        btn_top_2_shop = findViewById(R.id.btn_top_2_shop);
        btn_top_3_shop = findViewById(R.id.btn_top_3_shop);
    }

    private void setting(){
        //juhee
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        //동기적 처리(일단 동기 처리로)
        new FoodNetworkCall().execute(retrofitAPI.getRecommendMeal(user.getId(),dailymeal.getDatekey()));

        //비동기
        /*
        retrofitAPI.getrecommendMeal(user.getId(),dailymeal.getDatekey()).enqueue(new Callback<List<fooddata>>() {
            @Override
            public void onResponse(Call<List<fooddata>> call, Response<List<fooddata>> response) {

                for (fooddata repo : response.body()) {
                    recommendeList.add(repo);
                }
            }

            @Override
            public void onFailure(Call<List<fooddata>> call, Throwable t) {

            }
        });
        */
        /* juhee --fin */

        while(true) {
            if(CollectionUtils.isEmpty(recommendeList)) {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                tv_top_1_name.setText(recommendeList.get(0).getName());
                tv_top_2_name.setText(recommendeList.get(1).getName());
                tv_top_3_name.setText(recommendeList.get(2).getName());
                break;
            }
        }
    }

    private void addListener(){
        btn_back_recommend_food.setOnClickListener(listener_back_click);

        btn_top_1_recipe.setOnClickListener(listener_show_recipe_1);
        btn_top_2_recipe.setOnClickListener(listener_show_recipe_2);
        btn_top_3_recipe.setOnClickListener(listener_show_recipe_3);

        btn_top_1_shop.setOnClickListener(listener_shop_1);
        btn_top_2_shop.setOnClickListener(listener_shop_2);
        btn_top_3_shop.setOnClickListener(listener_shop_3);
    }

    private View.OnClickListener listener_back_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private View.OnClickListener listener_show_recipe_1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /*
                Intent intent = new Intent(activity, RecipeActivity.class);
                startActivity(intent);
            */

            String meal_name = tv_top_1_name.getText().toString();
            try {
                meal_name = URLEncoder.encode(meal_name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.10000recipe.com/recipe/list.html?q="+meal_name));
            startActivity(intent);
        }
    };

    private View.OnClickListener listener_show_recipe_2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /*
                Intent intent = new Intent(activity, RecipeActivity.class);
                startActivity(intent);
            */
            String meal_name = tv_top_2_name.getText().toString();
            try {
                meal_name = URLEncoder.encode(meal_name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.10000recipe.com/recipe/list.html?q="+meal_name));
            startActivity(intent);
        }
    };

    private View.OnClickListener listener_show_recipe_3 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /*
                Intent intent = new Intent(activity, RecipeActivity.class);
                startActivity(intent);
            */
            String meal_name = tv_top_3_name.getText().toString();
            try {
                meal_name = URLEncoder.encode(meal_name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.10000recipe.com/recipe/list.html?q="+meal_name));
            startActivity(intent);
        }
    };

    private View.OnClickListener listener_shop_1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, SelectMarketActivity.class);
            intent.putExtra("meal_name", tv_top_1_name.getText().toString());
            startActivity(intent);
        }
    };

    private View.OnClickListener listener_shop_2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, SelectMarketActivity.class);
            intent.putExtra("meal_name", tv_top_2_name.getText().toString());
            startActivity(intent);
        }
    };

    private View.OnClickListener listener_shop_3 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, SelectMarketActivity.class);
            intent.putExtra("meal_name", tv_top_3_name.getText().toString());
            startActivity(intent);
        }
    };

    private class FoodNetworkCall extends AsyncTask<Call, Void, String> {

        //동기적 처리
        @Override
        protected String doInBackground(Call[] params) {

            try {
                Call<List<fooddata>> call = params[0];
                Response<List<fooddata>> response = call.execute();

                //Log.d("Error:size",response.body().toString());
                recommendeList = response.body();
                if(CollectionUtils.isEmpty(recommendeList)){
                    //null 값이 나올확률은 없는데 에러나는것 확인 필요
                    recommendeList.add(new fooddata());
                    recommendeList.add(new fooddata());
                    recommendeList.add(new fooddata());
                }
                //null처리
                //if(CollectionUtils.isEmpty(response.body())) {
                //    return null;
                //}
                //for (fooddata repo : response.body()) {
                //    recommendeList.add(repo);
                //}


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}