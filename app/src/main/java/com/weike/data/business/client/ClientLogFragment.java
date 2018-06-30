package com.weike.data.business.client;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.business.log.AddLogActivity;
import com.weike.data.config.Config;
import com.weike.data.model.req.GetClientLogByIdReq;
import com.weike.data.model.resp.GetClientLogByIdResp;
import com.weike.data.model.viewmodel.ClientLogItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.TimeUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeoLu on 2018/6/7.
 */

@SuppressLint("ValidFragment")
public class ClientLogFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView recyclerView;

    private List<ClientLogItemVM> vms = new ArrayList<>();

    private BaseDataBindingAdapter adapter;

    private SmartRefreshLayout smartRefreshLayout;



    private String clientId;

    @SuppressLint("ValidFragment")
    public ClientLogFragment(String clientId) {
        this.clientId = clientId;
        LogUtil.d("acthome","id:" + clientId);
    }

    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_client_log;
    }

    @Override
    protected void loadFinish(View view) {



        adapter = new BaseDataBindingAdapter(getContext(),R.layout.widget_log_list_item,vms, BR.clientLogItemVM);
        TextView textView = view.findViewById(R.id.tv_add_log);
        if (TextUtils.isEmpty(clientId)){
            textView.setVisibility(View.GONE);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySkipUtil.skipAnotherAct(getActivity(),AddLogActivity.class);
            }
        });
        recyclerView = view.findViewById(R.id.recycler_log_list);
        smartRefreshLayout = view.findViewById(R.id.smartrefreshlayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        loadData(true,1);

    }

    public void loadData(boolean isFist,final int page){
        GetClientLogByIdReq req = new GetClientLogByIdReq();
        req.id = clientId;
        req.page = page;
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_LOG_BY_ID)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientLogByIdResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetClientLogByIdResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetClientLogByIdResp> getClientLogByIdRespBaseResp) throws Exception {
                for(int i = 0 ; i < getClientLogByIdRespBaseResp.getDatas().getLogList().size();i++){
                    GetClientLogByIdResp.LogListBean bean = getClientLogByIdRespBaseResp.getDatas().getLogList().get(i);
                    ClientLogItemVM vm = new ClientLogItemVM();
                    vm.content.set(bean.getContent());
                    vm.time.set(bean.getLogDate());
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
    }

    @Override
    public void onClick(View view) {

    }
}
