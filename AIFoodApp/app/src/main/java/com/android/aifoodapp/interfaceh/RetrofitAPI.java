package com.android.aifoodapp.interfaceh;

import com.android.aifoodapp.domain.user;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitAPI {
    @GET("/checkUserId.do")
    Call<user> getUser(@Query("id") String id);

    @FormUrlEncoded
    @POST("/userSave.do")
    Call<user> postSaveUser(@FieldMap HashMap<String, Object>param);

}
