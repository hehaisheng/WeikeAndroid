package com.weike.data.model.business;

import com.weike.data.config.Config;

public class ClientSortModel {

    private String name;//显示的数据
    private String sortLetters;//显示数据拼音的首字母

    private String clientId; //客户ID
    private String photoUrl = Config.ICON_URL; //用户头像

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
