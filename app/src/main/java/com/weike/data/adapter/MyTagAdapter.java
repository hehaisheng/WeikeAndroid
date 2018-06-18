package com.weike.data.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.model.viewmodel.RelateCLientItemVM;
import com.weike.data.util.LogUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

public class MyTagAdapter<RelateCLientItemVM> extends TagAdapter {
    Context context;


    public MyTagAdapter(List datas,Context context) {
        super(datas);
        this.context =context;
    }


    @Override
    public View getView(FlowLayout parent, int position, Object o) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_add_log_client_list_item,null, false);
        TextView tv =  view.findViewById(R.id.tv_widget_add_log_client);

        tv.setText("123");


        return view;
    }
}
