package com.android.aifoodapp.interfaceh;

import android.graphics.Bitmap;

import com.android.aifoodapp.domain.dailymeal;
import com.android.aifoodapp.domain.fooddata;
import com.android.aifoodapp.domain.meal;
import com.android.aifoodapp.domain.user;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.QueryName;

public interface RetrofitAPI {
    /* user */
    @GET("/checkUserId.do")
    Call<user> getUser(@Query("id") String id);

    @FormUrlEncoded
    @POST("/userSave.do")
    Call<user> postSaveUser(@FieldMap HashMap<String, Object>param);

    @FormUrlEncoded
    @POST("/updatePost.do")
    Call<user> postUpdateUser(@FieldMap HashMap<String, Object>param);

    @GET("/deleteUser.do")
    Call<user> getDeleteUser(@Query("id") String id);

    @GET("/checkDailyMeal.do")
    Call<dailymeal> getDailyMeal(
            @Query("id") String id,
            @Query("datekey") String datekey
    );

    @GET("updateDailyMeal.do")
    Call<Void> getUpdateDailyMeal(
            @Query("id") String id,
            @Query("datekey") String datekey
    );

    @GET("/checkWeeklyMealCalories.do")
    Call<List<Integer>> getWeeklyCalories(
            @Query("id") String id,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate
    );

    @GET("checkWeeklyMeal.do")
    Call<List<dailymeal>> getWeeklyMeal(
            @Query("id") String id,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate
    );

    @FormUrlEncoded
    @POST("/dailymealSave.do")
    Call<Void> postSaveDailyMeal(@FieldMap HashMap<String,Object>param);

    @FormUrlEncoded
    @POST("/mealSave.do")
    Call<Void> postSaveMeal(@FieldMap HashMap<String,Object> param);

    @GET("/selectFoodFromMeal.do")
    Call<List<fooddata>> getFoodFromMeal(@Query("userid") String userid,
                                         @Query("savetime") String savetime,
                                         @Query("timeflag") int timeflag
    );

    @GET("/selectFoodName.do")
    Call<List<fooddata>> getFood(@Query("name") String name);

    @GET("/selectFoodFromFoodName")
    Call<fooddata> getFoodFromFoodName(@Query("name") String name);

    @GET("/InitPositionMeal.do")
    Call<Void> InitPositionMeal(@Query("userid") String userid,
                          @Query("savetime") String savetime,
                          @Query("timeflag") int timeflag
    );

    @GET("/deleteMeal.do")
    Call<Void> deleteMeal(@Query("userid") String userid,
                          @Query("savetime") String savetime,
                          @Query("timeflag") int timeflag
    );

    @GET("/maxTimeFlag.do")
    Call<Integer> getTimeFlag(@Query("userid") String userid,
                              @Query("savetime") String savetime
    );

    @GET("/selectMeal.do")
    Call<List<meal>> getMeal(@Query("userid") String userid, @Query("savetime") String savetime,
                             @Query("timeflag") int timeflag);

    //해당 날짜의 모든 음식 가져오는
    @GET("/selectOneDayFood.do")
    Call<List<meal>> getFoodFromOneDay(@Query("userid") String userid,
                                     @Query("savetime") String savetime
    );

    @GET("/setStepCount.do")
    Call<Void> setStepCount(@Query("userid") String userid,
                            @Query("savetime") String savetime,
                            @Query("stepCount") int stepCount
    );

    //@FormUrlEncoded
    @Multipart
    @POST("/predict")
    //Call<String> postFoodNameFromAI(@Field("file") File file);
    Call<String> postFoodNameFromAI(@Part MultipartBody.Part ImgFile);

    @GET("/recommendMeal.do")
    Call<List<fooddata>> getRecommendMeal (@Query("userid") String userid, @Query("savetime") String savetime);

    @GET("/oneDayMealCount.do")
    Call<Integer> getOneDayMealCount (@Query("userid") String userid, @Query("savetime") String savetime);



}
