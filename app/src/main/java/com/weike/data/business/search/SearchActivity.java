package com.weike.data.business.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.SearchReq;
import com.weike.data.model.resp.SearchResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

/**
 * Created by LeoLu on 2018/5/30.
 * 搜索页面
 */
public class SearchActivity extends BaseActivity {


    @BindView(R.id.ed_input_search)
    public EditText editText;


    @OnTextChanged(R.id.ed_input_search)
    public void onTextChanger(CharSequence s, int start, int before, int count){

        if (count == 0) return;

        SearchReq req = new SearchReq();
        req.content = s.toString();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.SEARCH_CONTENT)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<SearchResp>>(){

                })).subscribe(new BaseObserver<BaseResp<SearchResp>>() {
            @Override
            protected void onSuccess(BaseResp<SearchResp> searchRespBaseResp) throws Exception {

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setCenterText("");
        setRightText("");
        setLeftText("搜索");
    }
}
