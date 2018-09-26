package com.weike.data.business.client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
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
import com.weike.data.business.log.AddLogActivity;
import com.weike.data.business.log.RemindSettingActivity;
import com.weike.data.config.Config;
import com.weike.data.listener.OnReduceListener;
import com.weike.data.model.business.ClientRelated;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.req.DeleteLogReq;
import com.weike.data.model.req.GetClientLogByIdReq;
import com.weike.data.model.req.ModifyLogTodoReq;
import com.weike.data.model.resp.GetClientLogByIdResp;
import com.weike.data.model.resp.ModifyLogTodoResp;
import com.weike.data.model.viewmodel.ClientLogItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.Constants;
import com.weike.data.util.DialogUtil;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by LeoLu on 2018/6/7.
 */

@SuppressLint("ValidFragment")
public class ClientLogFragment extends BaseFragment implements View.OnClickListener ,OnReduceListener<ClientLogItemVM>,OnRefreshLoadmoreListener,ClientLogItemVM.OnChangeListener{

    private RecyclerView recyclerView;

    private List<ClientLogItemVM> vms = new ArrayList<>();

    private BaseDataBindingAdapter adapter;

    private SmartRefreshLayout smartRefreshLayout;

    private int page = 1;

    private String clientId;

    private ClientLogItemVM lastLogVM;

    public android.support.v4.app.DialogFragment dialogFragment;


    @SuppressLint("ValidFragment")
    public ClientLogFragment(String clientId) {
        this.clientId = clientId;
        LogUtil.d("acthome","id:" + clientId);
    }

