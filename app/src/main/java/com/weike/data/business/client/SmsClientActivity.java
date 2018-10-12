package com.weike.data.business.client;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigInput;
import com.mylhyl.circledialog.params.InputParams;
import com.mylhyl.circledialog.view.listener.OnInputClickListener;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.business.ClientSortModel;
import com.weike.data.model.req.GetClientListReq;
import com.weike.data.model.resp.GetClientListResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.Constants;
import com.weike.data.util.DialogUtil;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;
import com.weike.data.view.citypicker.PinyinUtils;
import com.weike.data.view.citypicker.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import contactspicker.widget.RBaseViewHolder;
import contactspicker.widget.RGroupItemDecoration;
import contactspicker.widget.RModelAdapter;
import contactspicker.widget.RRecyclerView;

public class SmsClientActivity extends BaseActivity {

    private List<ClientSortModel> datas = new ArrayList<>();
    public List<ClientSortModel>  searchData=new ArrayList<>();
    private RRecyclerView mRecyclerView;
    private RModelAdapter<ClientSortModel> mModelAdapter;


    private DialogFragment dialog;
    private SideBar sideBar;
    public TextView selectText;
    public TextView  dialogText;

    public DialogFragment  dialogFragment;

    public int selectLength;
    public boolean isAllSelect = false;
    public boolean isSearchModel=false;
    public EditText searchEdit;





