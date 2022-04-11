package com.android.aifoodapp.remote;

import com.android.aifoodapp.item.MemberInfoItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RemoteService {
    String BASE_URL="http://49.50.175.225:3000/";
    String idToken=BASE_URL+"/member/";

    @GET("/member/{id}")
    Call<MemberInfoItem> selectMemberInfo(@Path("id") String id);

}
