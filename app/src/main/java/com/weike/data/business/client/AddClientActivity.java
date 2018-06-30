package com.weike.data.business.client;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;

import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.adapter.FragmentBaseAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.databinding.ActivityClientAddBinding;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.req.AddClientReq;
import com.weike.data.model.req.GetClientDetailMsgReq;
import com.weike.data.model.resp.AddClientResp;
import com.weike.data.model.resp.GetClientDetailMsgResp;
import com.weike.data.model.resp.UpLoadFileResp;
import com.weike.data.model.viewmodel.AddClientActVM;
import com.weike.data.model.viewmodel.ClientBaseMsgVM;
import com.weike.data.model.viewmodel.ClientServiceMsgVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.DialogUtil;
import com.weike.data.util.LQRPhotoSelectUtils;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import java.io.File;
import java.util.ArrayList;

import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by LeoLu on 2018/5/31.
 * 添加客户和修改客户信息都是这个Activity
 */
public class AddClientActivity extends BaseActivity {

    LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    /**
     * 用于获取标签id
     */
    public static final int KEY_OF_LABEL = 200;

    public ActivityClientAddBinding binding;

    public AddClientActVM vm;

    private AddClientReq addClientReq = new AddClientReq();



    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        mLqrPhotoSelectUtils.takePhoto();
    }


    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        mLqrPhotoSelectUtils.selectPhoto();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        LogUtil.d("AddClientActivity",requestCode + "," + resultCode  + "," + data );
        if (requestCode == KEY_OF_LABEL && resultCode == RESULT_OK && data != null) {
            String labelName = data.getStringExtra("labelName");
            String labelId = data.getStringExtra("labelId");
            vm.label.set(labelName);
            vm.labelId = labelId;
        }

        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);

    }

    private String currentPath;

    private boolean isUpdatePic = false;

    /**
     * 如果有客户ID 那么就是修改
     */
    public String clientId = null;

    /**
     * 当前的position
     */
    private int position = 0;

    /**
     * tabLayout标题
     */
    public String[] titles = {"基本信息","服务信息","跟踪日志"};

    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    boolean isModify = false;

    public static final String TAG_CLIENT_ID = "com.weike.data.TAG_CLIENT_ID";


    public static void startActivity(Activity activity,String clientId){
        Intent intent = new Intent(activity,AddClientActivity.class);
        intent.putExtra(TAG_CLIENT_ID,clientId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_client_add);
        vm = new AddClientActVM(this);
        binding.setAddClientVM(vm);

        setCenterText("");
        addFragment();
        setLeftText("添加客户");
        initPhotoSel();
        initMsg();


        initClickStatus();
    }

    /**
     * 状态初始化
     */
    private void initClickStatus() {
        boolean status = false;
        if (clientId != null) {
            setRightText("编辑");
            status = false;
        } else {
            status = true;
            setRightText("完成");
        }
        isModify = status;
        vm.isModify.set(isModify);
        ClientBaseMsgFragment fragment = (ClientBaseMsgFragment) fragments.get(0);
        fragment.isModify = status;
        ClientServiceMsgFragment serviceMsgFragment = (ClientServiceMsgFragment) fragments.get(1);
        serviceMsgFragment.isModify = status;


    }

    /**
     * 初始化图片选择
     */
    private void initPhotoSel(){
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                // 4、当拍照或从图库选取图片成功后回调
                vm.photoUrl.set(outputUri.getPath());
                isUpdatePic = true;

            }
        }, true);
        vm.setmLqrPhotoSelectUtils(mLqrPhotoSelectUtils);

    }








    private void addFragment() {

        clientId = getIntent().getStringExtra(TAG_CLIENT_ID);

        ClientBaseMsgFragment clientBaseMsgFragment = new ClientBaseMsgFragment();
        ClientServiceMsgFragment serviceMsgFragment = new ClientServiceMsgFragment();

        ClientLogFragment clientLogFragment = new ClientLogFragment(clientId);

        fragments.add(clientBaseMsgFragment);
        fragments.add(serviceMsgFragment);
        fragments.add(clientLogFragment);

        binding.viewpagerActivityClientAdd.setOffscreenPageLimit(3);
        FragmentBaseAdapter adapter = new FragmentBaseAdapter(getSupportFragmentManager(),fragments,titles);
        binding.viewpagerActivityClientAdd.setAdapter(adapter);
        binding.slidingTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                AddClientActivity.this.position = position;

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        binding.slidingTabLayout.setViewPager(binding.viewpagerActivityClientAdd);


    }


    /**
     * 初始化消息
     */
    private void initMsg(){
        if (TextUtils.isEmpty(getIntent().getStringExtra(TAG_CLIENT_ID))){
            return;
        }


        GetClientDetailMsgReq req = new GetClientDetailMsgReq();
        req.clientId = getIntent().getStringExtra(TAG_CLIENT_ID);
        req.sign = SignUtil.signData(req);


        RetrofitFactory.getInstance().getService().postAnything(req,Config.GET_CLIENT_DETAIL)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientDetailMsgResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetClientDetailMsgResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetClientDetailMsgResp> getClientDetailMsgRespBaseResp) throws Exception {
                   GetClientDetailMsgResp data = getClientDetailMsgRespBaseResp.getDatas();

                    vm.photoUrl.set(data.getPhotoUrl());
                    vm.userName.set(data.getUserName());
                    vm.remarks.set(data.getRemark());
                    vm.label.set(data.getClientLabelName());

                    //基本信息
                    ClientBaseMsgFragment clientBaseMsgFragment = (ClientBaseMsgFragment) fragments.get(0);
                    ClientServiceMsgFragment serviceMsgFragment = (ClientServiceMsgFragment) fragments.get(1);
                    ClientBaseMsgVM clientBaseMsgVM = clientBaseMsgFragment.clientBaseMsgVM;
                    ClientServiceMsgVM clientServiceMsgVM = serviceMsgFragment.serviceMsgVM;

                    String[] phone = new String[5];
                    phone[0] = data.getOnePhoneNumber();
                    phone[1] = data.getTwoPhoneNumber();
                    phone[2] = data.getThreePhoneNumber();
                    phone[3] = data.getFourPhoneNumber();
                    phone[4] = data.getFivePhoneNumber();
                    clientBaseMsgFragment.setPhoneNum(phone); //电话号码

                    clientBaseMsgVM.email.set(data.getEmail());
                    clientBaseMsgVM.companyName.set(data.getCompany());
                    clientBaseMsgVM.companyLocation.set(data.getCompanyDetailAddress());
                    clientBaseMsgVM.sex.set( Integer.parseInt(data.getSex()) == 1 ? "男" : "女");
                    clientBaseMsgVM.birthday.set(data.getBirthday());
                    clientBaseMsgVM.marry.set(Integer.parseInt(data.getMarriage()) == 1 ? "已婚": "未婚");
                    clientBaseMsgVM.idCard.set(data.getIdCard());
                    clientBaseMsgVM.son.set(data.getSonNum());
                    clientBaseMsgVM.job.set(data.getOffice());
                    clientBaseMsgVM.houseLocation.set(data.getHomeDetailAddress());
                    clientBaseMsgVM.gril.set(data.getDaughterNum());
                    clientBaseMsgVM.clientHeight.set(data.getHeight());
                    clientBaseMsgVM.clientWidght.set(data.getWeight());
                    ToDo toDo = new ToDo();
                    toDo.dateType = data.getBirthdayjson().getDateType();
                    toDo.priority = data.getBirthdayjson().getPriority();
                    toDo.toDoDate = data.getBirthdayjson().getRemindDate();
                    toDo.beforeRemindDay = data.getBirthdayjson().getBeforeRemindDay();
                    toDo.isRepeat = data.getBirthdayjson().getIsRepeat();
                    toDo.isRemind = data.getBirthdayjson().getIsRemind();
                    toDo.repeatIntervalHour = data.getBirthdayjson().getRepeatIntervalHour();
                    clientBaseMsgVM.birthDayTodo = toDo;

                    clientServiceMsgVM.liabilities.set(data.getLiabilities());
                    clientServiceMsgVM.moneyIn.set(data.getIncome());
                    clientServiceMsgVM.financialAssets.set(data.getFixedAssets());
                    clientServiceMsgVM.carType.set(data.getCar());
                    clientServiceMsgVM.moneyOut.set(data.getExpenditure());
                    clientServiceMsgVM.fixedAssets.set(data.getFixedAssets());

                    //服务信息




            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //DialogUtil.showButtonDialog(getSupportFragmentManager(),"提示","是否保存信息")

    }



    @Override
    public void onRightClick(boolean isModify) {
        super.onRightClick(isModify);
        vm.isModify.set(isModify);

        if (isModify == false){
            if (isUpdatePic) { //如果有刷新图片
                uploadAvatar(vm.photoUrl.get());
            } else {
                submitClient();
            }
        }

        fragments.get(1).onRightClick(isModify);
        fragments.get(0).onRightClick(isModify);
    }


    private void uploadAvatar(String path) {

        DialogFragment dialogFragment = DialogUtil.showLoadingDialog(getSupportFragmentManager(),"正在上传头像..");

        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RetrofitFactory.getInstance().getService().uploadAvatar(part, Config.UPLOAD_FILE)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<UpLoadFileResp>>(){

                })).subscribe(new BaseObserver<BaseResp<UpLoadFileResp>>() {
            @Override
            protected void onSuccess(BaseResp<UpLoadFileResp> upLoadFileRespBaseResp) throws Exception {
                dialogFragment.dismiss();
                vm.photoUrl.set(upLoadFileRespBaseResp.getDatas().photoUrl);
                submitClient();


            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    /**
     * 修改客户
     */
    private void modifyClient(AddClientReq req){
        RetrofitFactory.getInstance().getService().postAnything(req, Config.MODIFY_CLIENT)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<AddClientResp>>(){

                })).subscribe(new BaseObserver<BaseResp<AddClientResp>>() {
            @Override
            protected void onSuccess(BaseResp<AddClientResp> addClientRespBaseResp) throws Exception {
                ToastUtil.showToast("修改成功");

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    private void addClient(AddClientReq req) {
        RetrofitFactory.getInstance().getService().postAnything(req, Config.ADD_CLIENT)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<AddClientResp>>(){

                })).subscribe(new BaseObserver<BaseResp<AddClientResp>>() {
            @Override
            protected void onSuccess(BaseResp<AddClientResp> addClientRespBaseResp) throws Exception {
                clientId = addClientRespBaseResp.getDatas().clientId;
                ToastUtil.showToast("添加成功");
                isUpdatePic = false;

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    /**
     * 服务信息 和 客户信息
     */
    private void submitClient(){
        ClientBaseMsgFragment clientBaseMsgFragment = (ClientBaseMsgFragment) fragments.get(0);
        ClientServiceMsgFragment serviceMsgFragment = (ClientServiceMsgFragment) fragments.get(1);
        ClientBaseMsgVM clientBaseMsgVM = clientBaseMsgFragment.clientBaseMsgVM;
        ClientServiceMsgVM clientServiceMsgVM = serviceMsgFragment.serviceMsgVM;



        if(TextUtils.isEmpty(vm.userName.get())){

            ToastUtil.showToast("名字不能为空");
            return;
        }

        AddClientReq req = new AddClientReq();
        req.remark = vm.remarks.get(); //备注
        req.userName = vm.userName.get();
        req.clientLabelId = vm.labelId;//标签ID
        req.photoUrl = vm.photoUrl.get();
        req.token = SpUtil.getInstance().getCurrentToken();



        /**
         * 电话号码
         */
       String[] phoneNum  = {"","","","",""};
        ArrayList<String> p = new ArrayList<>();


        for(int i = 0 ; i < clientBaseMsgFragment.addPhoneVMS.size();i++){

            String phone = clientBaseMsgFragment.addPhoneVMS.get(i).phoneNumber.get();//电话号码
            if (TextUtils.isEmpty(phone))continue;
            p.add(phone);
        }

        if (p.size() == 0) {
            ToastUtil.showToast("必须要添加一个电话号码");
            return;
        }

        for(int i = 0 ; i < p.size() ;i++){
            phoneNum[i] = p.get(i);
        }


        //电话号码
        req.OnePhoneNumber = phoneNum[0];
        req.TwoPhoneNumber = phoneNum[1];
        req.ThreePhoneNumber = phoneNum[2];
        req.FourPhoneNumber = phoneNum[3];
        req.FivePhoneNumber = phoneNum[4]; //1 2 3 4 5个电话号码

        req.idCard = clientBaseMsgVM.idCard.get();//身份证
        req.email = clientBaseMsgVM.email.get();
        req.company = clientBaseMsgVM.companyName.get();
        req.office = clientBaseMsgVM.job.get();
        req.companyDetailAddress = clientBaseMsgVM.companyLocation.get();//公司地址
        req.homeDetailAddress = clientBaseMsgVM.houseLocation.get();
        req.sex = clientBaseMsgVM.sex.get().contains("男") ?  1 :2; //
        req.marriage = clientBaseMsgVM.sex.get().contains("未婚") ? 1: 2; //婚姻
        req.sonNum = clientBaseMsgVM.son.get(); //儿子
        req.daughterNum = clientBaseMsgVM.gril.get();//女儿
        req.birthday = clientBaseMsgVM.birthday.get();
        //req.birthdayJson = JsonUtil.GsonString(clientBaseMsgVM.birthDayTodo); //生日的东西
        req.height = clientBaseMsgVM.clientHeight.get();
        req.weight = clientBaseMsgVM.clientWidght.get();
        req.clientId = clientId;

        req.relatedClientId = ""; //TODO


        //服务信息
        req.income = clientServiceMsgVM.moneyIn.get();
        req.expenditure = clientServiceMsgVM.moneyOut.get();
        req.monetaryAssets = clientServiceMsgVM.financialAssets.get();
        req.fixedAssets = clientServiceMsgVM.fixedAssets.get();
        req.car = clientServiceMsgVM.carType.get();
        req.liabilities = clientServiceMsgVM.liabilities.get();
        req.sign = SignUtil.signData(req);


        if (TextUtils.isEmpty(clientId)) {
            addClient(req);
        } else {
            modifyClient(req);
        }




    }
}
