package com.android.aifoodapp.activity;

import static android.graphics.Color.*;

import static com.android.aifoodapp.interfaceh.baseURL.url;

import static java.lang.Thread.sleep;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.aifoodapp.BackService;
import com.android.aifoodapp.ProgressDialog;
import com.android.aifoodapp.R;
import com.android.aifoodapp.adapter.MealAdapter;
import com.android.aifoodapp.domain.dailymeal;
import com.android.aifoodapp.domain.fooddata;
import com.android.aifoodapp.domain.meal;
import com.android.aifoodapp.domain.user;
import com.android.aifoodapp.interfaceh.OnCameraClick;
import com.android.aifoodapp.interfaceh.OnEditMealHeight;
import com.android.aifoodapp.interfaceh.OnGalleryClick;
import com.android.aifoodapp.interfaceh.RetrofitAPI;
import com.android.aifoodapp.vo.MealMemberVo;

import com.android.aifoodapp.vo.SubItem;
import com.bumptech.glide.Glide;
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
import com.google.gson.annotations.SerializedName;
import com.kakao.sdk.user.UserApiClient;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class MainActivity<Unit> extends AppCompatActivity {

    Activity activity;
    Button btn_logout;
    TextView tv_userId, stepCountView;
    ImageView tv_userPhoto;

    GoogleSignInClient mGoogleSignInClient;

    TextView tv_month, tv_dialog_result;
    LinearLayout layout_date, layout_calories;
    RecyclerView rv_meal;
    Button btn_select_calendar;
    ProgressBar pb_total_calories, pb_carbohydrate, pb_protein, pb_fat;
    TextView tv_total_calories, tv_gram_of_carbohydrate, tv_gram_of_protein, tv_gram_of_fat;
    ImageView iv_user_setting_update;
    //Button btn_meal1_detail;

    RadarChart radarChart;
    SensorManager sensorManager;
    Sensor stepCountSensor;

    @SerializedName("prediction")
    String foodName="";
    fooddata foodAI;

    int percent_of_carbohydrate;
    int percent_of_protein;
    int percent_of_fat;
    Integer[] calories=new Integer[7];
    user user;
    dailymeal dailymeal;
    long dailymealid;
    String date_string="";
    int time=0;
    int currentSteps = 0 , firstSteps=0, totalSteps=0 ;
    HashMap<String, RequestBody> mapAi = new HashMap<>();
    String imgPath = "";
    ProgressDialog dialog;

    private RecyclerView rv_item;
    private MealAdapter mealAdapter;
    private ArrayList<MealMemberVo> memberList;
    private List<SubItem> subItemList;
    FloatingActionButton fab_add_meal;
    private int meal_num = 1; // ?????? ???????????? ?????? ??? ???????????? ???

    ArrayList<fooddata> foodList=new ArrayList<>();
    ArrayList<Double> intakeList=new ArrayList<>();
    ArrayList<String> photoList=new ArrayList<>();
    List<fooddata> list;
    List<meal> ml;

    private String flag;
    public static Activity _MainActivity;
    public static MainActivity mainActivity;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //????????? ??????(sql)
    //Camera & Gallery
    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 1005;
    private final int REQUEST_READ_EXTERNAL_STORAGE = 1006;

    private Uri photoUri;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    public interface OnSetImage {
        void onSetImage(Uri photoUri);
    }

    private OnSetImage onSetImage;

    public void setOnSetImageListener(OnSetImage onSetImageListener) {
        this.onSetImage = onSetImageListener;
    }

    //????????? ?????? ????????? ?????? ??????
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private Button btn_weekly_report;
    //?????? ?????? ??????(????????? ???????????? --> ?????? ?????????????????? ?????????????????????)
    private Button btn_recommend_meal;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        _MainActivity = MainActivity.this;
        mainActivity = this;
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");

        /* dialog */
        dialog = new ProgressDialog(MainActivity.this);//loading
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));//??????????????????
        dialog.setCanceledOnTouchOutside(false); //?????? ?????? ??????
        dialog.setCancelable(false);
        dialog.show();

        setting();
        addListener();
        //settingFoodListAdapter();

        // ????????? ????????? ?????? ??? ????????? ??????.
        Intent serviceIntent = new Intent(MainActivity.this, BackService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            serviceIntent.putExtra("user", user);
            serviceIntent.putExtra("dailymeal", dailymeal);
            startForegroundService(serviceIntent);
        }
        else {
            startService(serviceIntent);
        }

        flag=intent.getStringExtra("flag"); //?????? ????????? ???????????? ???????????????

        if(flag.equals("kakao")){
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserApiClient.getInstance().logout((throwable) -> {
                        if (throwable != null) {
                            Toast.makeText(getApplicationContext(),"????????? ???????????? ??????", Toast.LENGTH_SHORT);
                            Log.e("[?????????] ????????????", "??????", throwable);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"????????? ????????????", Toast.LENGTH_SHORT);
                            Log.i("[?????????] ????????????", "??????");
                            ((LoginActivity)LoginActivity.mContext).updateKakaoLoginUi();
                        }
                        return null;
                    });

                    //Intent backIntent = new Intent(getApplicationContext(), LoginActivity.class); // ????????? ???????????? ??????
                    Intent backIntent = new Intent(activity, LoginActivity.class); // ????????? ???????????? ??????
                    startActivity(backIntent);
                    finish();

                }
            });
        }
        else if(flag.equals("google")){
            /*
             modify*/
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


            //???????????? ??????
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_logout:
                            signOut();
                            Intent backIntent = new Intent(activity, LoginActivity.class); // ????????? ???????????? ??????
                            startActivity(backIntent);
                            finish();
                            break;
                    }
                }
            });
        }
        /*juhee modify--fin*/

        //????????? ?????????
        tv_userId.setText(user.getNickname());

    }

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this,"Signed Out ok ",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    //?????? ?????????
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
        //btn_meal1_detail = findViewById(R.id.btn_meal1_detail);

        radarChart = (RadarChart) findViewById(R.id.radarchart);

        rv_item = findViewById(R.id.rv_item);
        memberList = new ArrayList<>();
        mealAdapter = new MealAdapter(activity, memberList);
        fab_add_meal = findViewById(R.id.fab_add_meal);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), cameraResultCallback);
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), galleryResultCallback);

        btn_weekly_report = findViewById(R.id.btn_weekly_report);
        btn_recommend_meal = findViewById(R.id.btn_recommend_meal);

        tv_userPhoto=findViewById(R.id.tv_userPhoto);
        Glide.with(this).load(R.drawable.heart).into(tv_userPhoto);
        //stepCountView=findViewById(R.id.stepCountView);
    }

    //??????
    private void setting(){

        setDailymeal();
        settingWeeklyCalendar();
        settingNutriProgress();
        settingBalanceGraph();
        settingMealAdapter();
        settingInitialMeal();

    }

    //????????? ??????
    private void addListener(){
        btn_select_calendar.setOnClickListener(listener_select_calendar);
        iv_user_setting_update.setOnClickListener(listener_setting_update);
        //btn_meal1_detail.setOnClickListener(listener_meal1_detail);
        fab_add_meal.setOnClickListener(listener_add_meal);
        btn_weekly_report.setOnClickListener(listener_weekly_report);
        btn_recommend_meal.setOnClickListener(listener_recommend_meal);
    }

    private final View.OnClickListener listener_recommend_meal = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, RecommendFoodActivity.class);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create()).build();

            RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);


            //?????????
            retrofitAPI.getOneDayMealCount(user.getId(),dailymeal.getDatekey()).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.body()==0){
                        //????????? ????????? ???????????? ??????
                        //Toast.makeText("????????? ?????? ?????? ??????????????? ?????? ????????? ???????????????.",)
                        Toast.makeText(getApplicationContext(),"????????? ?????? ?????? ??????????????? ?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
                        Log.d("size??? 0??? ",Integer.toString(response.body()));

                    }
                    else{
                        Log.d("size ",Integer.toString(response.body()));
                        intent.putExtra("user",user);
                        intent.putExtra("dailymeal",dailymeal);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
        }
    };

    private final View.OnClickListener listener_weekly_report = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, WeeklyReportActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }
    };

    private final View.OnClickListener listener_add_meal = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addMeal();
            setListViewHeightBasedOnChildren(rv_item);
        }
    };

    private final View.OnClickListener listener_setting_update = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, UserSettingActivity.class);
            intent.putExtra("user",user);
            intent.putExtra("flag",flag);
            startActivity(intent);
        }
    };

    /*private final View.OnClickListener listener_meal1_detail = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, FoodAnalysisActivity.class);
            intent.putExtra("dailymeal",dailymeal);
            startActivity(intent);
            finish();
        }
    };*/

    private final View.OnClickListener listener_select_calendar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SelectCalendarDialog customDialog =  new SelectCalendarDialog(activity);
            customDialog.setCalendarDialogClickListener(new SelectCalendarDialog.OnCalendarDialogClickListener() {
                @Override
                public void onDoneClick(Date selectDate) {
//                  tv_dialog_result.setText(simpleDateFormat.format(selectDate) + "??? ?????????????????????.");

                    Calendar selected_past_day = Calendar.getInstance();
                    selected_past_day.setTime(selectDate);
                    //Log.i("check1",selected_past_day.get(Calendar.YEAR)+"/"+(selected_past_day.get(Calendar.MONTH)+1)+"/"+selected_past_day.get(Calendar.DATE)+"/"+selected_past_day.get(Calendar.DAY_OF_WEEK));


                    Intent intent = new Intent(activity, MainActivity.class);


                    intent.putExtra("main_flag", true);
                    intent.putExtra("selected_year", selected_past_day.get(Calendar.YEAR));
                    intent.putExtra("selected_month", selected_past_day.get(Calendar.MONTH));
                    intent.putExtra("selected_date", selected_past_day.get(Calendar.DATE));
                    intent.putExtra("kakao_userNickName", tv_userId.getText().toString());


                    Log.e("date",dateFormat.format(selectDate));
                    intent.putExtra("selected_day",dateFormat.format(selectDate));
                    intent.putExtra("user",user);
                    intent.putExtra("flag",flag);
                    //TODO: flag??? ?????? ?????????????????????.. ?????? ????????????????? flase..?! ?????? flag ?????? ?????? ??????
                    /*
                        hanbyul comment:
                        flag??? ???????????? false?????? ??? ?????? ??????????????? ????????? ????????? ????????? true??? ?????????.
                        ????????? settingWeeklyCalendar()?????? ??? ?????? false?????? ?????? ?????? ????????? ????????????,
                        true??? ???????????? ??????????????? ????????? ?????? ???????????? ????????? ????????? ???????????????.
                        ???????????? ?????? flag??? ???????????? ???????????? ?????? ??????????????? flag?????? ????????? ???????????? ?????? ???????????? ????????????
                        ??????(?)??? ????????????, ?????? ?????? ??? ????????? ???????????? ????????? ????????????..
                     */
                    /*
                        jisoo
                        ????????? ?????????????????? ?????? ?????????????????? ??????????????? flag="kakao" or "google"??? ???????????? ?????? ?????????
                        ?????? flag ????????? ????????? ????????????..?
                    * */
                    startActivity(intent);

                    finish();
                }
            });
            customDialog.show();
        }
    };

    private void settingWeeklyCalendar(){
        //juhee
        String startDate = null,endDate=null;

        Calendar today = Calendar.getInstance();
        today.setFirstDayOfWeek(Calendar.MONDAY);

        Intent get_selected_data = getIntent();
        if(get_selected_data.getBooleanExtra("main_flag", false)) {
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
            12??? ????????? ?????? ?????? ?????? ???, Year ?????? 12?????? ??????, WEEK_OF_YEAR ?????? 1??? ??????,
            ?????? ???????????? ???????????? ???????????? ?????? ?????? ?????? (???????????? ????????? ?????????)
            ex) 2021??? 12??? 30??? ?????? --> ????????? ?????? ??????????????? 2020??? 12??? - 2021??? 1?????? ????????? ?????????
            --> ??? ?????? ???????????? Year ?????? ???????????? 1??? ???????????????????????? ?????? (?????? 9 ??????)
            ex) 2021??? 12??? 30??? ?????? --> ?????? ??????????????? 2021??? 12??? - 2022??? 1?????? ????????? ?????????
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

            Date dat = day_of_this_week.getTime();
            if(i==0) {
                month1 = day_of_this_week.get(Calendar.MONTH) + 1;
                startDate = dateFormat.format(dat);
            }
            if(i==6) {
                month2 = day_of_this_week.get(Calendar.MONTH) + 1;
                endDate = dateFormat.format(dat);
            }

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
                day_of_this_week.compareTo(today) == 0 ?????? ???,
                ?????? ????????????, MILLISECOND??? 1 ???????????? ???????????? ????????? ?????? ??????
                (?????? ????????? ???????????? ?????????,  15?????? ????????? ??? 1??? ????????? ??????)
                --> Calendar??? Date??? ???????????? ????????? ???????????? (??????,???,???)??? ??????????????? ?????????????????? ?????? ??????
            */
            Date date1 = today.getTime();
            Date date2 = day_of_this_week.getTime();
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            if(dateFormat.format(date1).equals(dateFormat.format(date2))) {
                textView.setBackground(getDrawable(R.drawable.roundtv));
            }
            if(day_of_this_week.compareTo(Calendar.getInstance()) != 1)
                textView.setTextColor(WHITE);

            col.addView(textView);

            row_date.addView(col);
        }

        //https://www.daleseo.com/js-async-callback/

        /*juhee */
        //setWeelklyCalories(startDate,endDate);

        //calorie ?????????
        for(int i=0;i<7;i++)calories[i]=0;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        try{
            calories=new calorieNetworkCall().execute(retrofitAPI.getWeeklyCalories(user.getId(),startDate,endDate)).get().toArray(new Integer[7]);
            Log.e("weekly--test", String.valueOf(calories));
        }catch(Exception e){
            e.printStackTrace();
        }

        layout_date.addView(row_date);

        int year = day_of_this_week.get(Calendar.YEAR);//????????? ?????? ??????
        if(month1 == month2)
            tv_month.setText(year+"??? " + month1 + "???");
        else{// ???????????? ??? ?????? ????????? ?????? ?????? ?????? ??????
            if(month1 == 12)// ??? ?????? 12?????? ??????, ????????? ?????? ?????? 1???
                tv_month.setText( (year - 1) +"??? " +month1 + "??? - " + year + "??? "+ month2+"???");
            else
                tv_month.setText(year+"??? " +month1 + "??? - " + month2+"???");
        }

        //DB?????? ?????? ??? ??????????????? ????????????
        //int[] calories = new int[]{2100, 3800, 1200, 2200, 1000, 0, 0};
        int target_calorie = user.getTarget_calories();

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
            textView.setTypeface(Typeface.createFromAsset(getAssets(), "cafe24ssurroundair.ttf"), Typeface.BOLD);
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

    private void settingNutriProgress(){
        /*
            ???????????? ???, ?????? ???????????? ???????????? ????????? ??????, ?????? ?????? ????????? ????????????.(InitialSurveyActivity?????? DB??? ??????????????? ???)
            DB ?????? ??? ?????? getIntent()?????? ???????????? HashMap??? ???????????? ????????? ???
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
            //?????? ????????? ???????????? ???????????? ??? ?????????, DB?????? ?????????????????? ???
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

    private void settingBalanceGraph(){

        /*????????? ????????? ??????*/
        ArrayList<RadarEntry> visitors = new ArrayList<>();

        visitors.add(new RadarEntry(percent_of_carbohydrate));
        visitors.add(new RadarEntry(percent_of_fat));
        visitors.add(new RadarEntry(percent_of_protein));

/*      // yAxis ???????????? 100?????? ????????? ??????, ????????? ?????? ???????????? 100% ?????? ??? 100??? ????????? ??????
        visitors.add(new RadarEntry((percent_of_carbohydrate > 100)? 100 : percent_of_carbohydrate));
        visitors.add(new RadarEntry((percent_of_fat > 100)? 100 : percent_of_fat));
        visitors.add(new RadarEntry((percent_of_protein > 100)? 100 : percent_of_protein));
*/

        // ????????? ???????????? ???????????? ???????????? ??????, Red Color??? ?????? (?????? BLUE)
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

        IntegerPercentFormatter integerPercentFormatter = new IntegerPercentFormatter(radarChart);
        radarDataSet.setValueFormatter(integerPercentFormatter);
        radarDataSet.setValueTextSize(12f);
        radarDataSet.setValueTypeface(Typeface.createFromAsset(getAssets(), "cafe24ssurroundair.ttf"));
        //radarDataSet.setFillColor(parseColor("#3A531C"));
        //Log.i("check", percent_of_carbohydrate+","+percent_of_fat+","+percent_of_protein);
        // ?????? ????????? ???????????? ???????????? ???????????? ??????, Red Color??? ?????? (?????? Dark Green)
        if(percent_of_carbohydrate>100 && percent_of_fat>100 && percent_of_protein > 100){
            radarDataSet.setFillColor(RED);
        }else{
            radarDataSet.setFillColor(parseColor("#3A531C"));
        }

        RadarData radarData = new RadarData();
        radarData.addDataSet(radarDataSet);
        radarData.setValueTextColors(Arrays.asList(new Integer[]{color_of_carbonhydrate, color_of_fat, color_of_protein}));
        //radarData.setValueTextSize(10f);

        String[] labels = {"????????????","??????","?????????"};

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setTextSize(13f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setTypeface(Typeface.createFromAsset(getAssets(), "cafe24ssurroundair.ttf"));
        YAxis yAxis = radarChart.getYAxis();

        yAxis.setAxisMinimum(0f);
        //yAxis.setAxisMaximum(100f); // yAxis ????????? ?????? (100 ?????? ???, chart ?????? ?????????)
        //yAxis.setLabelCount(5, true); // web ?????? ?????? ??????
        yAxis.setDrawLabels(false); //yAxis ?????? ??? ??????

        radarChart.setTouchEnabled(false);
        // chart ?????? ?????? ??????
        radarChart.setScaleX(1.4f);
        radarChart.setScaleY(1.4f);
        radarChart.setY(80f);

        radarChart.getDescription().setEnabled(false);
        radarChart.getLegend().setEnabled(false);

        radarChart.setWebColorInner(parseColor("#3A531C"));
        radarChart.setWebLineWidthInner(1f);
        radarChart.setWebColor(parseColor("#3A531C"));

        radarChart.setData(radarData);
        radarChart.invalidate();

    }

    private void settingMealAdapter(){

        /* ?????? ?????????????????? ??????*/
        rv_item.setAdapter(mealAdapter);
        GridLayoutManager gManager = new GridLayoutManager(activity, 1);
        rv_item.setLayoutManager(gManager);

        // adapter ??????????????? ??????
        if (mealAdapter != null) {
            mealAdapter.setOnEditMealHeightListener(new OnEditMealHeight() {
                @Override
                public void onEditMealHeight() {
                    setListViewHeightBasedOnChildren(rv_item);
                }
            });

            mealAdapter.setOnCameraClickListener(new OnCameraClick() {
                @Override
                public void onCameraClick() {

                    if(
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    ){
                        String[] permissions = {
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        };
                        requestPermissions(permissions, REQUEST_WRITE_EXTERNAL_STORAGE);
                    }else{
                        loadCameraImage();
                    }
                }
            });

            mealAdapter.setOnGalleryClickListener(new OnGalleryClick() {
                @Override
                public void onGalleryClick() {

                    if(
                            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    ){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, REQUEST_READ_EXTERNAL_STORAGE);
                    }else{
                        loadGalleryImage();
                    }
                }
            });

            mealAdapter.setItemClickListener(new MealAdapter.ItemClickListener() {
                @Override
                public void onDetailButtonClicked(int position){//position??? 0?????? ??????

                    //?????? list position??? ???????????? ?????? foodList ????????????
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(url)
                            .addConverterFactory(GsonConverterFactory.create()).build();

                    RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

                    /*
                    try{
                        list=new NetworkCall().execute(retrofitAPI.getFoodFromMeal(dailymeal.getUserid(),dailymeal.getDatekey(),position)).get();
                        for(fooddata fd : list){
                            foodList.add(fd);
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                    }*/

                    retrofitAPI.getFoodFromMeal(dailymeal.getUserid(),dailymeal.getDatekey(),position).enqueue(new Callback<List<fooddata>>() {
                        @Override
                        public void onResponse(Call<List<fooddata>> call, Response<List<fooddata>> response) {
                            if(response.isSuccessful()) {
                                list=response.body();
                                //Log.e("@@@@",list.toString());
                                for(fooddata fd : list){
                                    foodList.add(fd);
                                }

                                Intent intent = new Intent(activity, FoodAnalysisActivity.class);
                                intent.putExtra("dailymeal",dailymeal);
                                intent.putExtra("position",position);
                                //intent.putExtra("intakeList",intakeList);
                                //intent.putExtra("photoList",photoList);
                                intent.putParcelableArrayListExtra("foodList",foodList);
                                startActivity(intent);
                                finish();
                            }
                        }
                        @Override
                        public void onFailure(Call<List<fooddata>> call, Throwable t) {
                            Log.e("food search","??????"+t);
                        }
                    });

                }
                @Override
                public void removeButtonClicked(int position){

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();

                    RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

                    Log.e("??????",date_string);
                    retrofitAPI.InitPositionMeal(user.getId(),date_string,position).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()) {
                                Log.e("main ?????? ?????? : ",Integer.toString(position));

                            }
                            else{
                                Log.e("main ??????","????????????");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("","call ??????"+t);
                        }
                    });

                    Toast.makeText(getApplicationContext(),"?????????????????????!", Toast.LENGTH_SHORT).show();

                    /* activity ?????????????????? */
                    finish();//????????? ??????
                    overridePendingTransition(0, 0);//????????? ?????? ?????????
                    Intent intent = getIntent(); //?????????
                    startActivity(intent); //???????????? ??????
                    overridePendingTransition(0, 0);//????????? ?????? ?????????
                }
                @Override
                public void mealSaveFromPhoto(byte[] byteArray, int position, Bitmap compressedBitmap, Uri photoUri, String tmp){
                    // AI ?????? (?????? ????????? ?????? ?????????)
                    Gson gson=new GsonBuilder().setLenient().create();

                    Retrofit retrofit2 = new Retrofit.Builder()
                            .baseUrl("http://hjb-desktop.asuscomm.com:12345/").addConverterFactory(GsonConverterFactory.create(gson)).build();

                    RetrofitAPI retrofitAPI2 = retrofit2.create(RetrofitAPI.class);

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();

                    RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

                    File postFile;

                    if(tmp.equals("gallery")){
                        //?????????????????? ??????????????? ?????? https://crazykim2.tistory.com/441
                        imgPath=getRealPathFromURI(photoUri);
                        postFile=new File(imgPath);
                        //File postFile=new File(photoUri.getPath());
                        //Log.e("jaja",postFile.getName());
                    /*
                    OutputStream out = null;
                    try{
                        postFile.createNewFile();
                        out=new FileOutputStream(postFile);
                        compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                    }catch(Exception e){
                        e.printStackTrace();
                    }finally{
                        try{
                            out.close();
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }*/
                    }
                    else{ //camera
                        //postFile=new File(photoUri.getPath());
                        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        postFile=new File(storageDir.toString()+"/"+photoUri.getLastPathSegment());
                        imgPath=storageDir.toString()+"/"+photoUri.getLastPathSegment();
                        //Log.e("postFile",storageDir.toString());
                        //postFile=photoFile;
                    }

                    dialog.show();

                    RequestBody rb = RequestBody.create(MediaType.parse("multipart/form-data"),postFile);
                    MultipartBody.Part Bmp = MultipartBody.Part.createFormData("file", postFile.getName(), rb);

                    retrofitAPI2.postFoodNameFromAI(Bmp).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful()) {
                                foodName=response.body();
                                Log.e("AI - prediction", foodName);

                                // AI?????? ???????????? ?????????????????? ????????? ????????? ?????? ?????? fooddata ????????????
                                retrofitAPI.getFoodFromFoodName(foodName).enqueue(new Callback<fooddata>() {
                                    @Override
                                    public void onResponse(Call<fooddata> call, Response<fooddata> response) {
                                        foodAI=response.body();

                                        //?????? position ????????? ?????? ???????????? ?????? foodList ????????????
                                        retrofitAPI.getFoodFromMeal(dailymeal.getUserid(),dailymeal.getDatekey(),position).enqueue(new Callback<List<fooddata>>() {
                                            @Override
                                            public void onResponse(Call<List<fooddata>> call, Response<List<fooddata>> response) {
                                                if(response.isSuccessful()) {
                                                    list=response.body();
                                                    //Log.e("@@@@",list.toString());
                                                    for(fooddata fd : list){
                                                        foodList.add(fd);
                                                    }
                                                    foodList.add(foodAI); //?????? ???????????? ?????? foodList??? ??????
                                                    dialog.dismiss();

                                                    Intent intent = new Intent(activity, FoodAnalysisActivity.class);
                                                    intent.putExtra("dailymeal",dailymeal);
                                                    intent.putExtra("position",position);
                                                    //intent.putExtra("intakeList",intakeList);
                                                    intent.putExtra("photoAI",photoUri);//uri??? ?????????
                                                    intent.putExtra("imgPath",imgPath);
                                                    //intent.putExtra("image",byteArray); //?????? ?????????
                                                    intent.putParcelableArrayListExtra("foodList",foodList);
                                                    intent.putExtra("activity","MainActivity");//?????? ?????????????????? ???????????????
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<List<fooddata>> call, Throwable t) {
                                                Log.e("food search","??????"+t);
                                            }
                                        });
                                    }
                                    @Override
                                    public void onFailure(Call<fooddata> call, Throwable t) {
                                        Log.e("",t.toString());
                                    }
                                });

                            }
                            else{
                                Log.e("","!response.isSuccessful()");
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("AI ?????? ??????",t.toString());
                            dialog.dismiss();
                            Toast toast;
                            toast = Toast.makeText(activity, t.toString(), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });

                }
            });
        }
    }
    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);
    }

    //??????????????? ????????? ????????? ????????????
    //content://com.android.providers.media.documents/document/ ?????? ???????????? ???????????? ????????????.
    private String getRealPathFromURI(Uri contentUri){

        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }
        Log.e("contentUri",contentUri.toString());
        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = { MediaStore.Files.FileColumns.DATA };
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex);
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    private void settingInitialMeal(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        //addMeal();
        try{
            time=new timeFlagNetworkCall().execute(retrofitAPI.getTimeFlag(user.getId(),date_string)).get();
            for(int i=0;i<=time;i++){
                addMeal();
            }
            dialog.dismiss();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void addMeal(){
        //mealAdapter.addItem(new MealMemberVo());
        mealAdapter.addItem(new MealMemberVo("  ?????? " + meal_num, buildSubItemList(meal_num)));
        meal_num++;
        setListViewHeightBasedOnChildren(rv_item);
    }

    /* ?????? ?????????????????? : https://stickode.tistory.com/271
     * ?????? ?????? ????????? ????????? ?????? ????????? ???????????? ??????.
     * */
    private List<SubItem> buildSubItemList(int meal_num) {
        subItemList=new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        try{
            ml=new MealNetworkCall().execute(retrofitAPI.getMeal(user.getId(),date_string,meal_num-1)).get();
            for (meal repo : ml) {
                SubItem subItem=new SubItem(repo.getMealname(), repo.getMealphoto());
                subItemList.add(subItem);
                //Log.e("mealname",repo.getMealname());
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        mealAdapter.notifyDataSetChanged();

        return subItemList;
    }


    public void setListViewHeightBasedOnChildren(RecyclerView recyclerView) {

        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();

        int cnt = recyclerView.getAdapter().getItemCount();
        params.height = (int) getResources().getDimension(R.dimen.meal_item_size) * cnt;
        recyclerView.requestLayout();
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
        Intent intent = getIntent();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        //String date_string="";
        //????????? ????????? ?????????????????? ??????????????? ???????????? ????????? ?????? ?????? ?????? ?????? (selecday intent ??? ????????? ???????????????)
        if(intent.getStringExtra("selected_day")!=null){
            date_string = intent.getStringExtra("selected_day");
        }
        else{
            //?????? ????????? yyyy-MM-dd HH:mm:ss ???????????? ???????????? ??????
            Date now = new Date(); //Date???????????? ?????? ??????
            date_string = dateFormat.format(now); //????????? string?????? ??????
        }

        try{
            dailymeal=new getDailyMealNetworkCall().execute(retrofitAPI.getDailyMeal(user.getId(),date_string)).get();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private ActivityResultCallback<ActivityResult> cameraResultCallback = new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                activity.getWindow().getDecorView().setEnabled(false);

                Toast toast;
                toast = Toast.makeText(activity, "????????? ??????????????????!", Toast.LENGTH_LONG);
                toast.show();
                //img_photo.setImageURI(photoUri);
                onSetImage.onSetImage(photoUri);
                toast.cancel();
                activity.getWindow().getDecorView().setEnabled(true);

/*                Intent intent = new Intent(activity, FoodAnalysisActivity.class);
                intent.putExtra("full_meal_img", photoUri);
                startActivity(intent);*/

            }
        }
    };

    private ActivityResultCallback<ActivityResult> galleryResultCallback = new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if(result.getResultCode() == RESULT_OK){
                activity.getWindow().getDecorView().setEnabled(false);

                Toast toast;
                toast = Toast.makeText(activity, "????????? ??????????????????!", Toast.LENGTH_LONG);
                toast.show();

                Uri uri = result.getData().getData();
                //getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //img_photo.setImageURI(uri);
                onSetImage.onSetImage(uri);
                toast.cancel();
                activity.getWindow().getDecorView().setEnabled(true);
            }
        }
    };

    private void loadCameraImage(){
        try{

            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String tmpFileName = timestamp;

            //???????????????
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File photoFile = File.createTempFile(tmpFileName, ".jpg", storageDir);//????????????
            Log.e("photoFile",photoFile.toString());

            photoUri = FileProvider.getUriForFile(activity, getPackageName(),photoFile);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            cameraLauncher.launch(intent);

            Log.i("file_name", Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pictures/"+photoFile.getName());

        }catch(Exception ex){

        }
    }

    private void loadGalleryImage(){
        try{
            Intent intent = new Intent();
            intent.setType("image/*");
            //intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);

            galleryLauncher.launch(intent);

        }catch(Exception ex){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0){

            if(requestCode == REQUEST_WRITE_EXTERNAL_STORAGE){

                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    loadCameraImage();
                }else{
                    requestUserPermission();
                }

            }else if(requestCode == REQUEST_READ_EXTERNAL_STORAGE){

                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    loadGalleryImage();
                }else{
                    requestUserPermission();
                }
            }
        }
    }

    private void requestUserPermission(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        alertDialog.setIcon(R.drawable.alert_icon);
        alertDialog.setTitle("??????");
        alertDialog.setMessage("?????? ?????????????????????????");

        alertDialog.setPositiveButton("??????",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){

                //???????????? ?????? ?????? ??????
                Intent appDetail = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:"+getPackageName()));
                appDetail.addCategory(Intent.CATEGORY_DEFAULT);
                appDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(appDetail);
            }
        });

        alertDialog.setNegativeButton("??????",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){

            }
        });

        alertDialog.show();
    }

    /* ???????????? ??????
    public void setWeelklyCalories(String startDate, String endDate){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        retrofitAPI.getWeeklyCalories(user.getId(),startDate,endDate).enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                calories = response.body().toArray(new Integer[7]);
                try {
                    Log.e("weekly", String.valueOf(calories));
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                Log.e("Error! call msg",call.toString());
                Log.e("Error! t messge",t.getMessage());
            }
        });
    }*/

    /* AsyncTask ?????? ??????*/
    private class calorieNetworkCall extends AsyncTask<Call, Void, List<Integer>>{
        @Override
        protected List<Integer> doInBackground(Call[] params) {

            try {
                Call<List<Integer>> call = params[0];
                Response<List<Integer>> response = call.execute();
                return response.body();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class MealNetworkCall extends AsyncTask<Call, Void, List<meal>>{
        @Override
        protected List<meal> doInBackground(Call[] params) {
            try {
                Call<List<meal>> call = params[0];
                Response<List<meal>> response = call.execute();
                return response.body();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class timeFlagNetworkCall extends AsyncTask<Call, Void, Integer>{
        @Override
        protected Integer doInBackground(Call[] params) {
            try {
                Call<Integer> call = params[0];
                Response<Integer> response = call.execute();
                return response.body();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class getDailyMealNetworkCall extends AsyncTask<Call, Void, dailymeal > {
        @Override
        protected dailymeal doInBackground(Call[] params) {
            try {
                Call<dailymeal> call = params[0];
                Response<dailymeal> response = call.execute();
                return response.body();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    int p = 1;
    public class IntegerPercentFormatter extends ValueFormatter {

        private final RadarChart chart;

        public IntegerPercentFormatter(RadarChart chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {
            String pr = "";
            int val = (int) value;

            switch(p){
                case 1://????????????
                    if(val == 0)
                        pr = "                            c:0";
                    else if(val <= 5)
                        pr = "              c:"+ val +"%";
                    else
                        pr = "      c:"+ val +"%";
                    break;
                case 2://??????
                    if(val == 0)
                        pr = "              f:0";
                    else if(val <= 5)
                        pr = "       f:"+ val +"%";
                    else
                        pr = "      f:"+ val +"%";
                    break;
                case 3://?????????
                    if(val == 0)
                        pr = "p:0";
                    else
                        pr = "p:"+val +"%";
                    break;
                default:
                    break;
            }
            p++;

            /*
                ????????? ??????????????? ????????? ?????? ???????????? ?????? ??????
                case 1 ) ????????????????????? ???????????? ???????????? ??????????????? ?????? ????????? ??????, ?????? ??????????????????
                        --> ??????
            */
            if(p == 4)
                p =1;


            return pr;
        }
    }
}

