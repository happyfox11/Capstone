package com.android.aifoodapp.fragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.aifoodapp.R;
import com.android.aifoodapp.domain.dailymeal;
import com.android.aifoodapp.domain.meal;
import com.android.aifoodapp.domain.user;
import com.android.aifoodapp.vo.ReportDayItemVo;

import java.util.ArrayList;
import java.util.List;

public class WeeklyReportFragment4 extends Fragment {

    private View view;
    ProgressBar averageActiveTime;
    ProgressBar averageWalk;
    TextView tv_activeTime, tv_walk, activeTimeInfo,burningCaloriesInfo , calculateActiveTime, activeTimeComment,
    protein_comment, fat_comment, exercise_comment;

    Button recommendBtn;

    user user;
    private List<dailymeal> dailyMealList = new ArrayList<>(); //일주일치 dailyMeal 데이터
    private List<dailymeal> lastDailyMealList = new ArrayList<>();

    int activeTime;
    int prevActiveTime;
    int walkCount = 0;
    int prevWalkCount = 0;
    int burningCalories;

    public WeeklyReportFragment4(user user, List<dailymeal> dailyMealList, List<dailymeal> lastDailyMealList ){
        this.user=user;
        this.dailyMealList=dailyMealList;
        this.lastDailyMealList=lastDailyMealList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.weekly_report_fragment_4, container, false);

        initialize();
        setting();

        tv_activeTime .setText(String.valueOf(activeTime));
        tv_walk.setText(String.valueOf(walkCount));
        activeTimeInfo.setText(String.valueOf(activeTime)+"분");
        burningCaloriesInfo.setText(String.valueOf(burningCalories)+"kcal");


        averageActiveTime.setIndeterminate(false);
        averageActiveTime.setProgress(activeTime);
        averageActiveTime.setMax(60); // 1시간 기준

        averageWalk.setIndeterminate(false);
        averageWalk.setProgress(walkCount);  // 걸음수 프로그레스바 초기값과 최대값 xml에서 설정


        if(activeTime < 30) {
            averageActiveTime.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
        }
        if (activeTime > 30){
            averageActiveTime.setProgressTintList(ColorStateList.valueOf(Color.RED));
        }

        if (walkCount < 5000){
            averageWalk.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
        }
        if(walkCount > 5000){
            averageWalk.setProgressTintList(ColorStateList.valueOf(Color.RED));
        }

        //전주와의 시간비교
        calculateActiveTime.setText(String.valueOf(activeTime-prevActiveTime));

        if(walkCount<=6000) exercise_comment.setText(String.valueOf("운동부족"));
        if(walkCount>6000) exercise_comment.setText(String.valueOf("운동적정"));

        //총평 일단 급해서 복붙
        double user_ate_carbonhydrate =0;
        double user_ate_protein =0;
        double user_ate_fat =0;
        int day_count =0;

        //작성 하지 않은 날은 계산 하지 않음
        for(int i=0;i<dailyMealList.size();i++){
            dailymeal temp = dailyMealList.get(i);
            if(temp.getCalorie()==0)
                continue;

            user_ate_fat += temp.getFat();
            user_ate_carbonhydrate = temp.getCarbohydrate();
            user_ate_protein = temp.getProtein();
            day_count++;
        }

        //일주일 섭취량의 평균
        user_ate_fat /= day_count;
        user_ate_protein /= day_count;

        double user_ate = user_ate_fat + user_ate_protein +user_ate_carbonhydrate;

        float percent_of_protein = (int) ((user_ate_protein *100.0)/user_ate);
        float percent_of_fat = (int) ((user_ate_fat*100.0)/user_ate);


        if (percent_of_protein > 35)protein_comment.setText(String.valueOf("단백질 과잉"));
        else if (percent_of_protein < 25)protein_comment.setText(String.valueOf("단백질 부족"));
        else protein_comment.setText(String.valueOf("단백질 적정"));

        if (percent_of_fat > 25)fat_comment.setText(String.valueOf("지방 과잉"));
        else if (percent_of_fat < 15) fat_comment.setText(String.valueOf("지방 부족"));
        else fat_comment.setText(String.valueOf("지방 적정"));

        return view;
    }

   public void initialize(){
       averageActiveTime = view.findViewById(R.id.averageActiveTime);
       averageWalk = view.findViewById(R.id.averageWalk);
       tv_activeTime = view.findViewById(R.id.tv_activeTme);
       tv_walk = view.findViewById(R.id.tv_walk);
       //recommendBtn = view.findViewById(R.id.recommedBtn);
       activeTimeInfo = view.findViewById(R.id.activeTimeInfo);
       burningCaloriesInfo = view.findViewById(R.id.buringCaloriesInfo);
       calculateActiveTime = view.findViewById(R.id.calculateActiveTime);
       activeTimeComment = view.findViewById(R.id.activeTimeComment);
       protein_comment = view.findViewById(R.id.protein_comment);
       fat_comment = view.findViewById(R.id.fat_comment);
       exercise_comment = view.findViewById(R.id.exercise_comment);
   }

   public  void setting(){
       int nonZeroDay=0;
       int prevNonZeroDay=0;
       for (int i=0; i<7; i++) {
           int tempStep=dailyMealList.get(i).getStepcount();
           if( tempStep != 0 ){
               walkCount += tempStep;
               nonZeroDay ++;
           }

           int prevStep = lastDailyMealList.get(i).getStepcount();
           if(prevStep !=0){
               prevWalkCount += prevStep;
               prevNonZeroDay ++;
           }
       }

       //0이 아닌 날짜 기준으로 평균 계산
       walkCount = (nonZeroDay==0)? 0:walkCount/nonZeroDay;
       prevWalkCount = (prevNonZeroDay==0)? 0:prevWalkCount/prevNonZeroDay;
       //시간 1000/1400
       activeTime = (int) ((1000.0/1400.0*walkCount)/94);
       activeTime = (activeTime<=1)?1:activeTime;
       prevActiveTime = (int) ((1000.0/1400.0*prevWalkCount)/94);
       prevActiveTime = (prevActiveTime<=1)?1:prevActiveTime;

       //칼로리
       burningCalories= (int)(3.8*(3.5*user.getWeight()*activeTime)*5/1000);
   }

}
