package com.weike.data.business.client;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.adapter.CheckedAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.business.setting.ClientTagSettingActivity;
import com.weike.data.config.Config;
import com.weike.data.model.business.ClientRelated;
import com.weike.data.model.req.GetClientByLabelReq;
import com.weike.data.model.req.GetClientTagListReq;
import com.weike.data.model.req.MoveClientToLabelReq;
import com.weike.data.model.resp.GetClientListResp;
import com.weike.data.model.resp.GetClientTagListResp;
import com.weike.data.model.resp.MoveClientToLabelResp;
import com.weike.data.model.viewmodel.ClientTagContentVM;
import com.weike.data.model.viewmodel.LabelOptionItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.ClientTagComparator;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClientTagActivity extends BaseActivity {

    public static final String[] tagLabel = {
            "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"

    };

    private List<LabelOptionItemVM> optionItemVMS = new ArrayList<>();

    private List<ClientTagContentVM> contentVMS = new ArrayList<>();

    private int lastClickPosition = 0;

    private HashMap<Integer , List<ClientTagContentVM>>  carshMap = new HashMap<>();

    @BindView(R.id.recyclerview_client_option)
    public RecyclerView recycle_option;

    @BindView(R.id.recyclerview_client_content)
    public RecyclerView recycle_client_list;

    @BindView(R.id.ll_nothing_view)
    public View nothingView;

    public boolean needFresh = true;

    @OnClick(R.id.tv_manager_label)
    public void goToManagerTag(View view) {
        ActivitySkipUtil.skipAnotherAct(this, ClientTagSettingActivity.class);
        needFresh = true;
    }

    @OnClick(R.id.tv_add_newClient)
    public void addNewClient(View view){
        ActivitySkipUtil.skipAnotherAct(this,AddClientActivity.class);
        needFresh = false;
    }


    public BaseDataBindingAdapter clientListContentAdapter;

    public BaseDataBindingAdapter clientTagOptionAdapter;

    private boolean isMove = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_tag);
        ButterKnife.bind(this);

        setCenterText("");
        setRightText("编辑");
        setLeftText("客户标签");


        initRecycleOption();

    }

    @Override
    public void onRightClick(boolean isModify) {
        super.onRightClick(isModify);



        if (isModify) {
            resetModify(isModify);

        } else {

            showChckDialog();

        }


        clientListContentAdapter.notifyDataSetChanged();
    }


    private void showChckDialog(){




        String[]  array = new String[optionItemVMS.size()];
        String[] ids = new String[optionItemVMS.size()];

        for(int i = 0 ; i <optionItemVMS.size() ; i++) {
            array[i] = optionItemVMS.get(i).text.get();
            ids[i] = optionItemVMS.get(i).id.get();
        };

        final CheckedAdapter checkedAdapter = new CheckedAdapter(this, array,true);

        new CircleDialog.Builder()
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.backgroundColorPress = Color.CYAN;
                    }
                })
                .setTitle("请选择您要关联的客户")
                .setItems(checkedAdapter, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        checkedAdapter.toggleId(position,ids[position]);
                        checkedAdapter.toggle(position, array[position]);


                    }
                })
                .setItemsManualClose(true)
                .setPositive("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetModify(false);
                        move(checkedAdapter.getids().valueAt(0));
                    }
                })
                .show(getSupportFragmentManager());

    }

    private void move(String labelId) {
        ArrayList<ClientRelated> relateds = new ArrayList<>();
        for(int i = 0 ; i < contentVMS.size();i++) {

            if (contentVMS.get(i).isCheck.get()) { //如果被选中
                LogUtil.d("TagClientActivity","ischeck:---");
                ClientRelated related = new ClientRelated();
                related.clientId = contentVMS.get(i).id.get();
                relateds.add(related);
            }
        }
        MoveClientToLabelReq req = new MoveClientToLabelReq();
        req.labelId = labelId;
        req.clientArr = "" + JsonUtil.GsonString(relateds) + "";
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.MOVE_CLIENT_TO_LABEL)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientTagListResp>>() {

                })).subscribe(new BaseObserver<BaseResp<MoveClientToLabelResp>>() {
            @Override
            protected void onSuccess(BaseResp<MoveClientToLabelResp> getClientTagListRespBaseResp) throws Exception {
                refreshTagClient(lastClickPosition);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });



    }

    private void resetModify(boolean isModify){
        if (isModify) {
            setRightText("移动");
            for (int i = 0; i < contentVMS.size(); i++) {
                contentVMS.get(i).isSelector.set(true);
            }
        } else {

            setRightText("编辑");
            for (int i = 0; i < contentVMS.size(); i++) {
                contentVMS.get(i).isSelector.set(isModify);
            }
        }
    }

    public void initLabel() {
        optionItemVMS.clear();
        GetClientTagListReq req = new GetClientTagListReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);
        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_CLIENT_TAG_LIST)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientTagListResp>>() {

                })).subscribe(new BaseObserver<BaseResp<GetClientTagListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetClientTagListResp> getClientTagListRespBaseResp) throws Exception {
                List<GetClientTagListResp.TagSub> list = getClientTagListRespBaseResp.getDatas().clientLabelList;

                Collections.sort(list, new ClientTagComparator());
                for (int i = 0; i < list.size(); i++) {
                    LabelOptionItemVM vm = new LabelOptionItemVM();
                    vm.text.set(list.get(i).sort + "." + list.get(i).labelName);
                    vm.id.set(list.get(i).id);
                    vm.isClick.set(false);

                    optionItemVMS.add(vm);
                }

                LabelOptionItemVM vm = new LabelOptionItemVM();
                vm.text.set("未分组客户");
                vm.id.set("");
                vm.isClick.set(true);
                optionItemVMS.add(0, vm);

                clientTagOptionAdapter.notifyDataSetChanged();
                refreshTagClient(0);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (needFresh == true) {
            lastClickPosition = 0;
            initLabel();
        }

    }

    /**
     * 初始化两个列表
     */
    public void initRecycleOption() {
        clientTagOptionAdapter = new BaseDataBindingAdapter(this, R.layout.widget_client_tag_option, optionItemVMS, BR.tagOptionVM);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycle_option.setLayoutManager(manager);
        recycle_option.setAdapter(clientTagOptionAdapter);
        clientTagOptionAdapter.setOnRecyclerViewItemClickListener(new BaseDataBindingAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                optionItemVMS.get(lastClickPosition).isClick.set(false);
                optionItemVMS.get(position).isClick.set(true);
                lastClickPosition = position;
                nothingView.setVisibility(View.GONE);
                refreshTagClient(position);
            }
        });

        clientListContentAdapter = new BaseDataBindingAdapter(this, R.layout.widget_layout_client_tag_list_item, contentVMS, BR.tagClientContentVM);
        clientListContentAdapter.setOnRecyclerViewItemClickListener(new BaseDataBindingAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                String id = contentVMS.get(position).id.get();
                AddClientActivity.startActivity(ClientTagActivity.this,id);
            }
        });
        recycle_client_list.setLayoutManager(new LinearLayoutManager(this));
        recycle_client_list.setAdapter(clientListContentAdapter);

    }


    /**
     * 刷新右边的列表
     * @param position
     */
    public void refreshTagClient(int position) {



        GetClientByLabelReq req = new GetClientByLabelReq();
        req.labelId = optionItemVMS.get(position).id.get();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_CLIENT_BY_LABEL)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientListResp>>() {

                })).subscribe(new BaseObserver<BaseResp<GetClientListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetClientListResp> getClientListRespBaseResp) throws Exception {
                contentVMS.clear();
                clientListContentAdapter.notifyDataSetChanged();;

                if (getClientListRespBaseResp.getDatas().allClientList.size() <= 0) {
                    nothingView.setVisibility(View.VISIBLE);
                } else {
                    nothingView.setVisibility(View.GONE);
                }

                for (int i = 0; i < getClientListRespBaseResp.getDatas().allClientList.size(); i++) {
                    GetClientListResp.ClientListSub sub = getClientListRespBaseResp.getDatas().allClientList.get(i);
                    ClientTagContentVM vm = new ClientTagContentVM();
                    vm.id.set(sub.id);
                    vm.content.set(sub.company);
                    vm.title.set(sub.userName);
                    vm.isSelector.set(false);
                    vm.photoUrl.set(sub.photoUrl);
                    contentVMS.add(vm);
                }


                clientListContentAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });

    }

}
