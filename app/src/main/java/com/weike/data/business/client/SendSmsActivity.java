package com.weike.data.business.client;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.adapter.SendSmsAdapter;
import com.weike.data.base.BaseModelActivity;
import com.weike.data.model.business.ClientSortModel;
import com.weike.data.util.ActivitySkipUtil;
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
    @BindView(R.id.select_linear)
    LinearLayout selectLinear;
    @BindView(R.id.send_check_image)
    ImageView sendCheckImage;
    @BindView(R.id.send_client_content)
    TextView sendClientContent;
    @BindView(R.id.send_sms_client_linear)
    LinearLayout sendSmsClientLinear;
    @BindView(R.id.content_linear)
    LinearLayout contentLinear;


    public List<ClientSortModel> clientSortModels;

    public SendSmsAdapter sendSmsAdapter;
    public SmsManager smsManager;

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


        WKBaseApplication.getInstance().clientSortModelList=null;
        if (WKBaseApplication.getInstance().clientSortModelList != null && WKBaseApplication.getInstance().clientSortModelList.size() > 0) {
            clientSortModels = WKBaseApplication.getInstance().clientSortModelList;
            sendRecycler.setVisibility(View.VISIBLE);
            sendSmsAdapter = new SendSmsAdapter(R.layout.send_sms_item, clientSortModels);
            sendRecycler.setAdapter(sendSmsAdapter);

        } else {
            // noneText.setVisibility(View.VISIBLE);


            contentLinear.setVisibility(View.VISIBLE);
            editClick.setVisibility(View.VISIBLE);
            editClick.setText("去选择客户");
        }
    }

    @Override
    public void initEvent() {
        changeAll.setOnClickListener(this);
        changeClient.setOnClickListener(this);
        bottom.setOnClickListener(this);
        editClick.setOnClickListener(this);
        noneText.setOnClickListener(this);
        sendSmsClientLinear.setOnClickListener(this);
        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                sendClientContent.setText(String.format("群发内容:%s", s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.change_all:
                if (editContent.getText().toString().trim().length() > 0) {

                    sendContentString = editContent.getText().toString().trim();
                    if(sendContentString.length()>60){
                        ToastUtil.showToast("字数长度不能超过60");
                    }else{
                        WKBaseApplication.getInstance().clientSortModelList = null;
                        ActivitySkipUtil.skipAnotherAct(SendSmsActivity.this, SmsClientActivity.class);
                    }

                }else{
                    ToastUtil.showToast("请先输入群发内容");
                }

                break;
            case R.id.change_client:
                if (editContent.getText().toString().trim().length() > 0) {
                    sendContentString = editContent.getText().toString().trim();
                    if(sendContentString.length()>60){
                        ToastUtil.showToast("字数长度不能超过60");
                    }else{
                        ActivitySkipUtil.skipAnotherAct(SendSmsActivity.this, SmsClientActivity.class);
                    }

                }else{
                    ToastUtil.showToast("请先输入群发内容");
                }


                break;
            case R.id.bottom:
                if (clientSortModels == null || clientSortModels.size() == 0) {
                    ToastUtil.showToast("请选择客户");
                } else {
                    sendContentString = editContent.getText().toString().trim();
                    if(sendContentString.length()>0){
                        if(sendContentString.length()>60){
                            ToastUtil.showToast("字数长度不能超过60");
                        }else{
                            sendSms();
                        }
                    }else{
                        sendClientContent.setText("");
                        ToastUtil.showToast("请先输入群发内容");
                    }

                }

                break;
            case R.id.edit_click:
                if (editContent.getText().toString().trim().length() > 0) {
                    sendContentString = editContent.getText().toString().trim();
                    if(sendContentString.length()>60){
                        ToastUtil.showToast("字数长度不能超过60");
                    }else{
                        if (WKBaseApplication.getInstance().clientSortModelList == null || WKBaseApplication.getInstance().clientSortModelList.size() == 0) {
                            ActivitySkipUtil.skipAnotherAct(SendSmsActivity.this, SmsClientActivity.class);
                        } else {

                            contentLinear.setVisibility(View.GONE);
                            editClick.setVisibility(View.GONE);
                            sendSms();
                        }
                    }
                } else {
                    sendClientContent.setText("");
                    ToastUtil.showToast("请先输入群发内容");
                }
                break;
            case R.id.none_text:
                ActivitySkipUtil.skipAnotherAct(SendSmsActivity.this, SmsClientActivity.class);
                break;
            case R.id.send_sms_client_linear:
                if(editClick.getVisibility()==View.VISIBLE){
                    contentLinear.setVisibility(View.GONE);
                    editClick.setVisibility(View.GONE);
                }else{
                    contentLinear.setVisibility(View.VISIBLE);
                    editClick.setVisibility(View.VISIBLE);
                    editClick.setText("开始群发");
                }

                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (WKBaseApplication.getInstance().clientSortModelList != null && WKBaseApplication.getInstance().clientSortModelList.size() > 0) {

            contentLinear.setVisibility(View.GONE);
            editClick.setVisibility(View.GONE);
            sendSmsClientLinear.setVisibility(View.VISIBLE);
            clientSortModels = WKBaseApplication.getInstance().clientSortModelList;
            sendRecycler.setVisibility(View.VISIBLE);
            sendSmsAdapter = new SendSmsAdapter(R.layout.send_sms_item, clientSortModels);
            sendRecycler.setAdapter(sendSmsAdapter);
            sendClientContent.setText(String.format("群发内容:%s", sendContentString));

        } else {
            if (clientSortModels != null) {
                clientSortModels.clear();
            }
            sendRecycler.setVisibility(View.GONE);
            contentLinear.setVisibility(View.VISIBLE);
            editClick.setVisibility(View.VISIBLE);
            sendSmsClientLinear.setVisibility(View.GONE);
            editClick.setText("去选择客户");

        }
    }

    public void sendSms() {
        smsManager = SmsManager.getDefault();
        contentLinear.setVisibility(View.GONE);
        editClick.setVisibility(View.GONE);
        sendSmsClientLinear.setVisibility(View.VISIBLE);
        sendClientContent.setText(String.format("群发内容:%s", sendContentString));
        String[] phones = new String[clientSortModels.size()];
        String[] nicks = new String[clientSortModels.size()];
        //smsManager = SmsManager.getDefault();//默认的短信管理器
        for (int i = 0; i < clientSortModels.size(); i++) {
            phones[i] = clientSortModels.get(i).getPhone();
            nicks[i] = clientSortModels.get(i).getRemark();
        }
        for (int i = 0; i < phones.length; i++) {
            String content = nicks[i] + ":" + sendContentString;
            List<String> texts = smsManager.divideMessage(content);
            PendingIntent sentIntent = PendingIntent.getBroadcast(SendSmsActivity.this, 0, new Intent(), 0);
            for (String text : texts) {
                smsManager.sendTextMessage(phones[i], null, text, sentIntent, null);
            }
        }
        Toast.makeText(SendSmsActivity.this, "短信发送完成", Toast.LENGTH_SHORT).show();
    }



}
