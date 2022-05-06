package com.android.aifoodapp.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.aifoodapp.R;
import com.android.aifoodapp.activity.MainActivity;
import com.android.aifoodapp.domain.dailymeal;
import com.android.aifoodapp.domain.user;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class WeeklyReportFragment2 extends Fragment {

    private LineChart lineChart;

    user user;
    private List<dailymeal> dailyMealList = new ArrayList<>(); //일주일치 dailyMeal 데이터

    ArrayList<Entry> average_chart = new ArrayList<Entry>(); //데이터를 담을 리스트 (평균 3대 비율)
    ArrayList<Entry> my_chart = new ArrayList<Entry>(); // 데이터를 담을 리스트 (나의 비율)
    ArrayList<String> xLabels = new ArrayList<>(); //x축 라벨
    LineData chartData = new LineData(); // 차트에 담길 데이터

    public WeeklyReportFragment2(user user, List<dailymeal> dailyMealList){
        this.user=user;
        this.dailyMealList=dailyMealList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.weekly_report_fragment_2, container, false);
        lineChart = (LineChart) rootView.findViewById(R.id.lineChart);

        //평균 3대 비율 차트
        average_chart.add(new Entry(0,50f)); //탄수화물
        average_chart.add(new Entry(3.5f,30f)); //단백질
        average_chart.add(new Entry(7f,20f)); //지방


        //나의 3대 비율 차트
        my_chart.add(new Entry(0,70f));
        my_chart.add(new Entry(3.5f,20f));
        my_chart.add(new Entry(7f,10f));



        // 데이터가 담긴 ArrayList를 LineDataSet으로 변환
        LineDataSet averageRatio = new LineDataSet(average_chart,"평균 3대 비율");
        LineDataSet myRatio = new LineDataSet(my_chart,"나의 3대 비율");
        //averageDataSets.add(averageRatio);

        LineData data = new LineData(averageRatio);

        averageRatio.setColor(Color.rgb(246,187,67));

        myRatio.setColor(Color.rgb(35,140,35));


        chartData.addDataSet(averageRatio);  // 해당 LineDataSet 을 적용될 차트에 들어갈 DataSet에 넣음
        chartData.addDataSet(myRatio);
        chartData.setValueTypeface(Typeface.createFromAsset(getActivity().getAssets(), "cafe24ssurroundair.ttf"));
        chartData.setValueTextSize(14f);

        xLabels.add("탄수화물");
        xLabels.add("단백질");
        xLabels.add("지방");

        XAxis xAxis = lineChart.getXAxis(); //x축 설정
        //xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        //xAxis.setAxisMinimum(0);
        //xAxis.setAxisMaximum(7f);

       /* xAxis.setValueFormatter(new ValueFormatter() {

            /*@Override
            public String getAxisLabel(float value, AxisBase axis) {
                String label = "";
                if(  value == 1)
                    label = "탄수화물";
                else if (value == 2)
                    label = "단백질";
                else if ( value ==3 )
                    label = "지방";
                return label;
            }
        });*/

        xAxis.setTextSize(18f);
        xAxis.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "cafe24ssurroundair.ttf"));
//        xAxis.setGranularity(1f); //간격 설정
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(3, true);
        //xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        //Log.i("cadfa", xAxis.getLabelCount()+"");
        LineAxisValueFormatter lineAxisValueFormatter = new LineAxisValueFormatter(lineChart);
        xAxis.setValueFormatter(lineAxisValueFormatter);
        //Log.i("cadfa", xAxis.getLabelCount()+"");

        //y축 격자선 없앰
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisLeft().setTextSize(12f);
        lineChart.getAxisLeft().setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "cafe24ssurroundair.ttf"));

        Legend legend = lineChart.getLegend(); //범례 위치 변경
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setTextSize(16f);
        legend.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "cafe24ssurroundair.ttf"));

        lineChart.getDescription().setEnabled(false);
        lineChart.setData(chartData);
        lineChart.invalidate(); // 차트 업데이트
        lineChart.setTouchEnabled(false);
        lineChart.setExtraBottomOffset(15f);
        lineChart.setExtraLeftOffset(15f);
        lineChart.setExtraRightOffset(15f);

        return rootView;



    }

    int d = 1;
    public class LineAxisValueFormatter extends ValueFormatter {

        private final LineChart chart;

        public LineAxisValueFormatter(LineChart chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {
            String res = "";

            if(d == 1) res = "탄수화물";
            else if(d == 2) res = "단백질";
            else res = "지방";

            d++;
            if(d>3)
                d = 1;

            return res;
        }
    }

}
