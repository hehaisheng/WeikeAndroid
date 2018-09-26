package com.weike.data.model.resp;

import java.util.List;

public class SmsClientData {


    public List<SubSmsClientData> getSubSmsClientDataList() {
        return subSmsClientDataList;
    }

    public void setSubSmsClientDataList(List<SubSmsClientData> subSmsClientDataList) {
        this.subSmsClientDataList = subSmsClientDataList;
    }

    public List<SubSmsClientData> subSmsClientDataList;


    public static  class SubSmsClientData{
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String name;

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String nick;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String phone;


        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public boolean isSelect;


        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String clientId;
    }





}
