package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;

import java.util.List;

/**
 * Created by LeoLu on 2018/5/29.
 */

public class MainPageDataResp extends BaseResp {
    public int count;

    public List<BannerUrl> slideshowList;


    public class BannerUrl{
        public String imgUrl;
    }
}
