package com.android.aifoodapp.activity;

import static com.android.aifoodapp.interfaceh.baseURL.url;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.aifoodapp.ProgressDialog;
import com.android.aifoodapp.R;
import com.android.aifoodapp.RecyclerView.FoodInfo;
import com.android.aifoodapp.RecyclerView.FoodItem;
import com.android.aifoodapp.RecyclerView.FoodItemAdapter;
import com.android.aifoodapp.RecyclerView.FoodInfoAdapter;
import com.android.aifoodapp.domain.dailymeal;
import com.android.aifoodapp.domain.fooddata;
import com.android.aifoodapp.domain.meal;
import com.android.aifoodapp.domain.user;
import com.android.aifoodapp.domain.dailymeal;
import com.android.aifoodapp.interfaceh.NullOnEmptyConverterFactory;
import com.android.aifoodapp.interfaceh.RetrofitAPI;
import com.android.aifoodapp.vo.ReportDaySubItemVo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class FoodAnalysisActivity extends AppCompatActivity {

    public static Activity _FoodAnalysis_Activity;
    public static Context mContext;
    public Bitmap compressedBitmap, tmpBitmap;
    Activity activity;

    ArrayList<FoodItem> foodItemList = new ArrayList<>();
    ArrayList<FoodInfo> foodInfoList = new ArrayList<>();

    Bitmap bitmap = null;
    FoodItemAdapter foodItemAdapter;
    FoodInfoAdapter foodInfoAdapter;
    RecyclerView recyclerView ,recyclerView2;
    TextView tv_foodName;
    ImageView iv_plusBtn, iv_foodAnalysis;
    Button btn_insert_dailymeal;
    user user;
    dailymeal dailymeal;
    ArrayList<fooddata> foodList=new ArrayList<>(); //기존에 담아두었던 식단 목록
    List<meal> ml;
    ArrayList<Double> intakeList=new ArrayList<>(); //meal에서 불러온값
    ArrayList<Double> intakeNew=new ArrayList<>(); //새롭게 저장되어야 하는 intake값
    public ArrayList<String> photoList=new ArrayList<>();
    //HashMap<String, List<meal>> map = new HashMap<>();
    HashMap<String, Object> map = new HashMap<>();
    HashMap<String, Object> dailyMap = new HashMap<>();
    int cnt=0;
    byte[] byteArray;

    int pos;
    String userid, mealname, mealphoto, savetime, handActivity, imgPath;
    long dailymealid, mealid, fooddataid;
    int calorie, protein, carbohydrate, fat, timeflag;
    double intake=1.0;
    Uri photoAI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_analysis);

        mContext = this;
        Intent intent = getIntent();
        foodList=intent.getParcelableArrayListExtra("foodList");
        dailymeal=intent.getParcelableExtra("dailymeal");
        pos=intent.getIntExtra("position",0);
        //byte[] byteArray=intent.getByteArrayExtra("image");
        //photoList=(ArrayList<String>) intent.getSerializableExtra("photoList");
        photoAI=intent.getParcelableExtra("photoAI");//photo의 uri 받아오기
        handActivity=intent.getStringExtra("activity");
        int modifyPosition=intent.getIntExtra("modify",-1);
        imgPath=intent.getStringExtra("imgPath");

        initialize();
        //setFoodList();
        _FoodAnalysis_Activity = FoodAnalysisActivity.this;

        //https://andro-jinu.tistory.com/entry/androidstudio2
        ProgressDialog dialog = new ProgressDialog(this);//loading
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));//배경투명하게
        dialog.setCanceledOnTouchOutside(false); //주변 터치 방지
        dialog.setCancelable(false);
        dialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        //기존에 이미 저장된 meal 불러오기 : intake와 사진 불러오기 위함
        try{
            ml=new getMealNetworkCall().execute(retrofitAPI.getMeal(dailymeal.getUserid(),dailymeal.getDatekey(),pos)).get();
            for (meal repo : ml) {
                intakeList.add(repo.getIntake());
                photoList.add(repo.getMealphoto());
            }
            Log.e("mealCall","완료");
            Log.e("photoList",photoList.toString());

            if(photoAI!=null){
                Log.e("fffff","AI값이 넘어옴");
                intakeList.add(1.0);

                /* 사진 회전 현상 해결 */
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(imgPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), photoAI);
                    bitmap = rotateBitmap(bitmap, orientation);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bitmap bi = getCompressedBitmap(bitmap);
                //bitmap을 string으로
                String bitmapAI = Base64.encodeToString(byteArray, Base64.DEFAULT);
                String temp="";
                try{//인코딩 된 데이터를 한번더 utf-8로 인코딩
                    //temp="&imagedevice="+ URLEncoder.encode(bitmapAI,"UTF-8");
                    temp=URLEncoder.encode(bitmapAI,"utf-8");
                }catch(Exception e){

                }
                photoList.add(temp);
            }
            dialog.dismiss();//데이터 다 불러오면 dialog 화면 해제

        }catch(Exception e){
            e.printStackTrace();
        }

        int k=0;
        intakeNew=(ArrayList<Double>) intent.getSerializableExtra("intakeNew");

        Log.e("photo",Integer.toString(photoList.size()));
        Log.e("food",Integer.toString(foodList.size()));
        Log.e("intake",Integer.toString(intakeList.size()));


        //새롭게 수기 입력을 했을 경우 반복문 실행해서 list 추가됨
        for(int i=photoList.size();i<foodList.size();i++){
            photoList.add("");
            intakeList.add(intakeNew.get(k));
            k++;
        }

        if(modifyPosition!=-1){
            intakeList.set(modifyPosition,1.0);
        }

        /* 사진 Bitmap으로 변경 */
        if(!foodList.isEmpty()){
            int cnt=0;
            for(fooddata repo : foodList){
                //if(byteArray!=null && cnt==foodList.size()-1){
                if(photoList.get(cnt).equals("")){
                    //String img = String.valueOf(R.drawable.ic_launcher_background); //기본 사진
                    //String img=String.valueOf(R.drawable.icon);
                    Drawable drawable = getResources().getDrawable(R.drawable.icon);
                    compressedBitmap = ((BitmapDrawable)drawable).getBitmap();
                    tmpBitmap=((BitmapDrawable)drawable).getBitmap();
                    //Log.e("ㅎㅎ1",compressedBitmap.toString());
                }
                else{
                    //String을 Bitmap으로
                    String temp="";
                    try{
                        temp= URLDecoder.decode(photoList.get(cnt),"UTF-8");
                    }catch(UnsupportedEncodingException e){
                        e.printStackTrace();
                    }

                    byte[] decodeByte = Base64.decode(temp, Base64.DEFAULT);
                    compressedBitmap = BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
                }
                foodItemList.add(new FoodItem(compressedBitmap,R.drawable.minusbtn,repo.getName()));
                foodInfoList.add(new FoodInfo(repo, compressedBitmap, intakeList.get(cnt))); //음식객체, 이미지, 인분
                cnt++;
            }
        }
        else{
            foodList=new ArrayList<>();
        }

        //어댑터 연결
        foodItemAdapter = new FoodItemAdapter(foodItemList);
        recyclerView.setAdapter(foodItemAdapter);

        //2번째 어댑터 연결
        foodInfoAdapter = new FoodInfoAdapter(foodInfoList);
        recyclerView2.setAdapter(foodInfoAdapter);

        setInfoRecyclerViewHeight(recyclerView2);
        foodItemAdapter.setItemClickListener(new FoodItemAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(FoodItem item) {
                tv_foodName.setText(item.getFl_foodName());
                iv_foodAnalysis.setImageBitmap(item.getFl_image());
            }
            @Override
            public void onRemoveButtonClicked(int position) {
                foodInfoAdapter.removeById(position);
                foodList.remove(position);
                intakeList.remove(position);
                photoList.remove(position);
                setInfoRecyclerViewHeight(recyclerView2);
            }
        });

        foodInfoAdapter.setItemClickListener(new FoodInfoAdapter.ItemClickListener() {
            @Override
            public void onIntakeChangeClicked(double cl_intake, int position){
                intakeList.set(position,cl_intake);
            }

            @Override
            public void modifyFoodClicked(int position){//position:현재 food 위치
                Intent intent = new Intent(activity, FoodInputActivity.class);
                intent.putExtra("dailymeal",dailymeal);
                intent.putParcelableArrayListExtra("foodList",foodList);
                intent.putExtra("position",pos);//timeflag를 의미
                intent.putExtra("intakeNew",intakeNew);
                //intent.putExtra("photoList",photoList);
                if(photoAI!=null) {
                    intent.putExtra("photoAI",photoAI);
                    intent.putExtra("imgPath",imgPath);
                }
                intent.putExtra("modify",position);
                startActivity(intent);
            }
        });

        iv_plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FoodInputActivity.class);
                intent.putExtra("dailymeal",dailymeal);
                intent.putParcelableArrayListExtra("foodList",foodList);
                intent.putExtra("position",pos);
                intent.putExtra("intakeNew",intakeNew);
                if(photoAI!=null) {
                    intent.putExtra("photoAI",photoAI);
                    intent.putExtra("imgPath",imgPath);
                }
                //intent.putExtra("photoList",photoList);
                startActivity(intent);
                setInfoRecyclerViewHeight(recyclerView2);
            }
        });

        //meal과 dailymeal에 입력
        btn_insert_dailymeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //데이트 포맷(sql)

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();

                RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

                userid = dailymeal.getUserid();
                savetime = dailymeal.getDatekey();
                timeflag = pos;

                /* 먼저 현재 postion에 이미 저장되어 있던 meal 데이터 전체 삭제 (업데이트를 위함) */
                retrofitAPI.deleteMeal(userid,savetime,timeflag).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()) {
                            Log.e("11111111111111","삭제완료");

                            Log.e("foodList",foodList.toString());
                            Log.e("photoList",photoList.toString());
                            //이걸 하나씩 저장하는게 아니라 foodList 전체를 retrofit으로 보낼 수 있는 방법이 없을까 ㅠㅠㅠㅠㅠㅠㅠ
                            for(fooddata repo : foodList){
                                userid = dailymeal.getUserid();
                                dailymealid = dailymeal.getDailymealid();
                                mealid=Integer.parseInt(Long.toString(dailymeal.getDailymealid())+Integer.toString(pos)+Integer.toString(cnt));
                                //mealid : dailymealid + timeflag + cnt
                                calorie=(int)repo.getCalorie();
                                protein=(int)repo.getProtein();
                                carbohydrate=(int)repo.getCarbohydrate();
                                fat=(int)repo.getFat();
                                mealname=repo.getName();
                                mealphoto=photoList.get(cnt);
                                //mealphoto="";
                                // new byte[] { 0x01, 0x02, 0x03 };
                                savetime=dailymeal.getDatekey();//해당 달력 날짜(과거날짜에서 데이터 추가하는 경우도 있기 때문)
                                //savetime = dateFormat.format(now); //날짜가 string으로 저장
                                //savetime=now;//형식없이 괜찮나?
                                timeflag=pos;//끼니별 식단 구분용으로? //main화면에서 list 식단 위치
                                fooddataid=repo.getId();
                                intake=intakeList.get(cnt);

                                Log.e("userid",userid);

                                meal meal = new meal(userid,dailymealid,mealid,calorie,protein,carbohydrate,fat,mealname,mealphoto,savetime,timeflag,fooddataid,intake);
                                map.put("userid",meal.getUserid());
                                map.put("dailymealid",meal.getDailymealid());
                                map.put("mealid",meal.getMealid());
                                map.put("calorie",meal.getCalorie());
                                map.put("protein",meal.getProtein());
                                map.put("carbohydrate",meal.getCarbohydrate());
                                map.put("fat",meal.getFat());
                                map.put("mealname",meal.getMealname());
                                map.put("mealphoto",meal.getMealphoto());
                                map.put("savetime",meal.getSavetime());
                                map.put("timeflag",meal.getTimeflag());
                                map.put("fooddataid",meal.getFooddataid());
                                map.put("intake",meal.getIntake());

                                cnt++;

                                //mealList.add(meal);

                                /* meal 데이터 저장 */
                                retrofitAPI.postSaveMeal(map).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if(response.isSuccessful()) {
                                            Log.e("222222222222222222","추가완료");
                                            Log.e("cnt",Integer.toString(cnt));
                                            if(foodList.size()==cnt){
                                                /* dailymeal 업데이트. */
                                                retrofitAPI.getUpdateDailyMeal(userid,savetime).enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                                        if(response.isSuccessful()) {
                                                            Log.e("333333333333333","dailymeal 업데이트");

                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Void> call, Throwable t) {
                                                        Log.e("1","call 실패"+t);
                                                    }
                                                });

                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.e("2","call 실패"+t);
                                    }
                                });
                            }

                            if(foodList.isEmpty()){
                                retrofitAPI.getUpdateDailyMeal(userid,savetime).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if(response.isSuccessful()) {
                                            Log.e("333333333333333","dailymeal 업데이트");

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.e("3","call 실패"+t);
                                    }
                                });
                            }

                        }
                        else{
                            Log.e("♥","삭제실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("4","call 실패"+t);
                    }
                });

                Intent intent = new Intent(activity, LoginActivity.class);
                intent.putExtra("load",true);
                startActivity(intent);
                finish();
            }
        });
    }

    //https://whereisusb.tistory.com/30
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
        alert_ex.setMessage("작성을 그만두시겠습니까?");

        alert_ex.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.putExtra("load",true);
                startActivity(intent);
                finish();
            }
        });
        alert_ex.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert_ex.setTitle("알림");
        AlertDialog alert = alert_ex.create();
        alert.show();

    }

    //변수 초기화
    private void initialize(){
        activity = this;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView2);
        iv_plusBtn=findViewById(R.id.iv_plusBtn);
        tv_foodName = findViewById(R.id.tv_foodName);
        iv_foodAnalysis = findViewById(R.id.iv_foodAnalysis);
        btn_insert_dailymeal = findViewById(R.id.btn_insert_dailymeal);
    }

    public void setInfoRecyclerViewHeight(RecyclerView recyclerView) {

        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();

        int k = recyclerView.getAdapter().getItemCount();
        params.height = (int) getResources().getDimension(R.dimen.food_analysis_sq_item_size) * k;
        recyclerView.requestLayout();
    }

    private Bitmap getCompressedBitmap(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);//사진 사이즈 200*200로 줄임
        bitmap.compress(Bitmap.CompressFormat.PNG,60, stream);//60% 압축
        byteArray = stream.toByteArray();
        compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

        return compressedBitmap;
    }

    //https://stackoverflow.com/questions/20478765/how-to-get-the-correct-orientation-of-the-image-selected-from-the-default-image
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    //AsyncTask 처리
    private class getMealNetworkCall extends AsyncTask<Call, Void, List<meal> > {
        @Override
        protected List<meal> doInBackground(Call[] params) {
            try {
                Call<List<meal>> call = params[0];
                Response<List<meal>> response = call.execute();
                return response.body();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
