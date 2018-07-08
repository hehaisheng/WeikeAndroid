package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;

import com.google.gson.reflect.TypeToken;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.business.client.AddClientActivity;
import com.weike.data.business.client.ClientTagActivity;
import com.weike.data.business.log.AddLogActivity;
import com.weike.data.business.search.SearchActivity;
import com.weike.data.business.working.HandleWorkingActivity;
import com.weike.data.config.Config;
import com.weike.data.model.req.MainPageDataReq;
import com.weike.data.model.resp.MainPageDataResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.BannerImageLoader;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeoLu on 2018/5/29.
 */

public class HomeFragmentVM {

    public Activity context;

    public ObservableArrayList<String> banenrData = new ObservableArrayList<>();

    private List<String> links = new ArrayList<>();

    public HomeFragmentVM(Activity context) {
        this.context = context;
        init();
    }

    private void init(){

        MainPageDataReq req = new MainPageDataReq();
        req.token = SpUtil.getInstance().getCurrentToken();

        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.MAIN_PAGE_DATA)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<MainPageDataResp>>(){

                })).subscribe(new BaseObserver<BaseResp<MainPageDataResp>>(){

            @Override
            protected void onSuccess(BaseResp<MainPageDataResp> mainPageData) throws Exception {
                List<MainPageDataResp.BannerUrl> bannerUrls = mainPageData.getDatas().slideshowList;
                List<String> data = new ArrayList<>();
                for(int i = 0 ; i < bannerUrls.size();i++){
                    data.add(bannerUrls.get(i).imgUrl);
                    links.add(bannerUrls.get(i).linkUrl);
                }
                banenrData.addAll(data);

                LogUtil.d("HomeFragment","currentFragment call back");
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    public void goToSearch(){
        ActivitySkipUtil.skipAnotherAct(context, SearchActivity.class);
    }

    /**
     * 添加客户
     */
    public void goToAddClient(){
        ActivitySkipUtil.skipAnotherAct(context, AddClientActivity.class);
    }

    /**
     * 添加日志
     */
    public void goToAddLog(){
        ActivitySkipUtil.skipAnotherAct(context, AddLogActivity.class);
    }

    /**
     * 待办事处
     */
    public void goToWorking(){
        ActivitySkipUtil.skipAnotherAct(context, HandleWorkingActivity.class);
    }

    public void goToClientTag(){
        ActivitySkipUtil.skipAnotherAct(context, ClientTagActivity.class);
    }

    @BindingAdapter({"bindDingBanner"})
    public static void bindDingBanner(Banner banner , List<String> data){

        LogUtil.d("acthome","BindBanner:" + data.size());
        banner.setImageLoader(new BannerImageLoader());
        banner.setImages(data);
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        banner.setDelayTime(2000);
        banner.start();
       /* banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                links.get(position).
            }
        });*/
    }

}
