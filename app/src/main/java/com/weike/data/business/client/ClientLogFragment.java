package com.weike.data.business.client;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.GetClientLogByIdReq;
import com.weike.data.model.resp.GetClientLogByIdResp;
import com.weike.data.model.viewmodel.ClientLogItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.SignUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeoLu on 2018/6/7.
 */

@SuppressLint("ValidFragment")
public class ClientLogFragment extends BaseFragment {

    private RecyclerView recyclerView;

    private List<ClientLogItemVM> vms = new ArrayList<>();

    private BaseDataBindingAdapter adapter;

    private String clientId;

    @SuppressLint("ValidFragment")
    public ClientLogFragment(String clientId) {
        this.clientId = clientId;
    }

    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_client_log;
    }

    @Override
    protected void loadFinish(View view) {

        GetClientLogByIdReq req = new GetClientLogByIdReq();
        req.id = clientId;
        req.page = 1;
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
                    vms.add(vm);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });

        adapter = new BaseDataBindingAdapter(getContext(),R.layout.widget_log_list_item,vms, BR.clientLogItemVM);
        recyclerView = view.findViewById(R.id.recycler_log_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }


}
