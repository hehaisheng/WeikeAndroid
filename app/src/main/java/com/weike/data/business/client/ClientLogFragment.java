package com.weike.data.business.client;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.model.viewmodel.ClientLogItemVM;
import com.weike.data.util.TimeUtil;

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

        for(int i = 0 ; i < 10;i ++){
            ClientLogItemVM vm = new ClientLogItemVM();
            vm.content.set("213213");
            if(i == 3) {
                vm.content.set("21312893u218u32183u1283u812u3812u32189u38921u38921u38912u38912u8932189u3812u38912893");
            }
            vm.time.set(TimeUtil.getTimeFormat());
            vms.add(vm);
        }

        adapter = new BaseDataBindingAdapter(getContext(),R.layout.widget_log_list_item,vms, BR.clientLogItemVM);
        recyclerView = view.findViewById(R.id.recycler_log_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }
}
