package com.weike.data.model.resp;


import com.weike.data.base.BaseResp;

import java.util.List;

public class GetClientByLabelResp extends BaseResp {

    public List<ClientListSub> allClientList ;



    public class ClientListSub{

        /**
         * ID
         */
        public String id ;

        /**
         * 用户名
         */
        public String userName;

        /**
         * 是否有未读
         */
        public String hasOr;

        /**
         * 公司名
         */
        public String company;
        /**
         * 客户头像
         */
        public String photoUrl;

    }

}
