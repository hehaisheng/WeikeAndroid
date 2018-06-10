package com.weike.data.business.client;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.adapter.SortAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.business.ClientSortModel;
import com.weike.data.model.req.GetClientListReq;
import com.weike.data.model.resp.GetClientListResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;
import com.weike.data.view.citypicker.PinyinComparator;
import com.weike.data.view.citypicker.PinyinUtils;
import com.weike.data.view.citypicker.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by LeoLu on 2018/5/21.
 * <p>
 * 客户 首页Fragment
 */
public class ClientFragment extends BaseFragment {

    private ListView clientList;
    private SideBar sideBar;

    private SortAdapter adapter;
    private List<ClientSortModel> datas = new ArrayList<>();

    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_client_list;
    }

    @Override
    protected void loadFinish(View view) {
        clientList = view.findViewById(R.id.lv_client_list);
        sideBar = view.findViewById(R.id.sidrbar);
        clientList.addHeaderView(initHeadView());


        adapter = new SortAdapter(context,datas);
        clientList.setAdapter(adapter);
        clientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddClientActivity.startActivity(getActivity(),datas.get(i-1).getClientId());
            }
        });


        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    clientList.setSelection(position + 1);
                }
            }
        });

        GetClientListReq req = new GetClientListReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);


        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_CLIENT_LIST)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientListResp>>() {

                })).subscribe(new BaseObserver<BaseResp<GetClientListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetClientListResp> getClientListRespBaseResp) throws Exception {
                datas = filledData(getClientListRespBaseResp.getDatas().allClientList);
                Collections.sort(datas, new PinyinComparator());
                adapter.updateListView(datas);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    public void initSlideBar() {

    }


    /**
     * 排序你的title
     *
     * @param date
     * @return
     */
    private List<ClientSortModel> filledData(List<GetClientListResp.ClientListSub> date) {
        List<ClientSortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.size(); i++) {
            ClientSortModel sortModel = new ClientSortModel();
            sortModel.setName(date.get(i).userName);
            sortModel.setClientId(date.get(i).id);
            String pinyin = PinyinUtils.getPingYin(date.get(i).userName);
            LogUtil.d("ClientFragment","---->" + date.get(i).userName);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                    LogUtil.d("ClientFragment","add");
                }
            }
            mSortList.add(sortModel);
        }



        return mSortList;
    }


    private View initHeadView() {
        View.OnClickListener headClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.ll_goto_add_client) {
                    ActivitySkipUtil.skipAnotherAct((Activity) context, AddClientActivity.class);
                } else if (view.getId() == R.id.ll_goto_client_tag) {
                    ActivitySkipUtil.skipAnotherAct((Activity) context, ClientTagActivity.class);
                } else if (view.getId() == R.id.ll_goto_add_for_phone) {

                }
            }
        };

        View headView = LayoutInflater.from(context).inflate(R.layout.widget_client_list_header, null);
        View gotoAddClient = headView.findViewById(R.id.ll_goto_add_client);
        View gotoClientTag = headView.findViewById(R.id.ll_goto_client_tag);
        View gotoAddForPhone = headView.findViewById(R.id.ll_goto_add_for_phone);
        gotoAddClient.setOnClickListener(headClick);
        gotoClientTag.setOnClickListener(headClick);
        gotoAddForPhone.setOnClickListener(headClick);

        return headView;
    }

}

