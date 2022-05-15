package com.android.aifoodapp.activity;

import static com.android.aifoodapp.interfaceh.baseURL.url;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.adapter.ReportPagerAdapter;
import com.android.aifoodapp.domain.dailymeal;
import com.android.aifoodapp.domain.meal;
import com.android.aifoodapp.domain.user;
import com.android.aifoodapp.fragment.WeeklyReportFragment1;
import com.android.aifoodapp.fragment.WeeklyReportFragment2;
import com.android.aifoodapp.fragment.WeeklyReportFragment3;
import com.android.aifoodapp.fragment.WeeklyReportFragment4;
import com.android.aifoodapp.interfaceh.RetrofitAPI;
import com.android.aifoodapp.vo.SubItem;
import com.hayahyts.dottedprogressindicator.DottedProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WeeklyReportActivity extends AppCompatActivity {

    Activity activity;
    private ViewPager pager;
    private ReportPagerAdapter reportPagerAdapter;

    private WeeklyReportFragment1 weekly_report_fragment_1;
    private WeeklyReportFragment2 weekly_report_fragment_2;
    private WeeklyReportFragment3 weekly_report_fragment_3;
    private WeeklyReportFragment4 weekly_report_fragment_4;

    private ImageButton btn_back_weekly_report;

    private ProgressBar pg_bar;
    private int value=100/2;

    TextView tv_week;
    user user;
    private List<dailymeal> dailyMealList = new ArrayList<>();
    private List<dailymeal> lastDailyMealList = new ArrayList<>();

    private DottedProgressBar dottedProgressBar;
    private String startDate = null, endDate = null, lastStartDate=null, lastEndDate=null;
    private String period;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weekly_report);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");

        getLastWeekDailyMeal();
        initialize();
        setting();
        addListener();

    }

    private void initialize(){

        activity = this;
        tv_week = findViewById(R.id.tv_week);
        pager = findViewById(R.id.pager);
        pager.canScrollVertically(1);
        reportPagerAdapter = new ReportPagerAdapter(getSupportFragmentManager());

        weekly_report_fragment_1 = new WeeklyReportFragment1(user, dailyMealList, lastDailyMealList);
        weekly_report_fragment_2 = new WeeklyReportFragment2(user, dailyMealList);
        weekly_report_fragment_3 = new WeeklyReportFragment3(user, dailyMealList);
        weekly_report_fragment_4 = new WeeklyReportFragment4();

        dottedProgressBar = findViewById(R.id.dotted_progress_bar);
        btn_back_weekly_report = findViewById(R.id.btn_back_weekly_report);
    }


    private void setting(){
        //__월 __일 ~ __월 __일
        tv_week.setText(period);

        pager.setOffscreenPageLimit(3);

        reportPagerAdapter.addItem(new WeeklyReportFragment1(user, dailyMealList, lastDailyMealList));
        reportPagerAdapter.addItem(weekly_report_fragment_1);
        reportPagerAdapter.addItem(weekly_report_fragment_2);
        reportPagerAdapter.addItem(weekly_report_fragment_3);
        reportPagerAdapter.addItem(weekly_report_fragment_4);
        reportPagerAdapter.addItem(new WeeklyReportFragment3(user, dailyMealList));

        pager.setAdapter(reportPagerAdapter);

        pager.setCurrentItem(1, false);

        setInitProgressBar();

        setProgressBar();
    }


    private void addListener(){

        btn_back_weekly_report.setOnClickListener(listener_back);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int currPosistion = 1;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currPosistion = position;

                if(currPosistion + 1 == reportPagerAdapter.getCount()) {
                    pager.setCurrentItem(1, false);
                }
                else if(currPosistion == 0) {
                    pager.setCurrentItem(reportPagerAdapter.getCount() - 2, false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                setProgressBar();
            }

        });
    }

    private final View.OnClickListener listener_back = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private void setInitProgressBar(){
        dottedProgressBar.setDotCount(4);
        dottedProgressBar.setScaleX(3f);
        dottedProgressBar.setScaleY(3f);
        dottedProgressBar.setSelectedColor(Color.rgb(65, 153, 24));
        dottedProgressBar.setActivated(true);
        dottedProgressBar.setFocusable(true);
    }

    private void setProgressBar(){

        dottedProgressBar.setCurrent(pager.getCurrentItem() - 1, true);
    }

    private void getLastWeekDailyMeal() {

        /* 해당 코드는 현재 날짜 기준으로만 계산을 한다. */
        //https://blog.naver.com/rlatmdtn81/222024742263
        //월~일로 시작한다.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7); //현재날짜에서 이번주(-7)
        int lastWeek = cal.get(Calendar.DAY_OF_WEEK); //이번주 요일
        cal.add(Calendar.DATE, 2 - lastWeek); //이번주의 월요일이 되도록한다. (일)
        //year : 년  month : 월  date : 일

        //이번주 월요일
        int startMonth = cal.get(Calendar.MONTH) + 1;//월
        startDate = cal.get(Calendar.YEAR)
                + "-"
                + (startMonth < 10 ? "0" + startMonth : startMonth + "")
                + "-"
                + (cal.get(Calendar.DATE) < 10 ? "0" + cal.get(Calendar.DATE) : cal.get(Calendar.DATE) + "");
        int tmp = cal.get(Calendar.DATE);

        //이번주 일요일
        cal.add(Calendar.DATE, 6);//6을 더해 일요일로 만든다.
        int lastMonth = cal.get(Calendar.MONTH) + 1;
        endDate = cal.get(Calendar.YEAR)
                + "-"
                + (lastMonth < 10 ? "0" + lastMonth : lastMonth + "")
                + "-"
                + (cal.get(Calendar.DATE) < 10 ? "0" + cal.get(Calendar.DATE) : cal.get(Calendar.DATE) + "");


        //tv_week 출력을 위함(상단바)
        period = (startMonth < 10 ? "0" + startMonth : startMonth + "")
                +"월 "
                +(tmp < 10 ? "0" + tmp : tmp + "")
                +"일 ~ "
                +(lastMonth < 10 ? "0" + lastMonth : lastMonth + "")
                +"월 "
                +(cal.get(Calendar.DATE) < 10 ? "0" + cal.get(Calendar.DATE) : cal.get(Calendar.DATE) + "")
                +"일";

        //getWeeklyMeal
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        //이번주 weeklyDailyMeal 불러오기
        /*
        retrofitAPI.getWeeklyMeal(user.getId(),startDate,endDate).enqueue(new Callback<List<dailymeal>>() {
            @Override
            public void onResponse(Call<List<dailymeal>> call, Response<List<dailymeal>> response) {
                List<dailymeal> list = response.body();
                for(dailymeal repo:list){
                    dailyMealList.add(repo);
                }
            }

            @Override
            public void onFailure(Call<List<dailymeal>> call, Throwable t) {
                Log.e("WeeklyReportPage1",t.getMessage());
            }
        });*/
        new dailyMealNetworkCall().execute(retrofitAPI.getWeeklyMeal(user.getId(),startDate,endDate));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Log.e("!zzzzzzzzzz!",dailyMealList.toString());


        /* 지난주 */
        cal.add(Calendar.DATE, -14); //-14
        lastWeek = cal.get(Calendar.DAY_OF_WEEK); //지난주 요일
        cal.add(Calendar.DATE, 2 - lastWeek); //지난주의 월요일이 되도록한다. (일)

        //지난주 월요일
        startMonth = cal.get(Calendar.MONTH) + 1;//월
        lastStartDate = cal.get(Calendar.YEAR)
                + "-"
                + (startMonth < 10 ? "0" + startMonth : startMonth + "")
                + "-"
                + (cal.get(Calendar.DATE) < 10 ? "0" + cal.get(Calendar.DATE) : cal.get(Calendar.DATE) + "");

        //지난주 일요일
        cal.add(Calendar.DATE, 6);//6을 더해 일요일로 만든다.
        lastMonth = cal.get(Calendar.MONTH) + 1;
        lastEndDate = cal.get(Calendar.YEAR)
                + "-"
                + (lastMonth < 10 ? "0" + lastMonth : lastMonth + "")
                + "-"
                + (cal.get(Calendar.DATE) < 10 ? "0" + cal.get(Calendar.DATE) : cal.get(Calendar.DATE) + "");

        /*
        //저번주 weeklyDailyMeal 불러오기
        retrofitAPI.getWeeklyMeal(user.getId(),LastStartDate,LastEndDate).enqueue(new Callback<List<dailymeal>>() {
            @Override
            public void onResponse(Call<List<dailymeal>> call, Response<List<dailymeal>> response) {
                List<dailymeal> list = response.body();
                for(dailymeal repo:list){
                    lastDailyMealList.add(repo);
                }
            }

            @Override
            public void onFailure(Call<List<dailymeal>> call, Throwable t) {
                Log.e("WeeklyReportPage2",t.getMessage());
            }
        });*/
        Log.e("lastStartDate",lastStartDate);
        new lastDailyMealNetworkCall().execute(retrofitAPI.getWeeklyMeal(user.getId(),lastStartDate,lastEndDate));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class dailyMealNetworkCall extends AsyncTask<Call, Void, String> {

        @Override
        protected String doInBackground(Call[] params) {

            try {
                Call<List<dailymeal>> call = params[0];
                Response<List<dailymeal>> response = call.execute();
                List<dailymeal> list=response.body();
                if(list.isEmpty()){
                }
                else{
                    for (dailymeal repo : list) {
                        dailyMealList.add(repo);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class lastDailyMealNetworkCall extends AsyncTask<Call, Void, String> {

        @Override
        protected String doInBackground(Call[] params) {

            try {
                Call<List<dailymeal>> call = params[0];
                Response<List<dailymeal>> response = call.execute();
                List<dailymeal> list=response.body();
                if(list.isEmpty()){
                }
                else{
                    for (dailymeal repo : list) {
                        lastDailyMealList.add(repo);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}