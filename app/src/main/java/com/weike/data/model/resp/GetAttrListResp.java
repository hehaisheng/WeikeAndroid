package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;

import java.util.List;

/**
 * Created by LeoLu on 2018/6/7.
 */

public class GetAttrListResp extends BaseResp {

    public List<AttrListSub> attributesValueList;


    public class AttrListSub{
        public String id;

        public String attributesName;
    }
}
