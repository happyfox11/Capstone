package com.android.aifoodapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.aifoodapp.R;

public class SelectMarketActivity extends AppCompatActivity {

    private Button btn_naver_shop;
    private Button btn_coupang_shop;
    private Button btn_gmarket_shop;
    private Button btn_auction_shop;

    private String meal_name;

    private ImageButton btn_shop_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_market);

        initialize();
        setting();
        addListener();
    }

    private void initialize(){
        btn_naver_shop = findViewById(R.id.btn_naver_shop);
        btn_coupang_shop = findViewById(R.id.btn_coupang_shop);
        btn_gmarket_shop = findViewById(R.id.btn_gmarket_shop);
        btn_auction_shop = findViewById(R.id.btn_auction_shop);
        btn_shop_back = findViewById(R.id.btn_shop_back);
    }

    private void setting(){
        meal_name = getIntent().getStringExtra("meal_name");
    }

    private void addListener(){
        btn_naver_shop.setOnClickListener(listener_naver);
        btn_coupang_shop.setOnClickListener(listener_coupang);
        btn_gmarket_shop.setOnClickListener(listener_gmarket);
        btn_auction_shop.setOnClickListener(listener_auction);
        btn_shop_back.setOnClickListener(listener_back);
    }

    private View.OnClickListener listener_back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            finish();
        }
    };

    private View.OnClickListener listener_naver = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://msearch.shopping.naver.com/search/all?query="+meal_name));
            startActivity(intent);
            finish();
        }
    };

    private View.OnClickListener listener_coupang = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coupang.com/np/search?component=&q="+meal_name));
            startActivity(intent);
            finish();
        }
    };

    private View.OnClickListener listener_gmarket = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://browse.gmarket.co.kr/search?keyword=" + meal_name));
            startActivity(intent);
            finish();
        }
    };


    private View.OnClickListener listener_auction = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://browse.auction.co.kr/search?keyword=" + meal_name));
            startActivity(intent);
            finish();
        }
    };
}