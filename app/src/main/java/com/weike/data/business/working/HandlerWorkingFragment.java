package com.weike.data.business.working;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;

import com.google.gson.reflect.TypeToken;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.GetHandleWorkListReq;
import com.weike.data.model.resp.GetHandleWorkListResp;
import com.weike.data.model.viewmodel.HandleWorkItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.SignUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 待办事
 */
public class HandlerWorkingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    private CheckBox cb_sort_date;
    private CheckBox cb_sort_level;

    private List<HandleWorkItemVM> vms = new ArrayList<>();

    private BaseDataBindingAdapter recycleAdapter;

    /**
     * 用于1级列表的
     */
    private RecyclerView recycleHandleWorking;

    /**
     * 用于有两级的
     */
    private ExpandableListView expandableListView;


    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_handle_working;
    }

    @Override
    protected void loadFinish(View view) {
        cb_sort_date = view.findViewById(R.id.checkbox_sort_of_date);
        cb_sort_level = view.findViewById(R.id.checkbox_sort_of_level);
        cb_sort_level.setOnCheckedChangeListener(this);
        cb_sort_date.setOnCheckedChangeListener(this);



        init();
        initRecycleView();
    }

    private void initRecycleView(){
        recycleHandleWorking.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleAdapter = new BaseDataBindingAdapter(getActivity(),R.layout.widget_handle_working_list_item,vms, BR.handleWorkItemVM);
        recycleHandleWorking.setAdapter(recycleAdapter);


    }

    private void init(){
        GetHandleWorkListReq req = new GetHandleWorkListReq();
        req.page = 1;
        req.toDoType = 1;
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_TO_DO_LIST)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetHandleWorkListResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetHandleWorkListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetHandleWorkListResp> getHandleWorkListRespBaseResp) throws Exception {


            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b && compoundButton.equals(cb_sort_date)) {
            cb_sort_level.setChecked(false);
        } else if (b && compoundButton.equals(cb_sort_level)){
            cb_sort_date.setChecked(false);
        }
    }
}
