package com.weike.data.model.req;

import com.weike.data.base.BaseReq;
import com.weike.data.model.business.AnotherAttributes;
import com.weike.data.model.business.BirthDayBean;
import com.weike.data.model.business.ClientRelated;
import com.weike.data.model.business.Product;

import java.util.List;

/**
 * Created by LeoLu on 2018/6/4.
 */

public class AddClientReq extends BaseReq{
    public String userName; //用户名

    public String OnePhoneNumber; //第一个手机号码

    public String TwoPhoneNumber; //第二个手机号码

    public String ThreePhoneNumber;//第三个

    public String FourPhoneNumber; //第四个

    public String FivePhoneNumber; //第五个

    public String clientLabelId;//标签ID

    public String clientId; //客户ID 用于刷新用

    public String photoUrl ; //用户的头像

    public String office; //职位

    public String idCard; //身份证

    public String remark ; //备注

    public String email; //邮箱

    public String company; //公司

    public String companyProvinceId; //公司省的id

    public String companyCityId; //公司市

    public String companyAreaId; //公司县

    public String companyDetailAddress ; //详细地址

    public String homeProvinceId ; //家庭省ID

    public String homeCityId; //家庭市ID

    public String homeAreaId ; //家庭区ID

    public String homeDetailAddress;

    public int sex ; // 性别 1男 2女

    public String birthday;//生日日期

    public int marriage ; //1 未婚 ;已婚

    public String sonNum ; //儿子数量

    public String daughterNum ; //女儿数量

    public String height;//身高

    public String weight; //体重

    /**
     * 生日json
     */
    public String birthdayJson; //BirthDayBean

    /**
     * 客户关联ID
     */
    public String relatedClientId;
    /**
     * 额外的标签集合
     */
    public String attributesValue;


    // 服务信息

    public String income; //收入

    public String expenditure ; //支出

    public String monetaryAssets ; //金融资产

    public String fixedAssets ; //固定资产

    public String car ; //车型

    public String liabilities; //负债

    /**
     * 产品字符串 集合
     */
    public String  product; //
}
