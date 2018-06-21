package com.weike.data.business.msg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.model.viewmodel.MessageItemVM;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LeoLu on 2018/6/1.
 */

public class MsgDetailActivity extends BaseActivity {

    @BindView(R.id.recycle_list)
    public RecyclerView msgDetailList;

    public BaseDataBindingAdapter adapter;

    private List<MessageItemVM>  vms = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_detail);
        ButterKnife.bind(this);

        for(int i = 0 ; i < 10;i++) {
            MessageItemVM vm = new MessageItemVM(this);
            vms.add(vm);
        }

        msgDetailList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseDataBindingAdapter(this,R.layout.widget_layout_msg_detail_list_item,vms, BR.messageDetailItemVM);
        msgDetailList.setAdapter(adapter);
    }
}
