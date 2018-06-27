package com.weike.data.business.client;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.databinding.FragmentClientServiceMsgBinding;
import com.weike.data.listener.OnReduceListener;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.viewmodel.ClientServiceMsgVM;
import com.weike.data.model.viewmodel.ProductItemVM;

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

    private RecyclerView product_list;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_client_service_msg,container,false);
        serviceMsgVM = new ClientServiceMsgVM();
        serviceMsgVM.clickable.set(isModify);
        binding.setClientServiceVM(serviceMsgVM);
        initProductRecycler();


        return binding.getRoot();
    }

    public void updateProductList(){

    }

    private void initProductRecycler(){


        ProductItemVM itemVM = new ProductItemVM();
        itemVM.isFirst.set(true);
        itemVM.isModify.set(false);
        itemVM.setListener(this);
        itemVMS.add(itemVM);

        adapter = new BaseDataBindingAdapter(getContext(),R.layout.widget_service_product_item,itemVMS, BR.productItemVM);
        binding.recyclerProductMsgList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerProductMsgList.setAdapter(adapter);


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
