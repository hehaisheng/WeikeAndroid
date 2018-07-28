package com.weike.data.business.client;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.business.log.RemindSettingActivity;
import com.weike.data.config.Config;
import com.weike.data.databinding.FragmentClientServiceMsgBinding;
import com.weike.data.model.business.Product;
import com.weike.data.model.business.ProductBean;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.req.DelAniDayReq;
import com.weike.data.model.resp.GetClientDetailMsgResp;
import com.weike.data.model.viewmodel.ClientServiceMsgVM;
import com.weike.data.model.viewmodel.ProductItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.DialogUtil;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.NoScrollLinearLayoutManager;
import com.weike.data.util.SignUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by LeoLu on 2018/6/4.
 * 客户服务信息Fragment
 */
public class ClientServiceMsgFragment extends BaseFragment implements ProductItemVM.OnProductListener{

    FragmentClientServiceMsgBinding binding;

    public ClientServiceMsgVM serviceMsgVM;

    private BaseDataBindingAdapter adapter;

    private List<ProductItemVM> itemVMS = new ArrayList<>();

    private ProductItemVM lastProductVM;

    @Override
    protected int setUpLayoutId() {
        return 0;
    }

    @Override
    protected void loadFinish(View view) {

    }

    @Override
    public void onRightClick(boolean status) {
        super.onRightClick(status);
        serviceMsgVM.clickable.set(status);

        if(status){
            showDisplayContent(null,true);
        }


        if (status) {//如果是编辑
            binding.productCardView.setVisibility(View.VISIBLE);
            binding.recyclerProductMsgList.setVisibility(View.VISIBLE);


            for(int i = 0 ; i< itemVMS.size();i++){
                itemVMS.get(i).isShowContent.set(false);
                itemVMS.get(i).isFirst.set(false);
                itemVMS.get(i).isModify.set(true);
            }
            initHead(itemVMS.size());

        } else {
            itemVMS.remove(0);

            if (itemVMS.size() == 0) {
                binding.recyclerProductMsgList.setVisibility(View.GONE);
                binding.productCardView.setVisibility(View.GONE);
                return;
            }
            for(int i = 0 ; i < itemVMS.size() ; i++) {
                itemVMS.get(i).isShowContent.set(true);
                itemVMS.get(i).isFirst.set(false);
                itemVMS.get(i).isModify.set(false);
            }
        }
        adapter.notifyDataSetChanged();
    }


    public void showDisplayContent(GetClientDetailMsgResp data , boolean isMoidfy){

        LogUtil.d("acthome","shoDisplay:" + isMoidfy + "," + data);

        if (isMoidfy) {
            serviceMsgVM.moneyInDisplay.set(true);
            serviceMsgVM.moneyOutDisplay.set(true);
            serviceMsgVM.fixedAssetsDisplay.set(true);
            serviceMsgVM.financialAssetsDisplay.set(true);
            serviceMsgVM.carTypeDisplay.set(true);
            serviceMsgVM.liabilitiesDisplay.set(true);
            serviceMsgVM.productDisplay.set(true);
        } else if (data != null) {

            serviceMsgVM.moneyInDisplay.set(TextUtils.isEmpty(data.getIncome()) ? false : true);
            serviceMsgVM.moneyOutDisplay.set(TextUtils.isEmpty(data.getExpenditure()) ? false : true);
            serviceMsgVM.fixedAssetsDisplay.set(TextUtils.isEmpty(data.getFixedAssets()) ? false : true);
            serviceMsgVM.financialAssetsDisplay.set(TextUtils.isEmpty(data.getMonetaryAssets()) ? false : true);
            serviceMsgVM.carTypeDisplay.set(TextUtils.isEmpty(data.getCar())? false : true);
            serviceMsgVM.liabilitiesDisplay.set(TextUtils.isEmpty(data.getLiabilities()) ? false : true);


            if (data.getProduct().size() == 0) {
                serviceMsgVM.productDisplay.set(false);
            } else {
                serviceMsgVM.productDisplay.set(true);
            }

        }

    }

    public String getAllProduct() {
        List<Product> products = new ArrayList<>();

        LogUtil.d("ClientServiceMsgFragment","---->" + itemVMS.size());
        for (int i = 0; i < itemVMS.size(); i++) {

            LogUtil.d("ClientServiceMsgFragment,","name: " + itemVMS.get(i).content.get());

            if (TextUtils.isEmpty(itemVMS.get(i).content.get())){
                LogUtil.d("ClientServiceMsgFragment","---->");
                continue;
            }

            Product product = new Product();
            ProductItemVM vm = itemVMS.get(i);
            product.remind = vm.toDo == null ? "" : JsonUtil.GsonString(vm.toDo);
            product.productName = vm.content.get();
            product.id = TextUtils.isEmpty(vm.productId) ? "" : vm.productId;

            products.add(product);
        }


        LogUtil.d("ClientServiceMsgFragment","final Sieze:" + products.size());

        if (products.size() == 0) {
            return "";
        }

        return "" + JsonUtil.GsonString(products) + "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RemindSettingActivity.CODE_OF_REQUEST && resultCode == RESULT_OK && data != null) {
            ToDo toDo = data.getParcelableExtra(RemindSettingActivity.KEY_OF_TODO);
            lastProductVM.toDo = toDo;
            if (toDo.isRemind == 1) {
                lastProductVM.remindIcon.set(R.mipmap.ic_remind);
            } else {
                lastProductVM.remindIcon.set(R.mipmap.ic_remind_dis);
            }

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_client_service_msg, container, false);
        serviceMsgVM = new ClientServiceMsgVM();
        serviceMsgVM.clickable.set(isModify);
        binding.setClientServiceVM(serviceMsgVM);
        initProductRecycler();

