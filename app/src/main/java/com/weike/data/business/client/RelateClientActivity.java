package com.weike.data.business.client;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
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
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.business.setting.ClientTagSettingActivity;
import com.weike.data.config.Config;
import com.weike.data.model.business.Client;
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
import com.weike.data.util.DialogUtil;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关联客户专用
 */
public class RelateClientActivity extends BaseActivity {



    public static final String[] tagLabel = {
            "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"

    };



    private List<LabelOptionItemVM> optionItemVMS = new ArrayList<>();

    private ArrayList<ClientTagContentVM> contentVMS = new ArrayList<>();

    private int lastClickPosition = 0;


    @BindView(R.id.recyclerview_client_option)
    public RecyclerView recycle_option;

    @BindView(R.id.recyclerview_client_content)
    public RecyclerView recycle_client_list;

    @BindView(R.id.ll_nothing_view)
    public View nothingView;

    public boolean needFresh = true;



    public boolean isSignleCheck =false;

    public static void startActivity(boolean singleCheck , Activity activity,int requestCode) {
        LogUtil.d("test","1");
        Intent intent = new Intent(activity,RelateClientActivity.class);
        intent.putExtra("signleCheck",singleCheck);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivityForResult(intent,requestCode);
    }

    public static void startActivity(boolean singleCheck , BaseFragment fragment, int requestCode,String id) {
        LogUtil.d("test","2");
        Intent intent = new Intent(fragment.getContext(),RelateClientActivity.class);
        intent.putExtra("signleCheck",singleCheck);
        intent.putExtra("id",id);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fragment.startActivityForResult(intent,requestCode);
    }


    @OnClick(R.id.tv_manager_label)
    public void goToManagerTag(View view) {
        ActivitySkipUtil.skipAnotherAct(this, ClientTagSettingActivity.class);
        needFresh = true;
    }

    @OnClick(R.id.tv_add_client)
    public void addNewClient(View view){
        ActivitySkipUtil.skipAnotherAct(this,AddClientActivity.class);
        needFresh = false;
    }


    private boolean isCheck;

    public static final int REQUEST_CODE = 100;

    public BaseDataBindingAdapter clientListContentAdapter;

    public BaseDataBindingAdapter clientTagOptionAdapter;

    private boolean isMove = true;

    public String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_tag);
        ButterKnife.bind(this);

        isSignleCheck = getIntent().getBooleanExtra("signleCheck",false);
        id=getIntent().getStringExtra("id");

        setCenterText("");
        if(isSignleCheck == false)
            setRightText("完成");
        else
            setRightText("");
        setLeftText("客户标签");


        initRecycleOption();

    }

    private ArrayList<ClientRelated> compass(){
        ArrayList<ClientRelated> news = new ArrayList<>();
        Iterator<Map.Entry<Integer,List<ClientRelated>>> iter = tmp.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<Integer,List<ClientRelated>> entry = iter.next();
            List<ClientRelated> current = entry.getValue();
            for(int i = 0 ; i < current.size() ; i++) {
                if (current.get(i).isCheck) {
                    news.add(current.get(i));
                }
            }
        }

        return news;
    }

    @Override
    public void onRightClick(boolean isModify) {
        super.onRightClick(isModify);

        saveStatus(lastClickPosition);




        Intent intent = new Intent();
        intent.putExtra("data",compass());
        setResult(RESULT_OK,intent);
        finish();


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
                lastClickPosition = 0;
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


                saveStatus(lastClickPosition);

                nothingView.setVisibility(View.GONE); //刷新
                refreshTagClient(position);
                lastClickPosition = position;

            }
        });

        clientListContentAdapter = new BaseDataBindingAdapter(this, R.layout.widget_layout_client_tag_list_item, contentVMS, BR.tagClientContentVM);
        clientListContentAdapter.setOnRecyclerViewItemClickListener(new BaseDataBindingAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (isSignleCheck == false) return;

                if(id.equals(contentVMS.get(position).id.get())){
                    DialogUtil.showButtonDialog(getSupportFragmentManager(), "提示", "自己不能关联自己", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {



                        }
                    });
                }else{
                    ArrayList<ClientRelated> relateds = new ArrayList<>();
                    ClientRelated clientRelated = new ClientRelated();
                    clientRelated.name = contentVMS.get(position).title.get();
                    clientRelated.clientId =contentVMS.get(position).id.get();
                    relateds.add(clientRelated);
                    Intent intent = new Intent();
                    intent.putExtra("data",relateds);
                    setResult(RESULT_OK,intent);
                    finish();
                }


            }
        });
        recycle_client_list.setLayoutManager(new LinearLayoutManager(this));
        recycle_client_list.setAdapter(clientListContentAdapter);

    }

    public void saveStatus(int position){
        List<ClientRelated> relateds = null;
        if (tmp.get(position) == null || tmp.get(position).size() == 0) {
            relateds = new ArrayList<>();
            for(int i = 0 ; i <contentVMS.size();i++) {
                ClientRelated clientRelated = new ClientRelated();
                clientRelated.clientId = contentVMS.get(i).id.get();
                clientRelated.name = contentVMS.get(i).title.get();
                clientRelated.isCheck =contentVMS.get(i).isCheck.get();
                relateds.add(clientRelated);
            }
        } else {
            relateds = tmp.get(position);

            for(int i = 0 ; i <contentVMS.size();i++) {
                ClientRelated clientRelated = new ClientRelated();
                clientRelated.clientId = contentVMS.get(i).id.get();
                clientRelated.name = contentVMS.get(i).title.get();
                clientRelated.isCheck =contentVMS.get(i).isCheck.get();
                relateds.set(i,clientRelated);
            }
        }

        tmp.put(position,relateds);
    }


    HashMap<Integer,List<ClientRelated>> tmp = new HashMap<>();

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
                    if (tmp.get(position) != null) {
                        List<ClientRelated> clientRelateds = tmp.get(position);
                        vm.isCheck.set(clientRelateds.get(i).isCheck);
                    }

                    vm.id.set(sub.id);
                    vm.content.set(sub.company);
                    vm.title.set(sub.userName);
                    if(isSignleCheck) { //是否是单选
                        vm.isSelector.set(false);
                    } else {
                        vm.isSelector.set(true);
                    }


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
