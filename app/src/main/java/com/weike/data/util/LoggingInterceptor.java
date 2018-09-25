package com.weike.data.util;

import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.weike.data.WKBaseApplication;
import com.weike.data.business.login.LoginActivity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;

import static okhttp3.internal.http.HttpHeaders.hasBody;

/**
 * Created by LeoLu on 2018/6/15.
 */

public class LoggingInterceptor implements Interceptor {

    private final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {


        Response response=null;



            Request request = chain.request();
            RequestBody requestBody = request.body();
            LogUtil.d("androidTest","链接"+request.url()+"\n");
            String body = null;

            if(requestBody != null) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                body = buffer.readString(charset);

            }


            long startNs = System.nanoTime();
            response = chain.proceed(request);
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

            ResponseBody responseBody = response.body();
            String rBody = null;

            if(hasBody(response)) {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8);
                    } catch (UnsupportedCharsetException e) {
                        e.printStackTrace();
                    }
                }
                rBody = buffer.clone().readString(charset);

            }

            LogUtil.d(Constants.LOG_DATA,"返回体"+rBody);

//            LogUtil.d(Constants.LOG_DATA,"不可用");
//            if(WKBaseApplication.getInstance().activity!=null){
//                Intent intent=new Intent(WKBaseApplication.getInstance(), LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                WKBaseApplication.getInstance().startActivity(intent);
//            }







        return response;
    }
}