    private static List<ClientSortModel> sort(List<ClientSortModel> list) {
        Collections.sort(list, new Comparator<ClientSortModel>() {
            @Override
            public int compare(ClientSortModel o1, ClientSortModel o2) {

                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        });
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_data);

        sideBar = findViewById(R.id.sidrbar);
        dialogText = findViewById(R.id.dialog);
        selectText=findViewById(R.id.select_model);
        selectText.setVisibility(View.VISIBLE);
        searchEdit=findViewById(R.id.ed_input_search);
        searchEdit.setVisibility(View.VISIBLE);

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().length() > 0){
                    isSearchModel=true;
                    searchData(s.toString());

                }else{
                    isSearchModel=false;

                    mModelAdapter.resetData(sort(datas));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        selectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                isAllSelect= !isAllSelect;
                if(isAllSelect){
                    selectText.setText("取消");
                }else{
                    selectText.setText("全选");
                }
                if(isAllSelect){

                    LogUtil.d(Constants.LOG_DATA,"是否是全选"+isAllSelect);
                }
                changeAllSelectModel(isAllSelect);

            }
        });
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s, boolean toShow) {

                if (toShow) {

                    if (dialogText != null) {

                        dialogText.setText(s);
                        dialogText.setVisibility(View.VISIBLE);
                    }

                } else {
                    dialogText.setVisibility(View.GONE);
                }
                scrollToLetter(s);
            }
        });


        setCenterText("请选择客户");
        //setLeftText("客户列表");
        setRightText("完成");
        initView();
    }

    @Override
    public void onRightClick() {
        //super.onRightClick();


        List<ClientSortModel> selectorData = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isSelect) {
                selectorData.add(datas.get(i));
            }

        }
        if (selectorData.size() == 0) {
            ToastUtil.showToast("您没有选中客户");

        } else {
            WKBaseApplication.getInstance().clientSortModelList = selectorData;

            finish();

        }


    }


    @Override
    public void onLeftClick() {

        DialogUtil.showButtonDialog(getSupportFragmentManager(), "提示", "是否要保存最新的数据", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ClientSortModel> selectorData = new ArrayList<>();
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).isSelect) {
                        selectorData.add(datas.get(i));
                    }

                }
                WKBaseApplication.getInstance().clientSortModelList = selectorData;
                finish();
            }
        });


    }

    private void scrollToLetter(String letter) {
        if (TextUtils.equals(letter, "#")) {
            ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
            return;
        }
        for (int i = 0; i < mModelAdapter.getAllDatas().size(); i++) {
            if (TextUtils.equals(letter, mModelAdapter.getAllDatas().get(i).getSortLetters())) {
                ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                break;
            }
        }
    }


    public void  reCreateAdapter(){


        mModelAdapter = new RModelAdapter<ClientSortModel>(this) {

            @Override
            protected int getItemLayoutId(int viewType) {
                return R.layout.sms_client_item;
            }

            @Override
            protected void onBindCommonView(final RBaseViewHolder holder, final int position, final ClientSortModel bean) {
                //holder.fillView(bean);

                if(isSearchModel){
                    LogUtil.d(Constants.LOG_DATA,"长度搜索"+position);
                    if (searchData.get(position).isSelect()) {
                        holder.imgV(R.id.check_image).setImageResource(R.drawable.icon_low_box_sel);
                    } else {
                        holder.imgV(R.id.check_image).setImageResource(R.drawable.icon_low_box_nor);
                    }
                }else{

                    if (datas.get(position).isSelect()) {
                        holder.imgV(R.id.check_image).setImageResource(R.drawable.icon_low_box_sel);
                    } else {
                        holder.imgV(R.id.check_image).setImageResource(R.drawable.icon_low_box_nor);
                    }
                }

                holder.textView(R.id.client_name).setText(bean.getName());

                holder.textView(R.id.client_phone).setText(String.format("号码:%s", bean.getPhone()));
                String remark = bean.remark;
                if (remark.equals("")) {
                    holder.textView(R.id.client_remark).setText("");
                } else {
                    holder.textView(R.id.client_remark).setText(String.format("" +
                            "尊称:%s",  bean.getRemark()));
                }


                holder.v(R.id.sms_client_linear).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (remark.equals("")) {

                            dialogFragment = new CircleDialog.Builder()
                                    .setCanceledOnTouchOutside(false)
                                    .setCancelable(true)
                                    .setInputManualClose(true)
                                    .setTitle("添加尊称")
                                    .setInputHint("请输入尊称")
                                    .setInputText(remark)
                                    .configInput(new ConfigInput() {
                                        @Override
                                        public void onConfig(InputParams params) {
                                            params.padding = new int[]{20, 20, 20, 20};
                                            params.inputType = InputType.TYPE_CLASS_TEXT;
                                        }
                                    })
                                    .setNegative("取消", null)
                                    .setPositiveInput("确定", new OnInputClickListener() {
                                        @Override
                                        public void onClick(String text, View v) {
                                            if (TextUtils.isEmpty(text)) {
                                                ToastUtil.showToast("尊称不能为空");
                                                return;
                                            }

                                            changeWholeContent(position,text,true);
                                            holder.textView(R.id.client_remark).setText(text);
                                            holder.imgV(R.id.check_image).setImageResource(R.drawable.icon_low_box_sel);


                                        }
                                    })
                                    .show(getSupportFragmentManager());
                        } else {
                            dialogFragment = new CircleDialog.Builder()
                                    .setCanceledOnTouchOutside(false)
                                    .setCancelable(true)
                                    .setInputManualClose(true)
                                    .setTitle("添加尊称")
                                    .setInputHint("请输入")
                                    .setInputText(remark)
                                    .configInput(new ConfigInput() {
                                        @Override
                                        public void onConfig(InputParams params) {
                                            params.padding = new int[]{20, 20, 20, 20};
                                            params.inputType = InputType.TYPE_CLASS_TEXT;
                                        }
                                    })
                                    .setNegative("取消", null)
                                    .setPositiveInput("确定", new OnInputClickListener() {
                                        @Override
                                        public void onClick(String text, View v) {
                                            if (TextUtils.isEmpty(text)) {
                                                ToastUtil.showToast("尊称不能为空");
                                                return;
                                            }
                                            changeWholeContent(position,text,true);
                                            holder.textView(R.id.client_remark).setText(text);
                                            holder.imgV(R.id.check_image).setImageResource(R.drawable.icon_low_box_sel);

                                        }
                                    }).show(getSupportFragmentManager());

                        }

                    }
                });
                holder.imgV(R.id.check_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (remark.equals("")) {
                            dialogFragment = new CircleDialog.Builder()
                                    .setCanceledOnTouchOutside(false)
                                    .setCancelable(true)
                                    .setInputManualClose(true)
                                    .setTitle("添加尊称")
                                    .setInputHint("请输入")
                                    .setInputText(remark)
                                    .configInput(new ConfigInput() {
                                        @Override
                                        public void onConfig(InputParams params) {
                                            params.padding = new int[]{20, 20, 20, 20};
                                            params.inputType = InputType.TYPE_CLASS_TEXT;
                                        }
                                    })
                                    .setNegative("取消", null)
                                    .setPositiveInput("确定", new OnInputClickListener() {
                                        @Override
                                        public void onClick(String text, View v) {
                                            if (TextUtils.isEmpty(text)) {
                                                ToastUtil.showToast("尊称不能为空");
                                                return;
                                            }
                                            changeWholeContent(position,text,true);
                                            holder.textView(R.id.client_remark).setText(text);
                                            holder.imgV(R.id.check_image).setImageResource(R.drawable.icon_low_box_sel);


                                        }
                                    })
                                    .show(getSupportFragmentManager());
                        } else {

                            if (bean.isSelect) {
                                changeWholeContent(position,"",false);
                                holder.imgV(R.id.check_image).setImageResource(R.drawable.icon_low_box_nor);

                            } else {
                                changeWholeContent(position,"",true);
                                holder.imgV(R.id.check_image).setImageResource(R.drawable.icon_low_box_sel);

                            }

                        }

                    }
                });


            }

            @Override
            protected void onBindModelView(int model, boolean isSelector, RBaseViewHolder holder, int position, ClientSortModel bean) {

            }

            @Override
            protected void onBindNormalView(RBaseViewHolder holder, int position, ClientSortModel bean) {

            }
        };

        mModelAdapter.setModel(RModelAdapter.MODEL_MULTI);//多选模式
        mRecyclerView.setAdapter(mModelAdapter);


        final TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(getResources().getDisplayMetrics().scaledDensity * 20);
        final RectF rectF = new RectF();
        final Rect rect = new Rect();
        mRecyclerView.addItemDecoration(new RGroupItemDecoration(new RGroupItemDecoration.GroupCallBack() {
            @Override
            public int getGroupHeight() {
                return dp2px(20);
            }

            @Override
            public String getGroupText(int position) {
                //空分组测试

                return mModelAdapter.getAllDatas().get(position).getSortLetters();
            }

            @Override
            public void onGroupDraw(Canvas canvas, View view, int position) {
                paint.setColor(Color.parseColor("#ffffff"));

                if (isHorizontal()) {
                    rectF.set(view.getLeft() - getGroupHeight(), view.getTop(), view.getLeft(), view.getBottom());
                } else {
                    rectF.set(view.getLeft(), view.getTop() - getGroupHeight(), view.getRight(), view.getTop());
                }

                canvas.drawRoundRect(rectF, dp2px(2), dp2px(2), paint);
                paint.setColor(Color.parseColor("#bebebe"));

                final String letter = mModelAdapter.getAllDatas().get(position).getSortLetters();
                paint.getTextBounds(letter, 0, letter.length(), rect);

                if (isHorizontal()) {
                    canvas.drawText(letter, view.getLeft() - getGroupHeight() / 2 - rect.width() / 2, view.getBottom() - dp2px(10), paint);
                } else {
                    canvas.drawText(letter, view.getLeft() + dp2px(10), view.getTop() - (getGroupHeight() - rect.height()) / 2 - dp2px(5), paint);
                }
            }

            @Override
            public void onGroupOverDraw(Canvas canvas, View view, int position, int offset) {
                paint.setColor(Color.parseColor("#ffffff"));

                if (isHorizontal()) {
                    rectF.set(-offset, view.getTop(), getGroupHeight() - offset, view.getBottom());
                } else {
                    rectF.set(view.getLeft(), -offset, view.getRight(), getGroupHeight() - offset);
                }

                canvas.drawRoundRect(rectF, dp2px(2), dp2px(2), paint);
                paint.setColor(Color.parseColor("#bebebe"));

                final String letter = mModelAdapter.getAllDatas().get(position).getSortLetters();
                paint.getTextBounds(letter, 0, letter.length(), rect);

                if (isHorizontal()) {
                    canvas.drawText(letter, (getGroupHeight() - rect.width()) / 2 - offset, view.getBottom() - dp2px(10), paint);
                } else {
                    canvas.drawText(letter, view.getLeft() + dp2px(10), (getGroupHeight() + rect.height()) / 2 - dp2px(5) - offset, paint);
                }
            }
        }));
    }
    private void initView() {
        dialog = DialogUtil.showLoadingDialog(getSupportFragmentManager(), "正在加载..");
        mRecyclerView = (RRecyclerView) findViewById(R.id.recycler_view);
        reCreateAdapter();

    }

    private boolean isHorizontal() {
        return ((LinearLayoutManager) mRecyclerView.getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL;
    }

    private int dp2px(int dp) {
        return (int) (getResources().getDisplayMetrics().density) * dp;
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d("test","获取数据");
        startLoading();
    }

    public void startLoading() {

        GetClientListReq req = new GetClientListReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);
        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_CLIENT_LIST)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientListResp>>() {

                })).subscribe(new BaseObserver<BaseResp<GetClientListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetClientListResp> getClientListRespBaseResp) throws Exception {
                dialog.dismiss();
                LogUtil.d(Constants.LOG_DATA,"获取的数据"+ JsonUtil.GsonString(getClientListRespBaseResp));


                datas = filledData(getClientListRespBaseResp.getDatas().allClientList);

                if (WKBaseApplication.getInstance().clientSortModelList != null&&WKBaseApplication.getInstance().clientSortModelList.size()>0) {

                    selectLength = WKBaseApplication.getInstance().clientSortModelList.size();
                    compareData();
                } else {

                    selectLength = 0;
                }


                mModelAdapter.resetData(sort(datas));


            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                dialog.dismiss();
                ToastUtil.showToast("网络错误");
            }
        });
    }


    public void compareData() {

        //全局数据
        List<ClientSortModel> sortModels = WKBaseApplication.getInstance().clientSortModelList;
        if (datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                for (int j = 0; j < sortModels.size(); j++) {
                    String newKey = datas.get(i).getClientId() + "key" + datas.get(i).getPhone();
                    String selectData = sortModels.get(j).getClientId() + "key" + sortModels.get(j).getPhone();
                    if (newKey.equals(selectData)) {
                        ClientSortModel clientSortModel = datas.get(i);
                        clientSortModel.isSelect = true;
                        datas.set(i, clientSortModel);

                    }

                }
            }
        }
    }

    private List<ClientSortModel> filledData(List<GetClientListResp.ClientListSub> date) {
        List<ClientSortModel> mSortList = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            if (date.get(i).phoneNumber.length > 1) {

                for (int j = 0; j < date.get(i).phoneNumber.length; j++) {

                    if (date.get(i).phoneNumber[j].equals("")) {

                    } else {
                        mSortList.add(setClientSortData(date.get(i),date.get(i).phoneNumber[j]));
                    }
                }

            } else {
                mSortList.add(setClientSortData(date.get(i),date.get(i).phoneNumber[0]));

            }
        }

        return mSortList;
    }

    public  ClientSortModel setClientSortData(GetClientListResp.ClientListSub  clientListSub,String phone){
        ClientSortModel sortModel = new ClientSortModel();
        sortModel.setName(clientListSub.userName);
        sortModel.setClientId(clientListSub.id);
        sortModel.setPhotoUrl(clientListSub.photoUrl);
        sortModel.setRemark(clientListSub.remark);
        sortModel.setPhoneNumber(clientListSub.phoneNumber);
        sortModel.setPhone(phone);
        String pinyin = PinyinUtils.getPingYin(clientListSub.userName);
        String alpha=PinyinUtils.getAlpha(clientListSub.userName);
        sortModel.setFirstSpelling(alpha.toLowerCase());
        String sortString = pinyin.substring(0, 1).toUpperCase();
        if (sortString.matches("[A-Z]")) {
            sortModel.setSortLetters(sortString.toUpperCase());
        } else {
            sortModel.setSortLetters("#");
        }
        return  sortModel;
    }



    //全选或取消
    public void changeAllSelectModel(boolean isAllSelect1) {

        if(isSearchModel){
            if (searchData != null && searchData.size() > 0) {

                for(int i=0;i<searchData .size();i++){
                    ClientSortModel clientSortModel=searchData.get(i);
                    if(clientSortModel.getRemark()==null||clientSortModel.getRemark().equals("")){

                    }else{
                        clientSortModel.setSelect(isAllSelect1);
                        searchData.set(i,clientSortModel);
                        changeWhole(clientSortModel);
                    }

                }

                mModelAdapter.resetData(sort(searchData));
            } else {

                ToastUtil.showToast("暂无客户数据");
            }
        }else{
            if (datas != null && datas.size() > 0) {

                for(int i=0;i<datas.size();i++){
                    ClientSortModel clientSortModel=datas.get(i);
                    if(clientSortModel.getRemark()==null||clientSortModel.getRemark().equals("")){

                    }else{
                        clientSortModel.setSelect(isAllSelect1);
                        datas.set(i,clientSortModel);
                    }

                }
                if(isAllSelect1){
                    WKBaseApplication.getInstance().clientSortModelList=datas;
                }else{
                    WKBaseApplication.getInstance().clientSortModelList=null;
                }

                mModelAdapter.resetData(sort(datas));
            } else {

                ToastUtil.showToast("暂无客户数据");
            }
        }


    }

    //搜索到的数据
    public void searchData(String inputString){

        searchData.clear();
        if(datas!=null){
            for(int i=0;i<datas.size();i++){
                if(datas.get(i).getName().contains(inputString)||datas.get(i).getFirstSpelling().contains(inputString)){

                    searchData.add(datas.get(i));
                }
            }

            mModelAdapter.resetData(sort(searchData));
        }

    }


    //改变内容和选择
    public void changeWholeContent(int position,String content,boolean isSelect){


        LogUtil.d(Constants.LOG_DATA,"点击的下"+position);

        if(isSearchModel){
            //在这里也需要改变全部数据的类型
            ClientSortModel clientSortModel = searchData.get(position);
            if(!content.equals("")){
                clientSortModel.remark = content;
            }
            clientSortModel.isSelect = isSelect;
            searchData.set(position, clientSortModel);
            changeWhole(clientSortModel);

            mModelAdapter.resetData(sort(searchData));
            if(dialogFragment!=null){
                dialogFragment.dismiss();
            }

        }else{
            ClientSortModel clientSortModel = datas.get(position);
            if(!content.equals("")){
                clientSortModel.remark = content;
            }
            clientSortModel.isSelect = isSelect;
            datas.set(position, clientSortModel);

            mModelAdapter.resetData(sort(datas));
            if(dialogFragment!=null){
                dialogFragment.dismiss();
            }
        }
    }
    //更改从网络获取的数据
    public void changeWhole(ClientSortModel clientSortModel){
        if (datas!=null&&datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                    String newKey = datas.get(i).getClientId() + "key" + datas.get(i).getPhone();
                    String selectData = clientSortModel.getClientId() + "key" + clientSortModel.getPhone();
                    if (newKey.equals(selectData)) {
                        ClientSortModel clientSortModel1 = datas.get(i);
                        clientSortModel1.isSelect = clientSortModel.isSelect;
                        datas.set(i, clientSortModel1);

                    }
            }
        }
    }



}
