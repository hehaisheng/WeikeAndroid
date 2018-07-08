package com.weike.data.business.setting;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import com.google.gson.reflect.TypeToken;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigInput;
import com.mylhyl.circledialog.callback.ConfigText;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.InputParams;
import com.mylhyl.circledialog.params.TextParams;
import com.mylhyl.circledialog.params.TitleParams;
import com.mylhyl.circledialog.view.listener.OnInputClickListener;
import com.orhanobut.logger.AndroidLogAdapter;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.business.AnotherAttributes;
import com.weike.data.model.business.User;
import com.weike.data.model.req.AddAttrReq;
import com.weike.data.model.req.DeleteAttrReq;
import com.weike.data.model.req.DeleteLabelReq;
import com.weike.data.model.req.GetAttrListReq;
import com.weike.data.model.resp.AddAttrResp;
import com.weike.data.model.resp.GetAttrListResp;
import com.weike.data.model.viewmodel.AttrItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;



import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AttrManagerActivity extends BaseActivity implements AttrItemVM.OnReduceListener {

    public static final int REQUEST_CODE = 1000;

    @BindView(R.id.recycleview_attr_manager)
    public RecyclerView recyclerView;

    private List<AttrItemVM> vms = new ArrayList<>();

    private BaseDataBindingAdapter adapter;

    android.support.v4.app.DialogFragment dialogFragment;

    @BindView(R.id.btn_add_attr)
    public Button submit;

    @OnClick(R.id.btn_add_attr)
    public void addNewOne(View view){
        dialogFragment = new CircleDialog.Builder()
                .setCanceledOnTouchOutside(false)
                .setCancelable(true)
                .setInputManualClose(true)
                .setTitle("添加属性")
                .setInputHint("请输入")
                .setInputText("")
                .configInput(new ConfigInput() {
                    @Override
                    public void onConfig(InputParams params) {
                        params.padding = new int[]{20, 20, 20, 20};
//                                params.inputBackgroundResourceId = R.drawable.bg_input;
//                                params.gravity = Gravity.CENTER;
                        params.inputType = InputType.TYPE_CLASS_TEXT;
                    }
                })
                .setNegative("取消", null)
                .setPositiveInput("确定", new OnInputClickListener() {
                    @Override
                    public void onClick(String text, View v) {
                        addAttr(text);

                        dialogFragment.dismiss();
                    }
                })
                .show(getSupportFragmentManager());
    }

    private void addAttr(String content){

        for(int i = 0  ; i < vms.size();i++) {
            if (vms.get(i).name.get().equals(content)) {
                ToastUtil.showToast("已存在相同的属性");
                return;
            }
        }



        AddAttrReq req = new AddAttrReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.attributesName = content;
        req.sign = SignUtil.signData(req);



        RetrofitFactory.getInstance().getService().postAnything(req,Config.ADD_ATTR)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<AddAttrResp>>(){

                })).subscribe(new BaseObserver<BaseResp<AddAttrResp>>() {
            @Override
            protected void onSuccess(BaseResp<AddAttrResp> baseResp) throws Exception {
                AttrItemVM vm = new AttrItemVM();
                vm.isDisplayReduce.set(true);
                vm.name.set(content);
                vm.id.set(baseResp.getDatas().attributesId);
                vms.add(vm);
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @Override
    public void onRightClick(boolean isModify) {
        super.onRightClick(isModify);
        if(isModify) {
            submit.setVisibility(View.GONE);

            for(int i = 18 ; i < vms.size() ; i++ ){
                vms.get(i).isDisplayReduce.set(true);
            }
        } else {

            for(int i = vms.size() - 18 ; i < vms.size() ; i++ ){
                vms.get(i).isDisplayReduce.set(false);
            }

            submit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        User user = SpUtil.getInstance().getUser();
        List<AnotherAttributes> list = user.anotherAttributes;
        list.clear();

        for(int i = vms.size() - 18 ; i < vms.size() ;i++){
            AnotherAttributes anotherAttributes = new AnotherAttributes();
            anotherAttributes.attributesId = vms.get(i).id.get();
            anotherAttributes.attributesName = vms.get(i).name.get();
            list.add(anotherAttributes);
        }
        SpUtil.getInstance().saveNewsUser(user);

        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attr_managr);
        ButterKnife.bind(this);
        setCenterText("");
        setLeftText("属性名称管理");
        setRightText("");

        initDefault();


        GetAttrListReq req = new GetAttrListReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);


        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_ATTR_LIST)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetAttrListResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetAttrListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetAttrListResp> getAttrListRespBaseResp) throws Exception {
                    for(int i = 0 ; i < getAttrListRespBaseResp.getDatas().attributesValueList.size();i++) {
                        AttrItemVM vm = new AttrItemVM();
                        vm.id.set(getAttrListRespBaseResp.getDatas().attributesValueList.get(i).id);
                        vm.name.set(getAttrListRespBaseResp.getDatas().attributesValueList.get(i).attributesName);
                        vm.isDisplayReduce.set(true);
                        vm.setListener(AttrManagerActivity.this);
                        vms.add(vm);
                    }
                    adapter.notifyDataSetChanged();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    private void deleteAttr(AttrItemVM vm) {
        DeleteAttrReq req = new DeleteAttrReq();
        req.attributesId = vm.id.get();
        req.sign = SignUtil.signData(req);
        RetrofitFactory.getInstance().getService().postAnything(req, Config.DEL_ATTR)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp>(){

                })).subscribe(new BaseObserver<BaseResp>() {
            @Override
            protected void onSuccess(BaseResp  getAttrListRespBaseResp) throws Exception {
                vms.remove(vm);
                adapter.notifyDataSetChanged();
                ToastUtil.showToast("删除成功");
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });

    }

    private void initDefault(){


        String[] defaultAttr = getResources().getStringArray(R.array.provinces);
        adapter = new BaseDataBindingAdapter(this,R.layout.widget_attr_list_item,vms,BR.attrManagerItemVM);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);
        for(int i = 0 ; i< defaultAttr.length ; i++) {
            AttrItemVM vm = new AttrItemVM();
            vm.name.set(defaultAttr[i]);
            vm.isDisplayReduce.set(false);
            vm.setListener(AttrManagerActivity.this);
            vms.add(vm);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onReduce(AttrItemVM vm) {
        new CircleDialog.Builder()
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.backgroundColor = Color.WHITE;
                        params.backgroundColorPress = Color.BLUE;
                    }
                })
                .setTitle("提示").configTitle(new ConfigTitle() {
            @Override
            public void onConfig(TitleParams params) {
                params.textColor = getResources().getColor(R.color.color_content);
            }
        })
                .setText("是否删除属性")
                .configText(new ConfigText() {
                    @Override
                    public void onConfig(TextParams params) {
                        params.padding = new int[]{100, 0, 100, 50};
                    }
                })
                .setNegative("取消", null).configNegative(new ConfigButton() {
            @Override
            public void onConfig(ButtonParams params) {
                params.backgroundColorPress = Color.WHITE;
            }
        })
                .setPositive("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            deleteAttr(vm);
                    }
                })
                .configPositive(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.backgroundColorPress = Color.WHITE;
                    }
                })
                .show(getSupportFragmentManager());
    }
}
