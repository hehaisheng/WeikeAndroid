package com.weike.data.business.myself;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.GetIntegralInfoReq;
import com.weike.data.model.resp.GetIntegralInfoResp;
import com.weike.data.model.viewmodel.IntegralItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.GET;

public class MyIntegralActivity extends BaseActivity {

    @BindView(R.id.ry_integral_list)
    public RecyclerView recyclerView;

    @BindView(R.id.tv_integral)
    public TextView integralDetail;

    private List<IntegralItemVM> vms = new ArrayList<>();

    GetIntegralInfoReq req = new GetIntegralInfoReq();

    BaseDataBindingAdapter adapter = new BaseDataBindingAdapter(this,R.layout.widget_integral_detail_item,vms, BR.integralVM);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_integral);
        ButterKnife.bind(this);




        init(true);

     }

    private void init(boolean isFirstLoad){
        req.token = SpUtil.getInstance().getCurrentToken();
        req.page = 1;

        req.sign = SignUtil.signData(req);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.INTEGRAL_INFO)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetIntegralInfoResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetIntegralInfoResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetIntegralInfoResp> getIntegralInfoRespBaseResp) throws Exception {
                for(int i = 0 ; i < getIntegralInfoRespBaseResp.getDatas().integralInfoList.size() ; i++) {
                    IntegralItemVM vm = new IntegralItemVM();

                    vm.time = getIntegralInfoRespBaseResp.getDatas().integralInfoList.get(i).createDate;
                    vm.name = (getIntegralInfoRespBaseResp.getDatas().integralInfoList.get(i).type == 1 ? "购买会员" :"其它");
                    vm.integralNum = (getIntegralInfoRespBaseResp.getDatas().integralInfoList.get(i).status == 1 ? "+" : "-") +
                            getIntegralInfoRespBaseResp.getDatas().integralInfoList.get(i).integralNum;
                    vms.add(vm);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }
}
