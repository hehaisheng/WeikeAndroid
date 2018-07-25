package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

public class ClientServiceMsgVM {

    /**
     * 是否可以编辑
     */
    public ObservableField<Boolean> clickable = new ObservableField<>(false);

    /**
     * 收入
     */
    public ObservableField<String> moneyIn = new ObservableField<>();

    /**
     * 支出
     */
    public ObservableField<String> moneyOut = new ObservableField<>();

    /**
     * 固定资产
     */
    public ObservableField<String> fixedAssets = new ObservableField<>();

    /**
     * 金融资产
     */
    public ObservableField<String> financialAssets = new ObservableField<>();


    /**
     * 汽车类型
     */
    public ObservableField<String> carType = new ObservableField<>();

    /**
     * 负债
     */
    public ObservableField<String> liabilities = new ObservableField<>();

    public ObservableField<Boolean> isModify = new ObservableField<>();
}
