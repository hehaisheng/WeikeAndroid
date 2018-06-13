package com.weike.data.business.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.AddClientReq;
import com.weike.data.model.req.AddLabelReq;
import com.weike.data.model.req.GetClientTagListReq;
import com.weike.data.model.resp.AddClientResp;
import com.weike.data.model.resp.GetClientTagListResp;
import com.weike.data.model.viewmodel.LabelOptionItemVM;
import com.weike.data.model.viewmodel.TagSettingVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.ClientTagComparator;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeoLu on 2018/6/4.
 */

public class ClientTagSettingActivity extends BaseActivity implements TagSettingVM.OnReduceListener {

    public BaseDataBindingAdapter adapter;



    public List<TagSettingVM> vms = new ArrayList<>();

    public static final String[] tagLabel = {
            "A","B","C","D","E","F","G","H","J","K","L","M","N","O","P",
            "Q","R","S","T","U","V","W","X","Y","Z"

    };

    public int canModifyPosition;

    @BindView(R.id.btn_add_client_tag)
    public Button addLabel;

    @OnClick(R.id.btn_add_client_tag)
    public void addLabel(View view){
        AddLabelReq req = new AddLabelReq();
        req.labelName = "你好";
        req.sort = tagLabel[vms.size() - 1];
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.ADD_LABEL)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientTagListResp>>(){

                })).subscribe(new BaseObserver<BaseResp<AddClientResp>>() {
            @Override
            protected void onSuccess(BaseResp<AddClientResp> getClientTagListRespBaseResp) throws Exception {


            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });

    }

    @BindView(R.id.recycle_list)
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_tag_setting);
        ButterKnife.bind(this);
        setRightText("");
        setCenterText("");
        setLeftText("标签管理");

        initView();
        initLabel();
    }

    private void initView(){
        adapter = new BaseDataBindingAdapter(this,R.layout.widget_clieng_tag_setting_list_item,vms, BR.tagSettingVM);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRightClick() {
        super.onRightClick();
    }

    public void initLabel(){


        vms.clear();
        GetClientTagListReq req = new GetClientTagListReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);
        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_CLIENT_TAG_LIST)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientTagListResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetClientTagListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetClientTagListResp> getClientTagListRespBaseResp) throws Exception {
                List<GetClientTagListResp.TagSub> list = getClientTagListRespBaseResp.getDatas().clientLabelList;
                Collections.sort(list,new ClientTagComparator());

                for(int i = 0 ; i < list.size() ; i++) {
                    TagSettingVM vm = new TagSettingVM();
                    vm.content.set(list.get(i).labelName);
                    vm.tagId.set(list.get(i).id);
                    vm.isModify.set(false);
                    vm.name.set(list.get(i).sort);
                    vms.add(vm);
                }

                TagSettingVM vm = new TagSettingVM();
                vm.content.set("未分组客户");
                vm.tagId.set("");
                vm.isModify.set(false);
                vms.add(vm);

                adapter.notifyDataSetChanged();

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @Override
    public void onReduce(TagSettingVM vm) {

    }
}