        showDisplayContent(null,true);
        return binding.getRoot();
    }


    private ToDo compass(ProductBean.RemindBean remindBean) {
        ToDo toDo = new ToDo();
        toDo.isRemind = remindBean.isRemind;
        toDo.isRepeat = remindBean.isRepeat;
        toDo.isAdvance = remindBean.isAdvance;
        toDo.birthdaydate = remindBean.remindDate;
        toDo.content = remindBean.content;
        toDo.advanceDateType = remindBean.advanceDateType;
        toDo.advanceInterval = remindBean.advanceInterval;
        toDo.repeatDateType = remindBean.repeatDateType;
        toDo.repeatInterval = remindBean.repeatInterval;

        return toDo;
    }

    public void loadDefault(BaseResp<GetClientDetailMsgResp> getClientDetailMsgRespBaseResp){
        GetClientDetailMsgResp data = getClientDetailMsgRespBaseResp.getDatas();
        showDisplayContent(data,false); //显示
        serviceMsgVM.liabilities.set(data.getLiabilities());
        serviceMsgVM.moneyIn.set(data.getIncome());
        serviceMsgVM.financialAssets.set(data.getMonetaryAssets());
        serviceMsgVM.carType.set(data.getCar());
        serviceMsgVM.moneyOut.set(data.getExpenditure());
        serviceMsgVM.fixedAssets.set(data.getFixedAssets());
        if (data.getProduct().size() == 0) {
            serviceMsgVM.productDisplay.set(false);
        } else {
            serviceMsgVM.productDisplay.set(true);
        }


       // updateProductList(data.getProduct());
    }

    public void updateProductList(List<ProductBean> product) {
        LogUtil.d("acthome","-->" + product.size());
        if (product.size() == 0) {
            binding.productCardView.setVisibility(View.GONE);
            binding.recyclerProductMsgList.setVisibility(View.GONE);
            return;
        }
        binding.productCardView.setVisibility(View.VISIBLE);
        binding.recyclerProductMsgList.setVisibility(View.GONE);
        itemVMS.clear();

        for (int i = 0; i < product.size(); i++) {
            ProductItemVM productItemVM = new ProductItemVM(this);
            productItemVM.isModify.set(false);
            productItemVM.isShowContent.set(true);
            productItemVM.isFirst.set(false);
            productItemVM.content.set(product.get(i).productName);
            productItemVM.productId = product.get(i).id + "";
            productItemVM.setListener(this);
            if (product.get(i).remind != null && TextUtils.isEmpty(product.get(i).remind.content)) {

                productItemVM.toDo = compass(product.get(i).remind);


                if (productItemVM.toDo.isRemind == 1) {
                    productItemVM.remindIcon.set(R.mipmap.ic_remind);
                } else {
                    productItemVM.remindIcon.set(R.mipmap.ic_remind_dis);
                }
            }
            itemVMS.add(productItemVM);
        }
        adapter = new BaseDataBindingAdapter(getContext(), R.layout.widget_service_product_item, itemVMS, BR.productItemVM);
        NoScrollLinearLayoutManager linearLayoutManager = new NoScrollLinearLayoutManager(getContext());
        linearLayoutManager.setScrollEnabled(false);
        binding.recyclerProductMsgList.setLayoutManager(linearLayoutManager);
        binding.recyclerProductMsgList.setAdapter(adapter);
    }

    private void initHead(int postion) {
        ProductItemVM itemVM = new ProductItemVM(this);
        itemVM.isFirst.set(true);
        itemVM.isModify.set(false);
        itemVM.setListener(this);
        itemVMS.add(postion,itemVM);
    }

    private void initProductRecycler() {


        initHead(0);

        adapter = new BaseDataBindingAdapter(getContext(), R.layout.widget_service_product_item, itemVMS, BR.productItemVM);
        binding.recyclerProductMsgList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerProductMsgList.setAdapter(adapter);
        binding.recyclerProductMsgList.setScrollContainer(false);


    }



    @Override
    public void onClick(ProductItemVM productItemVM, int type) {
        if (type ==1) { //add
            ProductItemVM vm = new ProductItemVM(this);
            vm.isModify.set(true);
            vm.setListener(this);
            itemVMS.add(0,vm);
            adapter.notifyDataSetChanged();
        } else if (type == 2) { // reduce
            DialogUtil.showButtonDialog(getFragmentManager(), "提示", "是否移除该产品", new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeProduct(productItemVM);

                }
            });

        } else if (type == 3) { //remind
            lastProductVM = productItemVM;

            RemindSettingActivity.startActivity(this,productItemVM.toDo);
        }
    }

    private void removeProduct(ProductItemVM item){

        DelAniDayReq req = new DelAniDayReq();
        req.id = item.productId;
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.DEL_PRODUCT)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp>(){
                })).subscribe(new BaseObserver<BaseResp>() {

            @Override
            protected void onSuccess(BaseResp baseResp) throws Exception {
                itemVMS.remove(item);
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });

    }
}
