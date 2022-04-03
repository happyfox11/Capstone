package com.android.aifoodapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;

import com.android.aifoodapp.R;

import java.util.Calendar;
import java.util.Date;

public class SelectCalendarDialog extends Dialog {

    public interface OnCalendarDialogClickListener {
        public void onDoneClick(Date selectDate);
    }

    private Activity activity;
    private CalendarView cv_dialog;
    private Button btn_dialog_yes, btn_dialog_no;
    private OnCalendarDialogClickListener onCalendarDialogClickListener;//콜백리스너

    public SelectCalendarDialog(@NonNull Context context){
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.activity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_calendar_dialog);

        initialize();
        setting();
        addListener();
    }

    //변수 초기화
    private void initialize(){
        cv_dialog = findViewById(R.id.cv_dialog);
        btn_dialog_yes = findViewById(R.id.btn_survey_yes);
        btn_dialog_no = findViewById(R.id.btn_survey_no);

    }

    //설정
    private void setting(){}

    //리스너 추가
    private void addListener(){
        cv_dialog.setOnDateChangeListener(listener_change_date);
        btn_dialog_yes.setOnClickListener(listener_dialog_yes);
        btn_dialog_no.setOnClickListener(listener_dialog_no);
    }


    public void setCalendarDialogClickListener(OnCalendarDialogClickListener onCalendarDialogClickListener){
        this.onCalendarDialogClickListener = onCalendarDialogClickListener;
    }

    //확인 버튼 클릭 이벤트
    private View.OnClickListener listener_dialog_yes = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Date date = new Date(cv_dialog.getDate());
            long now = System.currentTimeMillis();
            Date today = new Date(now);

            // 미래 날짜 선택 시, 알림창 발생
            if(date.compareTo(today) == 1){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                alertDialog.setIcon(R.drawable.alert_icon);
                alertDialog.setTitle("알림");
                alertDialog.setMessage("과거의 기록만 확인하실 수 있습니다. 날짜를 다시 선택해주세요.");

                alertDialog.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        //알림창의 확인 버튼 클릭
                    }
                });
                alertDialog.show();

            }else{
                dismiss();

                if(onCalendarDialogClickListener != null) {
                    onCalendarDialogClickListener.onDoneClick(date);
                }
            }

        }
    };

    //취소 버튼 클릭 이벤트
    private View.OnClickListener listener_dialog_no = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            dismiss();
        }
    };

    private CalendarView.OnDateChangeListener listener_change_date = new CalendarView.OnDateChangeListener(){
        @Override
        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth){
            Calendar calendar = Calendar.getInstance();

            calendar.set(year, month, dayOfMonth);
            cv_dialog.setDate(calendar.getTimeInMillis());
        }
    };
}