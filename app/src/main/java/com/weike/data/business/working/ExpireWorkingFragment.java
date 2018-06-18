package com.weike.data.business.working;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;

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
 * 过期事项
 */
public class ExpireWorkingFragment extends BaseFragment {

    public CheckBox cb_sort_date;

    public CheckBox cb_sort_level;


    public List<HandleWorkItemVM> vms = new ArrayList<>();

    public BaseDataBindingAdapter recycleAdapter;

    /**
     * 用于1级列表的
     */

    public RecyclerView recycleHandleWorking;

    /**
     * 用于有两级的
     */

    public ExpandableListView expandableListView;




    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_handle_working;
    }

    @Override
    protected void loadFinish(View view) {
        cb_sort_date = view.findViewById(R.id.checkbox_sort_of_date);
        cb_sort_level = view.findViewById(R.id.checkbox_sort_of_level);
        recycleHandleWorking = view.findViewById(R.id.reycle_handler_working);
        expandableListView = view.findViewById(R.id.lv_handler_working);
        expandableListView.setVisibility(View.GONE);
        cb_sort_date.setVisibility(View.GONE);
        cb_sort_level.setVisibility(View.GONE);



        loadDataOfList(2,1,true);
        initRecycleView();
    }

    private void initRecycleView(){

        recycleHandleWorking.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleAdapter = new BaseDataBindingAdapter(getActivity(),R.layout.widget_handle_working_list_item,vms, BR.handleWorkItemVM);
        recycleHandleWorking.setAdapter(recycleAdapter);


    }



    public void loadDataOfList(int type , int page,boolean needFresh){



        GetHandleWorkListReq req = new GetHandleWorkListReq();
        req.page = page;
        req.toDoType = type;
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_TO_DO_LIST)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetHandleWorkListResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetHandleWorkListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetHandleWorkListResp> getHandleWorkListRespBaseResp) throws Exception {



                for(int i = 0 ; i < getHandleWorkListRespBaseResp.getDatas().toDoList.size();i++) {
                    HandleWorkItemVM vm = new HandleWorkItemVM();
                    vm.userName.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).clientName);
                    vm.time.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).toDoDate);
                    vm.content.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).content);
                    vm.id.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).id);
                    vms.add(vm);
                }
                recycleAdapter.notifyDataSetChanged();

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }


}
