package com.android.aifoodapp.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
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

public class WeeklyReportFragment4 extends Fragment {

    private View view;
    ProgressBar averageActiveTime;
    ProgressBar averageWalk;
    TextView tv_activeTime, tv_walk, activeTimeInfo,burningCaloriesInfo , calculateActiveTime, activeTimeComment,
    protein_comment, fat_comment, exercise_comment;

    Button recommendBtn;

    int activeTime = 30;
    int walkCount = 3000;
    int burningCalories = 132;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.weekly_report_fragment_4, container, false);

        initialize();

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

        return view;
    }

   public void initialize(){
       averageActiveTime = view.findViewById(R.id.averageActiveTime);
       averageWalk = view.findViewById(R.id.averageWalk);
       tv_activeTime = view.findViewById(R.id.tv_activeTme);
       tv_walk = view.findViewById(R.id.tv_walk);
       recommendBtn = view.findViewById(R.id.recommedBtn);
       activeTimeInfo = view.findViewById(R.id.activeTimeInfo);
       burningCaloriesInfo = view.findViewById(R.id.buringCaloriesInfo);
       calculateActiveTime = view.findViewById(R.id.calculateActiveTime);
       activeTimeComment = view.findViewById(R.id.activeTimeComment);
       protein_comment = view.findViewById(R.id.protein_comment);
       fat_comment = view.findViewById(R.id.fat_comment);
       exercise_comment = view.findViewById(R.id.exercise_comment);

   }


}
