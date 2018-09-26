package com.weike.data.business.client;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.adapter.CloudDataAdapter;
import com.weike.data.base.BaseModelFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.listener.AdapterClickListener;
import com.weike.data.model.req.CloudDataRequest;
import com.weike.data.model.resp.CloudDataResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.ScrollManager;
import com.weike.data.util.SignUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;
import com.weike.data.view.DialogCommonLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CloudDataFragment extends BaseModelFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {


    @BindView(R.id.cloud_data_recycler)
    RecyclerView cloudDataRecycler;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.upload_image)
    TextView uploadImage;
    @BindView(R.id.upload_word)
    TextView uploadWord;
    @BindView(R.id.upload_linear)
    LinearLayout uploadLinear;
    @BindView(R.id.down_image)
    ImageView downImage;
    @BindView(R.id.down_text)
    TextView downText;
    @BindView(R.id.share_image)
    ImageView shareImage;
    @BindView(R.id.share_text)
    TextView shareText;
    @BindView(R.id.rename_image)
    ImageView renameImage;
    @BindView(R.id.rename_text)
    TextView renameText;
    @BindView(R.id.delete_image)
    ImageView deleteImage;
    @BindView(R.id.delete_text)
    TextView deleteText;
    @BindView(R.id.handle_linear)
    RelativeLayout handleLinear;
    @BindView(R.id.common_layout)
    DialogCommonLayout commonLayout;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;



    public CloudDataAdapter cloudDataAdapter;
    public List<CloudDataResp.SubCloudDataResp> subCloudDataRespList = new ArrayList<>();

    public String clientId;
    public AddClientActivity addClientActivity;
    public int page = 1;
    public boolean isRefresh = false;
    public boolean isLoad=false;




    @Override
    public int getLayoutId() {
        return R.layout.fragment_cloud_data;
    }

    @Override
    public void initInstance() {

        addClientActivity = (AddClientActivity) getActivity();
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        cloudDataRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        cloudDataRecycler = ScrollManager.newInstance().setScrollListener(cloudDataRecycler, getActivity());
        ScrollManager.newInstance().setListener(new ScrollManager.ScrollListener() {
            @Override
            public void scroll() {

               if(!isRefresh){
                   isLoad=true;
                   page = page + 1;
                   loadData();
               }else{
                   ToastUtil.showToast("刷新中...");
               }



            }
        });
    }

    @Override
    public void initData() {

        isLoad=true;
        loadData();
    }

    @Override
    public void initEvent() {


        uploadImage.setOnClickListener(this);
        uploadWord.setOnClickListener(this);
        shareImage.setOnClickListener(this);
        shareText.setOnClickListener(this);
        downImage.setOnClickListener(this);
        downText.setOnClickListener(this);
        deleteImage.setOnClickListener(this);
        deleteText.setOnClickListener(this);
        renameImage.setOnClickListener(this);
        renameText.setOnClickListener(this);


    }

    @Override
    public void onRefresh() {

        if(!isLoad&&!isRefresh){
            page = 1;

            swipeLayout.setRefreshing(true);
            isRefresh = true;
            loadData();
        }


    }

    public void loadData() {

        if (addClientActivity.clientId != null) {

            if(isLoad){
                relativeLayout.setVisibility(View.VISIBLE);

            }

            CloudDataRequest req = new CloudDataRequest();
            req.page = page;
            req.sign = SignUtil.signData(req);

            RetrofitFactory.getInstance().getService().postAnything(req, Config.FETCH_IMAGE)
                    .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<CloudDataResp>>() {

                    })).subscribe(new BaseObserver<BaseResp<CloudDataResp>>() {
                @Override
                protected void onSuccess(BaseResp<CloudDataResp> cloudDataResp) throws Exception {

                    if(isLoad){
                        isLoad=false;
                        relativeLayout.setVisibility(View.GONE);
                    }
                    if (isRefresh) {
                        isRefresh = false;
                        swipeLayout.setRefreshing(false);
                    }

                    if (page == 1) {
                        subCloudDataRespList.clear();

                    } else if (page > 1 && cloudDataResp.getDatas().subCloudDataRespList.size() == 0) {
                        ToastUtil.showToast("暂无更多");
                        page = page - 1;
                        return;
                    }


                    subCloudDataRespList.addAll(cloudDataResp.getDatas().subCloudDataRespList);
                    cloudDataAdapter = new CloudDataAdapter(getActivity(), R.layout.cloud_data_item, subCloudDataRespList);

                    cloudDataRecycler.setAdapter(cloudDataAdapter);
                    setCloudDataAdapter();


                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                    if(isLoad){
                        isLoad=false;
                        relativeLayout.setVisibility(View.GONE);
                    }
                    if (isRefresh) {
                        isRefresh = false;
                        swipeLayout.setRefreshing(false);
                    }
                    ToastUtil.showToast("获取失败,请重新刷新");
                }
            });
        }

    }


    public void setCloudDataAdapter() {
        cloudDataAdapter.setListener(new AdapterClickListener() {
            @Override
            public void handleClick(String handleModel, Object object) {
                if (handleModel.equals("image")) {



                } else if (handleModel.equals("text")) {
                    uploadLinear.setVisibility(View.GONE);
                    handleLinear.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.delete_image:
                break;
            case R.id.delete_text:
                break;
            case R.id.down_image:
                break;
            case R.id.down_text:
                break;
            case R.id.rename_image:
                break;
            case R.id.rename_text:
                break;
            case R.id.share_image:
                break;
            case R.id.share_text:
                break;
            case R.id.upload_image:
                break;
            case R.id.upload_word:
                break;


        }

    }


}
