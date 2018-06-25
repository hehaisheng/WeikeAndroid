package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;

import java.util.List;

public class SearchResp extends BaseResp {
    public List<SearchBean> nameList;

    public List<SearchBean> remarkList;

    public List<SearchBean> companyList;


    public static class SearchBean{
        public String userId;

        public String userName;

        public String remark;

        public String clienturl;
    }
}
