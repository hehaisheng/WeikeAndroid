package com.weike.data.business.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.model.viewmodel.LabelOptionItemVM;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientTagActivity extends BaseActivity {

    public static final String[] tagLabel = {
            "A","B","C","D","E","F","G","H","J","K","L","M","N","O","P",
            "Q","R","S","T","U","V","W","X","Y","Z"

    };

    private List<LabelOptionItemVM> optionItemVMS = new ArrayList<>();

    private int lastClickPosition = 0;



    @BindView(R.id.recyclerview_client_option)
    public RecyclerView recycle_option;

    @BindView(R.id.recyclerview_client_content)
    public RecyclerView recycle_client_list;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_tag);
        ButterKnife.bind(this);

        setCenterText("");
        setRightText("");
        setLeftText("客户标签");


        initRecycleOption();
    }
    public void initRecycleOption(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        BaseDataBindingAdapter adapter = new BaseDataBindingAdapter(this,R.layout.widget_client_tag_option,optionItemVMS, BR.tagOptionVM);
        for(int i = 0 ; i< 5;i++) {
            LabelOptionItemVM vm = new LabelOptionItemVM();
            vm.text.set("随便客户");
            if(i == 0) {
                vm.isClick.set(true);
            }else {
                vm.isClick.set(false);
            }
            optionItemVMS.add(vm);
        }
        adapter.setOnRecyclerViewItemClickListener(new BaseDataBindingAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                optionItemVMS.get(lastClickPosition).isClick.set(false);
                optionItemVMS.get(position).isClick.set(true);
                lastClickPosition = position;
            }
        });
        recycle_option.setLayoutManager(manager);
        recycle_option.setAdapter(adapter);
    }

}
