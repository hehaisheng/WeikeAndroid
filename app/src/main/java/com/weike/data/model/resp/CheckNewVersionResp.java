package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;

public class CheckNewVersionResp extends BaseResp {
    /*8
     *更新类型
     */
   public int upgradeType;
    /**
     * APK版本号
     */
    public  String apkCode;
    /**
     * APK包名
     */
    public  String apkPackage;
    /**
     * APK 大小
     */
    public  int apkSize;
    /**
     * 更新内容
     */
    public String updateContent;
    /**
     * 更新时间
     */
    public  String updateDate;
    /**
     * 更新路径
     */
    public String apkPath;
}
