package com.weike.data.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weike.data.R;
import com.weike.data.listener.AdapterClickListener;
import com.weike.data.model.resp.CloudDataResp;

import java.util.List;

public class CloudDataAdapter extends BaseQuickAdapter<CloudDataResp.SubCloudDataResp> {





    public Context context;

    public CloudDataAdapter(Context context,int layoutResId, List<CloudDataResp.SubCloudDataResp> data) {
        this(layoutResId, data);
        this.context=context;
    }


    private CloudDataAdapter(int layoutResId, List<CloudDataResp.SubCloudDataResp> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CloudDataResp.SubCloudDataResp cloudDataResp) {


        ImageView imageView=baseViewHolder.getView(R.id.item_image);


        if(cloudDataResp.getUrl()!=null){
            Glide.with(context).load(cloudDataResp.getUrl()).into(imageView);

        }

        baseViewHolder.setText(R.id.item_name,cloudDataResp.getFileName()).setText(R.id.item_time,cloudDataResp.getCreateDate().toString());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClickListener.handleClick("image",cloudDataResp);
            }
        });
        baseViewHolder.getView(R.id.item_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClickListener.handleClick("text",cloudDataResp);
            }
        });

        baseViewHolder.getView(R.id.item_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClickListener.handleClick("text",cloudDataResp);
            }
        });





    }

    private AdapterClickListener adapterClickListener;
    public void  setListener(AdapterClickListener adapterClickListener){
        this.adapterClickListener=adapterClickListener;
    }
}
