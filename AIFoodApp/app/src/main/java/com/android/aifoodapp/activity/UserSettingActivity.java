package com.android.aifoodapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.aifoodapp.R;
import com.android.aifoodapp.domain.user;

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
    TextView tv_sex;

    com.android.aifoodapp.domain.user user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersetting);
        initialize();

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        settingText();
    }

    private void settingText(){
        //iv_profile
        tv_userName.setText(user.getNickname());
        tv_userIdInfo.setText(user.getId());
        tv_sex.setText(String.valueOf(user.getSex()));
        //tv_modifyInfo
        //tv_modifyPwd
        tv_age.setText(String.valueOf(user.getAge()));
        tv_height.setText(String.valueOf(user.getHeight()));
        tv_kg.setText(String.valueOf(user.getWeight()));
        tv_bmi.setText(String.valueOf(user.getTarget_calories()));

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
        tv_sex=findViewById(R.id.tv_sex);


    }
}
