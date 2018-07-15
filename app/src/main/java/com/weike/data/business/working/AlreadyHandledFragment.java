package com.weike.data.business.working;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigText;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.TextParams;
import com.mylhyl.circledialog.params.TitleParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.business.log.AddLogActivity;
import com.weike.data.business.log.RemindSettingActivity;
import com.weike.data.config.Config;
import com.weike.data.config.DataConfig;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.req.EditAndDeleteTodoReq;
import com.weike.data.model.req.GetHandleWorkListReq;
import com.weike.data.model.resp.EditAndDeleteTodoResp;
import com.weike.data.model.resp.GetHandleWorkListResp;
import com.weike.data.model.viewmodel.HandleWorkItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.DialogUtil;
import com.weike.data.util.FileCacheUtils;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * 待办事
 */
public class AlreadyHandledFragment extends BaseFragment implements
        HandleWorkItemVM.OnHandleWorkClickListener<HandleWorkItemVM> ,OnRefreshLoadmoreListener {

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

    public LinearLayout ll_handle_working_sort;

    private TextView goToAdd;

    private SmartRefreshLayout smartRefreshLayout;

    private View nothingView;

    private ToDo toDo;
    private int page = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RemindSettingActivity.CODE_OF_REQUEST && resultCode == RESULT_OK && data != null) {
            ToDo toDo = data.getParcelableExtra(RemindSettingActivity.KEY_OF_TODO);
            lastModifyVM.time.set(toDo.birthdaydate);
            lastModifyVM.content.set(toDo.content);
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
        smartRefreshLayout = view.findViewById(R.id.smartrefreshlayout);
        smartRefreshLayout.setOnRefreshLoadmoreListener(this);
        cb_sort_date = view.findViewById(R.id.checkbox_sort_of_date);
        cb_sort_level = view.findViewById(R.id.checkbox_sort_of_level);
        recycleHandleWorking = view.findViewById(R.id.reycle_handler_working);
        ll_handle_working_sort = view.findViewById(R.id.ll_handle_working_sort);
        ll_handle_working_sort.setVisibility(View.GONE);
        loadingView = view.findViewById(R.id.loaddingview);
        goToAdd = view.findViewById(R.id.tv_add_log);
        nothingView = view.findViewById(R.id.ll_nothing_view);
        goToAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySkipUtil.skipAnotherAct(getActivity(), AddLogActivity.class);
            }
        });




        loadDataOfList(2, 1, true);
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

                for (int i = 0; i < getHandleWorkListRespBaseResp.getDatas().toDoList.size(); i++) {
                    HandleWorkItemVM vm = new HandleWorkItemVM();
                    vm.readClickBg.set(R.mipmap.ic_right_blue);
                    vm.userName.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).clientName);
                    vm.time.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).toDoDate);
                    vm.content.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).content);
                    vm.id.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).id);
                    vm.setListener(AlreadyHandledFragment.this);

                    int pro = getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).priority;
                    if (pro == DataConfig.RemindLevel.TYPE_OF_HEIGHT) {
                        vm.readClickBg.set(R.mipmap.ic_right_blue);
                    } else if (pro == DataConfig.RemindLevel.TYPE_OF_MID) {
                        vm.readClickBg.set(R.mipmap.ic_right_yellow);
                    } else if (pro == DataConfig.RemindLevel.TYPE_OF_LOAD) {
                        vm.readClickBg.set(R.mipmap.ic_finish_gray);
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
    public void onClick(int type, HandleWorkItemVM handleWorkItemVM) {
        if (type == TYPE_OF_CHECK) {


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

                if(type == 4){ //删除
                    vms.remove(vm);
                } else if (type == 1) {
                    ToastUtil.showToast("修改该事项成功");
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
        loadDataOfList(2, page, true);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadDataOfList(2, page, true);
    }
}
