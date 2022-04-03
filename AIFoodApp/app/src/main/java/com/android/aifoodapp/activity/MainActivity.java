package com.android.aifoodapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.aifoodapp.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kakao.sdk.user.UserApiClient;

import java.security.MessageDigest;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class MainActivity<Unit> extends AppCompatActivity {

    Activity activity;
    Button btn_logout;
    TextView tv_userId,tv_userName,tv_userEmail;
    ImageView tv_userPhoto;

    GoogleSignInClient mGoogleSignInClient;

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
        /*juhee modify*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_logout:
                        signOut();
                        break;
                }
            }
        });


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            tv_userId.setText(personId);
            tv_userName.setText(personName);
            tv_userEmail.setText(personEmail);
            //Glide 사용 가능
            //https://bumptech.github.io/glide/
            Glide.with(this).load(String.valueOf(personPhoto)).into(tv_userPhoto);
            //tv_userPhoto.setImageIcon(Icon.createWithContentUri(personPhoto));

        }
        /*juhee modify--fin*/


    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this,"Signed Out ok ",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    //변수 초기화
    private void initialize(){
        activity = this;
        btn_logout = findViewById(R.id.btn_logout);
        tv_userId=findViewById(R.id.tv_userId);

        tv_userPhoto=findViewById(R.id.tv_userPhoto);
        tv_userEmail=findViewById(R.id.tv_userEmail);
        tv_userName=findViewById(R.id.tv_userName);

    }


}