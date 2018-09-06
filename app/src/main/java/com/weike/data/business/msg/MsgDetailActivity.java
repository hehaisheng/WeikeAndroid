package com.weike.data.business.msg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.weike.data.listener.OnReduceListener;
import com.weike.data.model.req.GetClientMsgDetailListReq;
import com.weike.data.model.req.ModifyClientMsgRemindReq;
import com.weike.data.model.req.ModifyClientMsgReq;
import com.weike.data.model.resp.GetClientMsgDetailListResp;
import com.weike.data.model.resp.ModifyClientMsgRemindResp;
import com.weike.data.model.resp.ModifyClientMsgResp;
import com.weike.data.model.viewmodel.MsgDetailItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.DialogUtil;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.PickerUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.addapp.pickers.listeners.OnItemPickListener;

/**
 * Created by LeoLu on 2018/6/1.
 */

public class MsgDetailActivity extends BaseActivity implements OnRefreshLoadmoreListener, OnReduceListener<MsgDetailItemVM> {

    @BindView(R.id.recycle_list)
    public RecyclerView msgDetailList;

    @BindView(R.id.layout_moidfy_msg)
    public View bottomLayout;



    @OnClick(R.id.tv_delete_msg)
    public void delete(View view) {
        DialogUtil.showButtonDialog(getSupportFragmentManager(), "提示", "是否删除消息?", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOrHandleMsg(3);
            }
        });
    }

    public void deleteOrHandleMsg(int type) {

        ArrayList<String> ids = new ArrayList<>();


        for (int i = 0; i < vms.size(); i++) {
            if (vms.get(i).isCheck.get()) {
                ids.add(vms.get(i).id);
            }
        }


        ModifyClientMsgReq req = new ModifyClientMsgReq();
        req.status = type;
        req.id = JsonUtil.GsonString(ids).replace("\"", "").replace("[", "").replace("]", "");
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.MODIFY_CLIENT_MSG)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<ModifyClientMsgResp>>() {

                })).subscribe(new BaseObserver<BaseResp<ModifyClientMsgResp>>() {
            @Override
            protected void onSuccess(BaseResp<ModifyClientMsgResp> getClientMsgDetailListRespBaseResp) throws Exception {

                if (type == 3) {
                    for (Iterator<MsgDetailItemVM> it = vms.iterator(); it.hasNext(); ) {
                        MsgDetailItemVM vm = it.next();
                        if (vm.isCheck.get()) {
                            it.remove();
                        }
                    }
                } else if (type == 2) {
                    for (Iterator<MsgDetailItemVM> it = vms.iterator(); it.hasNext(); ) {
                        MsgDetailItemVM vm = it.next();
                        if (vm.isCheck.get()) {
                           vm.isTextRemind.set(false);
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @BindView(R.id.smartrefreshlayout)
    public SmartRefreshLayout smartRefreshLayout;

    public BaseDataBindingAdapter adapter;

    private List<MsgDetailItemVM> vms = new ArrayList<>();

    private String id;

    private int page = 1;

    public static void startActivity(Activity activity, String name, String id) {
        Intent intent = new Intent(activity, MsgDetailActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("id", id);
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
        adapter = new BaseDataBindingAdapter(this, R.layout.widget_layout_msg_detail_list_item, vms, BR.messageDetailItemVM);
        msgDetailList.setAdapter(adapter);
        smartRefreshLayout.setOnRefreshLoadmoreListener(this);
        loadMsg(id, page);
    }

    @Override
    public void onRightClick(boolean isModify) {
        super.onRightClick(isModify);

        if (isModify) {
            setRightText("取消");
        } else {
            setRightText("编辑");
        }
        for (int i = 0; i < vms.size(); i++) {
            vms.get(i).isSle.set(isModify);
        }
        adapter.notifyDataSetChanged();

        bottomLayout.setVisibility(isModify ? View.VISIBLE : View.GONE);

    }

    private void loadMsg(String id, int page) {
        GetClientMsgDetailListReq req = new GetClientMsgDetailListReq();
        req.clientId = id;
        req.page = page;
        req.sign = SignUtil.signData(req);


        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_CLIENT_DETAIL_MSG_LIST)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientMsgDetailListResp>>() {

                })).subscribe(new BaseObserver<BaseResp<GetClientMsgDetailListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetClientMsgDetailListResp> getClientMsgDetailListRespBaseResp) throws Exception {
                List<GetClientMsgDetailListResp.MessageListBean> list = getClientMsgDetailListRespBaseResp.getDatas().getMessageList();

                // LogUtil.d("test","消息"+JsonUtil.GsonString(list));

                if (page > 1 && list.size() == 0) {
                    MsgDetailActivity.this.page = MsgDetailActivity.this.page - 1;//恢复页码
                    ToastUtil.showToast("暂无更多");
                    smartRefreshLayout.finishLoadmore();
                    return;
                }

                if (page == 1) {//下拉

                    vms.clear();
                    smartRefreshLayout.finishRefresh();
                }


                for (int i = 0; i < list.size(); i++) {
                    MsgDetailItemVM itemVM = new MsgDetailItemVM(MsgDetailActivity.this);
                    itemVM.setListener(MsgDetailActivity.this);
                    itemVM.time.set(list.get(i).createDate);
                    itemVM.content.set(list.get(i).content);
                    itemVM.id = list.get(i).id + "";
                    itemVM.isSle.set(false);
                    itemVM.isCheck.set(false);
                    itemVM.remindType = list.get(i).remindType;

                    if (list.get(i).type == 0) { //如果是系统消息
                        itemVM.isSystemMsg.set(true);
                    }


                    if (list.get(i).handleType == 2) { //按钮颜色
                        itemVM.isTextRemind.set(true);
                    } else {
                        itemVM.isTextRemind.set(false);
                    }



                    if (list.get(i).remindType == 2) {
                        itemVM.rightText.set("稍后提醒");
                    } else if (list.get(i).remindType == 1) {
                        if(list.get(i).handleType == 2) {
                            itemVM.rightText.set("处理");
                        } else {
                            itemVM.rightText.set("已处理");
                        }

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
        loadMsg(id, page);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        loadMsg(id, page);
    }

    @Override
    public void onReduce(MsgDetailItemVM msgDetailItemVM) {
        if (msgDetailItemVM.isTextRemind.get() == false) {
            return;
        }


        if (msgDetailItemVM.rightText.get().equals("稍后提醒")) {
            PickerUtil.onOptionPicker(this, new OnItemPickListener() {
                @Override
                public void onItemPicked(int i, Object o) {
                    modifyRemind(msgDetailItemVM, i,1);
                }
            });
        } else {
            DialogUtil.showButtonDialog(getSupportFragmentManager(), "提示", "确定已处理吗?", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyRemind(msgDetailItemVM, -1,2);
                }
            });
        }
    }


    public void modifyRemind(MsgDetailItemVM msgDetailItemVM, int time,int type) {
        ModifyClientMsgRemindReq req = new ModifyClientMsgRemindReq();
        req.id = msgDetailItemVM.id;

        ArrayList<String> list = new ArrayList<>();
        list.add("0.5");
        list.add("1");
        list.add("1.5");
        list.add("2");
        list.add("2.5");
        list.add("3");
        LogUtil.d("test","开始"+time);
        if(time==-1){
            req.laterRemind=time+"";
        }else{
            req.laterRemind  = list.get(time);
        }

        req.remindType = type;
        req.sign = SignUtil.signData(req);
        LogUtil.d("test","稍后提醒"+JsonUtil.GsonString(req));
        RetrofitFactory.getInstance().getService().postAnything(req, Config.MODIFY_CLIENT_MSG_REMIND)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<ModifyClientMsgRemindResp>>() {

                })).subscribe(new BaseObserver<BaseResp<ModifyClientMsgRemindResp>>() {
            @Override
            protected void onSuccess(BaseResp<ModifyClientMsgRemindResp> getClientMsgDetailListRespBaseResp) throws Exception {
                if (type == 2) {
                    msgDetailItemVM.isTextRemind.set(false);
                    LogUtil.d("test","处理成功"+JsonUtil.GsonString(getClientMsgDetailListRespBaseResp));
                    ToastUtil.showToast("处理成功");
                } else {
                    LogUtil.d("test","修改成功"+JsonUtil.GsonString(getClientMsgDetailListRespBaseResp));
                    ToastUtil.showToast("修改提醒成功");
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }


    @Override
    public void onAdd(MsgDetailItemVM msgDetailItemVM) {

    }
}
