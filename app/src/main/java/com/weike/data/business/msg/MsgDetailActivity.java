package com.weike.data.business.msg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
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
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LeoLu on 2018/6/1.
 */

public class MsgDetailActivity extends BaseActivity implements OnRefreshLoadmoreListener {

    @BindView(R.id.recycle_list)
    public RecyclerView msgDetailList;

    @BindView(R.id.smartrefreshlayout)
    public SmartRefreshLayout smartRefreshLayout;

    public BaseDataBindingAdapter adapter;

    private List<MsgDetailItemVM>  vms = new ArrayList<>();

    private String id;

    private int page = 1;

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
        id = getIntent().getStringExtra("id");





        setLeftText(name);
        setRightText("编辑");
        setCenterText("");



        msgDetailList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseDataBindingAdapter(this,R.layout.widget_layout_msg_detail_list_item,vms, BR.messageDetailItemVM);
        msgDetailList.setAdapter(adapter);
        smartRefreshLayout.setOnRefreshLoadmoreListener(this);
        loadMsg(id,page);
    }

    @Override
    public void onRightClick(boolean isModify) {
        super.onRightClick(isModify);

        if (isModify) {
            setRightText("取消");
        } else {
            setCenterText("编辑");
        }
        for(int i = 0 ; i < vms.size() ; i++) {
            vms.get(i).isSle.set(isModify);
        }
        adapter.notifyDataSetChanged();

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


                if (page> 1 && list.size() == 0) {
                    MsgDetailActivity.this.page = MsgDetailActivity.this.page - 1;//恢复页码
                    ToastUtil.showToast("暂无更多");
                    smartRefreshLayout.finishLoadmore();
                    return;
                }

                if(page == 1) {//下拉

                    vms.clear();
                    smartRefreshLayout.finishRefresh();
                }


                for(int i = 0; i < list.size() ; i++) {
                    MsgDetailItemVM itemVM = new MsgDetailItemVM(MsgDetailActivity.this);
                    itemVM.time.set(list.get(i).getCreateDate());
                    itemVM.content.set(list.get(i).getContent());
                    itemVM.isSle.set(false);
                    itemVM.isCheck.set(false);

                    if (list.get(i).getType() ==0 ) { //如果是系统消息
                        itemVM.isSystemMsg.set(true);
                    }

                    if (list.get(i).getStatus() == 1) {
                        //未读itemvm
                        itemVM.rightText.set("处理");
                        itemVM.rightTextColor.set(R.color.color_41BCF6);
                        itemVM.isRead.set(true);
                    } else {
                        itemVM.isRead.set(false);
                        itemVM.rightText.set("已处理");
                        itemVM.rightTextColor.set(R.color.color_bebebe);
                    }

                    if (list.get(i).getIs_remind() == 1) {
                        itemVM.leftText.set("稍后提醒");
                    } else {
                        itemVM.leftText.set("不在提醒");
                    }


                    vms.add(itemVM);
                }
                adapter.notifyDataSetChanged();
                smartRefreshLayout.finishLoadmore();
                smartRefreshLayout.finishRefresh();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page++;
        loadMsg(id,page);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        loadMsg(id,page);
    }
}
