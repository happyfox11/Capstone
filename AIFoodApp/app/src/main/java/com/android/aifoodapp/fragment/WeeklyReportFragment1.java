package com.android.aifoodapp.fragment;

import static com.android.aifoodapp.interfaceh.baseURL.url;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.aifoodapp.R;
import com.android.aifoodapp.domain.dailymeal;
import com.android.aifoodapp.domain.user;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;


public class WeeklyReportFragment1 extends Fragment {

    private View v;
    private BarChart barChart;
    private ArrayList<Integer> jsonList = new ArrayList<>(); // ArrayList 선언
    private ArrayList<String> labelList = new ArrayList<>(); // ArrayList 선언
    private int d = 1;
    private CircleProgressView mCircleView;
    private TextView tv_compare_previous_kcal;
    private List<dailymeal> dailyMealList = new ArrayList<>();
    private List<dailymeal> lastDailyMealList = new ArrayList<>();
    user user;

    int target_calories;
    List<Integer> prev_week_calories_list = new ArrayList<>();
    List<Integer> curr_week_calories_list = new ArrayList<>();
    int prev_week_avg_calories;
    int curr_week_avg_calories;

    public WeeklyReportFragment1(user user, List<dailymeal> dailyMealList, List<dailymeal> lastDailyMealList ){
        this.user=user;
        this.dailyMealList=dailyMealList;
        this.lastDailyMealList=lastDailyMealList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.weekly_report_fragment_1, container, false);

        initialize();
        setting();
        addListener();

        return v;
    }

    private void initialize(){
        barChart = v.findViewById(R.id.barchart);
        mCircleView = v.findViewById(R.id.mCircleView);
        tv_compare_previous_kcal = v.findViewById(R.id.tv_compare_previous_kcal);

        // TODO : DB 값 받아오기 (target_calories, prev_calories_test_val, curr_calories_test_val)
        target_calories = user.getTarget_calories(); // 사용자의 목표 칼로리

        //Log.e("목표칼로리",Integer.toString(target_calories));
        int sum_of_curr_weekly_calories = 0;// 이번 주의 총 섭취 칼로리 합계
        int sum_of_prev_weekly_calories = 0;// 지난 주의 총 섭취 칼로리 합계

        //Log.e("dailyMealList",dailyMealList.toString());
        //Log.e("lastDailyMealList",lastDailyMealList.toString());

        int cnt_curr = 7; // 평균을 낼 날의 수(칼로리가 0인 날은 제외해야 함)
        int cnt_prev = 7; // 평균을 낼 날의 수(칼로리가 0인 날은 제외해야 함)

        int[] prev_calories_test_val = {1344, 599, 1800, 1914, 1044, 1704, 1434};//지난 주 하루 칼로리 테스트 값
        int[] curr_calories_test_val = {3344, 1599, 1400, 1114, 1244, 704, 2434};//이번 주 하루 칼로리 테스트 값

        for(int i=0; i<7; i++){
            prev_calories_test_val[i]=lastDailyMealList.get(i).getCalorie(); //저번주
            curr_calories_test_val[i]=dailyMealList.get(i).getCalorie(); //이번주
        }

        for(int i = 0; i< 7; i++){

            if(curr_calories_test_val[i] == 0)//칼로리가 0인 날은 평균계산에서 제외
                cnt_curr--;
            if(prev_calories_test_val[i] == 0)//칼로리가 0인 날은 평균계산에서 제외
                cnt_prev--;

            prev_week_calories_list.add(prev_calories_test_val[i]);
            curr_week_calories_list.add(curr_calories_test_val[i]);

            sum_of_prev_weekly_calories += prev_calories_test_val[i];
            sum_of_curr_weekly_calories += curr_calories_test_val[i];
        }

        if(cnt_prev == 0)//한 주간 어떤 식사 기록도 하지 않은 경우
            prev_week_avg_calories = 0;
        else
            prev_week_avg_calories = (int) (sum_of_prev_weekly_calories / cnt_prev);

        if(cnt_curr == 0)//한 주간 어떤 식사 기록도 하지 않은 경우
            curr_week_avg_calories = 0;
        else
            curr_week_avg_calories = (int) (sum_of_curr_weekly_calories / cnt_curr);

    }

    private void setting(){
        comparePrevCaloriesSetting();
        circularProgressBarSetting();
        graphInitSetting();//그래프 기본 세팅
    }

    private void addListener(){
        mCircleView.setOnTouchListener(listener_touch_disable);
    }

    private View.OnTouchListener listener_touch_disable = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    };

    private void comparePrevCaloriesSetting(){
        int diff_prev = curr_week_avg_calories - prev_week_avg_calories;

        if(diff_prev > 0) {//지난주보다 섭취량이 증가 (Red Color)
            tv_compare_previous_kcal.setText(tv_compare_previous_kcal.getText().toString() + diff_prev);
        }else {//감소 (Blue Color)
            tv_compare_previous_kcal.setText(Integer.toString(diff_prev));
            tv_compare_previous_kcal.setTextColor(Color.parseColor("#0000ff"));
        }
    }

    private void circularProgressBarSetting(){

        //mCircleView.setText("2300/ 1700");
        //mCircleView.setValue(80);

        int avg_per_target = (int) (100 * curr_week_avg_calories / target_calories);
        mCircleView.setText(curr_week_avg_calories + " / " + target_calories);
        mCircleView.setValue(avg_per_target);

        if(avg_per_target > 100){
            mCircleView.setValue(100);
            mCircleView.setBarColor(Color.parseColor("#ff0000"));
            mCircleView.setTextColor(Color.parseColor("#ff0000"));
        }else if(avg_per_target < 85){
            mCircleView.setBarColor(Color.parseColor("#0000ff"));
            mCircleView.setTextColor(Color.parseColor("#0000ff"));
            mCircleView.setRimColor(Color.parseColor("#b9c0ed"));
        }

        mCircleView.setTextSize(50);
        mCircleView.setUnitSize(50);
        mCircleView.setTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "cafe24ssurroundair.ttf"));
        mCircleView.setUnitTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "cafe24ssurroundair.ttf"));
    }

    private void graphInitSetting(){

        int xPos = 10;
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i = 0; i< 7; i++){
            entries.add(new BarEntry(Integer.valueOf(xPos * (i+1)), curr_week_calories_list.get(i)));
        }

       /* entries.add(new BarEntry((Integer) 10, 3344));
        entries.add(new BarEntry((Integer) 20, 1599));
        entries.add(new BarEntry((Integer) 30, 1400));
        entries.add(new BarEntry((Integer) 40, 1114));
        entries.add(new BarEntry((Integer) 50, 1244));
        entries.add(new BarEntry((Integer) 60, 704));
        entries.add(new BarEntry((Integer) 70, 2434));*/

        BarDataSet depenses = new BarDataSet(entries, "일일 사용시간"); // 변수로 받아서 넣어줘도 됨
        depenses.setAxisDependency(YAxis.AxisDependency.LEFT);
        depenses.setValueTextSize(18f);
        /*
        depenses.setBarBorderWidth(10f);
        depenses.setBarBorderColor(Color.rgb(65, 153, 24));
        */

        IntegerValueFormatter integerValueFormatter = new IntegerValueFormatter(barChart);
        depenses.setValueFormatter(integerValueFormatter);

        //barChart.setDescription(" ");

        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < labelList.size(); i++) {
            labels.add((String) labelList.get(i));
        }
        String[] lab = {"월","화","수","목", "금", "토","일"};
        //BarData data = new BarData(lab, depenses); // 라이브러리 v3.x 사용하면 에러 발생함
        BarData data = new BarData(); // 라이브러리 v3.x 사용하면 에러 발생함
        data.addDataSet(depenses);
        data.setBarWidth(5f);
        data.setValueTypeface(Typeface.createFromAsset(getActivity().getAssets(), "cafe24ssurroundair.ttf"));


        /*
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setLabelCount(7);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        */

        DayAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "cafe24ssurroundair.ttf"));
        xAxis.setTextSize(20f);
        xAxis.setYOffset(10f);
        //xAxis.setValueFormatter(new IndexAxisValueFormatter(lab));


        depenses.setColors(ColorTemplate.VORDIPLOM_COLORS); //
        barChart.setExtraBottomOffset(20f);
        barChart.setExtraLeftOffset(10f);
        barChart.setExtraRightOffset(10f);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setData(data);

        barChart.setTouchEnabled(false);
