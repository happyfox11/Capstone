package com.android.aifoodapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.aifoodapp.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

}