package com.android.aifoodapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.aifoodapp.R;
import com.android.aifoodapp.domain.user;
import com.android.aifoodapp.interfaceh.RetrofitAPI;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InitialSurveyActivity extends AppCompatActivity {

    Activity activity;
    Button btn_survey_yes, btn_survey_no;
    CheckBox cb_target_exist;
    LinearLayout layout_input_target_calorie;
    EditText et_age, et_height, et_weight, et_target_calorie;
    RadioGroup rg_activity_rate;
    RadioButton rb_lv1, rb_lv2, rb_lv3, rb_lv4;
    RadioGroup rg_gender;
    RadioButton rb_man, rb_woman;

    HashMap<String, Integer> survey_result = new HashMap<>();
    HashMap<String, Object> accounts = new HashMap<>();

    //account 정보
    String personName, personGivenName,personEmail,personId ;
    Uri personPhoto ;

    //test
    TextView test;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*juhee modify*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();

            //user account = new user();
            //user(String id, String nickname, char sex, int age, int height, int weight, int activity_index, int target_calories)
        }


        /*juhee modify--fin*/


        setContentView(R.layout.activity_initial_survey);
        initialize();
        setting();
        addListener();

    }

    private void initialize(){
        activity = this;

        btn_survey_yes = findViewById(R.id.btn_survey_yes);
        btn_survey_no = findViewById(R.id.btn_survey_no);
        cb_target_exist= (CheckBox) findViewById(R.id.cb_target_exist);
        layout_input_target_calorie = findViewById(R.id.layout_input_target_calorie);
        et_age = findViewById(R.id.et_age);
        et_height = findViewById(R.id.et_height);
        et_weight = findViewById(R.id.et_weight);
        et_target_calorie = findViewById(R.id.et_target_calorie);
        rg_activity_rate = findViewById(R.id.rg_activity_rate);
        rb_lv1 = findViewById(R.id.rb_lv1);
        rb_lv2 = findViewById(R.id.rb_lv2);
        rb_lv3 = findViewById(R.id.rb_lv3);
        rb_lv4 = findViewById(R.id.rb_lv4);
        rg_gender = findViewById(R.id.rg_gender);
        rb_man = findViewById(R.id.rb_man);
        rb_woman = findViewById(R.id.rb_woman);

        //test
        test = findViewById(R.id.testview2);
    }

    private void setting(){
        et_target_calorie.setText("0");
    }

    private void addListener(){
        btn_survey_yes.setOnClickListener(listener_click_yes);
        btn_survey_no.setOnClickListener(listener_click_no);
        cb_target_exist.setOnClickListener(listener_click_target_exist);
        rg_gender.setOnCheckedChangeListener(listener_select_gender);
        rg_activity_rate.setOnCheckedChangeListener(listener_select_activity_rate);
    }

    private void calc_recommended_calories(){
      /*  기초대사량 = (10×(체중)+6.25×(키)-5.0×나이)+성별(남자 5, 여자 -151)
        하루 권장칼로리 = 자신의 기초대사량 ×활동지수
        활동이 거의 없음	1.3 : 	집에서 활동하는 사람
        낮은 활동성	1.5	: 의자에 앉아서 근무하며 운동을 하지않는 사람
        활동적	1.7	: 매일 운동을 1시간 이상씩 하며 활동적인 사람
        매우 활동적	2.4	: 육체노동직장이나 매일 2시간이상씩 운동하는 사람*/

        int age_point = (survey_result.get("age") == 1)? 5 : -151;
        int basic_calories =
                (int) ((10*(survey_result.get("weight"))
                        +6.25*(survey_result.get("height"))
                        -5.0*survey_result.get("age"))
                        + age_point);

        double activity_point = 0;
        switch(survey_result.get("activity_rate")){
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
        //Log.i("check", ""+recommended_calories);
        survey_result.put("recommended_calories", recommended_calories);

        int gram_of_carbohydrate = (int) (recommended_calories * 0.5 / 4);
        int gram_of_protein = (int) (recommended_calories * 0.3 / 4);
        int gram_of_fat= (int) (recommended_calories * 0.2 / 9);

        survey_result.put("gram_of_carbohydrate", gram_of_carbohydrate);
        survey_result.put("gram_of_protein", gram_of_protein);
        survey_result.put("gram_of_fat", gram_of_fat);
    }

    private View.OnClickListener listener_click_yes = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(! et_age.getText().toString().equals(""))
                survey_result.put("age", Integer.valueOf(et_age.getText().toString()));
            if(! et_height.getText().toString().equals(""))
                survey_result.put("height", Integer.valueOf(et_height.getText().toString()));
            if(! et_weight.getText().toString().equals(""))
                survey_result.put("weight", Integer.valueOf(et_weight.getText().toString()));
            if(! et_target_calorie.getText().toString().equals(""))
                survey_result.put("target_calorie", Integer.valueOf(et_target_calorie.getText().toString()));

            if(survey_result.size() == 6){
                calc_recommended_calories();

                if(et_target_calorie.getText().toString().equals("0"))
                    survey_result.put("target_calorie", survey_result.get("recommended_calories"));
                else
                    survey_result.put("target_calorie", Integer.valueOf(et_target_calorie.getText().toString()));

                char sex = (survey_result.get("gender")==1)?'M':'F';
                user account = new user(personId,personName,sex,survey_result.get("age"),
                        survey_result.get("height"),survey_result.get("weight"),survey_result.get("activity_rate"),survey_result.get("target_calorie"));
                test.setText(personId);

                accounts.put("id",account.getId());
                accounts.put("nickname",account.getNickname());
                accounts.put("sex",account.getSex());
                accounts.put("age",account.getAge());
                accounts.put("height",account.getHeight());
                accounts.put("weight",account.getWeight());
                accounts.put("activity_index",account.getActivity_index());
                accounts.put("target_calories",account.getTarget_calories());

                Toast.makeText(activity, account.pringStirng(), Toast.LENGTH_LONG).show();

                //핸드폰이용하는 경우 -- https://rateye.tistory.com/1082?category=1026651
                //String url = "http://192.168.219.102:8080/userSave.do/"; //juhee
                //avd를 이용하는 경우
                String url = "http://10.0.2.2:8080";

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();

                RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
/*
                retrofitAPI.getData(accounts).enqueue(new Callback<user>() {
                    @Override
                    public void onResponse(Call<user> call, Response<user> response) {
                        if(response.isSuccessful()){
                            Log.d("TestTest","Post 성공");
                            //Log.d("TestTest",data.getNickname());
                        }
                    }

                    @Override
                    public void onFailure(Call<user> call, Throwable t) {
                        Log.d("TestError!!!!","Post 성공");
                    }
                });
*/

                retrofitAPI.postData(accounts).enqueue(new Callback<user>() {
                    @Override
                    public void onResponse(Call<user> call, Response<user> response) {
                        if(response.isSuccessful()){
                            user data = response.body();
                            Log.d("TestTest","Post 성공");
                            Log.d("TestTest",data.getNickname());
                        }
                    }

                    @Override
                    public void onFailure(Call<user> call, Throwable t) {
                        Log.d("TestError!!!!","Post 성공");
                    }
                });


                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("survey_result", (Serializable) survey_result);
                startActivity(intent);
                finish();


            }else{
                //Toast.makeText(activity, "작성되지 않은 항목이 존재합니다!", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                alertDialog.setIcon(R.drawable.alert_icon);
                alertDialog.setTitle("알림");
                alertDialog.setMessage("작성되지 않은 항목이 존재합니다! 모든 항목을 채워주세요.");

                alertDialog.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        //알림창의 확인 버튼 클릭
                    }
                });
                alertDialog.show();
            }

        }
    };

    private View.OnClickListener listener_click_no = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private View.OnClickListener listener_click_target_exist = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(cb_target_exist.isChecked()){
                layout_input_target_calorie.setVisibility(View.VISIBLE);
            }else{
                layout_input_target_calorie.setVisibility(View.GONE);
                et_target_calorie.setText("0");
                survey_result.put("target_calorie", 0);//권장칼로리를 기본으로 설정함
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener listener_select_gender = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            switch(checkedId){
                case R.id.rb_man:
                    survey_result.put("gender", 1);
                    break;
                case R.id.rb_woman:
                    survey_result.put("gender", 2);
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
                case R.id.rb_lv1:
                    survey_result.put("activity_rate", 1);
                    break;
                case R.id.rb_lv2:
                    survey_result.put("activity_rate", 2);
                    break;
                case R.id.rb_lv3:
                    survey_result.put("activity_rate", 3);
                    break;
                case R.id.rb_lv4:
                    survey_result.put("activity_rate", 4);
                    break;
                default:
                    break;
            }
        }
    };



}