/*        barChart.getAxisRight().setAxisMaxValue(4000);
        barChart.getAxisLeft().setAxisMaxValue(4000);*/
        barChart.getAxisRight().setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "cafe24ssurroundair.ttf"));
        barChart.getAxisLeft().setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "cafe24ssurroundair.ttf"));
        barChart.getAxisLeft().setTextSize(4f);
        barChart.getAxisLeft().setTextSize(4f);
        barChart.invalidate();

    }

    public class DayAxisValueFormatter extends ValueFormatter {

        private final BarLineChartBase<?> chart;

        public DayAxisValueFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {
            String day_of_week = "";
            Log.i("checkekek", value+","+(int) value);


            switch((int) d){
                case 1 :
                    day_of_week = "월";
                    break;
                case 2 :
                    day_of_week = "화";
                    break;
                case 3 :
                    day_of_week = "수";
                    break;
                case 4 :
                    day_of_week = "목";
                    break;
                case 5 :
                    day_of_week = "금";
                    break;
                case 6 :
                    day_of_week = "토";
                    break;
                case 7 :
                    day_of_week = "일";
                    break;
                default :
                    day_of_week = "---";
                    break;

            }
            d++;
            if(d == 8)
                d =1;
            return day_of_week;
        }
    }

    public class IntegerValueFormatter extends ValueFormatter {

        private final BarLineChartBase<?> chart1;

        public IntegerValueFormatter(BarLineChartBase<?> chart1) {
            this.chart1 = chart1;
        }

        @Override
        public String getFormattedValue(float value) {

            int val = (int) value;

            return Integer.toString(val);
        }
    }
}