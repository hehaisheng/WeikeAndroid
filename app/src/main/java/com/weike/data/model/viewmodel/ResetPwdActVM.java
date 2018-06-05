package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;
import android.text.TextUtils;

import com.weike.data.R;
import com.weike.data.base.BaseVM;
import com.weike.data.util.ToastUtil;

public class ResetPwdActVM extends BaseVM {
    public ObservableField<Integer> pwdImageId = new ObservableField<>(R.mipmap.icon_hide);

    public ObservableField<Integer> pwd2ImageId = new ObservableField<>(R.mipmap.icon_hide);

    public ObservableField<String> vCode = new ObservableField<>();

    public ObservableField<String> pwd = new ObservableField<>();

    public ObservableField<String> pwd2 = new ObservableField<>();

    /**
     * 眼睛标记
     */
    public ObservableField<Boolean> reset1 = new ObservableField<>();
    /**
     * 眼睛图案标记
     */
    public ObservableField<Boolean> reset2 = new ObservableField<>();

    public void submit(){
        if (TextUtils.isEmpty(vCode.get()) || TextUtils.isEmpty(pwd.get())
                ||TextUtils.isEmpty(pwd2.get())){
            ToastUtil.showToast("重要参数不能为空");
            return;
        }
        if (!pwd.get().equals(pwd2.get())) {
            ToastUtil.showToast("两次输入的密码不一致");
            return;
        }


    }

    public void pwd1Click(){

    }

    public void pwd2Click(){

    }
}
