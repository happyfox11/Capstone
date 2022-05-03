package com.android.aifoodapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.aifoodapp.R;
import com.android.aifoodapp.activity.MainActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class WeeklyReportFragment2 extends Fragment {

    private LineChart lineChart;

    ArrayList<Entry> average_chart = new ArrayList<Entry>(); //데이터를 담을 리스트 (평균 3대 비율)
    ArrayList<Entry> my_chart = new ArrayList<Entry>(); // 데이터를 담을 리스트 (나의 비율)
    ArrayList<String> xLabels = new ArrayList<>(); //x축 라벨
    LineData chartData = new LineData(); // 차트에 담길 데이터


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

        xLabels.add("탄수화물");
        xLabels.add("단백질");
        xLabels.add("지방");

        XAxis xAxis = lineChart.getXAxis(); //x축 설정
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(7f);

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
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));

        xAxis.setGranularity(1f); //간격 설정
        xAxis.setGranularityEnabled(true);

        //y축 격자선 없앰
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);

        Legend legend = lineChart.getLegend(); //범례 위치 변경
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        lineChart.getDescription().setEnabled(false);
        lineChart.setData(chartData);
        lineChart.invalidate(); // 차트 업데이트
        lineChart.setTouchEnabled(false);
        return rootView;



    }

}
