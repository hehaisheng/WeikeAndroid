package com.weike.data.network;

import com.weike.data.base.BaseReq;
import com.weike.data.base.BaseResp;
import com.weike.data.model.resp.LoginByCodeResp;
import com.weike.data.model.resp.LoginByPwdResp;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by LeoLu on 2018/5/22.
 */

public interface RetrofitService<T> {


    @POST
    public Observable<ResponseBody> postAnything(@Body Object data, @Url String url);


    @Multipart
    @POST
    public Observable<ResponseBody> postFile(@Part("description") ResponseBody des,@Part("file") RequestBody file);
}
