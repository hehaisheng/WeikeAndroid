package com.weike.data.business.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.SearchAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.SearchReq;
import com.weike.data.model.resp.SearchResp;
import com.weike.data.model.viewmodel.SearchItemVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.DialogManager;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

/**
 * Created by LeoLu on 2018/5/30.
 * 搜索页面
 */
public class SearchActivity extends BaseActivity {


    private List<SearchItemVM> searchItemVMS = new ArrayList<>();

    @BindView(R.id.recycle_search_list)
    public RecyclerView recyclerView;

    @BindView(R.id.ed_input_search)
    public EditText editText;

    @BindView(R.id.view_loadding)
    public View loaddingView;

    private SearchAdapter adapter;




    @OnTextChanged(R.id.ed_input_search)
    public void onTextChanger(CharSequence s, int start, int before, int count){

        if (count == 0) return;


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setCenterText("");
        setRightText("");
        setLeftText("搜索");




        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){

                    search(editText.getText().toString());
                    return true;
                }
                return false;
            }
        });

        initRecycleView();
    }

    private void initRecycleView(){

        /*for(int i = 0 ; i < 10 ; i++) {
            SearchItemVM searchItemVM = new SearchItemVM(this);


            searchItemVMS.add(searchItemVM);
        }
*/
        adapter = new SearchAdapter(this,R.layout.widget_search_list_item,searchItemVMS, BR.searchItemVM);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void search(String content) {
        loaddingView.setVisibility(View.VISIBLE);
        searchItemVMS.clear();
        adapter.notifyDataSetChanged();

        adapter.setKeyword(content);

        SearchReq req = new SearchReq();
        req.content = content;
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.SEARCH_CONTENT)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<SearchResp>>(){

                })).subscribe(new BaseObserver<BaseResp<SearchResp>>() {
            @Override
            protected void onSuccess(BaseResp<SearchResp> searchRespBaseResp) throws Exception {
                loaddingView.setVisibility(View.GONE);

                if(searchRespBaseResp.getDatas().nameList.size()>0||searchRespBaseResp.getDatas().remarkList.size()>0||searchRespBaseResp.getDatas().companyList.size()>0){
                    for(int i = 0 ; i < searchRespBaseResp.getDatas().nameList.size();i++) {
                        SearchItemVM searchItemVM = new SearchItemVM(SearchActivity.this);
                        searchItemVM.title.set(searchRespBaseResp.getDatas().nameList.get(i).userName);
                        searchItemVM.content.set(searchRespBaseResp.getDatas().nameList.get(i).remark);
                        searchItemVM.clientId.set(searchRespBaseResp.getDatas().nameList.get(i).userId);
                        searchItemVM.iconUrl.set(searchRespBaseResp.getDatas().nameList.get(i).clienturl);
                        if(i == 0) {
                            searchItemVM.tagName.set("联系人");
                            searchItemVM.isShowClientTag.set(true);
                        } else {
                            searchItemVM.isShowClientTag.set(false);
                        }
                        searchItemVMS.add(searchItemVM);
                    }


                    boolean  isShowTitle = false;
                    for(int i = 0 ; i < searchRespBaseResp.getDatas().remarkList.size() ; i++) {
                        SearchItemVM searchItemVM = new SearchItemVM(SearchActivity.this);
                        searchItemVM.title.set(searchRespBaseResp.getDatas().remarkList.get(i).userName);
                        searchItemVM.content.set(searchRespBaseResp.getDatas().remarkList.get(i).remark);
                        searchItemVM.clientId.set(searchRespBaseResp.getDatas().remarkList.get(i).userId);
                        searchItemVM.iconUrl.set(searchRespBaseResp.getDatas().remarkList.get(i).clienturl);
                        if(i == 0) {
                            isShowTitle = true;
                            searchItemVM.tagName.set("日志/公司");
                            searchItemVM.isShowClientTag.set(true);
                        } else {
                            searchItemVM.isShowClientTag.set(false);
                        }
                        searchItemVMS.add(searchItemVM);
                    }

                    for(int i = 0 ; i < searchRespBaseResp.getDatas().companyList.size() ; i++) {
                        SearchItemVM searchItemVM = new SearchItemVM(SearchActivity.this);
                        searchItemVM.title.set(searchRespBaseResp.getDatas().companyList.get(i).userName);
                        searchItemVM.content.set(searchRespBaseResp.getDatas().companyList.get(i).remark);
                        searchItemVM.clientId.set(searchRespBaseResp.getDatas().companyList.get(i).userId);
                        searchItemVM.iconUrl.set(searchRespBaseResp.getDatas().companyList.get(i).clienturl);
                        if(i == 0) {
                            if (isShowTitle) continue;
                            searchItemVM.tagName.set("日志/公司");
                            searchItemVM.isShowClientTag.set(true);
                        } else {
                            searchItemVM.isShowClientTag.set(false);
                        }
                        searchItemVMS.add(searchItemVM);
                    }

                }else{
                    DialogManager.showDialogByContent(SearchActivity.this,"该关键词没有数据","确认","取消");
                }





                adapter.notifyDataSetChanged();

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }
}
