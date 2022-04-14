package com.android.aifoodapp.activity;

import static com.android.aifoodapp.interfaceh.baseURL.url;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.aifoodapp.R;
import com.android.aifoodapp.domain.user;
import com.android.aifoodapp.interfaceh.NullOnEmptyConverterFactory;
import com.android.aifoodapp.interfaceh.RetrofitAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//db 처리용
public class RequestUserInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_user_info);

        Intent intent = getIntent();
        String userId=intent.getStringExtra("userId");
        String flag=intent.getStringExtra("flag");

        //user정보 불러오기 위한 호출
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url).addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create()).build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        retrofitAPI.getUser(userId).enqueue(new Callback<user>() {
            @Override
            public void onResponse(Call<user> call, Response<user> response) {
                user user = response.body();
                Log.d("RequestRetrofitAPI","Post 성공");

                Intent intent2 = new Intent(RequestUserInfo.this, MainActivity.class);
                intent2.putExtra("user",user);
                intent2.putExtra("flag",flag);
                startActivity(intent2);
                finish();

            }
            @Override
            public void onFailure(Call<user> call, Throwable t) {
                Log.d("RequestRetrofitAPI","Post 실패 ");
            }
        });
    }
}