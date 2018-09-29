package com.weike.data.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weike.data.R;
import com.weike.data.model.business.ClientSortModel;

import java.util.List;

public class SendSmsAdapter extends BaseQuickAdapter<ClientSortModel> {


    public SendSmsAdapter(int layoutResId, List<ClientSortModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ClientSortModel clientSortModel) {

        baseViewHolder.setText(R.id.send_client_name,clientSortModel.getName()).setText(R.id.send_client_remark,"尊称:"+clientSortModel.getRemark()).setText(R.id.send_client_phone,"号码:"+clientSortModel.getPhone());
    }
}
