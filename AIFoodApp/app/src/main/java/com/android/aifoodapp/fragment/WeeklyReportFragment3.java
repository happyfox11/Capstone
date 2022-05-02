package com.android.aifoodapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.adapter.ReportDayItemAdapter;
import com.android.aifoodapp.vo.ReportDayItemVo;
import com.android.aifoodapp.vo.ReportDaySubItemVo;

import java.util.ArrayList;
import java.util.List;

public class WeeklyReportFragment3 extends Fragment {

    private View v;
    private String[] day_of_week = {"월", "화", "수", "목", "금", "토", "일"};
    private RecyclerView rvItem;
    private LinearLayoutManager layoutManager;
    private ReportDayItemAdapter itemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.weekly_report_fragment_3, container, false);

        initialize();
        setting();

        return v;
    }

    private void initialize(){

        rvItem = v.findViewById(R.id.rv_item);
        layoutManager = new LinearLayoutManager(getActivity());
        itemAdapter = new ReportDayItemAdapter(buildItemList());
    }

    private void setting(){

        rvItem.setAdapter(itemAdapter);
        rvItem.setLayoutManager(layoutManager);
    }


    //상위 아이템
    private List<ReportDayItemVo> buildItemList() {
        List<ReportDayItemVo> itemList = new ArrayList<>();
        for (int i=0; i<7; i++) {
            ReportDayItemVo item = new ReportDayItemVo("__년 __월 __일 ( "+day_of_week[i]+" )", buildSubItemList());
            itemList.add(item);
        }
        return itemList;
    }

    //하위 아이템
    private List<ReportDaySubItemVo> buildSubItemList() {
        List<ReportDaySubItemVo> subItemList = new ArrayList<>();
        for (int i=0; i<5; i++) {
            ReportDaySubItemVo subItem = new ReportDaySubItemVo(Integer.toString(i), "Description "+i);
            subItemList.add(subItem);
        }
        return subItemList;
    }
}