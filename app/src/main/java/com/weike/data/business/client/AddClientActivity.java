package com.weike.data.business.client;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.reflect.TypeToken;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigSubTitle;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.SubTitleParams;
import com.mylhyl.circledialog.params.TitleParams;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.weike.data.MainActivity;
import com.weike.data.R;
import com.weike.data.adapter.FragmentBaseAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.business.setting.ClientTagSettingActivity;
import com.weike.data.config.Config;
import com.weike.data.databinding.ActivityClientAddBinding;
import com.weike.data.model.business.BirthDayBean;
import com.weike.data.model.business.Client;
import com.weike.data.model.req.AddClientReq;
import com.weike.data.model.req.GetClientDetailMsgReq;
import com.weike.data.model.resp.AddClientResp;
import com.weike.data.model.resp.GetClientDetailMsgResp;
import com.weike.data.model.resp.UpLoadFileResp;
import com.weike.data.model.viewmodel.AddClientActVM;
import com.weike.data.model.viewmodel.ClientBaseMsgVM;
import com.weike.data.model.viewmodel.ClientServiceMsgVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LQRPhotoSelectUtils;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;
import com.weike.data.view.CircleImageView;
import com.weike.data.view.NoScrollViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionGen;
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
        setLeftText("添加客户");
        setRightText("编辑");
        addFragment();
        initPhotoSel();

        initMsg();
    }

    private void initPhotoSel(){
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                // 4、当拍照或从图库选取图片成功后回调
                vm.photoUrl.set(outputUri.getPath());


            }
        }, true);
        vm.setmLqrPhotoSelectUtils(mLqrPhotoSelectUtils);

    }








    private void addFragment() {

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

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();



    }

    @Override
    public void onRightClick() {
        super.onRightClick();

        isModify = isModify ? false : true;
        vm.isModify.set(isModify);

        if (isModify) {

        } else {
            uploadAvatar(vm.photoUrl.get());
        }




        fragments.get(position).onRightClick(isModify);
    }

    private void uploadAvatar(String path) {
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RetrofitFactory.getInstance().getService().uploadAvatar(part, Config.UPLOAD_FILE)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<UpLoadFileResp>>(){

                })).subscribe(new BaseObserver<BaseResp<UpLoadFileResp>>() {
            @Override
            protected void onSuccess(BaseResp<UpLoadFileResp> upLoadFileRespBaseResp) throws Exception {

                vm.photoUrl.set(upLoadFileRespBaseResp.getDatas().photoUrl);
                addClient();


            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    /**
     * 修改客户
     */
    private void modifyClient(){

    }

    /**
     * 服务信息 和 客户信息
     */
    private void addClient(){
        ClientBaseMsgFragment clientBaseMsgFragment = (ClientBaseMsgFragment) fragments.get(0);
        ClientServiceMsgFragment serviceMsgFragment = (ClientServiceMsgFragment) fragments.get(1);
        ClientBaseMsgVM clientBaseMsgVM = clientBaseMsgFragment.clientBaseMsgVM;
        ClientServiceMsgVM clientServiceMsgVM = serviceMsgFragment.vm;

        if(clientBaseMsgFragment.addPhoneVMS.size() == 1) {
            ToastUtil.showToast("必须要添加一个电话号码");
            return;
        }
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
        String[] phoneNum = new String[6];
        for(int i = 0 ; i < clientBaseMsgFragment.addPhoneVMS.size();i++){
            phoneNum[i] = clientBaseMsgFragment.addPhoneVMS.get(i).phoneNumber.get();//电话号码
        }
        //电话号码
        req.OnePhoneNumber = phoneNum[1];
        req.TwoPhoneNumber = phoneNum[2];
        req.ThreePhoneNumber = phoneNum[3];
        req.FourPhoneNumber = phoneNum[4];
        req.FivePhoneNumber = phoneNum[5]; //1 2 3 4 5个电话号码

        req.idCard = clientBaseMsgVM.idCard.get();//身份证
        req.email = clientBaseMsgVM.email.get();
        req.company = clientBaseMsgVM.companyName.get();
        req.office = clientBaseMsgVM.job.get();
        req.companyDetailAddress = clientBaseMsgVM.companyLocation.get();//公司地址
        req.homeDetailAddress = clientBaseMsgVM.houseLocation.get();
        req.sex = clientBaseMsgVM.sex.get().contains("男") ?  1 :2; //
        req.marriage = clientBaseMsgVM.sex.get().contains("未婚") ? 1: 2; //婚姻
        req.somNum = clientBaseMsgVM.son.get(); //儿子
        req.daughterNum = clientBaseMsgVM.gril.get();//女儿
        //req.birthdayJson = JsonUtil.GsonString(clientBaseMsgVM.birthDayTodo); //生日的东西
        req.height = clientBaseMsgVM.clientHeight.get();
        req.weight = clientBaseMsgVM.clientWidght.get();

        req.relatedClientId = ""; //TODO


        //服务信息
        req.income = clientServiceMsgVM.moneyIn.get();
        req.expenditure = clientServiceMsgVM.moneyOut.get();
        req.monetaryAssets = clientServiceMsgVM.financialAssets.get();
        req.fixedAssets = clientServiceMsgVM.fixedAssets.get();
        req.car = clientServiceMsgVM.carType.get();
        req.liabilities = clientServiceMsgVM.liabilities.get();
        req.sign = SignUtil.signData(req);



        RetrofitFactory.getInstance().getService().postAnything(req, Config.ADD_CLIENT)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<AddClientResp>>(){

                })).subscribe(new BaseObserver<BaseResp<AddClientResp>>() {
            @Override
            protected void onSuccess(BaseResp<AddClientResp> addClientRespBaseResp) throws Exception {
                    clientId = addClientRespBaseResp.getDatas().clientId;

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });

    }
}
