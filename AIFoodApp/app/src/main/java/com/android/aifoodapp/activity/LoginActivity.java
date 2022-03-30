package com.android.aifoodapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.aifoodapp.R;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.security.MessageDigest;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {

    Activity activity;
    LinearLayout  btn_google, btn_email;
    ImageView btn_kakao;
    static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_login);
        initialize();
        addListener();
        getKeyHash();

        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if(oAuthToken != null) {
                    //로그인이 성공 (토큰 전달 완료)
                }
                if( throwable != null) {
                    //로그인 실패
                }
                updateKakaoLoginUi();
                return null;
            }
        };

        //1. 카카오 로그인 리스너 등록
        btn_kakao.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
            /*Intent intent = new Intent(activity, MainActivity.class);
            startActivity(intent);*/
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                    //카카오톡 존재시
                    UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, callback);
                }
                else {
                    UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, callback);
                }
            }
        });
        updateKakaoLoginUi();

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
        //btn_kakao.setOnClickListener(listener_kakao_sign);
        btn_google.setOnClickListener(listener_google_sign);
        btn_email.setOnClickListener(listener_email_sign);
    }

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

    //카카오
    public void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if ( user != null) { //정상 로그인 & 이미 로그인 되어있을 때

                    String userId = Long.toString(user.getId());
                    String userNickName = user.getKakaoAccount().getProfile().getNickname();
                    //Log.i(TAG, "id " + user.getId());
                    //Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname());

                    Intent intent = new Intent(activity, MainActivity.class);

                    //intent로 userId값 전달
                    intent.putExtra("kakao_userId", userId);
                    intent.putExtra("kakao_userNickName", userNickName);
                    startActivity(intent);

                    //finish 해도 정상 작동되는지 확인 필요!
                    finish();

                } else {
                    //mobileTextView.setText("로그인 해주세요");
                }
                return  null;
            }
        });
    }


    private void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String key = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key", key);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }
}