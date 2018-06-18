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
import com.weike.data.config.Config;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.req.EditAndDeleteTodoReq;
import com.weike.data.model.req.GetHandleWorkListReq;
import com.weike.data.model.resp.EditAndDeleteTodoResp;
import com.weike.data.model.resp.GetHandleWorkListResp;
import com.weike.data.model.viewmodel.HandleWorkItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.FileCacheUtils;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 待办事
 */
public class HandlerWorkingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener,
        HandleWorkItemVM.OnHandleWorkClickListener<HandleWorkItemVM> {

    public CheckBox cb_sort_date;

    public CheckBox cb_sort_level;

    private View loadingView;

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

    private ToDo toDo;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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



        loadDataOfList(1, 1, true);
        initRecycleView();
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
            new CircleDialog.Builder()
                    .setCanceledOnTouchOutside(false)
                    .setCancelable(false)
                    .configDialog(new ConfigDialog() {
                        @Override
                        public void onConfig(DialogParams params) {
                            params.backgroundColor = Color.WHITE;
                            params.backgroundColorPress = Color.BLUE;
                        }
                    })
                    .setTitle("提示").configTitle(new ConfigTitle() {
                @Override
                public void onConfig(TitleParams params) {

                }
            })
                    .setText("是否把该事项标记为已处理")
                    .configText(new ConfigText() {
                        @Override
                        public void onConfig(TextParams params) {
                            params.padding = new int[]{100, 0, 100, 50};
                        }
                    })
                    .setNegative("取消", null).configNegative(new ConfigButton() {
                @Override
                public void onConfig(ButtonParams params) {
                    params.backgroundColorPress = Color.WHITE;
                }
            })
                    .setPositive("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            read(handleWorkItemVM);
                        }
                    })
                    .configPositive(new ConfigButton() {
                        @Override
                        public void onConfig(ButtonParams params) {
                            params.backgroundColorPress = Color.WHITE;
                        }
                    })
                    .show(getActivity().getSupportFragmentManager());



        } else if (type == TYPE_OF_MODIFY) {

            modify(handleWorkItemVM);
        } else if (type == TYPE_OF_DELETE) {

            new CircleDialog.Builder()
                    .setCanceledOnTouchOutside(false)
                    .setCancelable(false)
                    .configDialog(new ConfigDialog() {
                        @Override
                        public void onConfig(DialogParams params) {
                            params.backgroundColor = Color.WHITE;
                            params.backgroundColorPress = Color.BLUE;
                        }
                    })
                    .setTitle("提示").configTitle(new ConfigTitle() {
                @Override
                public void onConfig(TitleParams params) {
                        delete(handleWorkItemVM);
                }
            })
                    .setText("是否确定删除该事项")
                    .configText(new ConfigText() {
                        @Override
                        public void onConfig(TextParams params) {
                            params.padding = new int[]{100, 0, 100, 50};
                        }
                    })
                    .setNegative("取消", null).configNegative(new ConfigButton() {
                @Override
                public void onConfig(ButtonParams params) {
                    params.backgroundColorPress = Color.WHITE;
                }
            })
                    .setPositive("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete(handleWorkItemVM);
                        }
                    })
                    .configPositive(new ConfigButton() {
                        @Override
                        public void onConfig(ButtonParams params) {
                            params.backgroundColorPress = Color.WHITE;
                        }
                    })
                    .show(getActivity().getSupportFragmentManager());


        }
    }

    private void modify(HandleWorkItemVM vm) {
        ToDo toDo = new ToDo();
        toDo.toDoDate  = vm.time.get();
        toDo.content = vm.content.get();


    }

    private void read(HandleWorkItemVM vm) {
        updateStatus(1,vm);
    }

    private void updateStatus(int type , HandleWorkItemVM vm){
        EditAndDeleteTodoReq req = new EditAndDeleteTodoReq();
        req.toDoId = vm.id.get();
        req.toDoType = type + "";
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req,Config.EDIT_AND_DEL_TODO_STATUS)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<EditAndDeleteTodoResp>>(){

                })).subscribe(new BaseObserver<BaseResp<EditAndDeleteTodoResp>>() {
            @Override
            protected void onSuccess(BaseResp<EditAndDeleteTodoResp> editAndDeleteTodoRespBaseResp) throws Exception {

                if(type == 1){
                    vm.readClickBg.set(R.mipmap.ic_right_blue);
                    recycleAdapter.notifyDataSetChanged();
                 } else {
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

    }
}
