package com.weike.data;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.weike.data.config.Config;
import com.weike.data.network.RetrofitService;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.APP_SERVER).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitService service = retrofit.create(RetrofitService.class);

        Map<String,String> map = new HashMap<>();
        map.put("appKey","android");
        map.put("sign","62e1f24949dfad11db7d22fa59887c72");
        map.put("phoneNumber","15766506263");
        map.put("password","123456");

        service.postData(map,Config.APP_SERVER + "wkzs-restful/userLogin/loginByPassword").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("acthome",response.raw().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("acthome","falied:"+ t.getMessage());
            }
        });



    }


}
