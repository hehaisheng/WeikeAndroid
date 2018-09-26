package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;

import java.util.List;

/**
 * Created by LeoLu on 2018/6/6.
 */

public class GetClientListResp extends BaseResp{

    public List<ClientListSub> allClientList;



    public static class ClientListSub{

        /**
         * ID
         */
        public String id ;

        /**
         * 用户名
         */
        public String userName = "";

        /**
         * 是否有未读
         */
        public String hasOr ;

        /**
         * 公司名
         */
        public String company;
        /**
         * 客户头像
         */
        public String photoUrl;

        /**
         * 手机号码
         */
        public String[] phoneNumber;

        //备注
        public String remark;









    }


}
