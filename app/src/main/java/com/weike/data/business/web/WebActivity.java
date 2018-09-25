package com.weike.data.business.web;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.AgreeRequest;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.Constants;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LeoLu on 2018/6/19.
 */

public class WebActivity extends BaseActivity {

    @BindView(R.id.web_view)
    public WebView webView;


    public String model;
    public TextView textView;
    public boolean  isChangeSelecting=false;



    public static void startActivity(Activity activity , String url) {
        Intent intent = new Intent(activity,WebActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("url",url);
        intent.putExtra("model","normal");
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity , String url,String local) {
        Intent intent = new Intent(activity,WebActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("url",url);
        intent.putExtra("model",local);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        setRightText("");
        setCenterText("");
        setLeftText("");
        model=getIntent().getStringExtra("model");
        if(model.equals("local")){
            textView=findViewById(R.id.bottom);
            textView.setVisibility(View.VISIBLE);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //调用取消的链接
                    if(!isChangeSelecting){
                        isChangeSelecting=true;
                        AgreeRequest req = new AgreeRequest();
                        req.token = SpUtil.getInstance().getCurrentToken();
                        req.sign = SignUtil.signData(req);
                        req.isOpen=1;

                        LogUtil.d(Constants.LOG_DATA,"open上传的数据"+ JsonUtil.GsonString(req));

                        RetrofitFactory.getInstance().getService().postAnything(req, Config.CHANGE_AGREE)
                                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp>(){

                                })).subscribe(new BaseObserver<BaseResp>() {
                            @Override
                            protected void onSuccess(BaseResp getUserInfoRespBaseResp) throws Exception {


                                if(getUserInfoRespBaseResp.getResult().equals("1")){
                                    ToastUtil.showToast("缓存设置成功");
                                    WKBaseApplication.getInstance().hasSelectLocal=true;

                                    isChangeSelecting=false;
                                }
                            }

                            @Override
                            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                                   isChangeSelecting=false;
                            }
                        });
                    }

                }
            });
        }

        initConfig();
    }
    private void initConfig(){
        WebSettings webSettings = webView.getSettings();

//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
// 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
// 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

//支持插件


//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

//其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        String url = getIntent().getStringExtra("url");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://" + url);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
