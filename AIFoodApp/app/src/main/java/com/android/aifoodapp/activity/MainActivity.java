package com.android.aifoodapp.activity;

import static android.graphics.Color.*;

import static com.android.aifoodapp.interfaceh.baseURL.url;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.aifoodapp.R;
import com.android.aifoodapp.adapter.MealAdapter;
import com.android.aifoodapp.domain.dailymeal;
import com.android.aifoodapp.domain.user;
import com.android.aifoodapp.interfaceh.GsonDateFormatAdapter;
import com.android.aifoodapp.interfaceh.NullOnEmptyConverterFactory;
import com.android.aifoodapp.interfaceh.OnEditMealHeight;
import com.android.aifoodapp.interfaceh.RetrofitAPI;
import com.android.aifoodapp.vo.MealMemberVo;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kakao.sdk.user.UserApiClient;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity<Unit> extends AppCompatActivity {

    Activity activity;
    Button btn_logout;
    TextView tv_userId;
    //ImageView tv_userPhoto;

    GoogleSignInClient mGoogleSignInClient;

    TextView tv_month, tv_dialog_result;
    LinearLayout layout_date, layout_calories;
    Button btn_select_calendar;
    ProgressBar pb_total_calories, pb_carbohydrate, pb_protein, pb_fat;
    TextView tv_total_calories, tv_gram_of_carbohydrate, tv_gram_of_protein, tv_gram_of_fat;

    ImageView iv_user_setting_update;
    Button btn_meal1_detail;

    RadarChart radarChart;

    int percent_of_carbohydrate;
    int percent_of_protein;
    int percent_of_fat;

    user user;
    dailymeal dailymeal;

    private ListView lv_meal_item;
    private MealAdapter mealAdapter;
    private ArrayList<MealMemberVo> memberList;
    FloatingActionButton fab_add_meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        //서버에서 dailymeal을 받아오는데 비 동기적으로 작동해서 뒤에코드가 먼저 실행되는 에러 발
        setDailymeal();


        //setting();
        addListener();


        /*Log.e("userERRRRRRRR",user.pringStirng());*/


        String flag=intent.getStringExtra("flag"); //현재 계정이 구글인지 카카오인지

        if(flag.equals("kakao")){
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserApiClient.getInstance().logout((throwable) -> {
                        if (throwable != null) {
                            Toast.makeText(getApplicationContext(),"카카오 로그아웃 실패", Toast.LENGTH_SHORT);
                            Log.e("[카카오] 로그아웃", "실패", throwable);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"카카오 로그아웃", Toast.LENGTH_SHORT);
                            Log.i("[카카오] 로그아웃", "성공");
                            ((LoginActivity)LoginActivity.mContext).updateKakaoLoginUi();
                        }
                        return null;
                    });

                    //Intent backIntent = new Intent(getApplicationContext(), LoginActivity.class); // 로그인 화면으로 이동
                    Intent backIntent = new Intent(activity, LoginActivity.class); // 로그인 화면으로 이동
                    startActivity(backIntent);
                    finish();

                }
            });
        }
        else if(flag.equals("google")){
            /*juhee modify*/
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


            //로그아웃 코드
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_logout:
                            signOut();
                            Intent backIntent = new Intent(activity, LoginActivity.class); // 로그인 화면으로 이동
                            startActivity(backIntent);
                            finish();
                            break;
                    }
                }
            });
        }

        /*사진 이메일등 구글 정보 가져오는*/
        /*GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {

            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            tv_userId.setText(personId);
            tv_userName.setText(personName);
            //tv_userEmail.setText(personEmail);
            //Glide 사용 가능
            //https://bumptech.github.io/glide/
            //Glide.with(this).load(String.valueOf(personPhoto)).into(tv_userPhoto);
            //tv_userPhoto.setImageIcon(Icon.createWithContentUri(personPhoto));

        }*/
        /*juhee modify--fin*/

        //로그인 닉네임
        tv_userId.setText(user.getNickname());

    }

    //Todo 구글 로그인을 계속 연동해서 사용할지 아니면 토큰만 써서 할지 결정
    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
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

        radarChart = (RadarChart) findViewById(R.id.radarchart);

        lv_meal_item = findViewById(R.id.lv_custom_item);
        memberList = new ArrayList<>();
        mealAdapter = new MealAdapter(activity, memberList);
        fab_add_meal = findViewById(R.id.fab_add_meal);
    }

    //설정
    private void setting(){
        setting_weekly_calendar();
        setting_nutri_progress();
        setting_balance_graph();
        setting_meal_adapter();
        setting_initial_meal();
    }

    //리스너 추가
    private void addListener(){
        btn_select_calendar.setOnClickListener(listener_select_calendar);
        iv_user_setting_update.setOnClickListener(listener_setting_update);
        btn_meal1_detail.setOnClickListener(listener_meal1_detail);
        fab_add_meal.setOnClickListener(listener_add_meal);
    }

    private final View.OnClickListener listener_add_meal = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addMeal();
            setListViewHeightBasedOnChildren(lv_meal_item);
        }
    };

    private final View.OnClickListener listener_setting_update = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, UserSettingActivity.class);
            intent.putExtra("user",user);
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
                textView.setTextColor(WHITE);

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
        int target_calorie = 2150;

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

            if(target_calorie - 50 <= calories[i] && calories[i] <= target_calorie + 50)
                textView.setTextColor(parseColor("#7CFC00"));
            else if(target_calorie - 50 > calories[i])
                textView.setTextColor(parseColor("#FFFF00"));
            else
                textView.setTextColor(parseColor("#FF4500"));

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
        int target_calorie, gram_of_carbohydrate, gram_of_protein, gram_of_fat;

        Intent intent = getIntent();
        /*
        Log.i("HashMapTest", user.pringStirng());
        if(intent.getSerializableExtra("survey_result") != null){
            HashMap<String, Integer> survey_result = (HashMap<String, Integer>)intent.getSerializableExtra("survey_result");
            Log.i("HashMapTest", String.valueOf(survey_result));

            target_calorie = survey_result.get("target_calorie");
            gram_of_carbohydrate = survey_result.get("gram_of_carbohydrate");
            gram_of_protein = survey_result.get("gram_of_protein");
            gram_of_fat = survey_result.get("gram_of_fat");


        }else{
            //이미 회원인 상황에서 로그인을 한 경우에, DB에서 받아와야하는 값
            target_calorie = 2000;
            gram_of_carbohydrate = 100;
            gram_of_protein = 100;
            gram_of_fat = 100;
        }

        int current_calories = 1000;
        int current_carbohydrate = 50;
        int current_protein = 30;
        int current_fat = 20;
        */
        target_calorie = user.getTarget_calories();

        gram_of_carbohydrate = (int) (target_calorie * 0.5 / 4);
        gram_of_protein = (int) (target_calorie * 0.3 / 4);
        gram_of_fat = (int) (target_calorie * 0.2 / 9);

        int current_calories = dailymeal.getCalorie();
        int current_carbohydrate = dailymeal.getCarbohydrate();
        int current_protein = dailymeal.getProtein();
        int current_fat = dailymeal.getFat();


        tv_total_calories.setText(current_calories + " / " + target_calorie + "kcal");
        tv_gram_of_carbohydrate.setText(current_carbohydrate + " / " + gram_of_carbohydrate + "g");
        tv_gram_of_protein.setText(current_protein + " / " + gram_of_protein + "g");
        tv_gram_of_fat.setText(current_fat + " / " + gram_of_fat + "g");

        int percent_of_total_calories = (int) ((current_calories*100.0)/target_calorie);
        pb_total_calories.setProgress(percent_of_total_calories);

        if(percent_of_total_calories > 100)
            pb_total_calories.setProgressTintList(ColorStateList.valueOf(Color.RED));

        percent_of_carbohydrate = (int) ((current_carbohydrate*100.0)/gram_of_carbohydrate);
        percent_of_protein = (int) ((current_protein*100.0)/gram_of_protein);
        percent_of_fat = (int) ((current_fat*100.0)/gram_of_fat);

