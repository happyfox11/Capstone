package com.android.aifoodapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.aifoodapp.R;

public class UserSettingActivity extends AppCompatActivity {

    Activity activity;
    ImageView iv_profile;
    TextView tv_userName;
    TextView tv_userIdInfo;
    TextView tv_modifyInfo;
    TextView tv_modifyPwd;
    TextView tv_age;
    TextView tv_height;
    TextView tv_kg;
    TextView tv_bmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersetting);
        initialize();


    }


    //변수 초기화
    private void initialize(){
        activity = this;
        iv_profile = findViewById(R.id.iv_profile);
        tv_userName = findViewById(R.id.tv_userName);
        tv_userIdInfo = findViewById(R.id.tv_userIdInfo);
        tv_modifyInfo = findViewById(R.id.tv_modifyInfo);
        tv_modifyPwd = findViewById(R.id.tv_userName);
        tv_age = findViewById(R.id.tv_age);
        tv_height = findViewById(R.id.tv_height);
        tv_kg = findViewById(R.id.tv_kg);
        tv_bmi = findViewById(R.id.tv_bmi);


    }
}
