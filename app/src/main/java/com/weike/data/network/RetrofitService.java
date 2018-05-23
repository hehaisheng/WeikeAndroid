package com.weike.data.network;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by LeoLu on 2018/5/22.
 */

public interface RetrofitService {


    @POST
    public Call<ResponseBody> postData(@Body Object data, @Url String url);

}
