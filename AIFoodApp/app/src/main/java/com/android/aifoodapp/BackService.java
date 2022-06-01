package com.android.aifoodapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.aifoodapp.activity.LoginActivity;
import com.android.aifoodapp.domain.dailymeal;
import com.android.aifoodapp.domain.user;
import com.android.aifoodapp.interfaceh.RetrofitAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.android.aifoodapp.interfaceh.baseURL.url;

import static java.lang.Thread.sleep;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class BackService extends Service implements SensorEventListener {

    private static final String TAG = "서비스";
    SensorManager sensorManager;
    Sensor stepCountSensor;
    user user;
    dailymeal dailymeal;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    int currentSteps=0;

    NotificationCompat.Builder NCBuilder;

    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 화면 Activity 사이에서 데이터를 주고받을 때
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        //23:59:59에 초기화
        Calendar resetCal = Calendar.getInstance();
        resetCal.setTimeInMillis(System.currentTimeMillis());
        resetCal.set(Calendar.HOUR_OF_DAY,23);
        resetCal.set(Calendar.MINUTE,59);
        resetCal.set(Calendar.SECOND,59);
        Timer time = new Timer();
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                Date now = new Date();
                currentSteps=0;
                Log.d("reset 성공" + now.toString(),Integer.toString(currentSteps));
            }
        };

        time.schedule(timerTask, new Date(resetCal.getTimeInMillis()),1000*60*60*24);
        //1000*60*60*24
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "서비스 시작");

        if(intent == null){
            return Service.START_STICKY; // 서비스가 종료 되었을 때도 다시 자동으로 실행 함.
        }else{
            processCommand(intent); //user 정보 가져옴
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // PendingIntent를 이용하면 포그라운드 서비스 상태에서 알림을 누르면 앱의 LoginActivity를 다시 열게 된다.
            PendingIntent mPendingIntent = PendingIntent.getActivity(
                    getApplicationContext(),
                    0, // 보통 default값 0을 삽입
                    new Intent(getApplicationContext(), LoginActivity.class),
                    PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE
            );

            NotificationManager NoManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("walk", "Service", NotificationManager.IMPORTANCE_LOW);// 알림음 발생 --> 알림음 발생하지 않도록 변경
            channel.enableLights(true);     //채널에 게시 된 알림에 알림 표시 등을 표시
            channel.setLightColor(Color.RED);

            NoManager.createNotificationChannel(channel);
            NCBuilder = new NotificationCompat.Builder(this, "walk");
            NCBuilder.setSmallIcon(R.drawable.burning_icon);
            NCBuilder.setContentTitle("걸음수 측정");
            NCBuilder.setContentText("현재 걸음수 : " + currentSteps);
            NCBuilder.setOngoing(true);
            NCBuilder.setContentIntent(mPendingIntent);
            NCBuilder.setAutoCancel(true);


            Notification notification = NCBuilder.build();

            //현재 노티피케이션 메시지를 포그라운드 서비스의 메시지로 등록
            startForeground(10, notification);
            // 활동 퍼미션 체크
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            }

            // 걸음 센서 연결
            // * 옵션
            // - TYPE_STEP_DETECTOR:  리턴 값이 무조건 1, 앱이 종료되면 다시 0부터 시작
            // - TYPE_STEP_COUNTER : 앱 종료와 관계없이 계속 기존의 값을 가지고 있다가 1씩 증가한 값을 리턴
            //
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

            // 디바이스에 걸음 센서의 존재 여부 체크
            if (stepCountSensor == null) {
                Toast.makeText(this, "No Step Sensor", Toast.LENGTH_SHORT).show();
            }
            if (stepCountSensor != null) {
                // 센서 속도 설정
                // * 옵션
                // - SENSOR_DELAY_NORMAL: 20,000 초 딜레이
                // - SENSOR_DELAY_UI: 6,000 초 딜레이
                // - SENSOR_DELAY_GAME: 20,000 초 딜레이
                // - SENSOR_DELAY_FASTEST: 딜레이 없음
                //
                sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_FASTEST);
            }
        }

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "서비스 종료");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);

                //완료되면 노티피케이션 메시지 사라지게
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    stopForeground(STOP_FOREGROUND_REMOVE);
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.cancel(10);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        super.onDestroy();
    }

    private void processCommand(Intent intent){
        //MainActivity에서 user정보를 받아옴!
        user = intent.getParcelableExtra("user");
        dailymeal = intent.getParcelableExtra("dailymeal");
        currentSteps = dailymeal.getStepcount();
        //Log.e("user",user.getNickname());
        //자정 동작 ( 아래걸로 하면 메인 에서 동작해서 앞에서 보임)
        //resetAlarm(getApplicationContext());
        //resetAlarm(getBaseContext());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {

            if (event.values[0] == 1.0f) {
                // 센서 이벤트가 발생할때 마다 걸음수 증가
                currentSteps++;
                NCBuilder.setContentText("현재 걸음수 : " + currentSteps);
                Notification notification = NCBuilder.build();

                //현재 노티피케이션 메시지를 포그라운드 서비스의 메시지로 등록
                startForeground(10, notification);

                /* ~ 시간에 DB 저장? */
                // 이 코드는  onSensorChanged 함수내부에 짜여진 코드이기 때문에 sensor 변화가 있을때만 해당 코드가 작동한다.
                //TODO 원하는 시간에 DB에 저장할 수 있게 해야 하는데.
                //지금 currentSteps가 하나씩 증가할때마다 db에 저장되고 있음..ㅎㅎ

                Date now = new Date(); //Date타입으로 변수 선언
                String date_string = dateFormat.format(now);
                String time_string = timeFormat.format(now);//HH:mm:ss

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

                //Log.e("time",time_string);
                //if(time_string.equals("13:35:30")){
                    retrofitAPI.setStepCount(user.getId(),date_string,currentSteps).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.e("걸음수 저장 ","완");
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("걸음수 저장 ",t.toString());
                        }
                    });
                //}
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public void resetAlarm(Context context){

        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent resetIntent = new Intent(context, BroadcastReceive.class);
        //sendBroadcast(resetIntent);
        // midnight
        Calendar resetCal = Calendar.getInstance();
        resetCal.setTimeInMillis(System.currentTimeMillis());
        resetCal.set(Calendar.HOUR_OF_DAY,0);
        resetCal.set(Calendar.MINUTE,0);
        resetCal.set(Calendar.SECOND,0);

        //dailymeal을 새로만들고 거기에 저장하도록 해야함 (이거 서버에서 해야하나.. 일단 그냥 안드로이드에서 처리)
        //그냥 메인에서 다시 작동 하도록 설정 dailymeal이 새로 생성 될것
        PendingIntent resetSender = PendingIntent.getBroadcast(context,0,resetIntent,0);


        //0시에 작동 24시를 뜻하는 AlarmManager.INTERVAL_DAY를 더한다ㅇ
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,resetCal.getTimeInMillis()+AlarmManager.INTERVAL_DAY,AlarmManager.INTERVAL_DAY,resetSender);
        //manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,resetCal.getTimeInMillis(),60*1000,resetSender);//1분 마다 반복 이거 이용해서 한시간 단위 저장 가능

        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.d("resetAlarm","ResetHour: "+datetimeFormat.format(new Date(resetCal.getTimeInMillis()+AlarmManager.INTERVAL_DAY)));

    }


}