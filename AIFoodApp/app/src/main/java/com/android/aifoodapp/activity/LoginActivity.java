package com.android.aifoodapp.activity;

import static com.android.aifoodapp.interfaceh.baseURL.url;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import com.android.aifoodapp.domain.user;
import com.android.aifoodapp.interfaceh.NullOnEmptyConverterFactory;
import com.android.aifoodapp.interfaceh.RetrofitAPI;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.security.MessageDigest;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    Activity activity;
    LinearLayout  btn_google, btn_email, btn_kakao;
    ImageView loginImage, tv_title;
    static Context mContext;

    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_login);
        initialize();
        if(getIntent().getBooleanExtra("load",false)){
            tv_title.setVisibility(View.INVISIBLE);
            btn_kakao.setVisibility(View.INVISIBLE);
            btn_google.setVisibility(View.INVISIBLE);
            Glide.with(this).load(R.drawable.loading).into(loginImage);
            //loginImage.setImageResource(R.drawable.loading);
        };
        //getKeyHash();


        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if(oAuthToken != null) {
                    //???????????? ?????? (?????? ?????? ??????)
                    updateKakaoLoginUi();
                }
                if( throwable != null) {
                    //????????? ??????
                }
                //updateKakaoLoginUi();
                return null;
            }
        };

        //1. ????????? ????????? ????????? ??????
        btn_kakao.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
            /*Intent intent = new Intent(activity, MainActivity.class);
            startActivity(intent);*/
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                    //???????????? ?????????
                    UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, callback);
                }
                else {
                    UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, callback);
                }
            }
        });
        updateKakaoLoginUi();


        /*?????? ???????????? ?????? ?????????*/
        //TODO ?????? ????????? ????????? -> ???????????? ???????????? ????????? intent page??? ???????????? ????????? ?????? ????????? ???
        //?????????????????? ????????? ????????? ?????? ?????? ????????? ?????????..
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_google:
                        signIn();
                        break;
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.

        //?????? ?????????
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);

        if (gsa != null) {
            signIn();
        }


    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url).addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create()).build();

            RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

            retrofitAPI.getUser(account.getId()).enqueue(new Callback<user>() {
                @Override
                public void onResponse(Call<user> call, Response<user> response) {
                    user user = response.body();
                    if(user != null){ // ????????? db??? ?????? ??????
                        //?????? ????????? ?????? ??????

                        //main?????? ??????????????????
                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.putExtra("user",user);
                        //intent.putExtra("userId", account.getId());
                        intent.putExtra("flag","google");
                        startActivity(intent);
                        finish();
                    }
                    else { // ????????????
                        //servey ???????????? ??????????????? --> intent.putExtra??? ?????? ????????? ????????? ????????? ?????? ????????? ????????? ???????????? ??????
                        Intent intent = new Intent(activity, InitialSurveyActivity.class);
                        intent.putExtra("flag", "google");
                        startActivity(intent);
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<user> call, Throwable t) {
                    Log.d("dj",call.toString());
                    Log.d("t",t.getMessage());
                    Log.d("TestError!!!!","????????? ??????");
                }
            });


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }
    /*?????? ???????????? ?????? ????????? ???*/


    //?????? ?????????
    private void initialize(){
        activity = this;
        tv_title = findViewById(R.id.tv_title);
        btn_kakao = findViewById(R.id.btn_kakao);
        btn_google = findViewById(R.id.btn_google);
        loginImage = findViewById(R.id.loginImage);
        //btn_email = findViewById(R.id.btn_email);

        //test = findViewById(R.id.textView19);
    }

    //?????????
    public void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if ( user != null) { //?????? ????????? & ?????? ????????? ???????????? ???

                    String userId = Long.toString(user.getId());
                    String userNickName = user.getKakaoAccount().getProfile().getNickname();
                    String img=user.getKakaoAccount().getProfile().getProfileImageUrl(); //????????? ?????? uri??? ????????????.
                    String email=user.getKakaoAccount().getEmail();
                    //Log.i(TAG, "id " + user.getId());
                    //Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname());
                    //Toast.makeText(getApplicationContext(),"????????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(url).addConverterFactory(new NullOnEmptyConverterFactory())
                            .addConverterFactory(GsonConverterFactory.create()).build();

                    RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

                    retrofitAPI.getUser(userId).enqueue(new Callback<user>() {
                        @Override
                        public void onResponse(Call<user> call, Response<user> response) {
                            user user = response.body();
                            if(user != null){  //?????? id ??????
                                Intent intent = new Intent(activity, MainActivity.class);
                                //Intent intent = new Intent(activity, RequestUserInfo.class);
                                intent.putExtra("user",user);
                                //intent.putExtra("userId", userId);
                                intent.putExtra("flag","kakao");
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Intent intent = new Intent(activity, InitialSurveyActivity.class);
                                intent.putExtra("kakao_userId", userId);
                                intent.putExtra("kakao_userNickName", userNickName);
                                intent.putExtra("kakao_img",img);
                                intent.putExtra("flag","kakao");
                                intent.putExtra("kakao_email",email);
                                startActivity(intent);
                                finish();
                            }

                        }

                        @Override
                        public void onFailure(Call<user> call, Throwable t) {

                        }
                    });

                } else {
                    //mobileTextView.setText("????????? ????????????");
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
            Log.e("name not found", e.toString());
        }
    }

}