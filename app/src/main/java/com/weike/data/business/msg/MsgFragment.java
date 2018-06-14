package com.weike.data.business.msg;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.EditAndDelMsgReq;
import com.weike.data.model.req.GetMsgListReq;
import com.weike.data.model.resp.GetMsgListResp;
import com.weike.data.model.viewmodel.MessageItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.FileCacheUtils;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLPeerUnverifiedException;

import butterknife.ButterKnife;

/**
 * Created by LeoLu on 2018/5/21.
 * <p>
 * 消息的Fragment
 */
public class MsgFragment extends BaseFragment {


    RecyclerView msgList;

    public BaseDataBindingAdapter adapter;

    public List<MessageItemVM> vms = new ArrayList<>();


    GetMsgListReq req = new GetMsgListReq();

    View nothingView;

    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_msg_notice;
    }

    private void initView(View view) {
        msgList = view.findViewById(R.id.ry_msg_list);
        nothingView = view.findViewById(R.id.ll_nothing_view);
        msgList.setLayoutManager(new LinearLayoutManager(WKBaseApplication.getInstance().getApplicationContext()));
        adapter = new BaseDataBindingAdapter(WKBaseApplication.getInstance().getApplicationContext(), R.layout.widget_message_item, vms, BR.messageItemVM);
        msgList.setAdapter(adapter);


    }

    private boolean isSle = false;

    @Override
    public void onRightClick() {
        super.onRightClick();

        isSle = isSle == true ? false : true;

        for (int i = 0; i < vms.size(); i++) {
            vms.get(i).isSel.set(isSle);
            vms.get(i).isCheck.set(false);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void loadFinish(View view) {

        initView(view);

        req.page = 1;
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);
        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_MSG_LIST)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetMsgListResp>>() {

                })).subscribe(new BaseObserver<BaseResp<GetMsgListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetMsgListResp> getMsgListRespBaseResp) throws Exception {


                if (getMsgListRespBaseResp.getDatas().messageGroupVmList.size() == 0) {
                    nothingView.setVisibility(View.VISIBLE);
                } else {
                    nothingView.setVisibility(View.GONE);
                }

                for (int i = 0; i < getMsgListRespBaseResp.getDatas().messageGroupVmList.size(); i++) {
                    MessageItemVM vm = new MessageItemVM((FragmentActivity) context);
                    vm.title.set(getMsgListRespBaseResp.getDatas().messageGroupVmList.get(i).clientName);
                    vm.content.set(getMsgListRespBaseResp.getDatas().messageGroupVmList.get(i).messageContent);
                    vm.iconUrl.set(getMsgListRespBaseResp.getDatas().messageGroupVmList.get(i).photoUrl);
                    vm.msgId = getMsgListRespBaseResp.getDatas().messageGroupVmList.get(i).id;
                    vms.add(vm);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });


    }

    private void deleteMsg(int state) {


        ArrayList<String> ids = new ArrayList<>();


        for (int i = 0; i < vms.size(); i++) {
            if (vms.get(i).isCheck.get()) {
                LogUtil.d("MsgFragment", "delete:" + vms.get(i).msgId);
                ids.add(vms.get(i).msgId);
            }
        }


        EditAndDelMsgReq req = new EditAndDelMsgReq();
        req.status = state;
        req.id = JsonUtil.GsonString(ids);
        req.sign = SignUtil.signData(req);


        RetrofitFactory.getInstance().getService().postAnything(req, Config.EDIT_AND_DEL_MSG)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp>() {

                })).subscribe(new BaseObserver<BaseResp>() {

            @Override
            protected void onSuccess(BaseResp baseResp) throws Exception {
                for (Iterator<MessageItemVM> it = vms.iterator(); it.hasNext(); ) {
                    MessageItemVM vm = it.next();
                    if (vm.isCheck.get()) {
                        LogUtil.d("MessageItem","remove");
                        it.remove();
                    }
                }

                adapter.notifyDataSetChanged(); //刷新


            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });

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
}
