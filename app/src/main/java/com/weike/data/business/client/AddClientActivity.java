package com.weike.data.business.client;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;

import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.adapter.FragmentBaseAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.business.login.LoginActivity;
import com.weike.data.config.Config;
import com.weike.data.databinding.ActivityClientAddBinding;
import com.weike.data.model.business.AnniversaryDay;
import com.weike.data.model.business.AnotherAttributes;
import com.weike.data.model.business.ClientRelateForNet;
import com.weike.data.model.business.User;
import com.weike.data.model.req.AddClientReq;
import com.weike.data.model.req.GetClientDetailMsgReq;
import com.weike.data.model.resp.AddClientResp;
import com.weike.data.model.resp.GetClientDetailMsgResp;
import com.weike.data.model.resp.UpLoadFileResp;
import com.weike.data.model.viewmodel.AddClientActVM;
import com.weike.data.model.viewmodel.AnniversariesItemVM;
import com.weike.data.model.viewmodel.ClientBaseMsgVM;
import com.weike.data.model.viewmodel.ClientServiceMsgVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.CompressBitmapManager;
import com.weike.data.util.Constants;
import com.weike.data.util.DialogUtil;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LQRPhotoSelectUtils;
import com.weike.data.util.LogSortManager;
import com.weike.data.util.LogUtil;
import com.weike.data.util.NetManager;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;
import com.weike.data.view.DialogCommonLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public BaseResp<GetClientDetailMsgResp> getClientDetailMsgResp;



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
    public String[] titles = {"基本信息","服务信息","跟踪日志","资料库"};

    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    boolean isModify = false;

    public static final String TAG_CLIENT_ID = "com.weike.data.TAG_CLIENT_ID";


    public static void startActivity(Activity activity,String clientId){
        Intent intent = new Intent(activity,AddClientActivity.class);
        intent.putExtra(TAG_CLIENT_ID,clientId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }


    public int count=0;
    public DialogCommonLayout dialogCommonLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_client_add);
        vm = new AddClientActVM(this);
        binding.setAddClientVM(vm);
        setCenterText("");

        dialogCommonLayout=findViewById(R.id.common_layout);
      initPhotoSel();
      addFragment();
      initMsg();
      initClickStatus();


    }






    /**
     * 状态初始化
     */
    private void initClickStatus() {
        boolean status = false;
        if (clientId != null) {
            setLeftText("客户信息");
            setRightText("编辑");
            status = false;

        } else {
            setLeftText("添加客户");
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

                vm.photoUrl.set("");
                vm.photoUrl.set(outputUri.getPath());
                isUpdatePic = true;

            }
        }, true);
        vm.setmLqrPhotoSelectUtils(mLqrPhotoSelectUtils);

    }

    public void addFource(){
        if (clientId == null ) {
            EditText cliientName = findViewById(R.id.ed_client_name);
            cliientName.setFocusableInTouchMode(true);
            cliientName.requestFocus();
        }
    }


    /**
     * 加载显示
     */
    private void ClientBaseFragmentDisplay(){

    }

    private void addFragment() {

        clientId = getIntent().getStringExtra(TAG_CLIENT_ID);
        if(clientId==null){
            WKBaseApplication.getInstance().hasNoClientId=true;
        }

        ClientBaseMsgFragment clientBaseMsgFragment = new ClientBaseMsgFragment();
        ClientServiceMsgFragment serviceMsgFragment = new ClientServiceMsgFragment();
        CloudDataFragment  cloudDataFragment=new CloudDataFragment();

        ClientLogFragment clientLogFragment = new ClientLogFragment(clientId);

        fragments.add(clientBaseMsgFragment);
        fragments.add(serviceMsgFragment);
        fragments.add(clientLogFragment);
        fragments.add(cloudDataFragment);

        binding.viewpagerActivityClientAdd.setOffscreenPageLimit(4);
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
        if (clientId == null){
            List<AnotherAttributes>  anotherAttributesList=new ArrayList<>();
            WKBaseApplication.getInstance().hasNoClientId=true;
            User user= SpUtil.getInstance().getUser();
            user.anotherAttributes=anotherAttributesList;
            SpUtil.getInstance().saveNewsUser(user);
            ClientBaseMsgFragment clientBaseMsgFragment = (ClientBaseMsgFragment) fragments.get(0);
            clientBaseMsgFragment.initAnotherAttr();

            return;
        }




        GetClientDetailMsgReq req = new GetClientDetailMsgReq();
        req.clientId = clientId;

        req.sign = SignUtil.signData(req);
        WKBaseApplication.getInstance().id=clientId;


        RetrofitFactory.getInstance().getService().postAnything(req,Config.GET_CLIENT_DETAIL)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientDetailMsgResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetClientDetailMsgResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetClientDetailMsgResp> getClientDetailMsgRespBaseResp) throws Exception {


                getClientDetailMsgResp=getClientDetailMsgRespBaseResp;
                String jsonString=JsonUtil.GsonString(getClientDetailMsgRespBaseResp);

                String sortString=LogSortManager.newInstance().toSort(jsonString);
                LogUtil.d(Constants.LOG_DATA,"\n"+"返回的排序数据"+"\n"+sortString);


                GetClientDetailMsgResp data = getClientDetailMsgRespBaseResp.getDatas();
                List<GetClientDetailMsgResp.AnotherAttrBean> anotherAttrBeans=data.getUserAttributesList();


                vm.photoUrl.set(data.getPhotoUrl());
                vm.userName.set(data.getUserName());
                vm.remarks.set(data.getRemark());
                vm.label.set(TextUtils.isEmpty(data.getClientLabelName()) ? "未分组" : data.getClientLabelName());

                ClientBaseMsgFragment clientBaseMsgFragment = (ClientBaseMsgFragment) fragments.get(0);
                clientBaseMsgFragment.loadDefault(getClientDetailMsgRespBaseResp);

                    //基本信息

                    ClientServiceMsgFragment serviceMsgFragment = (ClientServiceMsgFragment) fragments.get(1);

                    ClientServiceMsgVM clientServiceMsgVM = serviceMsgFragment.serviceMsgVM;


                    serviceMsgFragment.loadDefault(getClientDetailMsgRespBaseResp);


                    //服务信息

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

                LogUtil.d("test","失败");
            }
        });
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {



        WKBaseApplication.getInstance().hasNoClientId=false;
        if(keyCode == KeyEvent.KEYCODE_BACK){


            if(!TextUtils.isEmpty(vm.userName.get())&&clientId==null){
                LogUtil.d(Constants.LOG_DATA,"保存");
                showDialog("是否保存数据");

            }else{

                if(vm.isModify!=null){
                    if(vm.isModify.get()){
                        LogUtil.d(Constants.LOG_DATA,"编辑");
                        showDialog("是否有更改客户信息");

                    }else{
                        LogUtil.d(Constants.LOG_DATA,"结束");
                        finish();
                    }
                }else{
                    LogUtil.d(Constants.LOG_DATA,"不是编辑");
                }

            }


            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void showDialog(String content){


        dialogCommonLayout.setContentAndListener(content, new DialogCommonLayout.DialogListener() {
             @Override
             public void handle(String model) {
                 if(model.equals("handle")){
                     submitClient();
                 }else if(model.equals("finish")){
                     finish();
                 }
             }
         });

    }


    @Override
    public void onRightClick(boolean isModify) {
        super.onRightClick(isModify);




        if (isUpdatePic) {
            uploadAvatar(vm.photoUrl.get());
        } else{
             if (clientId != null && isModify == true) {
                fragments.get(0).onRightClick(isModify);
                fragments.get(1).onRightClick(isModify);
                fragments.get(2).onRightClick(isModify);
                vm.isModify.set(isModify);
                setLeftText("编辑客户");
                //submitClient();

            } else if (isModify == false && submitClient() == false){ //恢复状态
                setRightText("完成");
                AddClientActivity.this.isModify = true;
                vm.isModify.set(true);
                vm.isModify.set(AddClientActivity.this.isModify);

            } else if (isModify && clientId == null) { //添加客户
                submitClient();


            }
        }


    }

    @Override
    public void onLeftClick() {



        if(!TextUtils.isEmpty(vm.userName.get())&&clientId==null){

            showDialog("是否保存数据");

        }else{

            if(vm.isModify!=null){
                if(vm.isModify.get()){
                    showDialog("是否有修改客户信息");

                }else{
                    finish();
                }
            }

        }


    }

    private void uploadAvatar(String path) {

        DialogFragment dialogFragment = DialogUtil.showLoadingDialog(getSupportFragmentManager(),"正在上传头像..");


        File file = new File(path);
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                try {
                    size = fis.available();

                    if(size>600000){
                        boolean success= CompressBitmapManager.compressBitmap(path,500,path);
                        size = fis.available();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }



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
                ToastUtil.showToast("上传失败");
                dialogFragment.dismiss();

            }
        });
    }

    /**
     * 修改客户
     */
    private void modifyClient(AddClientReq req){

        count=count+1;

        LogUtil.d("androidTest","上传的数据"+JsonUtil.GsonString(req));



        RetrofitFactory.getInstance().getService().postAnything(req, Config.MODIFY_CLIENT)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<AddClientResp>>(){

                })).subscribe(new BaseObserver<BaseResp<AddClientResp>>() {
            @Override
            protected void onSuccess(BaseResp<AddClientResp> addClientRespBaseResp) throws Exception {

               ToastUtil.showToast("修改成功");
                setLeftText("客户信息");
                WKBaseApplication.getInstance().hasNoClientId=false;
                resetRight();

                initMsg();

                sendBroadcast(new Intent(ClientFragment.ACTION_UPDATE_CLIENT));

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    /**
     * 恢复状态
     */
    private void resetRight(){
        vm.isModify.set(false); //恢复状态
        setRightText("编辑");
        setModify(false);
        fragments.get(0).onRightClick(false);
        fragments.get(1).onRightClick(false);
        fragments.get(2).onRightClick(false);
        vm.isModify.set(false);
    }


    private void addClient(AddClientReq req) {
        LogUtil.d("test","添加的数据"+JsonUtil.GsonString(req));
        RetrofitFactory.getInstance().getService().postAnything(req, Config.ADD_CLIENT)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<AddClientResp>>(){

                })).subscribe(new BaseObserver<BaseResp<AddClientResp>>() {
            @Override
            protected void onSuccess(BaseResp<AddClientResp> addClientRespBaseResp) throws Exception {

                clientId = addClientRespBaseResp.getDatas().clientId;
                ToastUtil.showToast("添加成功");
                isUpdatePic = false;
                WKBaseApplication.getInstance().hasNoClientId=false;
                resetRight();

                initMsg();
                ((ClientLogFragment)fragments.get(2)).setClientId(clientId);
                sendBroadcast(new Intent(ClientFragment.ACTION_UPDATE_CLIENT));

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 服务信息 和 客户信息
     */
    private boolean submitClient(){

        if(NetManager.newInstance().isNetworkConnected(this)){
            ClientBaseMsgFragment clientBaseMsgFragment = (ClientBaseMsgFragment) fragments.get(0);
            ClientServiceMsgFragment serviceMsgFragment = (ClientServiceMsgFragment) fragments.get(1);
            ClientBaseMsgVM clientBaseMsgVM = clientBaseMsgFragment.clientBaseMsgVM;
            ClientServiceMsgVM clientServiceMsgVM = serviceMsgFragment.serviceMsgVM;




            if(TextUtils.isEmpty(vm.userName.get())){

                ToastUtil.showToast("名字不能为空");
                return false;
            }

            if(TextUtils.isEmpty(vm.remarks.get())){

                ToastUtil.showToast("尊称不能为空");
                return false;
            }


            AddClientReq req = new AddClientReq();
            req.remark = vm.remarks.get(); //备注
            req.userName = vm.userName.get();
            req.clientLabelId = vm.labelId;//标签ID
            req.photoUrl = vm.photoUrl.get();



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



            if (TextUtils.isEmpty(clientBaseMsgVM.sex.get())) {
                req.sex = -1;
            } else {
                req.sex = clientBaseMsgVM.sex.get().contains("男") ? 1 : 2; //
            }




            if (clientBaseMsgVM.marry.get().contains("未婚")){
                req.marriage = 2;
            } else if (clientBaseMsgVM.marry.get().contains("已婚")){
                req.marriage = 1;
            } else if (clientBaseMsgVM.marry.get().contains("离异")){
                req.marriage = 3;
            } else if (TextUtils.isEmpty(clientBaseMsgVM.marry.get())){
                req.marriage = -1;
            }





            req.sonNum = clientBaseMsgVM.son.get(); //儿子
            req.daughterNum = clientBaseMsgVM.gril.get();//女儿
            req.birthday = clientBaseMsgVM.birthday.get();
            req.birthdayJson = clientBaseMsgVM.birthDayTodo == null ? "" : JsonUtil.GsonString(clientBaseMsgVM.birthDayTodo); //生日的东西
            req.height = clientBaseMsgVM.clientHeight.get();
            req.weight = clientBaseMsgVM.clientWidght.get();
            req.clientId = clientId;



            //关联客户
            ArrayList<ClientRelateForNet> clientRelateds = new ArrayList<>();
            for(int i = 0 ; i < clientBaseMsgFragment.clientRelateItemVMS.size();i++){
                String name = clientBaseMsgFragment.clientRelateItemVMS.get(i).clientName.get();
                String id = clientBaseMsgFragment.clientRelateItemVMS.get(i).clientId;
                String relateId=clientBaseMsgFragment.clientRelateItemVMS.get(i).id;

                if (TextUtils.isEmpty(name)) {
                    continue;
                } else {
                    ClientRelateForNet c = new ClientRelateForNet();
                    c.relatedClientId = id;
                    c.id=relateId;
                    clientRelateds.add(c);
                }
            }
            req.clientRelated = "" + JsonUtil.GsonString(clientRelateds) + ""; //TODO


            //纪念日


            ArrayList<AnniversaryDay> anniversaryDays = new ArrayList<>();



            for(int i = 0 ; i < clientBaseMsgFragment.anniDayVMS.size();i++) {
                AnniversariesItemVM vm = clientBaseMsgFragment.anniDayVMS.get(i);


                if (TextUtils.isEmpty(vm.name.get()) || TextUtils.isEmpty(vm.time.get())){

                    continue;
                } else {

                    AnniversaryDay day = new AnniversaryDay();
                    day.remind = vm.toDo == null ? "" : JsonUtil.GsonString(vm.toDo);
                    day.anniversaryDate = vm.time.get();
                    day.anniversaryName = vm.name.get();
                    day.id = vm.id.get();
                    anniversaryDays.add(day);
                }
            }

            if (anniversaryDays.size() > 0)
                req.anniversary = "" + JsonUtil.GsonString(anniversaryDays) + "";


            //服务信息
            req.income = clientServiceMsgVM.moneyIn.get();
            req.expenditure = clientServiceMsgVM.moneyOut.get();
            req.monetaryAssets = clientServiceMsgVM.financialAssets.get();
            req.fixedAssets = clientServiceMsgVM.fixedAssets.get();
            req.car = clientServiceMsgVM.carType.get();
            req.liabilities = clientServiceMsgVM.liabilities.get();
            req.attributesValue = clientBaseMsgFragment.getAnotherAttr();



            req.product = TextUtils.isEmpty(serviceMsgFragment.getAllProduct()) ? "" : "" + serviceMsgFragment.getAllProduct() + "";


            req.sign = SignUtil.signData(req);
            LogUtil.d("test","上传的数据"+JsonUtil.GsonString(req));



            if (TextUtils.isEmpty(clientId)) {
                addClient(req);
            } else {

                modifyClient(req);
            }


        }else{
            dialogCommonLayout=findViewById(R.id.common_layout);
            dialogCommonLayout.setContentAndListener("登陆已过期,请重新登陆", new DialogCommonLayout.DialogListener() {
                @Override
                public void handle(String model) {
                    ActivitySkipUtil.skipAnotherAct(AddClientActivity.this, LoginActivity.class,true);
//                    if(model.equals("handle")){
//                        submitClient();
//                    }else if(model.equals("finish")){
//                        finish();
//                    }
                }
            });

        }
        return true;

    }
}
