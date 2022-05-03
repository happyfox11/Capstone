package com.android.aifoodapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.aifoodapp.R;

public class WeeklyReportFragment4 extends Fragment {

    private View view;
    ProgressBar averageActiveTime;
    ProgressBar averageWalk;
    Button recommendBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.weekly_report_fragment_4, container, false);

        initialize();





        return view;
    }

   public void initialize(){
       averageActiveTime = view.findViewById(R.id.averageActiveTime);
       averageWalk = view.findViewById(R.id.averageWalk);
       recommendBtn = view.findViewById(R.id.recommedBtn);

   }


}
