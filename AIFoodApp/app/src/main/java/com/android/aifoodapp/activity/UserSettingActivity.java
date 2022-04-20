package com.android.aifoodapp.activity;

import static com.android.aifoodapp.interfaceh.baseURL.url;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.aifoodapp.R;
import com.android.aifoodapp.domain.user;
import com.android.aifoodapp.interfaceh.NullOnEmptyConverterFactory;
import com.android.aifoodapp.interfaceh.RetrofitAPI;
import com.bumptech.glide.Glide;
import com.kakao.sdk.user.UserApiClient;

import java.io.IOException;
import java.util.HashMap;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserSettingActivity extends AppCompatActivity {

    Activity activity;
    ImageView iv_profile;
    EditText tv_userName;
    EditText tv_userIdInfo;
    Button btn_modifyInfo, btn_withdrawal, btn_bmi_modify;
    EditText tv_age;
    EditText tv_height;
    EditText tv_kg;
    EditText tv_bmi;
    EditText tv_sex;
    RadioGroup rg_modify_activity_rate, rg_modify_gender;
    RadioButton rb_modify_lv1, rb_modify_lv2, rb_modify_lv3, rb_modify_lv4, rb_modify_man, rb_modify_woman;
    HashMap<String, Object> accounts = new HashMap<>();
    private int rb_activity_rate,modify_gender;
    private String flag;

    com.android.aifoodapp.domain.user user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersetting);
        initialize();

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        settingText();

        rg_modify_gender.setOnCheckedChangeListener(listener_modify_gender);
        rg_modify_activity_rate.setOnCheckedChangeListener(listener_select_activity_rate);

        /*목표 칼로리 자동 계산*/
        btn_bmi_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int gender_point = (modify_gender == 1)? 5 : -151;
                int basic_calories =
                        (int) ((10*(Integer.parseInt(tv_kg.getText().toString()))
                                +6.25*(Integer.parseInt(tv_height.getText().toString()))
                                -5.0*(Integer.valueOf(tv_age.getText().toString())))
                                + gender_point);

                double activity_point = 0;
                switch(rb_activity_rate){
                    case 1:
                        activity_point = 1.3;
                        break;
                    case 2:
                        activity_point = 1.5;
                        break;
                    case 3:
                        activity_point = 1.7;
                        break;
                    case 4:
                        activity_point = 2.4;
                        break;
                    default:
                        break;
                }

                int recommended_calories = (int) (basic_calories * activity_point);
                tv_bmi.setText(String.valueOf(recommended_calories));
            }
        });

        //TODO 회원탈퇴
        btn_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                flag=intent.getStringExtra("flag");

                if(flag.equals("kakao")){
                    new AlertDialog.Builder(UserSettingActivity.this).setMessage("회원 탈퇴 하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {

                                @Override
                                // 카카오 연결 끊기
                                public void onClick(DialogInterface dialog, int which) {
                                    UserApiClient.getInstance().unlink(new Function1<Throwable, Unit>() {
                                        @Override
                                        public Unit invoke(Throwable throwable) {
                                            //db에서 해당 id 지워야 함
                                            Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();

                                            RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

                                            retrofitAPI.getDeleteUser(tv_userIdInfo.getText().toString()).enqueue(new Callback<user>() {
                                                @Override
                                                public void onResponse(Call<user> call, Response<user> response) {
                                                    if(response.isSuccessful()){
                                                        Log.d("delete user","Post 성공");
                                                    }
                                                    else{
                                                        String body = response.errorBody().toString();
                                                        Log.d("delete user", "error - body : " + body);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<user> call, Throwable t) {
                                                    Log.d("postUpdateUser","Post 실패 ");
                                                }
                                            });

                                            MainActivity MA = (MainActivity)MainActivity._MainActivity; // https://itun.tistory.com/357 [Bino]
                                            MA.finish();

                                            Intent intent = new Intent(activity, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                            return null;
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
                else if(flag.equals("google")){

                }
            }
        });


        btn_modifyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                alertDialog.setIcon(R.drawable.alert_icon);
                alertDialog.setTitle("알림");
                alertDialog.setMessage("수정하시겠습니까?");

                alertDialog.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        String personId=tv_userIdInfo.getText().toString();
                        String personName=tv_userName.getText().toString();
                        char sex=(modify_gender==1)?'M':'F';
                        int age=Integer.parseInt(tv_age.getText().toString());
                        int height=Integer.parseInt(tv_height.getText().toString());
                        int weight=Integer.parseInt(tv_kg.getText().toString());
                        int activity_rate=rb_activity_rate;
                        int target_calorie=Integer.parseInt(tv_bmi.getText().toString());
                        String personPhoto=user.getProfile();


                        user account = new user(personId,personName,sex,age,
                                height,weight,activity_rate,target_calorie,personPhoto);

                        accounts.put("id",account.getId());
                        accounts.put("nickname",account.getNickname());
                        accounts.put("sex",account.getSex());
                        accounts.put("age",account.getAge());
                        accounts.put("height",account.getHeight());
                        accounts.put("weight",account.getWeight());
                        accounts.put("activity_index",account.getActivity_index());
                        accounts.put("target_calories",account.getTarget_calories());
                        accounts.put("profile",account.getProfile());


                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();

                        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

                        retrofitAPI.postUpdateUser(accounts).enqueue(new Callback<user>() {
                            @Override
                            public void onResponse(Call<user> call, Response<user> response) {
                                if(response.isSuccessful()){
                                    Log.d("Modify userInfo","Post 성공");
                                }
                                else{
                                    String body = response.errorBody().toString();
                                    Log.d("Modify userInfo", "error - body : " + body);
                                }
                            }

                            @Override
                            public void onFailure(Call<user> call, Throwable t) {
                                Log.d("postUpdateUser","Post 실패 ");
                            }
                        });

                        MainActivity MA = (MainActivity)MainActivity._MainActivity; // https://itun.tistory.com/357 [Bino]
                        MA.finish();
                        Intent intent = new Intent(activity, LoginActivity.class);
                        //수정 후 user 정보 업데이트를 하고 새로 user정보를 가져오기 위해 다시 LoginActivity
                        intent.putExtra("load",true);

                        startActivity(intent);
                        finish();
                    }
                });
                alertDialog.show();

            }
        });

    }

    private void settingText(){
        if(user.getProfile().equals("")) {  }
        else {
            Glide.with(iv_profile).load(user.getProfile()).circleCrop().into(iv_profile);
            Uri uri = Uri.parse(user.getProfile());
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                iv_profile.setImageBitmap(bm);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //iv_profile.setImageURI(Uri.parse(user.getProfile()));
        }
        tv_userName.setText(user.getNickname());
        tv_userIdInfo.setText(user.getId());
        //btn_modifyInfo
        //btn_withdrawal
        tv_age.setText(String.valueOf(user.getAge()));
        tv_height.setText(String.valueOf(user.getHeight()));
        tv_kg.setText(String.valueOf(user.getWeight()));
        tv_bmi.setText(String.valueOf(user.getTarget_calories()));


        modify_gender=(user.getSex()=='M')?1:2;
        switch(modify_gender){
            case 1:
                rb_modify_man.setChecked(true);
                break;
            case 2:
                rb_modify_woman.setChecked(true);
                break;
            default:
                break;
        }

        rb_activity_rate=user.getActivity_index();
        switch(rb_activity_rate){
            case 1:
                rb_modify_lv1.setChecked(true);
                break;
            case 2:
                rb_modify_lv2.setChecked(true);
                break;
            case 3:
                rb_modify_lv3.setChecked(true);
                break;
            case 4:
                rb_modify_lv4.setChecked(true);
                break;
            default:
                break;
        }

    }

    //변수 초기화
    private void initialize(){
        activity = this;
        iv_profile = (ImageView) findViewById(R.id.iv_profile);
        tv_userName = findViewById(R.id.tv_userName);
        tv_userIdInfo = findViewById(R.id.tv_userIdInfo);
        btn_modifyInfo = findViewById(R.id.btn_modifyInfo);
        btn_withdrawal=findViewById(R.id.btn_withdrawal);
        tv_age = findViewById(R.id.tv_age);
        tv_height = findViewById(R.id.tv_height);
        tv_kg = findViewById(R.id.tv_kg);
        tv_bmi = findViewById(R.id.tv_bmi);

        rg_modify_gender=findViewById(R.id.rg_modify_gender);
        rb_modify_man=findViewById(R.id.rb_modify_man);
        rb_modify_woman=findViewById(R.id.rb_modify_woman);

        rg_modify_activity_rate = findViewById(R.id.rg_modify_activity_rate);
        rb_modify_lv1 = findViewById(R.id.rb_modify_lv1);
        rb_modify_lv2 = findViewById(R.id.rb_modify_lv2);
        rb_modify_lv3 = findViewById(R.id.rb_modify_lv3);
        rb_modify_lv4 = findViewById(R.id.rb_modify_lv4);

        btn_bmi_modify=findViewById(R.id.btn_bmi_modify);

    }

    private RadioGroup.OnCheckedChangeListener listener_modify_gender = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            switch(checkedId){
                case R.id.rb_modify_man:
                    modify_gender=1;
                    break;
                case R.id.rb_modify_woman:
                    modify_gender=2;
                    break;
                default:
                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener listener_select_activity_rate = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            switch(checkedId){
                case R.id.rb_modify_lv1:
                    rb_activity_rate=1;
                    break;
                case R.id.rb_modify_lv2:
                    rb_activity_rate=2;
                    break;
                case R.id.rb_modify_lv3:
                    rb_activity_rate=3;
                    break;
                case R.id.rb_modify_lv4:
                    rb_activity_rate=4;
                    break;
                default:
                    break;
            }
        }
    };
}
