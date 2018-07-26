package com.weike.data.business.working;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.adapter.ExpandableAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.business.log.AddLogActivity;
import com.weike.data.business.log.RemindSettingActivity;
import com.weike.data.config.Config;
import com.weike.data.config.DataConfig;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.req.EditAndDeleteTodoReq;
import com.weike.data.model.req.GetClientTagListReq;
import com.weike.data.model.req.GetHandleWorkListReq;
import com.weike.data.model.req.GetToDoByTagReq;
import com.weike.data.model.resp.EditAndDeleteTodoResp;
import com.weike.data.model.resp.GetClientTagListResp;
import com.weike.data.model.resp.GetHandleWorkListResp;
import com.weike.data.model.resp.GetTodoByTagResp;
import com.weike.data.model.viewmodel.ExpandGroupVM;
import com.weike.data.model.viewmodel.HandleWorkItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.ClientTagComparator;
import com.weike.data.util.DialogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 待办事
 */
public class HandlerWorkingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener,
        HandleWorkItemVM.OnHandleWorkClickListener<HandleWorkItemVM> ,View.OnClickListener,OnRefreshLoadmoreListener{

    public CheckBox cb_sort_date;

    public CheckBox cb_sort_level;

    private View loadingView;

    private View nothing;

    private HandleWorkItemVM lastModifyVM;

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

    private ExpandableAdapter adapter;

    private TextView goToAdd;

    private boolean isLeft =false;

    private ToDo toDo;

    private int groupPosition;

    private int page;

    DialogFragment dialogFragment;
    private List<List<HandleWorkItemVM>> childVMs = new ArrayList<>();
    private List<ExpandGroupVM> groupVMS = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RemindSettingActivity.CODE_OF_REQUEST && resultCode == RESULT_OK && data != null) {
            ToDo toDo = data.getParcelableExtra(RemindSettingActivity.KEY_OF_TODO);

            if (lastModifyVM.type == 1) {
                lastModifyVM.time.set(toDo.birthdaydate);
                lastModifyVM.content.set(toDo.content);
                recycleAdapter.notifyDataSetChanged();
            } else {
                lastModifyVM.time.set(toDo.birthdaydate);
                lastModifyVM.content.set(toDo.content);
                expandableListView.collapseGroup(groupPosition);
                expandableListView.expandGroup(groupPosition);
            }

        }
    }



    public void initLabel() {


        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                HandlerWorkingFragment.this.groupPosition = groupPosition;

                if ( expandableListView.isGroupExpanded(groupPosition) ) {
                    expandableListView.collapseGroup(groupPosition);
                    return true;
                }


                dialogFragment = DialogUtil.showLoadingDialog(getFragmentManager(),"正在加载");
                getLabelToDo(groupPosition,groupVMS.get(groupPosition).id);

                return true;
            }
        });





        GetClientTagListReq req = new GetClientTagListReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);
        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_CLIENT_TAG_LIST)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientTagListResp>>() {

                })).subscribe(new BaseObserver<BaseResp<GetClientTagListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetClientTagListResp> getClientTagListRespBaseResp) throws Exception {
                List<GetClientTagListResp.TagSub> list = getClientTagListRespBaseResp.getDatas().clientLabelList;

                Collections.sort(list, new ClientTagComparator());
                for (int i = 0; i < list.size(); i++) {
                    ExpandGroupVM vm = new ExpandGroupVM();
                    vm.name.set(list.get(i).sort + "." + list.get(i).labelName);
                    vm.id = list.get(i).id;
                    groupVMS.add(vm);
                }

                ExpandGroupVM vm = new ExpandGroupVM();
                vm.name.set("未分组客户");
                vm.id = "0";
                groupVMS.add(0,vm);


                Display display = getActivity().getWindowManager().getDefaultDisplay();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                display.getMetrics(displayMetrics);
                int widthPixels = displayMetrics.widthPixels;

                int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();

                for(int i = 0 ; i < groupVMS.size();i++) {
                    List<HandleWorkItemVM> vms = new ArrayList<>();
                    childVMs.add(vms);
                }

                adapter = new ExpandableAdapter(groupVMS,childVMs,R.layout.widget_expandable_group_tag,R.layout.widget_handle_working_list_item,BR.expanableVM,BR.handleWorkItemVM);
                expandableListView.setAdapter(adapter);
                expandableListView.setIndicatorBounds(width - 120, width - 30);

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    private void getLabelToDo(int groupPosition ,String labelId){
        GetToDoByTagReq req = new GetToDoByTagReq();
        req.lableId = labelId;
        req.page = 1;
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_TODO_BY_TAG)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetTodoByTagResp>>() {

                })).subscribe(new BaseObserver<BaseResp<GetTodoByTagResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetTodoByTagResp> getClientTagListRespBaseResp) throws Exception {
                dialogFragment.dismiss();
                List<HandleWorkItemVM>  cs = childVMs.get(groupPosition);
                cs.clear();

                for (int i = 0; i < getClientTagListRespBaseResp.getDatas().toDoList.size(); i++) {
                    HandleWorkItemVM vm = new HandleWorkItemVM();
                    vm.userName.set(getClientTagListRespBaseResp.getDatas().toDoList.get(i).clientName);
                    vm.time.set(getClientTagListRespBaseResp.getDatas().toDoList.get(i).toDoDate);
                    vm.content.set(getClientTagListRespBaseResp.getDatas().toDoList.get(i).content);
                    vm.id.set(getClientTagListRespBaseResp.getDatas().toDoList.get(i).id);
                    vm.setListener(HandlerWorkingFragment.this);
                    vm.type = 2;

                    int pro = getClientTagListRespBaseResp.getDatas().toDoList.get(i).priority;
                    if (pro == DataConfig.RemindLevel.TYPE_OF_HEIGHT) {
                        vm.readClickBg.set(R.mipmap.ic_finish_red);
                    } else if (pro == DataConfig.RemindLevel.TYPE_OF_MID) {
                        vm.readClickBg.set(R.mipmap.ic_right_yellow);
                    } else if (pro == DataConfig.RemindLevel.TYPE_OF_LOAD) {
                        vm.readClickBg.set(R.mipmap.ic_right_blue);
                    }
                    cs.add(vm);
                }

                if (cs.size() == 0) {
                    ToastUtil.showToast("暂无数据");
                    expandableListView.collapseGroup(groupPosition);
                    return;
                }

                expandableListView.expandGroup(groupPosition, true);



            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
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
        expandableListView = view.findViewById(R.id.lv_handler_working);
        Drawable d = getResources().getDrawable(R.color.color_touming);
        expandableListView.setDivider(d);
        expandableListView.setChildDivider(d);
        loadingView = view.findViewById(R.id.loaddingview);
        cb_sort_level.setOnCheckedChangeListener(this);
        cb_sort_date.setOnCheckedChangeListener(this);
        nothing = view.findViewById(R.id.ll_nothing_view);
        goToAdd = view.findViewById(R.id.tv_add_log);
        goToAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySkipUtil.skipAnotherAct(getActivity(), AddLogActivity.class);
            }
        });


        loadDataOfList(1, 1, true);
        initRecycleView();
        initLabel();

        cb_sort_date.setClickable(false);
        cb_sort_level.setClickable(true);
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



    public void loadDataOfList(int type, int page, boolean needFresh) {
        loadingView.setVisibility(View.VISIBLE);
        nothing.setVisibility(View.GONE);

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
                if(getHandleWorkListRespBaseResp.getDatas().toDoList.size() == 0) {
                    nothing.setVisibility(View.VISIBLE);
                    return;
                }

                if (page == 1) {
                    vms.clear();
                }

                for (int i = 0; i < getHandleWorkListRespBaseResp.getDatas().toDoList.size(); i++) {
                    HandleWorkItemVM vm = new HandleWorkItemVM();
                    vm.userName.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).clientName);
                    vm.time.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).toDoDate);
                    vm.content.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).content);
                    vm.id.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).id);
                    vm.setListener(HandlerWorkingFragment.this);
                    int pro = getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).priority;
                    if (pro == DataConfig.RemindLevel.TYPE_OF_HEIGHT) {
                        vm.readClickBg.set(R.mipmap.ic_finish_red);
                    } else if (pro == DataConfig.RemindLevel.TYPE_OF_MID) {
                        vm.readClickBg.set(R.mipmap.ic_right_yellow);
                    } else if (pro == DataConfig.RemindLevel.TYPE_OF_LOAD) {
                        vm.readClickBg.set(R.mipmap.ic_right_blue);
                    }
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
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b && compoundButton.equals(cb_sort_date)) {

            cb_sort_level.setChecked(false);
            expandableListView.setVisibility(View.GONE);
            recycleHandleWorking.setVisibility(View.VISIBLE);
            loadDataOfList(1,1,true);
            cb_sort_date.setClickable(false);
            cb_sort_level.setClickable(true);
        } else if (b && compoundButton.equals(cb_sort_level)) {
            compoundButton.setChecked(true);
            nothing.setVisibility(View.GONE);
            recycleHandleWorking.setVisibility(View.GONE);
            expandableListView.setVisibility(View.VISIBLE);
            cb_sort_date.setChecked(false);

            cb_sort_date.setClickable(true);
            cb_sort_level.setClickable(false);
        }
    }

    @Override
    public void onClick(int type, HandleWorkItemVM handleWorkItemVM) {
        if (type == TYPE_OF_CHECK) {

            DialogUtil.showButtonDialog(getActivity().getSupportFragmentManager(), "提示", "事情是否已办?", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    read(handleWorkItemVM);
                }
            });

        } else if (type == TYPE_OF_MODIFY) {

            DialogUtil.showButtonDialog(getActivity().getSupportFragmentManager(), "提示", "是否修改该事项?", new View.OnClickListener() {
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

            DialogUtil.showButtonDialog(getActivity().getSupportFragmentManager(), "提示", "是否确定删除该事项?", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(handleWorkItemVM);
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
        EditAndDeleteTodoReq req = new EditAndDeleteTodoReq();
        req.toDoId = vm.id.get();
        req.toDoType = type ;
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req,Config.EDIT_AND_DEL_TODO_STATUS)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<EditAndDeleteTodoResp>>(){

                })).subscribe(new BaseObserver<BaseResp<EditAndDeleteTodoResp>>() {
            @Override
            protected void onSuccess(BaseResp<EditAndDeleteTodoResp> editAndDeleteTodoRespBaseResp) throws Exception {

                if (vm.type == 1) { //这是
                    if (type == 2) {
                        vms.remove(vm);
                        recycleAdapter.notifyDataSetChanged();
                    } else if (type == 4) {
                        vms.remove(vm);
                        recycleAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showToast("修改成功");
                    }
                } else {
                    childVMs.get(groupPosition).remove(vm);
                    expandableListView.collapseGroup(groupPosition);
                    expandableListView.expandGroup(groupPosition);
                }


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
    public void onClick(View view) {

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

    }
}
