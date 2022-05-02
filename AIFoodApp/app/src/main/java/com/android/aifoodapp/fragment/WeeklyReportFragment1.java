package com.android.aifoodapp.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.android.aifoodapp.R;
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
import at.grabner.circleprogress.CircleProgressView;


public class WeeklyReportFragment1 extends Fragment {

    private View v;
    private BarChart barChart;
    private ArrayList<Integer> jsonList = new ArrayList<>(); // ArrayList 선언
    private ArrayList<String> labelList = new ArrayList<>(); // ArrayList 선언
    private int d = 1;
    private CircleProgressView mCircleView;

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

    }

    private void setting(){
        circularProgressBar();
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

    private void circularProgressBar(){
        mCircleView.setText("2300/ 1700");
        mCircleView.setValue(80);
        mCircleView.setTextSize(50);
        mCircleView.setUnitSize(50);
        mCircleView.setTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "cafe24ssurroundair.ttf"));
        mCircleView.setUnitTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "cafe24ssurroundair.ttf"));
    }

    private void graphInitSetting(){

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry((Integer) 10, 3344));
        entries.add(new BarEntry((Integer) 20, 1599));
        entries.add(new BarEntry((Integer) 30, 1400));
        entries.add(new BarEntry((Integer) 40, 1114));
        entries.add(new BarEntry((Integer) 50, 1244));
        entries.add(new BarEntry((Integer) 60, 704));
        entries.add(new BarEntry((Integer) 70, 2434));
        int max = 3344;

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