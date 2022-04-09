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

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    Activity activity;
    LinearLayout  btn_google, btn_email;
    ImageView btn_kakao;
    static Context mContext;
    TextView test;


    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_login);
        initialize();
        addListener();
        getKeyHash();

        //juhee
        // URL 설정. 나중에 서버가면 수정 필요
        //String url = "http://       .cafe24.com/LoadPat        ";
        //String url = "https://naver.com";

        //핸드폰이용하는 경우 -- https://rateye.tistory.com/1082?category=1026651
        String url = "http://192.168.219.102:8080/json.do"; //juhee
        //avd를 이용하는 경우
        //String url = "http://10.0.2.2:8080/json.do";
        
        
        // AsyncTask를 통해 HttpURLConnection 수행.
        //LoginActivity.NetworkTask networkTask = new LoginActivity.NetworkTask(url, null);
        LoginActivity.NetworkTask networkTask = new LoginActivity.NetworkTask(url, null);

        networkTask.execute();



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


        /*주희 수정부분 구글 로그인*/
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
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



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
            Intent intent = new Intent(activity, MainActivity.class);
            startActivity(intent);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }
    /*주희 수정부분 구글 로그인 끝*/


    //변수 초기화
    private void initialize(){
        activity = this;
        btn_kakao = (ImageView) findViewById(R.id.btn_kakao);
        btn_google = findViewById(R.id.btn_google);
        btn_email = findViewById(R.id.btn_email);

        test = findViewById(R.id.textView19);
    }

    //리스너 생성
    private void addListener(){
        //btn_kakao.setOnClickListener(listener_kakao_sign);
        //btn_google.setOnClickListener(listener_google_sign);
        btn_email.setOnClickListener(listener_email_sign);
    }


    //2. 구글 로그인 리스너 등록
    /*
    private final View.OnClickListener listener_google_sign = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, MainActivity.class);
            startActivity(intent);
        }
    };

    */

    //3. 이메일 로그인 리스너 등록
    private final View.OnClickListener listener_email_sign = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, InitialSurveyActivity.class);
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
                    /*  hanbyul comment:
                        MainActivity에서 로그아웃 버튼을 누르고 다시 앱에 들어가는 경우, 로그아웃 처리가 되지 않는 문제가 발생합니다.
                        아래 finish()를 주석처리하면 문제가 발생하지 않는 것 같아서 일단 주석처리 해두었습니다.
                    */
                    //finish();

                } else {
                    //mobileTextView.setText("로그인 해주세요");
                }
                return  null;
            }
        });
    }

    /*juhee*/
    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            test.setText(s);
        }
    }

    /*juhee -- fin*/




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