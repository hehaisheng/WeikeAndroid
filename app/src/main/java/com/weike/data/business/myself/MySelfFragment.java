package com.weike.data.business.myself;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.databinding.FragmentPersonalCenterBinding;
import com.weike.data.model.business.User;
import com.weike.data.model.req.GetUserInfoReq;
import com.weike.data.model.resp.GetUserInfoResp;
import com.weike.data.model.viewmodel.PersonalFragmentVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by LeoLu on 2018/5/21.
 * 个人的Fragment
 */
public class MySelfFragment extends BaseFragment {

    private FragmentPersonalCenterBinding binding;

    private PersonalFragmentVM vm;

    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_personal_center;
    }

    @Override
    protected void loadFinish(View view) {



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_personal_center,container,false);
        binding.swipeRefreshLayout.setColorSchemeColors(WKBaseApplication.getInstance().getResources().getColor((R.color.color_41BCF6)));
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                init();
            }
        });

        vm = new PersonalFragmentVM(this,getActivity());

        binding.setPersonalVM(vm);
        init();

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode==RESULT_OK) {
            init();
        }
    }

    private void init(){
        GetUserInfoReq req = new GetUserInfoReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.USER_INFO)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetUserInfoResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetUserInfoResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetUserInfoResp> getUserInfoRespBaseResp) throws Exception {
                binding.swipeRefreshLayout.setRefreshing(false);
                if (Integer.parseInt(getUserInfoRespBaseResp.getResult()) == 0) {

                    if (TextUtils.isEmpty(getUserInfoRespBaseResp.getDatas().userName)) {
                        vm.nickName.set("请输入姓名");
                    } else {
                        vm.nickName.set(getUserInfoRespBaseResp.getDatas().userName);
                    }


                    vm.phoneNum.set("绑定手机：" + getUserInfoRespBaseResp.getDatas().phoneNumber );
                    vm.photoUrl.set(getUserInfoRespBaseResp.getDatas().photoUrl);
                    vm.integral.set(getUserInfoRespBaseResp.getDatas().currentIntegral);
                    if(getUserInfoRespBaseResp.getDatas().memberLevel == 1) {
                        vm. vipTime.set("开通");
                    } else {
                        vm.vipTime.set(getUserInfoRespBaseResp.getDatas().timeoutDate + "   到期");
                    }

                    User user = SpUtil.getInstance().getUser();
                    user.phoneNumber = getUserInfoRespBaseResp.getDatas().phoneNumber;
                    user.iconUrl = getUserInfoRespBaseResp.getDatas().photoUrl;
                    user.userName = getUserInfoRespBaseResp.getDatas().userName;
                    user.email = getUserInfoRespBaseResp.getDatas().email;
                    user.job = getUserInfoRespBaseResp.getDatas().occupation;
                    user.address  = getUserInfoRespBaseResp.getDatas().detailAddress;

                    SpUtil.getInstance().saveNewsUser(user);

                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }
}
