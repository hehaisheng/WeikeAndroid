package com.weike.data.business.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigInput;
import com.mylhyl.circledialog.callback.ConfigText;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.InputParams;
import com.mylhyl.circledialog.params.TextParams;
import com.mylhyl.circledialog.params.TitleParams;
import com.mylhyl.circledialog.view.listener.OnInputClickListener;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.business.User;
import com.weike.data.model.req.AddLabelReq;
import com.weike.data.model.req.DeleteLabelReq;
import com.weike.data.model.req.GetClientTagListReq;
import com.weike.data.model.req.GetLabelNumReq;
import com.weike.data.model.resp.AddLabelResp;
import com.weike.data.model.resp.GetClientTagListResp;
import com.weike.data.model.resp.GetLabelNumResp;
import com.weike.data.model.viewmodel.TagSettingVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.ClientTagComparator;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
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

    private String[] currentLabelTagArray = {};

    boolean isModify = false;

    public int canModifyPosition;

    private DialogFragment dialogFragment;

    @BindView(R.id.btn_add_client_tag)
    public Button addLabel;

    @OnClick(R.id.btn_add_client_tag)
    public void addLabel(View view){

        dialogFragment = new CircleDialog.Builder()
                .setCanceledOnTouchOutside(false)
                .setCancelable(true)
                .setInputManualClose(true)
                .setTitle("添加客户标签")
                .setInputHint("请输入(最长限制6个字符)")
                .configInput(new ConfigInput() {
                    @Override
                    public void onConfig(InputParams params) {
                        params.padding = new int[]{20, 20, 20, 20};
                        params.inputType = InputType.TYPE_CLASS_TEXT;

                    }
                })
                .setNegative("取消", null)
                .setPositiveInput("确定", new OnInputClickListener() {
                    @Override
                    public void onClick(String text, View v) {
                        if (TextUtils.isEmpty(text)) {
                            ToastUtil.showToast("信息不能为空");
                            return;
                        }

                        if (text.length() > 6){
                            text = text.substring(0,6);
                        }

                        addLabel(text,null,null);

                        dialogFragment.dismiss();
                    }
                })
                .show(getSupportFragmentManager());


    }

    private void checkLabelNum(){
        GetLabelNumReq req = new GetLabelNumReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req,Config.CHECK_LABEL_TAG_NUM)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetLabelNumResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetLabelNumResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetLabelNumResp> getLabelNumRespBaseResp) throws Exception {
                    currentLabelTagArray = getLabelNumRespBaseResp.getDatas().labelSort;
                    initLabel();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    private void addLabel(String content,String id,TagSettingVM vm) {

        for(int  i = 0 ; i < vms.size();i++) {
            if (vms.get(i).content.get().equals(content)) {
                ToastUtil.showToast("存在相同的客户签名");
                return;
            }
        }


        AddLabelReq req = new AddLabelReq();
        req.labelName = content;
        req.sort = TextUtils.isEmpty(id) ?  currentLabelTagArray[0] : vm.sort;
        req.labelId = id;
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.ADD_LABEL)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<AddLabelResp>>(){

                })).subscribe(new BaseObserver<BaseResp<AddLabelResp>>() {
            @Override
            protected void onSuccess(BaseResp<AddLabelResp> getClientTagListRespBaseResp) throws Exception {


                /*if (TextUtils.isEmpty(id)) {
                    TagSettingVM vm = new TagSettingVM();
                    vm.setListener(ClientTagSettingActivity.this);
                    vm.tagId.set(getClientTagListRespBaseResp.getDatas().id + "");
                    vm.name.set(req.sort);
                    vm.isModify.set(true);
                    vm.content.set(content);
                    vms.add(vm);
                    adapter.notifyDataSetChanged();

                } else {
                    vm.content.set(content);
                    adapter.notifyDataSetChanged();
                    ToastUtil.showToast("修改成功");
                }

                // 本地保存

                checkLabelNum();
                User user = SpUtil.getInstance().getUser();
                GetClientTagListResp.TagSub tagSub = new GetClientTagListResp.TagSub();
                tagSub.id = getClientTagListRespBaseResp.getDatas().id + "";
                tagSub.labelName = content;
                tagSub.sort = req.sort;
                user.labelList.add(tagSub);

                SpUtil.getInstance().saveNewsUser(user); //保存本地*/


                User user = SpUtil.getInstance().getUser();
                GetClientTagListResp.TagSub tagSub = new GetClientTagListResp.TagSub();
                tagSub.id = getClientTagListRespBaseResp.getDatas().id + "";
                tagSub.labelName = content;
                tagSub.sort = req.sort;
                user.labelList.add(tagSub);

                checkLabelNum();


                SpUtil.getInstance().saveNewsUser(user); //保存本地
                ToastUtil.showToast("修改成功");


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

        checkLabelNum(); //获取剩余的标签
        setCenterText("");
        setLeftText("客户标签设置");
        setRightText("");

        initView();

        //initLabel();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        User user = SpUtil.getInstance().getUser();
        List<GetClientTagListResp.TagSub> list = user.labelList;
        list.clear();
        for(int i = 0 ; i < vms.size();i++){
            GetClientTagListResp.TagSub su = new GetClientTagListResp.TagSub();
            su.id = vms.get(i).tagId.get();
            su.sort = vms.get(i).name.get();
            su.labelName = vms.get(i).content.get();
            list.add(su);
        }
        SpUtil.getInstance().saveNewsUser(user);
    }

    private void initView(){
        adapter = new BaseDataBindingAdapter(this,R.layout.widget_clieng_tag_setting_list_item,vms, BR.tagSettingVM);
        adapter.setOnRecyclerViewItemClickListener(new BaseDataBindingAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if(position > 0 && position <= 4) {
                    dialogFragment = new CircleDialog.Builder()
                            .setCanceledOnTouchOutside(false)
                            .setCancelable(true)
                            .setInputManualClose(true)
                            .setTitle("修改标签客户" + vms.get(position).name.get())
                            .setInputHint("请输入")
                            .setInputText(vms.get(position).content.get())
                            .configInput(new ConfigInput() {
                                @Override
                                public void onConfig(InputParams params) {
                                    params.padding = new int[]{20, 20, 20, 20};
                                    params.inputType = InputType.TYPE_CLASS_TEXT;
                                }
                            })
                            .setNegative("取消", null)
                            .setPositiveInput("确定", new OnInputClickListener() {
                                @Override
                                public void onClick(String text, View v) {
                                    if (TextUtils.isEmpty(text)) {
                                        ToastUtil.showToast("信息不能为空");
                                        return;
                                    }
                                    addLabel(text,vms.get(position).tagId.get(),vms.get(position));
                                    dialogFragment.dismiss();
                                }
                            })
                            .show(getSupportFragmentManager());

                }
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void modify(TagSettingVM vm){

    }

    @Override
    public void onRightClick(boolean isModify) {
        super.onRightClick(isModify);
        for(int i = 5 ; i < vms.size() ; i++ ){
            vms.get(i).isModify.set(isModify);
        }
        adapter.notifyDataSetChanged();
    }




    private void deleteLabel(TagSettingVM vm){
        DeleteLabelReq req = new DeleteLabelReq();
        req.labelId = vm.tagId.get();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req,Config.DELETE_LABEL)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp>(){

                })).subscribe(new BaseObserver<BaseResp>() {
            @Override
            protected void onSuccess(BaseResp baseResp) throws Exception {
                vms.remove(vm);
                adapter.notifyDataSetChanged();

                User u = SpUtil.getInstance().getUser();
                for(int i = 0 ; i < u.labelList.size();i++) {
                    if (u.labelList.get(i).id == vm.tagId.get()) {
                        u.labelList.remove(i);
                    }
                }
                SpUtil.getInstance().saveNewsUser(u);

                checkLabelNum();

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
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
                    if (i >= 4) {
                        vm.isModify.set(true);
                    } else {
                        vm.isModify.set(false);
                    }
                    vm.setListener(ClientTagSettingActivity.this);
                    vm.name.set(list.get(i).sort);
                    vm.isShow.set(true);
                    vms.add(vm);
                }

                TagSettingVM vm = new TagSettingVM();
                vm.content.set("未分组客户");
                vm.setListener(ClientTagSettingActivity.this);
                vm.tagId.set("");
                vm.isShow.set(false);
                vm.isModify.set(false);
                vms.add(0,vm);

                adapter.notifyDataSetChanged();

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @Override
    public void onReduce(TagSettingVM vm) {

        LogUtil.d("ClientTagSettingAct","---->delete:" + vm.tagId + ",serviceMsgVM:" + vm.name + ",content:" + vm.content);
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
                params.textColor = getResources().getColor(R.color.color_content);
            }
        })
                .setText("是否删除标签")
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
                        deleteLabel(vm);
                    }
                })
                .configPositive(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.backgroundColorPress = Color.WHITE;
                    }
                })
                .show(getSupportFragmentManager());
    }
}
