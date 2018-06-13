package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;

import java.util.List;


public class GetClientTagListResp extends BaseResp {

    public List<TagSub> clientLabelList;

    public class TagSub{
        public String id ;

        public String labelName;

        public String sort;
    }
}
