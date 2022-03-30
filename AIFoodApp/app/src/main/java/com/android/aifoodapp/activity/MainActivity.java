package com.android.aifoodapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.aifoodapp.R;
import com.kakao.sdk.user.UserApiClient;

import java.security.MessageDigest;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class MainActivity<Unit> extends AppCompatActivity {

    Activity activity;
    Button btn_logout;
    TextView tv_userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        Intent intent = getIntent();
        String userId=intent.getStringExtra("kakao_userNickName");

        tv_userId.setText(userId);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserApiClient.getInstance().logout((throwable) -> {
                    if (throwable != null) {
                        Log.e("[카카오] 로그아웃", "실패", throwable);
                    }
                    else {
                        Log.i("[카카오] 로그아웃", "성공");
                        ((LoginActivity)LoginActivity.mContext).updateKakaoLoginUi();
                    }
                    return null;
                });

                Intent backIntent = new Intent(getApplicationContext(), LoginActivity.class); // 로그인 화면으로 이동
                startActivity(backIntent);
                finish();

            }
        });
    }

    //변수 초기화
    private void initialize(){
        activity = this;
        btn_logout = findViewById(R.id.btn_logout);
        tv_userId=findViewById(R.id.tv_userId);
    }


}