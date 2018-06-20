package com.weike.data.business.log;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.reflect.TypeToken;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.ProgressParams;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.adapter.CheckedAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.databinding.ActivityAddLogBinding;
import com.weike.data.listener.OnReduceListener;
import com.weike.data.model.business.ClientLog;
import com.weike.data.model.business.ClientRelated;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.req.AddLogReq;
import com.weike.data.model.resp.GetClientListResp;
import com.weike.data.model.viewmodel.AddLogActVM;
import com.weike.data.model.viewmodel.RelateCLientItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by LeoLu on 2018/5/30.
 * 新建日志页面
 */
public class AddLogActivity extends BaseActivity implements  OnReduceListener<RelateCLientItemVM> {

    public static String keyOfLog = "AddLogActivity";
    ActivityAddLogBinding binding;
    private ToDo toDo;
    private ClientLog clientLog;
    AddLogActVM logActVM;

    DialogFragment dialogFragment ;

    List<RelateCLientItemVM> clientRelateVMS = new ArrayList<>();

    /**
     *
     */
    BaseDataBindingAdapter clientRelateAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_log);
        logActVM = new AddLogActVM(this);

        binding.setAddLogVM(logActVM);
        binding.btnAddClientRelate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRelate();
            }
        });
        setCenterText("");
        setRightText("保存");

        if(clientLog !=null)
            setLeftText("编辑日志");
        else
            setLeftText("添加日志");

        initFlowLayout();;
    }

    /**
     * 加载客户关联的layout
     */
    public void initFlowLayout(){


        clientRelateAdapter = new BaseDataBindingAdapter(this,R.layout.widget_add_log_client_list_item, clientRelateVMS,BR.relateClientItemVM);

        binding.tagFlowLayout.setLayoutManager(new GridLayoutManager(this,3));
        binding.tagFlowLayout.setAdapter(clientRelateAdapter);

    }

    @Override
    public void onRightClick() {


        dialogFragment = new CircleDialog.Builder()
                .setProgressText("正在保存")
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(getSupportFragmentManager());
        addLog();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RemindSettingActivity.CODE_OF_REQUEST  && resultCode == RESULT_OK) {
            toDo = data.getParcelableExtra(RemindSettingActivity.KEY_OF_TODO);
            logActVM.setToDo(toDo);
        }
    }

    private void addLog(){




        List<ClientRelated> subs = new ArrayList<>();
        for(int  i = 0 ; i < clientRelateVMS.size();i++) {
            ClientRelated related = new ClientRelated();
            related.clientId = clientRelateVMS.get(i).id;
            subs.add(related);
        }

        if (toDo == null) {
            dialogFragment.dismiss();
            ToastUtil.showToast("请设置您的提醒");
            return;
        }

        if (subs.size() == 0) { //没有关联客户
            ToastUtil.showToast("至少关联一个客户");
            dialogFragment.dismiss();
            return;
        }
        if (TextUtils.isEmpty(logActVM.time.get())) {
            ToastUtil.showToast("请设置时间");
            dialogFragment.dismiss();
            return;
        }


        AddLogReq addLogReq = new AddLogReq();
        addLogReq.logDate =  logActVM.time.get();
        addLogReq.content = logActVM.content.get();
        addLogReq.clientArr = "" + JsonUtil.GsonString(subs) + "";
        addLogReq.toDo = JsonUtil.GsonString(toDo);
        addLogReq.sign = SignUtil.signData(addLogReq);

        RetrofitFactory.getInstance().getService().postAnything(addLogReq, Config.ADD_LOG)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp>(){

                })).subscribe(new BaseObserver<BaseResp>() {
            @Override
            protected void onSuccess(BaseResp baseResp) throws Exception {
                dialogFragment.dismiss();
                finish();
                ToastUtil.showToast("添加成功");
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }


    public void addRelate(){
        if(SpUtil.getInstance().getUser().clietList.size() == 0) {
            ToastUtil.showToast("您还没有客户,不能添加日志");
            return;
        } else  {

            if (toDo == null) {

            }



            List<GetClientListResp.ClientListSub> subList = SpUtil.getInstance().getUser().clietList;

            String[]  array = new String[subList.size()];
            String[] ids = new String[subList.size()];

            for(int i = 0 ; i <subList.size() ; i++) {
                array[i] = subList.get(i).userName;
                ids[i] = subList.get(i).id;
            };

            final CheckedAdapter checkedAdapter = new CheckedAdapter(this, array);

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
                            addClient(checkedAdapter);
                        }
                    })
                    .show(getSupportFragmentManager());
        }
    }

    private void addClient(CheckedAdapter checkedAdapter){
        if (checkedAdapter.getids().size() == 0) return; //如果选择到什么都没有 不管 否则删掉旧的 重新来
        clientRelateVMS.clear();


        for(int i = 0 ; i <checkedAdapter.getids().size(); i++) {
            RelateCLientItemVM vm = new RelateCLientItemVM();
            vm.name = checkedAdapter.getSaveChecked().valueAt(i);
            vm.id = checkedAdapter.getids().valueAt(i);
            vm.setLientItemVMOnReduceListener(this);
            clientRelateVMS.add(vm);
        }
        clientRelateAdapter.notifyDataSetChanged();
    }


    @Override
    public void onReduce(RelateCLientItemVM relateCLientItemVM) {
        clientRelateVMS.remove(relateCLientItemVM);
        clientRelateAdapter.notifyDataSetChanged();
    }
}
