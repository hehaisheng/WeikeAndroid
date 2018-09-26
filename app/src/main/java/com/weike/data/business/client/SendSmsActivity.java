package com.weike.data.business.client;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.adapter.SendSmsAdapter;
import com.weike.data.base.BaseModelActivity;
import com.weike.data.model.business.ClientSortModel;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.Constants;
import com.weike.data.util.LogUtil;
import com.weike.data.util.ToastUtil;

import java.util.List;

import butterknife.BindView;

public class SendSmsActivity extends BaseModelActivity implements View.OnClickListener {



    @BindView(R.id.send_recycler)
    RecyclerView sendRecycler;
    @BindView(R.id.bottom)
    TextView bottom;
    @BindView(R.id.send_content)
    TextView sendContent;
    @BindView(R.id.change_client)
    TextView changeClient;
    @BindView(R.id.change_all)
    TextView changeAll;
    @BindView(R.id.edit_content)
    EditText editContent;
    @BindView(R.id.edit_click)
    TextView editClick;
    @BindView(R.id.none_text)
    TextView noneText;


    public List<ClientSortModel> clientSortModels;

    public SendSmsAdapter sendSmsAdapter;
    public   SmsManager   smsManager;

    public String sendContentString;




    @Override
    public int getLayout() {
        return R.layout.activity_send_sms;
    }

    @Override
    public void initInstance() {

        sendRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void initData() {


        if (WKBaseApplication.getInstance().clientSortModelList != null) {
            clientSortModels = WKBaseApplication.getInstance().clientSortModelList;
            sendRecycler.setVisibility(View.VISIBLE);
            sendSmsAdapter = new SendSmsAdapter(R.layout.send_sms_item, clientSortModels);
            sendRecycler.setAdapter(sendSmsAdapter);

        }else{
            noneText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initEvent() {
        changeAll.setOnClickListener(this);
        changeClient.setOnClickListener(this);
        bottom.setOnClickListener(this);
        editClick.setOnClickListener(this);
        noneText.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.change_all:
                WKBaseApplication.getInstance().clientSortModelList = null;
                ActivitySkipUtil.skipAnotherAct(SendSmsActivity.this, SmsClientActivity.class);
                break;
            case R.id.change_client:

                ActivitySkipUtil.skipAnotherAct(SendSmsActivity.this, SmsClientActivity.class);

                break;
            case R.id.bottom:
                if (clientSortModels == null) {
                    ToastUtil.showToast("请选择客户");
                } else {

                    editContent.setVisibility(View.VISIBLE);
                    editClick.setVisibility(View.VISIBLE);

                }

                break;
            case R.id.edit_click:
                if (editContent.getText().toString().trim().length() > 0) {
                    sendContentString = editContent.getText().toString();
                    editContent.setVisibility(View.GONE);
                    editClick.setVisibility(View.GONE);
                    sendContent.setVisibility(View.VISIBLE);
                    sendSms();
                } else {
                    ToastUtil.showToast("内容不能为空");

                }
                break;
            case R.id.none_text:
                ActivitySkipUtil.skipAnotherAct(SendSmsActivity.this, SmsClientActivity.class);
                break;



        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (WKBaseApplication.getInstance().clientSortModelList != null) {
            noneText.setVisibility(View.GONE);
            clientSortModels = WKBaseApplication.getInstance().clientSortModelList;
            sendRecycler.setVisibility(View.VISIBLE);
            sendSmsAdapter = new SendSmsAdapter(R.layout.send_sms_item, clientSortModels);
            sendRecycler.setAdapter(sendSmsAdapter);
            LogUtil.d(Constants.LOG_DATA,"长度"+clientSortModels.size());


        }else{
            sendRecycler.setVisibility(View.GONE);
            noneText.setVisibility(View.VISIBLE);
        }
    }

    public void sendSms() {

        String[] phones=new String[clientSortModels.size()];
        String[] nicks=new String[clientSortModels.size()];
        //smsManager = SmsManager.getDefault();//默认的短信管理器
        for(int i=0;i<clientSortModels.size();i++){
            phones[i]=clientSortModels.get(i).getPhone();
            nicks[i]=clientSortModels.get(i).getRemark();
        }


        for (int i = 0; i < phones.length; i++) {
            String content = nicks[i]+","+sendContentString;
            sendContent.setText(content);
            if(i==phones.length-1){
                sendContent.setVisibility(View.GONE);
            }

            // PendingIntent sentIntent = PendingIntent.getBroadcast(SendSmsActivity.this, 0, new Intent(), 0);
           // List<String> msgs = smsManager.divideMessage(content);
//            for (String msg : msgs) {
//                smsManager.sendTextMessage(phones[i], null, msg, sentIntent, null);
//
//            }
//            Toast.makeText(SendSmsActivity.this, "短信发送完成", Toast.LENGTH_LONG).show();

        }
    }



}
