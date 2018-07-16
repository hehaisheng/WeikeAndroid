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

import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.databinding.FragmentClientServiceMsgBinding;
import com.weike.data.listener.OnReduceListener;
import com.weike.data.model.business.Product;
import com.weike.data.model.business.ProductBean;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.viewmodel.ClientServiceMsgVM;
import com.weike.data.model.viewmodel.ProductItemVM;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.NoScrollLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeoLu on 2018/6/4.
 * 客户服务信息Fragment
 */
public class ClientServiceMsgFragment extends BaseFragment implements OnReduceListener<ProductItemVM> {

    FragmentClientServiceMsgBinding binding;

    public ClientServiceMsgVM serviceMsgVM;

    private BaseDataBindingAdapter adapter;

    private List<ProductItemVM> itemVMS = new ArrayList<>();

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

        if (status) {//如果是编辑
            binding.recyclerProductMsgList.setVisibility(View.VISIBLE);

            for(int i = 0 ; i< itemVMS.size();i++){
                itemVMS.get(i).isShowContent.set(false);
                itemVMS.get(i).isFirst.set(false);
                itemVMS.get(i).isModify.set(true);
            }
            initHead();
        } else {
            itemVMS.remove(0);

            if (itemVMS.size() == 0) {
                binding.recyclerProductMsgList.setVisibility(View.GONE);
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

    public String getAllProduct() {
        List<Product> products = new ArrayList<>();

        for (int i = 1; i < itemVMS.size(); i++) {

            if (TextUtils.isEmpty(itemVMS.get(i).content.get()))continue;

            Product product = new Product();
            ProductItemVM vm = itemVMS.get(i);
            product.remind = vm.toDo;
            product.productName = vm.content.get();
            product.id = vm.productId;

            products.add(product);
        }

        return "" + JsonUtil.GsonString(products) + "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_client_service_msg, container, false);
        serviceMsgVM = new ClientServiceMsgVM();
        serviceMsgVM.clickable.set(isModify);
        binding.setClientServiceVM(serviceMsgVM);
        initProductRecycler();


        return binding.getRoot();
    }


    private ToDo compass(ProductBean.RemindBean remindBean) {
        ToDo toDo = new ToDo();
        /*if (TextUtils.isEmpty(remindBean.isRemind) == false) {
            toDo.isAdvance = Integer.parseInt(remindBean.isRemind);
        }
        if (TextUtils.isEmpty(remindBean.remindDate) == false) {
            toDo.birthdaydate = remindBean.remindDate;
        }
        if (TextUtils.isEmpty(remindBean.repeatIntervalHour) == false) {
            toDo.repeatIntervalHour = Integer.parseInt(remindBean.repeatIntervalHour);
        }
        if (TextUtils.isEmpty(remindBean.isRepeat) == false)
            toDo.isRepeat = Integer.parseInt(remindBean.isRepeat);

        if (TextUtils.isEmpty(remindBean.priority) == false )
            toDo.priority = Integer.parseInt(remindBean.priority);

        if (TextUtils.isEmpty(remindBean.beforeRemindDay) == false)
            toDo.beforeRemindDay = Integer.parseInt(remindBean.beforeRemindDay);

        if (TextUtils.isEmpty(remindBean.dateType) == false)
            toDo.dateType = Integer.parseInt(remindBean.dateType);
*/
        toDo.content = remindBean.content;
        return toDo;
    }

    public void updateProductList(List<ProductBean> product) {
        if (product.size() == 0) {
            binding.recyclerProductMsgList.setVisibility(View.GONE);
            return;
        }
        itemVMS.clear();

        for (int i = 0; i < product.size(); i++) {
            ProductItemVM productItemVM = new ProductItemVM();
            productItemVM.isModify.set(false);
            productItemVM.isShowContent.set(true);
            productItemVM.isFirst.set(false);
            productItemVM.content.set(product.get(i).productName);
            productItemVM.productId = product.get(i).id + "";
            productItemVM.setListener(this);
            if (product.get(i).remind != null) {

                productItemVM.toDo = compass(product.get(i).remind);


                int remind = 1;
                if (TextUtils.isEmpty(product.get(i).remind.isRemind) == false) {
                    remind = Integer.parseInt(product.get(i).remind.isRemind);
                }

                if (remind == 1) {
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

    private void initHead() {
        ProductItemVM itemVM = new ProductItemVM();
        itemVM.isFirst.set(true);
        itemVM.isModify.set(false);
        itemVM.setListener(this);
        itemVMS.add(0,itemVM);
    }

    private void initProductRecycler() {


        initHead();

        adapter = new BaseDataBindingAdapter(getContext(), R.layout.widget_service_product_item, itemVMS, BR.productItemVM);
        binding.recyclerProductMsgList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerProductMsgList.setAdapter(adapter);
        binding.recyclerProductMsgList.setScrollContainer(false);


    }


    public void updateProduct() {

    }


    @Override
    public void onReduce(ProductItemVM productItemVM) {
        itemVMS.remove(productItemVM);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAdd(ProductItemVM productItemVM) {
        ProductItemVM vm = new ProductItemVM();
        ToDo toDo = new ToDo();
        vm.toDo = toDo;
        vm.isModify.set(true);
        vm.setListener(this);
        itemVMS.add(vm);
        adapter.notifyDataSetChanged();

    }
}