    public void setClientId(String clientId){
        this.clientId = clientId;
    }

    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_client_log;
    }

    @Override
    public void onRightClick(boolean status) {
        super.onRightClick(status);

        LogUtil.d("LogFragment","--->" + status);
        for(int i  = 0 ; i < vms.size();i++) {
            vms.get(i).isModify.set(status);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 400 && resultCode == RESULT_OK){
            loadData(false,1);
        } else if (requestCode == RemindSettingActivity.CODE_OF_REQUEST && resultCode == RESULT_OK && data != null) {
            ToDo toDo = data.getParcelableExtra(RemindSettingActivity.KEY_LOG_ID);

            if (toDo.isRemind == 1) {
                lastLogVM.remindIcon.set(R.mipmap.ic_remind);
            } else {
                lastLogVM.remindIcon.set(R.mipmap.ic_remind_dis);
            }
            modify(lastLogVM, "" +JsonUtil.GsonString(toDo) + "");
        }else if(requestCode == 600 && resultCode == RESULT_OK){
            loadData(true,1);
        }
    }

    @Override
    protected void loadFinish(View view) {



        adapter = new BaseDataBindingAdapter(getContext(),R.layout.widget_log_list_item,vms, BR.clientLogItemVM);
        TextView textView = view.findViewById(R.id.tv_add_log);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clientId == null) {
                    ToastUtil.showToast("保存客户信息后才能添加日志");
                    return;
                }

                ClientRelated clientRelated = new ClientRelated();
                clientRelated.clientId =clientId;
                clientRelated.name = ((AddClientActivity)getActivity()).vm.userName.get();
                AddLogActivity.startActivity(clientRelated,ClientLogFragment.this);
            }
        });
        recyclerView = view.findViewById(R.id.recycler_log_list);
        smartRefreshLayout = view.findViewById(R.id.smartrefreshlayout);
        smartRefreshLayout.setOnRefreshLoadmoreListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);



        loadData(true,1);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            LogUtil.d(Constants.LOG_DATA,"修改");
            loadData(true,1);

        } else {

        }
    }


    public void loadData(boolean isFist,final int page){

        if(!WKBaseApplication.getInstance().hasNoClientId){

            GetClientLogByIdReq req = new GetClientLogByIdReq();
            req.id = clientId;
            req.page = page;
            req.sign = SignUtil.signData(req);

            RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_LOG_BY_ID)
                    .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientLogByIdResp>>(){

                    })).subscribe(new BaseObserver<BaseResp<GetClientLogByIdResp>>() {
                @Override
                protected void onSuccess(BaseResp<GetClientLogByIdResp> getClientLogByIdRespBaseResp) throws Exception {



                    smartRefreshLayout.finishLoadmore();
                    smartRefreshLayout.finishRefresh();
                    if (page == 1) {
                        vms.clear();
                    } else if (page > 1 && getClientLogByIdRespBaseResp.getDatas().getLogList().size() > 0)  {
                        vms.get(vms.size()-1).isShowLine.set(true);
                    } else if (page > 1 && getClientLogByIdRespBaseResp.getDatas().getLogList().size() == 0) {
                        ToastUtil.showToast("暂无更多");
                        ClientLogFragment.this.page = ClientLogFragment.this.page - 1;//恢复页码
                        return;
                    }


                    for(int i = 0 ; i < getClientLogByIdRespBaseResp.getDatas().getLogList().size();i++){
                        GetClientLogByIdResp.LogListBean bean = getClientLogByIdRespBaseResp.getDatas().getLogList().get(i);
                        ClientLogItemVM vm = new ClientLogItemVM();
                        if (bean.isRemind == 1) {
                            vm.remindIcon.set(R.mipmap.ic_remind);
                        } else {
                            vm.remindIcon.set(R.mipmap.ic_remind_dis);
                        }

                        vm.id = bean.getId() + "";
                        vm.content.set(bean.getContent());
                        vm.time.set(bean.getLogDate());
                        vm.isModify.set(isModify);
                        vm.setListener(ClientLogFragment.this);
                        vm.setOnChangeListener(ClientLogFragment.this);
                        if(i == getClientLogByIdRespBaseResp.getDatas().getLogList().size() - 1) {
                            vm.isShowLine.set(false);
                        } else {
                            vm.isShowLine.set(true);
                        }
                        vms.add(vm);

                    }
                    adapter.notifyDataSetChanged();

                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

                }
            });
        }else{

        }

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onReduce(ClientLogItemVM clientLogItemVM) {
        DialogUtil.showButtonDialog(getFragmentManager(), "提示", "是否删除该日志", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(clientLogItemVM);


            }
        });
    }



    private void delete(ClientLogItemVM del){
        DeleteLogReq req = new DeleteLogReq();
        req.logId = del.id;
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.DELETE_LOG)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientLogByIdResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetClientLogByIdResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetClientLogByIdResp> getClientLogByIdRespBaseResp) throws Exception {
                vms.remove(del);
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    private void modify(ClientLogItemVM modify,String todoStr){
        ModifyLogTodoReq req = new ModifyLogTodoReq();
        req.content = modify.content.get();
        req.logDate = modify.time.get();
        req.logId = modify.id;
        req.remind = todoStr;


        RetrofitFactory.getInstance().getService().postAnything(req, Config.MODIFY_LOG_TODO)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<ModifyLogTodoResp>>(){

                })).subscribe(new BaseObserver<BaseResp<ModifyLogTodoResp>>() {
            @Override
            protected void onSuccess(BaseResp<ModifyLogTodoResp> getClientLogByIdRespBaseResp) throws Exception {
                    ToastUtil.showToast("修改成功");
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @Override
    public void onAdd(ClientLogItemVM clientLogItemVM) {
        DialogUtil.showButtonDialog(getFragmentManager(), "提示", "是否修改该事项?", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastLogVM = clientLogItemVM;
               RemindSettingActivity.startActivity(ClientLogFragment.this,0,clientLogItemVM.id);
            }
        });

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

        page++;
        loadData(false,page);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadData(true,page);
    }

    @Override
    public void change(ClientLogItemVM clientLogItemVM) {

        String[] logContent={clientLogItemVM.id+"",clientLogItemVM.content.get()};
        AddLogActivity.startActivity(logContent,ClientLogFragment.this);

    }
}
