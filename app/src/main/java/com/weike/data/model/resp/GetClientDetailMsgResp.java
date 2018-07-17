package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;
import com.weike.data.model.business.ProductBean;

import java.util.List;

/**
 * Created by LeoLu on 2018/6/19.
 */

public class GetClientDetailMsgResp extends BaseResp {


    /**
     * client : {"userName":"培啦","clientLabelId":"","clientLabelName":"","remark":"","onePhoneNumber":"13800138000","twoPhoneNumber":"","threePhoneNumber":"","fourPhoneNumber":"","fivePhoneNumber":"","email":"","company":"","office":"","companyProvinceId":"","companyCityId":"","companyAreaId":"","companyDetailAddress":"","homeProvinceId":"","homeCityId":"","homeAreaId":"","homeDetailAddress":"","sex":"","idCard":"","marriage":"","sonNum":"","daughterNum":"","height":"","weight":"","photoUrl":"http://ja3.ssssgame.com/wkzs-photo/client_head.png","birthday":"1999-05-02"}
     * birthdayjson : {"id":63,"isRemind":1,"content":"生日快乐","remindDate":"1999-05-02 20:20","beforeRemindDay":5,"repeatIntervalHour":3,"priority":1,"isRepeat":1,"dateType":1}
     * clientRelatedList : []
     * anniversaryList : [{"id":126,"anniversaryName":"啦啦啦啦","anniversaryDate":"2018-04-28","remind":{"id":64,"isRemind":1,"content":"测试","remindDate":"1999-05-02 20:20","beforeRemindDay":3,"repeatIntervalHour":1,"priority":1,"isRepeat":1,"dateType":2}},{"id":127,"anniversaryName":"ada","anniversaryDate":"2018-06-15","remind":{}}]
     * userAttributesList : []
     * productList : {"id":149,"clentId":354,"income":"5000","expenditure":"1200","monetaryAssets":"十五万","fixedAssets":"五百万","car":"桑普纳","liabilities":"10000","product":[{"id":33,"productName":"产品名称","remind":{"id":65,"isRemind":1,"content":"测试2","remindDate":"1999-05-02 20:20","beforeRemindDay":3,"repeatIntervalHour":1,"priority":1,"isRepeat":1,"dateType":2}},{"id":34,"productName":"asd","remind":{}}]}
     */
   // public List<ProductListBean.ProductBean> product;
    public BirthdayjsonBean birthdayjson;

    private List<ProductBean> product;

    private List<ClientRelateBean> clientRelatedList;
    private List<AnniversaryListBean> anniversaryList;
    private List<?> userAttributesList;
    private String userName;
    private String clientLabelId;
    private String clientLabelName;
    private String remark;
    private String onePhoneNumber;
    private String twoPhoneNumber;
    private String threePhoneNumber;
    private String fourPhoneNumber;
    private String fivePhoneNumber;
    private String email;
    private String company;
    private String office;
    private String companyProvinceId;
    private String companyCityId;
    private String companyAreaId;
    private String companyDetailAddress;
    private String homeProvinceId;
    private String homeCityId;
    private String homeAreaId;
    private String homeDetailAddress;
    private String sex;
    private String idCard;
    private String marriage;
    private String sonNum;
    private String daughterNum;
    private String height;
    private String weight;
    private String photoUrl;
    private String birthday;
    private int id;
    private int clentId;
    private String income;
    private String expenditure;
    private String monetaryAssets;
    private String fixedAssets;
    private String car;
    private String liabilities;

    public List<ProductBean> getProduct() {
        return product;
    }

