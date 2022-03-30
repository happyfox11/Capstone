package com.android.aifoodapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.aifoodapp.R;

public class LoginActivity extends AppCompatActivity {

    Activity activity;
    LinearLayout  btn_google, btn_email;
    ImageView btn_kakao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        initialize();
        addListener();
    }

    //변수 초기화
    private void initialize(){
        activity = this;
        btn_kakao = (ImageView) findViewById(R.id.btn_kakao);
        btn_google = findViewById(R.id.btn_google);
        btn_email = findViewById(R.id.btn_email);
    }

    //리스너 생성
    private void addListener(){
        btn_kakao.setOnClickListener(listener_kakao_sign);
        btn_google.setOnClickListener(listener_google_sign);
        btn_email.setOnClickListener(listener_email_sign);
    }

    //1. 카카오 로그인 리스너 등록
    private final View.OnClickListener listener_kakao_sign = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, MainActivity.class);
            startActivity(intent);
        }
    };

    //2. 구글 로그인 리스너 등록
    private final View.OnClickListener listener_google_sign = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, MainActivity.class);
            startActivity(intent);
        }
    };

    //3. 이메일 로그인 리스너 등록
    private final View.OnClickListener listener_email_sign = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, MainActivity.class);
            startActivity(intent);
        }
    };
}