package com.weike.data.business.msg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.GetClientDetailMsgReq;
import com.weike.data.model.req.GetClientMsgDetailListReq;
import com.weike.data.model.resp.GetClientMsgDetailListResp;
import com.weike.data.model.viewmodel.MessageItemVM;
import com.weike.data.model.viewmodel.MsgDetailItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LeoLu on 2018/6/1.
 */

public class MsgDetailActivity extends BaseActivity {

    @BindView(R.id.recycle_list)
    public RecyclerView msgDetailList;

    public BaseDataBindingAdapter adapter;

    private List<MsgDetailItemVM>  vms = new ArrayList<>();



    public static void startActivity(Activity activity , String name , String id) {
        Intent intent = new Intent(activity,MsgDetailActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("id",id);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_detail);
        ButterKnife.bind(this);

        String name = getIntent().getStringExtra("name");
        String id = getIntent().getStringExtra("id");





        setLeftText(name);
        setRightText("");
        setCenterText("");



        msgDetailList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseDataBindingAdapter(this,R.layout.widget_layout_msg_detail_list_item,vms, BR.messageDetailItemVM);
        msgDetailList.setAdapter(adapter);
        loadMsg(id,1);
    }

    private void loadMsg(String id , int page){
        GetClientMsgDetailListReq req = new GetClientMsgDetailListReq();
        req.clientId = id;
        req.page = page;
        req.sign = SignUtil.signData(req);


        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_CLIENT_DETAIL_MSG_LIST)
        .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientMsgDetailListResp>>(){

        })).subscribe(new BaseObserver<BaseResp<GetClientMsgDetailListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetClientMsgDetailListResp> getClientMsgDetailListRespBaseResp) throws Exception {
                List<GetClientMsgDetailListResp.MessageListBean> list = getClientMsgDetailListRespBaseResp.getDatas().getMessageList();
                for(int i = 0; i < list.size() ; i++) {
                    MsgDetailItemVM itemVM = new MsgDetailItemVM(MsgDetailActivity.this);
                    itemVM.content.set(list.get(i).getContent());
                    vms.add(itemVM);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }
}
