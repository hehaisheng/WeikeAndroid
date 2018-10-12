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
    public  List<CloudDataResp.SubCloudDataResp>  cloudDataRespList;

    public CloudDataAdapter(Context context,int layoutResId, List<CloudDataResp.SubCloudDataResp> data) {
        this(layoutResId, data);
        this.context=context;
        this.cloudDataRespList=data;
    }


    private CloudDataAdapter(int layoutResId, List<CloudDataResp.SubCloudDataResp> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CloudDataResp.SubCloudDataResp cloudDataResp) {


        ImageView imageView=baseViewHolder.getView(R.id.item_image);
        ImageView circleImage=baseViewHolder.getView(R.id.item_circle);
        circleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index=fetchIndex(cloudDataResp.getUrl());
                CloudDataResp.SubCloudDataResp subCloudDataResp=cloudDataRespList.get(index);
               if(cloudDataResp.isSelect()){

                   subCloudDataResp.setSelect(false);
                   circleImage.setImageResource(R.drawable.icon_low_box_nor);

               }else{
                   subCloudDataResp.setSelect(true);
                   circleImage.setImageResource(R.drawable.icon_low_box_sel);

               }
                cloudDataRespList.set(index,subCloudDataResp);
                CloudDataAdapter.this.notifyItemChanged(index,subCloudDataResp);
                adapterClickListener.handleClick("select",subCloudDataResp);

            }
        });


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


    //删除
    public void delete(String key){
        int deleteIndex=-1;
        for(int i=0;i<cloudDataRespList.size();i++){
            if(cloudDataRespList.get(i).getUrl().equals(key)){
                deleteIndex=i;
            }
        }
        if(deleteIndex!=-1){
            cloudDataRespList.remove(deleteIndex);
        }
    }
    //获取下标
    public   int fetchIndex(String key){

        for(int i=0;i<cloudDataRespList.size();i++){
            if(cloudDataRespList.get(i).getUrl().equals(key)){
                return i;
            }
        }
        return -1;

    }

    //加载更多
    public void  add( List<CloudDataResp.SubCloudDataResp>  cloudDataResps){

        int length=cloudDataRespList.size();
        cloudDataRespList.addAll(cloudDataResps);
        CloudDataAdapter.this.notifyItemRangeChanged(length,cloudDataRespList.size()-length);

    }
    private AdapterClickListener adapterClickListener;
    public void  setListener(AdapterClickListener adapterClickListener){
        this.adapterClickListener=adapterClickListener;
    }
}
