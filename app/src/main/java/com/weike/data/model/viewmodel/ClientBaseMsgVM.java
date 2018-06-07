package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;

import java.util.ArrayList;

/**
 * Created by LeoLu on 2018/6/7.
 */

public class ClientBaseMsgVM extends BaseVM {
    public ArrayList<String> clientIds = new ArrayList<>();

    public ObservableField<String> email = new ObservableField<>("");

    public ObservableField<String> job = new ObservableField<>("");

    public ObservableField<String> companyLocation = new ObservableField<>("");

    public ObservableField<String> houseLocation = new ObservableField<>("");

    public ObservableField<String> birthday = new ObservableField<>("");

    public ObservableField<String> sex = new ObservableField<>("");

    public ObservableField<String> marry = new ObservableField<>("");

    public ObservableField<String> son = new ObservableField<>("");

    public ObservableField<String> gril = new ObservableField<>("");



}
