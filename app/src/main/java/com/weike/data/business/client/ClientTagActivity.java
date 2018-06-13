package com.weike.data.business.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.GetClientByLabelReq;
import com.weike.data.model.req.GetClientTagListReq;
import com.weike.data.model.req.GetUserInfoReq;
import com.weike.data.model.resp.GetClientByLabelResp;
import com.weike.data.model.resp.GetClientTagListResp;
import com.weike.data.model.viewmodel.LabelOptionItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.ClientTagComparator;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientTagActivity extends BaseActivity {

    public static final String[] tagLabel = {
            "A","B","C","D","E","F","G","H","J","K","L","M","N","O","P",
            "Q","R","S","T","U","V","W","X","Y","Z"

    };

    private List<LabelOptionItemVM> optionItemVMS = new ArrayList<>();

    private int lastClickPosition = 0;



    @BindView(R.id.recyclerview_client_option)
    public RecyclerView recycle_option;

    @BindView(R.id.recyclerview_client_content)
    public RecyclerView recycle_client_list;

    @BindView(R.id.ll_nothing_view)
    public View nothingView;


    public BaseDataBindingAdapter cliengtagListAdapter;

    public BaseDataBindingAdapter clientTagOptionAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_tag);
        ButterKnife.bind(this);

        setCenterText("");
        setRightText("");
        setLeftText("客户标签");


        initRecycleOption();

        initLabel();
    }

    public void initLabel(){
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
                        LabelOptionItemVM vm = new LabelOptionItemVM();
                        vm.text.set(list.get(i).sort + "." +list.get(i).labelName);
                        vm.id.set(list.get(i).id);
                        vm.isClick.set(false);

                        optionItemVMS.add(vm);
                    }

                    LabelOptionItemVM vm = new LabelOptionItemVM();
                    vm.text.set("未分组客户");
                    vm.id.set("");
                    vm.isClick.set(true);
                    optionItemVMS.add(0,vm);

                    clientTagOptionAdapter.notifyDataSetChanged();

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void initRecycleOption(){
        clientTagOptionAdapter = new BaseDataBindingAdapter(this,R.layout.widget_client_tag_option,optionItemVMS, BR.tagOptionVM);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycle_option.setLayoutManager(manager);
        recycle_option.setAdapter(clientTagOptionAdapter);
        clientTagOptionAdapter.setOnRecyclerViewItemClickListener(new BaseDataBindingAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                optionItemVMS.get(lastClickPosition).isClick.set(false);
                optionItemVMS.get(position).isClick.set(true);
                lastClickPosition = position;

                refreshTagClient(position);
            }
        });

    }

    public void refreshTagClient(int position) {
        GetClientByLabelReq req = new GetClientByLabelReq();
        req.labelId = optionItemVMS.get(position).id.get();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req,Config.GET_CLIENT_BY_LABEL)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientByLabelResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetClientByLabelResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetClientByLabelResp> getClientByLabelRespBaseResp) throws Exception {
               if(getClientByLabelRespBaseResp.getDatas().allClientList.size() == 0) { //TODO 没有东西
                   nothingView.setVisibility(View.VISIBLE);
               } else {
                   nothingView.setVisibility(View.GONE);
               }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });

    }

}