/*        //Test Data
        percent_of_carbohydrate = 150;
        percent_of_protein = 80;
        percent_of_fat = 101;*/

        pb_carbohydrate.setProgress(percent_of_carbohydrate);
        pb_protein.setProgress(percent_of_protein);
        pb_fat.setProgress(percent_of_fat);

        if(percent_of_carbohydrate > 100)
            pb_carbohydrate.setProgressTintList(ColorStateList.valueOf(Color.RED));
        if(percent_of_protein > 100)
            pb_protein.setProgressTintList(ColorStateList.valueOf(Color.RED));
        if(percent_of_fat > 100)
            pb_fat.setProgressTintList(ColorStateList.valueOf(Color.RED));

    }

    private void setting_balance_graph(){

        /*밸런스 그래프 출력*/
        ArrayList<RadarEntry> visitors = new ArrayList<>();

        visitors.add(new RadarEntry(percent_of_carbohydrate));
        visitors.add(new RadarEntry(percent_of_fat));
        visitors.add(new RadarEntry(percent_of_protein));

/*      // yAxis 최댓값을 100으로 지정한 경우, 영양소 섭취 퍼센트가 100% 초과 시 100이 되도록 지정
        visitors.add(new RadarEntry((percent_of_carbohydrate > 100)? 100 : percent_of_carbohydrate));
        visitors.add(new RadarEntry((percent_of_fat > 100)? 100 : percent_of_fat));
        visitors.add(new RadarEntry((percent_of_protein > 100)? 100 : percent_of_protein));
*/

        // 영양소 섭취량이 권장량을 초과하는 경우, Red Color로 변경 (일반 BLUE)
        int color_of_carbonhydrate = (percent_of_carbohydrate > 100)? RED : BLUE;
        int color_of_fat = (percent_of_fat > 100)? RED : BLUE;
        int color_of_protein = (percent_of_protein > 100)? RED : BLUE;

        RadarDataSet radarDataSet = new RadarDataSet(visitors, "visitors");
        radarDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return super.getFormattedValue((int)value);
            }
        });

        radarDataSet.setHighlightLineWidth(2f);
        radarDataSet.setColor(parseColor("#3A531C"));
        radarDataSet.setLineWidth(2f);
        radarDataSet.setValueTextSize(16f);
        radarDataSet.setDrawFilled(true);
        //radarDataSet.setFillColor(parseColor("#3A531C"));

        //Log.i("check", percent_of_carbohydrate+","+percent_of_fat+","+percent_of_protein);
        // 모든 영양소 섭취량이 권장량을 초과하는 경우, Red Color로 변경 (일반 Dark Green)
        if(percent_of_carbohydrate>100 && percent_of_fat>100 && percent_of_protein > 100){
            radarDataSet.setFillColor(RED);
        }else{
            radarDataSet.setFillColor(parseColor("#3A531C"));
        }

        RadarData radarData = new RadarData();
        radarData.addDataSet(radarDataSet);
        radarData.setValueTextColors(Arrays.asList(new Integer[]{color_of_carbonhydrate, color_of_fat, color_of_protein}));
        //radarData.setValueTextSize(10f);

        String[] labels = {"탄수화물","지방","단백질"};

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setTextSize(13f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setTypeface(Typeface.createFromAsset(getAssets(), "cafe24ssurroundair.ttf"));
        YAxis yAxis = radarChart.getYAxis();

        yAxis.setAxisMinimum(0f);
        //yAxis.setAxisMaximum(100f); // yAxis 최대값 지정 (100 초과 시, chart 영역 벗어남)
        //yAxis.setLabelCount(5, true); // web 개수 강제 지정
        yAxis.setDrawLabels(false); //yAxis 기준 값 표시

        radarChart.setTouchEnabled(false);
        // chart 영역 위치 조정
        radarChart.setScaleX(1.4f);
        radarChart.setScaleY(1.4f);
        radarChart.setY(80f);

        radarChart.getDescription().setEnabled(false);
        radarChart.getLegend().setEnabled(false);

        radarChart.setWebColorInner(parseColor("#3A531C"));
        radarChart.setWebLineWidthInner(1f);
        radarChart.setWebColor(parseColor("#3A531C"));

        radarChart.setData(radarData);

    }

    private void setting_meal_adapter(){

        lv_meal_item.setAdapter(mealAdapter);

        // adapter 콜백리스너 등록
        if (mealAdapter != null) {
            mealAdapter.setOnEditMealHeightListener(new OnEditMealHeight() {
                @Override
                public void onEditMealHeight() {
                    //콜백 메소드로 전달받은 값을 프래그먼트 쪽 멤버 변수에 할당해준다.
                    setListViewHeightBasedOnChildren(lv_meal_item);
                }
            });
        }
    }

    private void setting_initial_meal(){
        addMeal();
        addMeal();
    }

    private void addMeal(){
        mealAdapter.addItem(new MealMemberVo());
        setListViewHeightBasedOnChildren(lv_meal_item);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            ////listItem.measure(0, 0);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();

        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight;
        listView.setLayoutParams(params);

        listView.requestLayout();
    }
    public void setDailymeal(){

        //https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=gracefulife&logNo=220992369673
        //https://velog.io/@ptm0304/Android-Retrofit%EC%9C%BC%EB%A1%9C-DateTime-%ED%98%95%EC%8B%9D-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EB%B0%9B%EC%95%84%EC%98%A4%EA%B8%B0
        //GsonBuilder gson = new GsonBuilder();
        //gson.registerTypeAdapter(Date.class , new GsonDateFormatAdapter());

        //Gson gson = new GsonBuilder()
        //        .setDateFormat("yyyy-MM-dd")
        //        .create();
        //"2022-04-16T15:00:00.000+00:00"//"2022-04-16T15:00:00.000+00:00",

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        //현재 시간을 UNIX타입 으로 저장하는 코드
        Long now1 = System.currentTimeMillis();

        //현재 시간을 yyyy-MM-dd HH:mm:ss 포맷으로 저장하는 코드
        Date now = new Date(); //Date타입으로 변수 선언
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //데이트 포맷
        String date_string = dateFormat.format(now); //날짜가 string으로 저장

        //Date datekey = new Date(sdf.format(date)); //똥 코드..

        Log.e("Retrodate",date_string);


        retrofitAPI.getDailyMeal(user.getId(),date_string).enqueue(new Callback<dailymeal>() {
            @Override
            public void onResponse(Call<dailymeal> call, Response<dailymeal> response) {
                dailymeal = response.body();
                Log.e("dailymeal",Integer.toString(dailymeal.getCalorie()));
                try {
                    Thread.sleep(1000);
                    setting();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<dailymeal> call, Throwable t) {
                Log.e("Error! call msg",call.toString());
                Log.e("Error! t messge",t.getMessage());
            }
        });
    }
}