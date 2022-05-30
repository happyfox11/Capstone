package com.android.aifoodapp;

import static com.android.aifoodapp.interfaceh.baseURL.url;
import static java.time.MonthDay.now;

import android.app.AlarmManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.android.aifoodapp.activity.LoginActivity;
import com.android.aifoodapp.activity.MainActivity;
import com.android.aifoodapp.domain.dailymeal;
import com.android.aifoodapp.domain.user;
import com.android.aifoodapp.interfaceh.NullOnEmptyConverterFactory;
import com.android.aifoodapp.interfaceh.RetrofitAPI;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BroadcastReceive extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    user user = null;
    dailymeal dailymeal = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        String userid = intent.getDataString();
        //Toast.makeText(context, "동작중"+user.getId(), Toast.LENGTH_LONG).show();
        Log.d(TAG,"ResetHour: "+"  "+userid);
        //test
        //비동기
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //데이트 포맷(sql)

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Date now = new Date(); //Date타입으로 변수 선언


        String date_string = dateFormat.format(now); //날짜가 string으로 저장
        try{
            dailymeal=new getDailyMealNetworkCall().execute(retrofitAPI.getDailyMeal(userid,date_string)).get();
        }catch(Exception e){
            e.printStackTrace();
        }


        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date now = new Date(); //Date타입으로 변수 선언

        Log.d(TAG,"ResetHour: "+datetimeFormat.format(now));


        retrofitAPI.getUser(userid).enqueue(new Callback<user>() {
            @Override
            public void onResponse(Call<user> call, Response<user> response) {
                user = response.body();
            }

            @Override
            public void onFailure(Call<user> call, Throwable t) {

            }
        });
        Intent sintent = new Intent(context, MainActivity.class);
        sintent.putExtra("user",user);
        sintent.putExtra("dailymeal",dailymeal);
        context.startService(sintent);



    }

    private class getDailyMealNetworkCall extends AsyncTask<Call, Void, dailymeal > {
        @Override
        protected dailymeal doInBackground(Call[] params) {
            try {
                Call<dailymeal> call = params[0];
                Response<dailymeal> response = call.execute();
                return response.body();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
