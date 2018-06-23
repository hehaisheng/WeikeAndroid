package com.weike.data.business.working;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;

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
public class HandlerWorkingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener,
        HandleWorkItemVM.OnHandleWorkClickListener<HandleWorkItemVM> ,View.OnClickListener{

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

    private TextView goToAdd;

    private ToDo toDo;



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RemindSettingActivity.CODE_OF_REQUEST && resultCode == RESULT_OK && data != null) {
            ToDo toDo = data.getParcelableExtra(RemindSettingActivity.KEY_OF_TODO);
            lastModifyVM.time.set(toDo.toDoDate);
            lastModifyVM.content.set(toDo.content);
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
        expandableListView = view.findViewById(R.id.lv_handler_working);
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

                for (int i = 0; i < getHandleWorkListRespBaseResp.getDatas().toDoList.size(); i++) {
                    HandleWorkItemVM vm = new HandleWorkItemVM();
                    vm.userName.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).clientName);
                    vm.time.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).toDoDate);
                    vm.content.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).content);
                    vm.id.set(getHandleWorkListRespBaseResp.getDatas().toDoList.get(i).id);
                    vm.setListener(HandlerWorkingFragment.this);
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
            vms.clear();
            cb_sort_level.setChecked(false);
            loadDataOfList(1, 1, true);
        } else if (b && compoundButton.equals(cb_sort_level)) {
            vms.clear();
            cb_sort_date.setChecked(false);
            loadDataOfList(2, 1, true);
        }
    }

    @Override
    public void onClick(int type, HandleWorkItemVM handleWorkItemVM) {
        if (type == TYPE_OF_CHECK) {


            DialogUtil.showButtonDialog(getActivity().getSupportFragmentManager(), "提示", "是否把该事项标记为已处理?", new View.OnClickListener() {
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

                if(type == 2){
                    vm.readClickBg.set(R.mipmap.ic_right_blue);
                    recycleAdapter.notifyDataSetChanged();
                 } else if(type == 4) {
                    vms.remove(vm);
                }  else {
                    ToastUtil.showToast("修改成功");
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
    public void onClick(View view) {

    }
}
