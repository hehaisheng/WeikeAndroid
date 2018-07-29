package com.weike.data.model.business;

public class JpushModel {


    /**
     * wkzs_ext : {"type":1,"clientId":70}
     */

    private String wkzs_ext;

    public void setWkzs_ext(String wkzs_ext) {
        this.wkzs_ext = wkzs_ext;
    }

    public String getWkzs_ext() {
        return wkzs_ext;
    }

    public static class JPushSubBean{
        public int type;

        public String clientId;

        public String name;
    }
}
