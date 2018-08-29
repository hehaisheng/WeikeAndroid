package com.weike.data.business.working;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.business.log.AddLogActivity;
import com.weike.data.business.log.RemindSettingActivity;
import com.weike.data.config.Config;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.req.EditAndDeleteTodoReq;
import com.weike.data.model.req.GetHandleWorkListReq;
import com.weike.data.model.resp.EditAndDeleteTodoResp;
import com.weike.data.model.resp.GetHandleWorkListResp;
import com.weike.data.model.viewmodel.HandleWorkItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.DialogUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 待办事
 */
public class ExpireWorkingFragment extends BaseFragment implements
        HandleWorkItemVM.OnHandleWorkClickListener<HandleWorkItemVM> ,OnRefreshLoadmoreListener,HandleWorkItemVM.ChangeContentListener<HandleWorkItemVM>{

    public CheckBox cb_sort_date;

    public CheckBox cb_sort_level;

    private View loadingView;

    private HandleWorkItemVM lastModifyVM;

    public List<HandleWorkItemVM> vms = new ArrayList<>();

    public BaseDataBindingAdapter recycleAdapter;

    /**
     * 用于1级列表的
     */

    public RecyclerView recycleHandleWorking;

    private SmartRefreshLayout smartRefreshLayout;

    public LinearLayout ll_handle_working_sort;

    private View nothingView;

    private TextView goToAdd;

    private int page = 1;

    private ToDo toDo;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RemindSettingActivity.CODE_OF_REQUEST && resultCode == RESULT_OK && data != null) {
            ToDo toDo = data.getParcelableExtra(RemindSettingActivity.KEY_OF_TODO);
            lastModifyVM.time.set(toDo.birthdaydate);
            lastModifyVM.userName.set(toDo.content);
            updateStatus(1,lastModifyVM);
            recycleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_handle_working;
    }

    @Override
    protected void loadFinish(View view) {
        cb_sort_date = view.findViewById(R.id.checkbox_sort_of_date);
        cb_sort_level = view.findViewById(R.id.checkbox_sort_of_level);
        recycleHandleWorking = view.findViewById(R.id.reycle_handler_working);
        ll_handle_working_sort = view.findViewById(R.id.ll_handle_working_sort);

        ll_handle_working_sort.setVisibility(View.GONE);
        smartRefreshLayout = view.findViewById(R.id.smartrefreshlayout);
        smartRefreshLayout.setOnRefreshLoadmoreListener(this);
        loadingView = view.findViewById(R.id.loaddingview);
        nothingView = view.findViewById(R.id.ll_nothing_view);
        goToAdd = view.findViewById(R.id.tv_add_log);
        goToAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySkipUtil.skipAnotherAct(getActivity(), AddLogActivity.class);
            }
        });




        loadDataOfList(3, page, true);
        initRecycleView();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initRecycleView() {

        recycleHandleWorking.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleAdapter = new BaseDataBindingAdapter(getActivity(), R.layout.widget_handle_working_list_item, vms, BR.handleWorkItemVM);
        recycleHandleWorking.setAdapter(recycleAdapter);


    }

    public void loadDataByLabel(int type, int page, boolean needFresh) {

    }

    public void loadDataOfList(int type, int page, boolean needFresh) {
        loadingView.setVisibility(View.VISIBLE);
        nothingView.setVisibility(View.GONE);

        GetHandleWorkListReq req = new GetHandleWorkListReq();
        req.page = page;
        req.toDoType = type;
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_TO_DO_LIST)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetHandleWorkListResp>>() {

                })).subscribe(new BaseObserver<BaseResp<GetHandleWorkListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetHandleWorkListResp> getHandleWorkListRespBaseResp) throws Exception {
                loadingView.setVisibility(View.GONE);
                if (getHandleWorkListRespBaseResp.getDatas().toDoList.size() == 0) {
                    nothingView.setVisibility(View.VISIBLE);
                    return;
                }

                if (page > 1 && getHandleWorkListRespBaseResp.getDatas().toDoList.size() == 0) {
                    ExpireWorkingFragment.this.page = ExpireWorkingFragment.this.page - 1;//恢复页码
                    ToastUtil.showToast("暂无更多");
                    smartRefreshLayout.finishLoadmore();
                    return;
                }

                if(page == 1) { //如果是第一页
                    vms.clear();
                }


                for (int i = 0; i < getHandleWorkListRespBaseResp.getDatas().toDoList.size(); i++) {
                    HandleWorkItemVM vm = new HandleWorkItemVM();
                    vm.userName.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).clientName);
                    vm.time.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).toDoDate);
                    vm.content.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).content);
                    vm.readVisibility.set(true);
                    vm.id.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).id);
                    vm.setListener(ExpireWorkingFragment.this);
                    vm.setChangeContentListener(ExpireWorkingFragment.this);


                    vms.add(vm);
                }
                recycleAdapter.notifyDataSetChanged();

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }


    @Override
    public void onClick(int type, HandleWorkItemVM handleWorkItemVM) {
        LogUtil.d("test",type+"");

        if (type == TYPE_OF_MODIFY) {

            DialogUtil.showButtonDialog(getActivity().getSupportFragmentManager(), "提示", "是否修改该事项吗?", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modify(handleWorkItemVM);
                }
            });


        } else if (type == TYPE_OF_DELETE) {

            DialogUtil.showButtonDialog(getActivity().getSupportFragmentManager(), "提示", "确定删除该事项吗?", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(handleWorkItemVM);
                }
            });


        }else if(type==TYPE_OF_CHECK){
            DialogUtil.showButtonDialog(getActivity().getSupportFragmentManager(), "提示", "是否完成该事项?", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    read(handleWorkItemVM);
                }
            });

        }
    }

    private void modify(HandleWorkItemVM vm) {


        RemindSettingActivity.startActivity(this,vm.id.get());
        lastModifyVM = vm;
    }

    private void read(HandleWorkItemVM vm) {
        updateStatus(2,vm);
    }

    private void updateStatus(int type , HandleWorkItemVM vm){
        LogUtil.d("acthome","type:"  + type);
        EditAndDeleteTodoReq req = new EditAndDeleteTodoReq();
        req.toDoId = vm.id.get();
        req.toDoType = type;
        req.sign = SignUtil.signData(req);


        RetrofitFactory.getInstance().getService().postAnything(req,Config.EDIT_AND_DEL_TODO_STATUS)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<EditAndDeleteTodoResp>>(){

                })).subscribe(new BaseObserver<BaseResp<EditAndDeleteTodoResp>>() {
            @Override
            protected void onSuccess(BaseResp<EditAndDeleteTodoResp> editAndDeleteTodoRespBaseResp) throws Exception {

                if(type == 1){
                    ToastUtil.showToast("修改事项成功");
                } else if(type == 4){
                    vms.remove(vm);
                }else if(type==2){
                    vms.remove(vm);
                }

                recycleAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    private void delete(HandleWorkItemVM vm) {
          updateStatus(4,vm);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page ++;
        loadDataOfList(3, page, true);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        loadDataOfList(3, page, true);
    }

    @Override
    public void change(HandleWorkItemVM handleWorkItemVM) {
        if(handleWorkItemVM.toBottom.get()){
            handleWorkItemVM.toBottom.set(false);

        }else{
            handleWorkItemVM.toBottom.set(true);
        }

    }
}
