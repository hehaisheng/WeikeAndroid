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
import com.weike.data.model.business.Client;
import com.weike.data.model.req.AddClientReq;
import com.weike.data.model.resp.AddClientResp;
import com.weike.data.model.viewmodel.AddClientActVM;
import com.weike.data.model.viewmodel.ClientBaseMsgVM;
import com.weike.data.model.viewmodel.ClientServiceMsgVM;
import com.weike.data.network.RetrofitFactory;
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
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
        if (requestCode == KEY_OF_LABEL && resultCode == RESULT_OK) {

        }

    }

    /**
     * 如果有客户ID 那么就是修改
     */
    public String clientId;

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

        //testAdd();
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
       // testAdd();
    }




    private void testAdd(String photoUrl){
        AddClientReq req = new AddClientReq();
        req.OnePhoneNumber = "15692022243";
        req.userName = "test11111";
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);
        req.photoUrl = photoUrl;

        RetrofitFactory.getInstance().getService().postAnything(req, Config.ADD_CLIENT)
        .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<AddClientResp>>(){

        })).subscribe(new BaseObserver<BaseResp<AddClientResp>>() {
            @Override
            protected void onSuccess(BaseResp<AddClientResp> addClientRespBaseResp) throws Exception {

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                LogUtil.d("acthome",e.getMessage());
            }
        });


    }





    private void addFragment() {
        binding.viewpagerActivityClientAdd.setNoScroll(true);
        ClientBaseMsgFragment clientBaseMsgFragment = new ClientBaseMsgFragment();
        ClientServiceMsgFragment serviceMsgFragment = new ClientServiceMsgFragment();

        ClientLogFragment clientLogFragment = new ClientLogFragment();

        fragments.add(clientBaseMsgFragment);
        fragments.add(serviceMsgFragment);
        fragments.add(clientLogFragment);


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

    }

    @Override
    public void onRightClick() {
        super.onRightClick();

        isModify = isModify ? false : true;



        if (clientId == null) {


        } else {
            modifyClient(null,null);
        }



        fragments.get(position).onRightClick(isModify);
    }

    /**
     * 修改客户
     */
    private void modifyClient(ClientBaseMsgVM baseMsgVM, ClientServiceMsgVM serviceMsgVM){

    }

    /**
     * 服务信息 和 客户信息
     * @param vm
     * @param serviceMsgVM
     */
    private void addClient(ClientBaseMsgVM vm , ClientServiceMsgVM serviceMsgVM){
        AddClientReq req = new AddClientReq();
        req.OnePhoneNumber = "15692022243";
        req.userName = "test11111";
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);


        RetrofitFactory.getInstance().getService().postAnything(req, Config.ADD_CLIENT)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<AddClientResp>>(){

                })).subscribe(new BaseObserver<BaseResp<AddClientResp>>() {
            @Override
            protected void onSuccess(BaseResp<AddClientResp> addClientRespBaseResp) throws Exception {

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                LogUtil.d("acthome",e.getMessage());
            }
        });

    }
}
