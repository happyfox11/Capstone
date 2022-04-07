package com.android.aifoodapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class MainActivity<Unit> extends AppCompatActivity {

    Activity activity;
    Button btn_logout;
    TextView tv_userId,tv_userName,tv_userEmail;
    ImageView tv_userPhoto;

    GoogleSignInClient mGoogleSignInClient;

    TextView tv_month, tv_dialog_result;
    LinearLayout layout_date, layout_calories;
    Button btn_select_calendar;
    ProgressBar pb_total_calories, pb_carbohydrate, pb_protein, pb_fat;
    TextView tv_total_calories, tv_gram_of_carbohydrate, tv_gram_of_protein, tv_gram_of_fat;

    ImageView iv_user_setting_update;
    Button btn_meal1_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        setting();
        addListener();

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

        layout_date = findViewById(R.id.layout_date);
        tv_month = findViewById(R.id.tv_month);
        layout_calories = findViewById(R.id.layout_calories);
        btn_select_calendar = findViewById(R.id.btn_select_calendar);
        tv_dialog_result = findViewById(R.id.tv_dialog_result);
        pb_total_calories = findViewById(R.id.pb_total_calories);
        pb_carbohydrate = findViewById(R.id.pb_carbohydrate);
        pb_protein = findViewById(R.id.pb_protein);
        pb_fat = findViewById(R.id.pb_fat);
        tv_total_calories = findViewById(R.id.tv_total_calories);
        tv_gram_of_carbohydrate = findViewById(R.id.tv_gram_of_carbohydrate);
        tv_gram_of_protein = findViewById(R.id.tv_gram_of_protein);
        tv_gram_of_fat = findViewById(R.id.tv_gram_of_fat);
        iv_user_setting_update = findViewById(R.id.iv_user_setting_update);
        btn_meal1_detail = findViewById(R.id.btn_meal1_detail);
    }

    //설정
    private void setting(){
        setting_weekly_calendar();
        setting_nutri_progress();
    }

    //리스너 추가
    private void addListener(){
        btn_select_calendar.setOnClickListener(listener_select_calendar);
        iv_user_setting_update.setOnClickListener(listener_setting_update);
        btn_meal1_detail.setOnClickListener(listener_meal1_detail);
    }

    private final View.OnClickListener listener_setting_update = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, UserSettingActivity.class);
            startActivity(intent);
        }
    };

    private final View.OnClickListener listener_meal1_detail = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, FoodAnalysisActivity.class);
            startActivity(intent);
        }
    };


    private final View.OnClickListener listener_select_calendar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SelectCalendarDialog customDialog =  new SelectCalendarDialog(activity);
            customDialog.setCalendarDialogClickListener(new SelectCalendarDialog.OnCalendarDialogClickListener() {
                @Override
                public void onDoneClick(Date selectDate) {

//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
//                    tv_dialog_result.setText(simpleDateFormat.format(selectDate) + "을 선택하셨습니다.");

                    Calendar selected_past_day = Calendar.getInstance();
                    selected_past_day.setTime(selectDate);
                    //Log.i("check1",selected_past_day.get(Calendar.YEAR)+"/"+(selected_past_day.get(Calendar.MONTH)+1)+"/"+selected_past_day.get(Calendar.DATE)+"/"+selected_past_day.get(Calendar.DAY_OF_WEEK));

                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.putExtra("flag", true);
                    intent.putExtra("selected_year", selected_past_day.get(Calendar.YEAR));
                    intent.putExtra("selected_month", selected_past_day.get(Calendar.MONTH));
                    intent.putExtra("selected_date", selected_past_day.get(Calendar.DATE));
                    intent.putExtra("kakao_userNickName", tv_userId.getText().toString());
                    startActivity(intent);

                    finish();
                }
            });
            customDialog.show();
        }
    };

    private void setting_weekly_calendar(){
        Calendar today = Calendar.getInstance();
        today.setFirstDayOfWeek(Calendar.MONDAY);

        Intent get_selected_data = getIntent();
        if(get_selected_data.getBooleanExtra("flag", false)) {
            today.set(Calendar.YEAR, get_selected_data.getIntExtra("selected_year",0));
            today.set(Calendar.MONTH, get_selected_data.getIntExtra("selected_month",0));
            today.set(Calendar.DATE, get_selected_data.getIntExtra("selected_date",0));
            tv_dialog_result.setText(String.format(String.format(today.get(Calendar.YEAR) + "/" + (today.get(Calendar.MONTH) + 1) + "/" + today.get(Calendar.DATE) + "/" + today.get(Calendar.DAY_OF_WEEK))));
            //Log.i("check2",today.get(Calendar.YEAR)+"/"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.DATE)+"/"+today.get(Calendar.DAY_OF_WEEK));
        }

        Calendar day_of_this_week = Calendar.getInstance();
        day_of_this_week.setFirstDayOfWeek(Calendar.MONDAY);
        day_of_this_week.set(Calendar.WEEK_OF_YEAR, today.get(Calendar.WEEK_OF_YEAR));
        day_of_this_week.set(Calendar.YEAR, today.get(Calendar.YEAR));

        LinearLayout row_date = new LinearLayout(activity);
        row_date.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        row_date.setOrientation(LinearLayout.HORIZONTAL);


        /*
            12월 마지막 주의 날짜 선택 시, Year 값은 12월의 연도, WEEK_OF_YEAR 값은 1이 되어,
            주간 캘린더가 올바르게 출력되지 않는 문제 발생 (전년도의 날짜가 출력됨)
            ex) 2021년 12월 30일 선택 --> 실제로 주간 캘린더에는 2020년 12월 - 2021년 1월의 날짜가 출력됨
            --> 이 경우 수동으로 Year 값을 수동으로 1을 증가시켜줌으로써 해결 (이하 9 라인)
            ex) 2021년 12월 30일 선택 --> 주간 캘린더에는 2021년 12월 - 2022년 1월의 날짜가 출력됨
        */
        int start = today.get(Calendar.MONTH) + 1;
        day_of_this_week.set(Calendar.DAY_OF_WEEK, 2);
        int start_of_this_week = day_of_this_week.get(Calendar.MONTH) +1;
        day_of_this_week.set(Calendar.DAY_OF_WEEK, 1);
        int end_of_this_week= day_of_this_week.get(Calendar.MONTH) +1;
        if(start == 12 && start_of_this_week == 12 && end_of_this_week == 1){
            day_of_this_week.set(Calendar.YEAR, today.get(Calendar.YEAR)+1);
        }

        int month1 = 1, month2 = 1;
        for(int i = 0 ; i < 7 ; i++) {

            day_of_this_week.set(Calendar.DAY_OF_WEEK, i+2);
            //Log.i("check3",day_of_this_week.get(Calendar.YEAR)+"/"+(day_of_this_week.get(Calendar.MONTH)+1)+"/"+day_of_this_week.get(Calendar.DATE)+"/"+day_of_this_week.get(Calendar.DAY_OF_WEEK));

            if(i==0) month1 = day_of_this_week.get(Calendar.MONTH) + 1;
            if(i==6) month2 = day_of_this_week.get(Calendar.MONTH) + 1;

            LinearLayout col = new LinearLayout(activity);
            col.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            col.setOrientation(LinearLayout.HORIZONTAL);
            col.setGravity(Gravity.CENTER);

            TextView textView = new TextView(activity);
            textView.setText(Integer.toString(day_of_this_week.get(Calendar.DATE)));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);
            /*Log.i("check4",today.get(Calendar.YEAR)+"/"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.DATE)+"/"+today.get(Calendar.DAY_OF_WEEK));
            Log.i("check5", String.valueOf(day_of_this_week.compareTo(today)));
            Log.i("check5", String.valueOf(day_of_this_week));
            Log.i("check5", String.valueOf(today));*/

            /* hanbyul :
                day_of_this_week.compareTo(today) == 0 사용 시,
                모두 동일하나, MILLISECOND가 1 차이나서 다르다고 나오는 문제 발생
                (발생 원인은 알아내지 못했고,  15번의 테스트 당 1번 정도씩 발생)
                --> Calendar를 Date로 변환하여 시간을 제외하고 (연도,월,일)만 비교하도록 수정함으로써 문제 해결
            */
            Date date1 = today.getTime();
            Date date2 = day_of_this_week.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            if(sdf.format(date1).equals(sdf.format(date2))) {
                textView.setBackground(getDrawable(R.drawable.roundtv));
            }
            if(day_of_this_week.compareTo(Calendar.getInstance()) != 1)
                textView.setTextColor(Color.WHITE);

            col.addView(textView);

            row_date.addView(col);
        }
        layout_date.addView(row_date);

        int year = day_of_this_week.get(Calendar.YEAR);//마지막 날의 연도
        if(month1 == month2)
            tv_month.setText(year+"년 " + month1 + "월");
        else{// 일주일의 첫 날과 마지막 날의 달이 다른 경우
            if(month1 == 12)// 첫 날이 12월인 경우, 마지막 날의 달은 1월
                tv_month.setText( (year - 1) +"년 " +month1 + "월 - " + year + "년 "+ month2+"월");
            else
                tv_month.setText(year+"년 " +month1 + "월 - " + month2+"월");
        }


        //DB에서 하루 총 섭취칼로리 받아오기
        int[] calories = new int[]{2100, 3800, 1200, 2200, 1000, 0, 0};
        int target_calory = 2150;

        LinearLayout row_calories = new LinearLayout(activity);
        row_calories.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        row_calories.setOrientation(LinearLayout.HORIZONTAL);

        for(int i = 0 ; i < 7 ; i++) {

            LinearLayout col = new LinearLayout(activity);
            col.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            col.setOrientation(LinearLayout.HORIZONTAL);
            col.setGravity(Gravity.CENTER);

            TextView textView = new TextView(activity);
            textView.setText(Integer.toString(calories[i]));
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextSize(10);

            if(target_calory - 50 <= calories[i] && calories[i] <= target_calory + 50)
                textView.setTextColor(Color.parseColor("#7CFC00"));
            else if(target_calory - 50 > calories[i])
                textView.setTextColor(Color.parseColor("#FFFF00"));
            else
                textView.setTextColor(Color.parseColor("#FF4500"));

            col.addView(textView);

            row_calories.addView(col);
        }
        layout_calories.addView(row_calories);
    }

    private void setting_nutri_progress(){
        /*
            회원가입 후, 초기 개인정보 화면에서 넘어온 경우, 값을 바로 받아서 적용한다.(InitialSurveyActivity에서 DB에 저장되어야 함)
            DB 생성 후 이하 getIntent()에서 받아오는 HashMap은 사용하지 않아도 됨
         */
        int target_calory, gram_of_carbohydrate, gram_of_protein, gram_of_fat;

        Intent intent = getIntent();
        if(intent.getSerializableExtra("survey_result") != null){
            HashMap<String, Integer> survey_result = (HashMap<String, Integer>)intent.getSerializableExtra("survey_result");
            Log.i("HashMapTest", String.valueOf(survey_result));

            target_calory = survey_result.get("target_calory");
            gram_of_carbohydrate = survey_result.get("gram_of_carbohydrate");
            gram_of_protein = survey_result.get("gram_of_protein");
            gram_of_fat = survey_result.get("gram_of_fat");


        }else{
            //이미 회원인 상황에서 로그인을 한 경우에, DB에서 받아와야하는 값
            target_calory = 2000;
            gram_of_carbohydrate = 100;
            gram_of_protein = 100;
            gram_of_fat = 100;
        }

        int current_calories = 1000;
        int current_carbohydrate = 50;
        int current_protein = 30;
        int current_fat = 20;

        tv_total_calories.setText(current_calories + " / " + target_calory + "kcal");
        tv_gram_of_carbohydrate.setText(current_carbohydrate + " / " + gram_of_carbohydrate + "g");
        tv_gram_of_protein.setText(current_protein + " / " + gram_of_protein + "g");
        tv_gram_of_fat.setText(current_fat + " / " + gram_of_fat + "g");

        pb_total_calories.setProgress((int) ((current_calories*100.0)/target_calory));
        pb_carbohydrate.setProgress((int) ((current_carbohydrate*100.0)/gram_of_carbohydrate));
        pb_protein.setProgress((int) ((current_protein*100.0)/gram_of_protein));
        pb_fat.setProgress((int) ((current_fat*100.0)/gram_of_fat));
    }
}