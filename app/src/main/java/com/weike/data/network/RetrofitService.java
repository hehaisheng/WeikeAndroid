package com.weike.data.network;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by LeoLu on 2018/5/22.
 */

public interface RetrofitService<T> {

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST
    public Observable<ResponseBody> postAnything(@Body Object data, @Url String url);



    @Multipart
    @POST
    Observable<ResponseBody> uploadAvatar(@Part MultipartBody.Part imgs,@Url String url);


    @Multipart
    @POST("userOss/upload.do")
    rx.Observable<ResponseBody> uploadImages3(@Part MultipartBody.Part[] files);


    @Multipart
    @POST
    Observable<ResponseBody> uploadFile(@Part MultipartBody.Part file,@Url String url);
}
