package com.weike.data.business.log;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.reflect.TypeToken;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.params.ProgressParams;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.adapter.CheckedAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.business.client.RelateClientActivity;
import com.weike.data.config.Config;
import com.weike.data.databinding.ActivityAddLogBinding;
import com.weike.data.listener.OnReduceListener;
import com.weike.data.model.business.ClientLog;
import com.weike.data.model.business.ClientRelateForNet;
import com.weike.data.model.business.ClientRelated;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.req.AddLogReq;
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

    public int requestCode = 100;

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

        if(getIntent().getSerializableExtra("clientRelate") != null) {
            ClientRelated clientRelated = (ClientRelated) getIntent().getSerializableExtra("clientRelate");
            RelateCLientItemVM vm = new RelateCLientItemVM();
            vm.name = clientRelated.name;
            vm.id = clientRelated.clientId;
            vm.setLientItemVMOnReduceListener(this);
            clientRelateVMS.add(vm);

        }

    }

    @Override
    public void onRightClick() {


        dialogFragment = new CircleDialog.Builder()
                .setProgressText("正在保存")
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(getSupportFragmentManager());
        addLog();

    }

    public static void startActivity(ClientRelated clientRelated,BaseFragment fragment){
        Intent intent = new Intent(fragment.getContext(),AddLogActivity.class);
        intent.putExtra("clientRelate",clientRelated);
        fragment.startActivityForResult(intent,400);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RemindSettingActivity.CODE_OF_REQUEST  && resultCode == RESULT_OK) {
             toDo = data.getParcelableExtra(RemindSettingActivity.KEY_OF_TODO);
            LogUtil.d("addLog","todo:" + toDo.isRemind);
            logActVM.setToDo(toDo);
        } else if (requestCode == RelateClientActivity.REQUEST_CODE && resultCode == RESULT_OK ){

            ArrayList<ClientRelated> relateds = (ArrayList<ClientRelated>) data.getSerializableExtra("data");

            for(int i = 0 ; i <relateds.size(); i++) {
                RelateCLientItemVM vm = new RelateCLientItemVM();
                vm.name = relateds.get(i).name;
                vm.id = relateds.get(i).clientId;
                vm.setLientItemVMOnReduceListener(this);
                clientRelateVMS.add(vm);
            }
            clientRelateAdapter.notifyDataSetChanged();
        }
    }

    private void addLog(){


        List<ClientRelateForNet> subs = new ArrayList<>();
        for(int  i = 0 ; i < clientRelateVMS.size();i++) {
            ClientRelateForNet related = new ClientRelateForNet();
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
                setResult(RESULT_OK);
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
            RelateClientActivity.startActivity(false,this,RelateClientActivity.REQUEST_CODE);
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

    @Override
    public void onAdd(RelateCLientItemVM relateCLientItemVM) {

    }
}
