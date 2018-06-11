package com.weike.data.business.setting;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.fdh.generaldialog.EditInputDialog;
import com.google.gson.reflect.TypeToken;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigInput;
import com.mylhyl.circledialog.params.InputParams;
import com.mylhyl.circledialog.view.listener.OnInputClickListener;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.AddAttrReq;
import com.weike.data.model.req.GetAttrListReq;
import com.weike.data.model.resp.GetAttrListResp;
import com.weike.data.model.viewmodel.AttrItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AttrManagerActivity extends BaseActivity {

    @BindView(R.id.recycleview_attr_manager)
    public RecyclerView recyclerView;

    private List<AttrItemVM> vms = new ArrayList<>();

    private BaseDataBindingAdapter adapter;

    android.support.v4.app.DialogFragment dialogFragment;

    @OnClick(R.id.btn_add_attr)
    public void addNewOne(View view){
        dialogFragment = new CircleDialog.Builder()
                .setCanceledOnTouchOutside(false)
                .setCancelable(true)
                .setInputManualClose(true)
                .setTitle("输入框")
                .setInputHint("请输入条件")
                .setInputText("默认文本")
                .configInput(new ConfigInput() {
                    @Override
                    public void onConfig(InputParams params) {
                        params.padding = new int[]{20, 20, 20, 20};
//                                params.inputBackgroundResourceId = R.drawable.bg_input;
//                                params.gravity = Gravity.CENTER;
                        params.inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                                | InputType.TYPE_TEXT_FLAG_MULTI_LINE;
                    }
                })
                .setNegative("取消", null)
                .setPositiveInput("确定", new OnInputClickListener() {
                    @Override
                    public void onClick(String text, View v) {

                    }
                })
                .show(getSupportFragmentManager());
    }

    private void addAttr(String content){
        AddAttrReq req = new AddAttrReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.attributesName = content;
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req,Config.ADD_ATTR)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp>(){

                })).subscribe(new BaseObserver<BaseResp>() {
            @Override
            protected void onSuccess(BaseResp baseResp) throws Exception {
                AttrItemVM vm = new AttrItemVM();
                vm.isDisplayReduce.set(false);
                vm.name.set(content);
                vms.add(vm);

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @Override
    public void onRightClick() {
        super.onRightClick();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attr_managr);
        ButterKnife.bind(this);
        setCenterText("");
        setLeftText("属性管理");
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
            vms.add(vm);
        }
        adapter.notifyDataSetChanged();
    }
}
