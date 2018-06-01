package com.weike.data.business.msg;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.model.viewmodel.MessageItemVM;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by LeoLu on 2018/5/21.
 *
 * 消息的Fragment
 */
public class MsgFragment extends BaseFragment {


    RecyclerView msgList;

    private BaseDataBindingAdapter adapter;

    private List<MessageItemVM> vms = new ArrayList<>();

    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_msg_notice;
    }

    @Override
    protected void loadFinish(View view) {
        msgList  =  view.findViewById(R.id.ry_msg_list);
        msgList.setLayoutManager(new LinearLayoutManager(WKBaseApplication.getInstance().getApplicationContext()));
        adapter = new BaseDataBindingAdapter(WKBaseApplication.getInstance().getApplicationContext(),R.layout.widget_message_item,vms, BR.messageItemVM);
        msgList.setAdapter(adapter);


        for(int i = 0 ; i < 12 ; i++) {
            MessageItemVM vm = new MessageItemVM((Activity) context);
            vm.isReadMsg.set(false);
            vm.content.set("这是内容");
            vm.title.set("这是标题");
            vms.add(vm);
        }

    }
}