    public void setProduct(List<ProductBean> product) {
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClentId() {
        return clentId;
    }

    public void setClentId(int clentId) {
        this.clentId = clentId;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(String expenditure) {
        this.expenditure = expenditure;
    }

    public String getMonetaryAssets() {
        return monetaryAssets;
    }

    public void setMonetaryAssets(String monetaryAssets) {
        this.monetaryAssets = monetaryAssets;
    }

    public String getFixedAssets() {
        return fixedAssets;
    }

    public void setFixedAssets(String fixedAssets) {
        this.fixedAssets = fixedAssets;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(String liabilities) {
        this.liabilities = liabilities;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClientLabelId() {
        return clientLabelId;
    }

    public void setClientLabelId(String clientLabelId) {
        this.clientLabelId = clientLabelId;
    }

    public String getClientLabelName() {
        return clientLabelName;
    }

    public void setClientLabelName(String clientLabelName) {
        this.clientLabelName = clientLabelName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOnePhoneNumber() {
        return onePhoneNumber;
    }

    public void setOnePhoneNumber(String onePhoneNumber) {
        this.onePhoneNumber = onePhoneNumber;
    }

    public String getTwoPhoneNumber() {
        return twoPhoneNumber;
    }

    public void setTwoPhoneNumber(String twoPhoneNumber) {
        this.twoPhoneNumber = twoPhoneNumber;
    }

    public String getThreePhoneNumber() {
        return threePhoneNumber;
    }

    public void setThreePhoneNumber(String threePhoneNumber) {
        this.threePhoneNumber = threePhoneNumber;
    }

    public String getFourPhoneNumber() {
        return fourPhoneNumber;
    }

    public void setFourPhoneNumber(String fourPhoneNumber) {
        this.fourPhoneNumber = fourPhoneNumber;
    }

    public String getFivePhoneNumber() {
        return fivePhoneNumber;
    }

    public void setFivePhoneNumber(String fivePhoneNumber) {
        this.fivePhoneNumber = fivePhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getCompanyProvinceId() {
        return companyProvinceId;
    }

    public void setCompanyProvinceId(String companyProvinceId) {
        this.companyProvinceId = companyProvinceId;
    }

    public String getCompanyCityId() {
        return companyCityId;
    }

    public void setCompanyCityId(String companyCityId) {
        this.companyCityId = companyCityId;
    }

    public String getCompanyAreaId() {
        return companyAreaId;
    }

    public void setCompanyAreaId(String companyAreaId) {
        this.companyAreaId = companyAreaId;
    }

    public String getCompanyDetailAddress() {
        return companyDetailAddress;
    }

    public void setCompanyDetailAddress(String companyDetailAddress) {
        this.companyDetailAddress = companyDetailAddress;
    }

    public String getHomeProvinceId() {
        return homeProvinceId;
    }

    public void setHomeProvinceId(String homeProvinceId) {
        this.homeProvinceId = homeProvinceId;
    }

    public String getHomeCityId() {
        return homeCityId;
    }

    public void setHomeCityId(String homeCityId) {
        this.homeCityId = homeCityId;
    }

    public String getHomeAreaId() {
        return homeAreaId;
    }

    public void setHomeAreaId(String homeAreaId) {
        this.homeAreaId = homeAreaId;
    }

    public String getHomeDetailAddress() {
        return homeDetailAddress;
    }

    public void setHomeDetailAddress(String homeDetailAddress) {
        this.homeDetailAddress = homeDetailAddress;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getSonNum() {
        return sonNum;
    }

    public void setSonNum(String sonNum) {
        this.sonNum = sonNum;
    }

    public String getDaughterNum() {
        return daughterNum;
    }

    public void setDaughterNum(String daughterNum) {
        this.daughterNum = daughterNum;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public BirthdayjsonBean getBirthdayjson() {
        return birthdayjson;
    }

    public void setBirthdayjson(BirthdayjsonBean birthdayjson) {
        this.birthdayjson = birthdayjson;
    }


    public List<ClientRelateBean> getClientRelatedList() {
        return clientRelatedList;
    }

    public void setClientRelatedList(List<ClientRelateBean> clientRelatedList) {
        this.clientRelatedList = clientRelatedList;
    }

    public List<AnniversaryListBean> getAnniversaryList() {
        return anniversaryList;
    }

    public void setAnniversaryList(List<AnniversaryListBean> anniversaryList) {
        this.anniversaryList = anniversaryList;
    }

    public List<?> getUserAttributesList() {
        return userAttributesList;
    }

    public void setUserAttributesList(List<?> userAttributesList) {
        this.userAttributesList = userAttributesList;
    }


    public static class ClientRelateBean{
        public int id;

        public String relatedClientId;

        public String relatedUserName;
    }

    public static class ClientBean {
        /**
         * userName : 培啦
         * clientLabelId :
         * clientLabelName :
         * remark :
         * onePhoneNumber : 13800138000
         * twoPhoneNumber :
         * threePhoneNumber :
         * fourPhoneNumber :
         * fivePhoneNumber :
         * email :
         * company :
         * office :
         * companyProvinceId :
         * companyCityId :
         * companyAreaId :
         * companyDetailAddress :
         * homeProvinceId :
         * homeCityId :
         * homeAreaId :
         * homeDetailAddress :
         * sex :
         * idCard :
         * marriage :
         * sonNum :
         * daughterNum :
         * height :
         * weight :
         * photoUrl : http://ja3.ssssgame.com/wkzs-photo/client_head.png
         * birthday : 1999-05-02
         */


    }

    public static class BirthdayjsonBean {
        /**
         * 是否提醒
         */
        public int isRemind = 1;

        /**
         * 待办事时间
         */
        public String remindDate;

        /**
         * 是否提前提醒
         */
        public int isAdvance;  // 1是 2 否

        public int advanceDateType; //提前提醒时间类型

        public int advanceInterval; // 提醒时间间隔


        /**
         * id啦
         */
        public String id;

        /**
         * 内容
         */
        public String content;

        /**
         * 优先级 1 == 高 2 == 中 3== 低
         */
        public int priority = 1;

        /*
         * 是否重复提醒
         */
        public int isRepeat = 1; // 是 2 否

        public int repeatInterval;//重复提醒时间间隔

        public int repeatDateType; //重复提醒时间类型
    }



    public static class AnniversaryListBean {
        /**
         * id : 126
         * anniversaryName : 啦啦啦啦
         * anniversaryDate : 2018-04-28
         * remind : {"id":64,"isRemind":1,"content":"测试","remindDate":"1999-05-02 20:20","beforeRemindDay":3,"repeatIntervalHour":1,"priority":1,"isRepeat":1,"dateType":2}
         */

        private int id;
        private String anniversaryName;
        private String anniversaryDate;
        private RemindBeanX remind;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAnniversaryName() {
            return anniversaryName;
        }

        public void setAnniversaryName(String anniversaryName) {
            this.anniversaryName = anniversaryName;
        }

        public String getAnniversaryDate() {
            return anniversaryDate;
        }

        public void setAnniversaryDate(String anniversaryDate) {
            this.anniversaryDate = anniversaryDate;
        }

        public RemindBeanX getRemind() {
            return remind;
        }

        public void setRemind(RemindBeanX remind) {
            this.remind = remind;
        }

        public static class RemindBeanX {
            /**
             * 是否提醒
             */
            public int isRemind = 1;

            /**
             * 待办事时间
             */
            public String remindDate;

            /**
             * 是否提前提醒
             */
            public int isAdvance;  // 1是 2 否

            public int advanceDateType; //提前提醒时间类型

            public int advanceInterval; // 提醒时间间隔


            /**
             * id啦
             */
            public String id;

            /**
             * 内容
             */
            public String content;

            /**
             * 优先级 1 == 高 2 == 中 3== 低
             */
            public int priority = 1;

            /*
             * 是否重复提醒
             */
            public int isRepeat = 1; // 是 2 否

            public int repeatInterval;//重复提醒时间间隔

            public int repeatDateType; //重复提醒时间类型
        }
    }
}

