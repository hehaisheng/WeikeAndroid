package com.weike.data.business.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.GetAttrListReq;
import com.weike.data.model.resp.GetAttrListResp;
import com.weike.data.model.viewmodel.AttrItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;

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

    @OnClick(R.id.btn_add_attr)
    public void addNewOne(View view){

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
