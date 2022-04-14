package com.android.aifoodapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.aifoodapp.R;
import com.android.aifoodapp.domain.user;

public class UserSettingActivity extends AppCompatActivity {

    Activity activity;
    ImageView iv_profile;
    EditText tv_userName;
    EditText tv_userIdInfo;
    Button btn_modifyInfo;
    Button btn_withdrawal;
    EditText tv_age;
    EditText tv_height;
    EditText tv_kg;
    EditText tv_bmi;
    EditText tv_sex;

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
        if(user.getProfile()!=null) { iv_profile.setImageURI(Uri.parse(user.getProfile())); }
        tv_userName.setText(user.getNickname());
        tv_userIdInfo.setText(user.getId());
        tv_sex.setText(String.valueOf(user.getSex()));
        //btn_modifyInfo
        //btn_withdrawal
        tv_age.setText(String.valueOf(user.getAge()));
        tv_height.setText(String.valueOf(user.getHeight()));
        tv_kg.setText(String.valueOf(user.getWeight()));
        tv_bmi.setText(String.valueOf(user.getTarget_calories()));

    }

    //변수 초기화
    private void initialize(){
        activity = this;
        iv_profile = (ImageView) findViewById(R.id.iv_profile);
        tv_userName = findViewById(R.id.tv_userName);
        tv_userIdInfo = findViewById(R.id.tv_userIdInfo);
        btn_modifyInfo = findViewById(R.id.btn_modifyInfo);
        btn_withdrawal=findViewById(R.id.btn_withdrawal);
        tv_age = findViewById(R.id.tv_age);
        tv_height = findViewById(R.id.tv_height);
        tv_kg = findViewById(R.id.tv_kg);
        tv_bmi = findViewById(R.id.tv_bmi);
        tv_sex=findViewById(R.id.tv_sex);

    }
}
