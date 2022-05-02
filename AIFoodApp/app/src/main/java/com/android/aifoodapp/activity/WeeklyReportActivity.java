package com.android.aifoodapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import com.android.aifoodapp.R;
import com.android.aifoodapp.adapter.ReportPagerAdapter;
import com.android.aifoodapp.fragment.WeeklyReportFragment1;
import com.android.aifoodapp.fragment.WeeklyReportFragment3;
import com.hayahyts.dottedprogressindicator.DottedProgressBar;


public class WeeklyReportActivity extends AppCompatActivity {

    Activity activity;
    private ViewPager pager;
    private ReportPagerAdapter reportPagerAdapter;

    private WeeklyReportFragment1 weekly_report_fragment_1;
    //private WeeklyReportFragment2 weekly_report_fragment_2;
    private WeeklyReportFragment3 weekly_report_fragment_3;
    //private WeeklyReportFragment4 weekly_report_fragment_4;

    private ImageButton btn_back_weekly_report;


    private ProgressBar pg_bar;
    private int value=100/2;

    private DottedProgressBar dottedProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weekly_report);

        initialize();
        setting();
        addListener();

    }

    private void initialize(){

        activity = this;
        pager = findViewById(R.id.pager);
        pager.canScrollVertically(1);
        reportPagerAdapter = new ReportPagerAdapter(getSupportFragmentManager());

        weekly_report_fragment_1 = new WeeklyReportFragment1();
        //weekly_report_fragment_2 = new WeeklyReportFragment2();
        weekly_report_fragment_3 = new WeeklyReportFragment3();
        //weekly_report_fragment_4 = new WeeklyReportFragment4();

        dottedProgressBar = findViewById(R.id.dotted_progress_bar);
        btn_back_weekly_report = findViewById(R.id.btn_back_weekly_report);
    }


    private void setting(){

        pager.setOffscreenPageLimit(3);

        reportPagerAdapter.addItem(new WeeklyReportFragment1());
        reportPagerAdapter.addItem(weekly_report_fragment_1);
        //reportPagerAdapter.addItem(weekly_report_fragment_2);
        reportPagerAdapter.addItem(weekly_report_fragment_3);
        //reportPagerAdapter.addItem(weekly_report_fragment_4);
        reportPagerAdapter.addItem(new WeeklyReportFragment3());

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


}