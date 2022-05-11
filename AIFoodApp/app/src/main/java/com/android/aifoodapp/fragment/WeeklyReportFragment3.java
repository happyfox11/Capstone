package com.android.aifoodapp.fragment;

import static com.android.aifoodapp.interfaceh.baseURL.url;

import static java.lang.Thread.sleep;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.R;
import com.android.aifoodapp.adapter.ReportDayItemAdapter;
import com.android.aifoodapp.domain.dailymeal;
import com.android.aifoodapp.domain.meal;
import com.android.aifoodapp.domain.user;
import com.android.aifoodapp.interfaceh.RetrofitAPI;
import com.android.aifoodapp.vo.ReportDayItemVo;
import com.android.aifoodapp.vo.ReportDaySubItemVo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeeklyReportFragment3 extends Fragment {

    private View v;
    private String[] day_of_week = {"월", "화", "수", "목", "금", "토", "일"};
    private RecyclerView rvItem;
    private LinearLayoutManager layoutManager;
    private ReportDayItemAdapter itemAdapter;

    user user;
    private List<dailymeal> dailyMealList = new ArrayList<>(); //일주일치 dailyMeal 데이터
    List<meal> ml;


    public WeeklyReportFragment3(user user, List<dailymeal> dailyMealList ) {
        this.user=user;
        this.dailyMealList=dailyMealList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.weekly_report_fragment_3, container, false);

        try {//동기화 처리를 위한 예외처리
            initialize();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setting();

        return v;
    }

    private void initialize()throws ExecutionException, InterruptedException{

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
            String day = dailyMealList.get(i).getDatekey();

            ReportDayItemVo item = new ReportDayItemVo(day.substring(0,4)+"년 "+day.substring(5,7)+"월 "+ day.substring(8)+"일 ( "+day_of_week[i]+" )", buildSubItemList(day));
            itemList.add(item);
        }
        return itemList;
    }

    //하위 아이템
    private List<ReportDaySubItemVo> buildSubItemList(String day)  {

        //동기적 처리 부분 음식 가져오는부분 juhee
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        List<ReportDaySubItemVo> subItemList = new ArrayList<>();

        try{//get 에 대한 예외처리
            subItemList = new FoodNetworkCall().execute(retrofitAPI.getFoodFromOneDay(user.getId(),day)).get();
            //get 을 사용하면 결과값까지 받아올수 있음
        }catch(Exception e){
            e.printStackTrace();
        }

        //동기화 부분에서 처리
        //for (int i=0; i<subItemList.size(); i++) {
        //    ReportDaySubItemVo subItem = new ReportDaySubItemVo(Integer.toString(i), "Description "+i);
        //    subItemList.add(subItem);
        //}
        //Log.e("size",Integer.toString(subItemList.size()));
        return subItemList;
    }

    private class FoodNetworkCall extends AsyncTask<Call, Void, List<ReportDaySubItemVo> > {
        //동기적 처리
        @Override
        protected List<ReportDaySubItemVo> doInBackground(Call[] params) {
            List<ReportDaySubItemVo> subItemList = new ArrayList<>();

            try {
                Call<List<meal>> call = params[0];
                Response<List<meal>> response = call.execute();
                ml=response.body();
                if(ml.isEmpty()) {
                    return subItemList;
                }
                for (meal repo : ml) {
                    //int subItemImage = 0 ;
                    String subItemTitle = repo.getMealname();
                    String subItemDesc = Integer.toString(repo.getCalorie()); //칼로리만 출력 (추가 가능)

                    subItemList.add(new ReportDaySubItemVo(subItemDesc,subItemTitle));
                    Log.e("mealname",repo.getMealname());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return subItemList;
        }
    }
}