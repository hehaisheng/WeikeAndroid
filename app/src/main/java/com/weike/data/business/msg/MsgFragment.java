package com.weike.data.business.msg;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigText;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.TextParams;
import com.mylhyl.circledialog.params.TitleParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.DeleteHomeMsgReq;
import com.weike.data.model.req.GetMsgListReq;
import com.weike.data.model.resp.GetMsgListResp;
import com.weike.data.model.viewmodel.MessageItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.DialogUtil;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by LeoLu on 2018/5/21.
 * <p>
 * 消息的Fragment
 */
public class MsgFragment extends BaseFragment implements OnRefreshLoadmoreListener {


    RecyclerView msgList;

    public BaseDataBindingAdapter adapter;

    public List<MessageItemVM> vms = new ArrayList<>();

    private SmartRefreshLayout smartRefreshLayout;



    private int page = 1;

    View nothingView;

    RelativeLayout relativeLayout;


    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_msg_notice;
    }

    private void initView(View view) {
        relativeLayout=view.findViewById(R.id.relative_layout);
        msgList = view.findViewById(R.id.ry_msg_list);
        nothingView = view.findViewById(R.id.ll_nothing_view);
        smartRefreshLayout = view.findViewById(R.id.smartrefreshlayout);
        smartRefreshLayout.setOnRefreshLoadmoreListener(this);
        msgList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BaseDataBindingAdapter(getActivity(), R.layout.widget_message_item, vms, BR.messageItemVM);
        adapter.setOnRecyclerViewItemClickListener(new BaseDataBindingAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                //ActivitySkipUtil.skipAnotherAct(getActivity(),MsgDetailActivity.class);


            }
        });
        msgList.setAdapter(adapter);


    }

    private boolean isSle = false;

    //public boolean isFirst=true;

    @Override
    public void onRightClick() {
        super.onRightClick();

        isSle = !isSle;
        //isSle = isSle == true ? false : true;

        if(isSle){
            LogUtil.d("test","编辑");
        }else{
            LogUtil.d("test","不是编辑");
        }
        for (int i = 0; i < vms.size(); i++) {
            LogUtil.d("test","vms有数值");
            if(vms.get(i).clientId!=null){
                if(vms.get(i).clientId.equals("0")){
                    vms.get(i).isSel.set(false);
                }else{
                    vms.get(i).isSel.set(isSle);
                }

            }

            vms.get(i).isCheck.set(false);
        }

        adapter = new BaseDataBindingAdapter(getActivity(), R.layout.widget_message_item, vms, BR.messageItemVM);
        adapter.notifyDataSetChanged();



    }

    private void initMsg(int page) {

        relativeLayout.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GetMsgListReq req = new GetMsgListReq();
                req.page = page;
                req.token = SpUtil.getInstance().getCurrentToken();
                req.sign = SignUtil.signData(req);
                LogUtil.d("test","消息"+JsonUtil.GsonString(req));
                RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_MSG_LIST)
                        .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetMsgListResp>>() {

                        })).subscribe(new BaseObserver<BaseResp<GetMsgListResp>>() {
                    @Override
                    protected void onSuccess(BaseResp<GetMsgListResp> getMsgListRespBaseResp) throws Exception {
                        relativeLayout.setVisibility(View.GONE);

                        LogUtil.d("test","消息"+JsonUtil.GsonString(getMsgListRespBaseResp));

                        if (page> 1 && getMsgListRespBaseResp.getDatas().messageGroupVmList.size() == 0) {
                            ToastUtil.showToast("暂无更多");
                            MsgFragment.this.page = MsgFragment.this.page - 1;//恢复页码
                            smartRefreshLayout.finishLoadmore();
                            return;
                        }

                        if(page == 1) {//下拉

                            vms.clear();
                            smartRefreshLayout.finishRefresh();
                        }

                        if (getMsgListRespBaseResp.getDatas().messageGroupVmList.size() == 0) {
                            nothingView.setVisibility(View.VISIBLE);
                        } else {
                            nothingView.setVisibility(View.GONE);
                        }

                        for (int i = 0; i < getMsgListRespBaseResp.getDatas().messageGroupVmList.size(); i++) {
                            MessageItemVM vm = new MessageItemVM((FragmentActivity) context);
                            vm.clientId = getMsgListRespBaseResp.getDatas().messageGroupVmList.get(i).clientId;
                            vm.title.set(getMsgListRespBaseResp.getDatas().messageGroupVmList.get(i).clientName);

                            vm.content.set(getMsgListRespBaseResp.getDatas().messageGroupVmList.get(i).messageContent);
                            vm.iconUrl.set(getMsgListRespBaseResp.getDatas().messageGroupVmList.get(i).photoUrl);
                            vm.msgId = getMsgListRespBaseResp.getDatas().messageGroupVmList.get(i).id;
                            vms.add(vm);
                        }
                        adapter = new BaseDataBindingAdapter(getActivity(), R.layout.widget_message_item, vms, BR.messageItemVM);
                        msgList.setAdapter(adapter);

                       // adapter.notifyDataSetChanged();
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.finishLoadmore();
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        relativeLayout.setVisibility(View.GONE);
                    }
                });
            }
        },350);




    }

    @Override
    protected void loadFinish(View view) {
        //isFirst=true;

        initView(view);
        initMsg(page);


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            page=1;
            initMsg(page);
        } else {

        }
    }

    private void deleteMsg(int state) {


        ArrayList<String> ids = new ArrayList<>();

        boolean hasServer=false;

        for (int i = 0; i < vms.size()&&!hasServer; i++) {
            if (vms.get(i).isCheck.get()) {
                if(vms.get(i).title.get().equals("系统消息")){
                    hasServer=true;

                }else{
                    ids.add(vms.get(i).clientId);
                }

            }
        }
        if(hasServer){
            DialogUtil.showButtonDialog(getFragmentManager(), "提示", "系统消息不可删除", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }else{
            DeleteHomeMsgReq req = new DeleteHomeMsgReq();
            req.clientId = JsonUtil.GsonString(ids).replace("\"","").replace("[","").replace("]","");
            req.sign = SignUtil.signData(req);


            RetrofitFactory.getInstance().getService().postAnything(req, Config.DELETE_HOME_MSG)
                    .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp>() {

                    })).subscribe(new BaseObserver<BaseResp>() {

                @Override
                protected void onSuccess(BaseResp baseResp) throws Exception {

                    initMsg(1);
//                    for (Iterator<MessageItemVM> it = vms.iterator(); it.hasNext(); ) {
//                        MessageItemVM vm = it.next();
//                        if (vm.isCheck.get()) {
//                            it.remove();
//                        }
//                    }

                    //adapter.notifyDataSetChanged(); //刷新


                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

                }
            });
        }





    }

    public void onBottomClick() {
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
                params.textColor = getActivity().getResources().getColor(R.color.color_content);
            }
        })
                .setText("是否删除消息")
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
                        deleteMsg(3);
                    }
                })
                .configPositive(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.backgroundColorPress = Color.WHITE;
                    }
                })
                .show(((AppCompatActivity) getActivity()).getSupportFragmentManager());
    }

    public void loadView() {

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page++;
        initMsg(page);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page=1;
        initMsg(page);
    }
}
