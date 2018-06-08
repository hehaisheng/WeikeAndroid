package com.weike.data.business.msg;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.GetMsgListReq;
import com.weike.data.model.resp.GetMsgListResp;
import com.weike.data.model.viewmodel.MessageItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by LeoLu on 2018/5/21.
 *
 * 消息的Fragment
 */
public class MsgFragment extends BaseFragment {


    RecyclerView msgList;

    private BaseDataBindingAdapter adapter;

    private List<MessageItemVM> vms = new ArrayList<>();

    GetMsgListReq req = new GetMsgListReq();

    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_msg_notice;
    }

    private void initView(View view) {
        msgList  =  view.findViewById(R.id.ry_msg_list);
        msgList.setLayoutManager(new LinearLayoutManager(WKBaseApplication.getInstance().getApplicationContext()));
        adapter = new BaseDataBindingAdapter(WKBaseApplication.getInstance().getApplicationContext(),R.layout.widget_message_item,vms, BR.messageItemVM);
        msgList.setAdapter(adapter);


    }


    @Override
    protected void loadFinish(View view) {

        initView(view);

        req.page = 1;
        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_MSG_LIST)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetMsgListResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetMsgListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetMsgListResp> getMsgListRespBaseResp) throws Exception {
                if (getMsgListRespBaseResp.getResult() == 0) {
                    for(int i = 0 ; i < getMsgListRespBaseResp.getDatas().messageGroupVmList.size() ;i++){
                        MessageItemVM vm = new MessageItemVM((FragmentActivity) context);
                        vm.title.set(getMsgListRespBaseResp.getDatas().messageGroupVmList.get(i).clientName);
                        vm.content.set(getMsgListRespBaseResp.getDatas().messageGroupVmList.get(i).messageContent);

                    }
                } else {
                    //TODO error
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });

        for(int i = 0 ; i < 12 ; i++) {
            MessageItemVM vm = new MessageItemVM((FragmentActivity) context);
            vm.isReadMsg.set(false);
            vm.content.set("这是内容");
            vm.title.set("这是标题");
            vms.add(vm);
        }

    }

    public void loadView(){

    }
}
