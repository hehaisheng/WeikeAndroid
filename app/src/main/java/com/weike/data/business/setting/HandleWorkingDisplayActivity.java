package com.weike.data.business.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigText;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.TextParams;
import com.mylhyl.circledialog.params.TitleParams;
import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.business.User;
import com.weike.data.model.req.GetHandleDisplayReq;
import com.weike.data.model.req.ModifyHandleDisplayReq;
import com.weike.data.model.resp.GetHandleDisplayResp;
import com.weike.data.model.resp.ModifyHandleDisplayResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.PickerUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.addapp.pickers.listeners.OnItemPickListener;

/**
 * 待办事显示页面
 */
public class HandleWorkingDisplayActivity extends BaseActivity {

    public static final int TYPE_OF_WEEK = 1;

    public static final int TYPE_OF_MONTH = 2;

    public static final int TYPE_OF_QUARTER = 3;

    public static final int TYPE_OF_YEAR = 4;

    public static final int TYPE_OF_CUSTOM = 5;



    @BindView(R.id.radiobutton_selector)
    public RadioGroup radioGroup;

    @BindView(R.id.radiobutton_recent_week)
    public RadioButton radio_week;

    @BindView(R.id.radiobutton_recent_month)
    public RadioButton radio_month;

    @BindView(R.id.radiobutton_recent_quarter)
    public RadioButton radio_quarter;

    @BindView(R.id.radiobutton_recent_year)
    public RadioButton radio_year;

    @BindView(R.id.radiobutton_custom)
    public RadioButton radio_custom;

    @BindView(R.id.tv_customer_day)
    public TextView tv_customer_day;

    private int time;

    private User user;

    private boolean isFirst = true;

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_working_display);

        setCenterText("");
        setLeftText("待办事项显示");
        setRightText("");
        ButterKnife.bind(this);

        user = SpUtil.getInstance().getUser();

       /* if (user.handleWorkingType == TYPE_OF_WEEK) {
            radio_week.setChecked(true);
        } else if (user.handleWorkingType == TYPE_OF_MONTH) {
            radio_month.setChecked(true);
        } else if (user.handleWorkingType == TYPE_OF_QUARTER) {
            radio_quarter.setChecked(true);
        } else if (user.handleWorkingType == TYPE_OF_YEAR) {
            radio_year.setChecked(true);
        } else {
            radio_custom.setChecked(true);
        }*/

        loadDefault();



        CompoundButton.OnCheckedChangeListener listener  = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && compoundButton.equals(radio_week)) {
                    radio_custom.setChecked(false);
                    user.handleWorkingType = TYPE_OF_WEEK;
                } else if (b && compoundButton.equals(radio_month)) {
                    radio_custom.setChecked(false);
                    user.handleWorkingType = TYPE_OF_MONTH;
                } else if (b && compoundButton.equals(radio_quarter)) {
                    radio_custom.setChecked(false);
                    user.handleWorkingType = TYPE_OF_QUARTER;
                } else if (b && compoundButton.equals(radio_year)) {
                    radio_custom.setChecked(false);
                    user.handleWorkingType = TYPE_OF_YEAR;
                } else if (b && compoundButton.equals(radio_custom)) {
                    user.handleWorkingType = TYPE_OF_CUSTOM;

                    radio_year.setChecked(false);
                    radio_week.setChecked(false);
                    radio_quarter.setChecked(false);
                    radio_month.setChecked(false);


                    if (isFirst)return;

                    ArrayList<String> data = new ArrayList<>();
                    for(int i = 1 ; i < 15;i ++) {
                        if (i < 10) {
                            data.add("0" + i);
                        } else {
                            data.add(i + "");
                        }
                    }
                    PickerUtil.onOptionPicker(data, HandleWorkingDisplayActivity.this, new OnItemPickListener() {
                        @Override
                        public void onItemPicked(int i, Object o) {
                            time = Integer.parseInt((String) o);
                            tv_customer_day.setText(o + "小时");
                        }
                    });
                }
            }
        };

        radio_quarter.setOnCheckedChangeListener(listener);
        radio_week.setOnCheckedChangeListener(listener);
        radio_year.setOnCheckedChangeListener(listener);
        radio_custom.setOnCheckedChangeListener(listener);
        radio_month.setOnCheckedChangeListener(listener);

        /*radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                radio_custom.setChecked(false);
                if (checkedId == R.id.radiobutton_recent_week) {
                    user.handleWorkingType = TYPE_OF_WEEK;
                } else  if (checkedId ==R.id.radiobutton_recent_month) {
                    user.handleWorkingType = TYPE_OF_MONTH;
                } else if (checkedId == R.id.radiobutton_recent_quarter) {
                    user.handleWorkingType = TYPE_OF_QUARTER;
                } else if (checkedId == R.id.radiobutton_recent_year) {
                    user.handleWorkingType = TYPE_OF_YEAR;
                }
            }
        });*/

    }



    private void loadDefault(){
        GetHandleDisplayReq req = new GetHandleDisplayReq();

        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_HANDLE_DISPLAY)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetHandleDisplayResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetHandleDisplayResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetHandleDisplayResp> getHandleDisplayRespBaseResp) throws Exception {
                int type = getHandleDisplayRespBaseResp.getDatas().upcomingType;
                user.handleWorkingType = type;
                time = getHandleDisplayRespBaseResp.getDatas().upcomingDate;

                if (user.handleWorkingType == TYPE_OF_WEEK) {
                    radio_week.setChecked(true);
                } else if (user.handleWorkingType == TYPE_OF_MONTH) {
                    radio_month.setChecked(true);
                } else if (user.handleWorkingType == TYPE_OF_QUARTER) {
                    radio_quarter.setChecked(true);
                } else if (user.handleWorkingType == TYPE_OF_YEAR) {
                    radio_year.setChecked(true);
                } else {

                    tv_customer_day.setText(getHandleDisplayRespBaseResp.getDatas().upcomingDate + "小时");
                    radio_custom.setChecked(true);
                }
                isFirst = false;
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    private void modify(){
        ModifyHandleDisplayReq req = new ModifyHandleDisplayReq();
        req.upcomingDate = time;
        req.upcomingType = user.handleWorkingType;
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.MODIFY_HANDLE_DISPLAY)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<ModifyHandleDisplayResp>>(){

                })).subscribe(new BaseObserver<BaseResp<ModifyHandleDisplayResp>>() {
            @Override
            protected void onSuccess(BaseResp<ModifyHandleDisplayResp> getHandleDisplayRespBaseResp) throws Exception {
                ToastUtil.showToast("修改成功");
                finish();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @Override
    public void onBackPressed() {

        new CircleDialog.Builder()
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.backgroundColor = Color.WHITE;
                        params.backgroundColorPress = Color.BLUE;
                    }
                })
                .setTitle("提示").configTitle(new ConfigTitle() {
            @Override
            public void onConfig(TitleParams params) {
                params.textColor = getResources().getColor(R.color.color_content);
            }
        })
                .setText("是否保存你的修改")
                .configText(new ConfigText() {
                    @Override
                    public void onConfig(TextParams params) {
                        params.padding = new int[]{100, 0, 100, 50};
                    }
                })
                .setNegative("取消", new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).configNegative(new ConfigButton() {
            @Override
            public void onConfig(ButtonParams params) {
                params.backgroundColorPress = Color.WHITE;
            }
        })
                .setPositive("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modify();
                        SpUtil.getInstance().saveNewsUser(user);
                    }
                })
                .configPositive(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.backgroundColorPress = Color.WHITE;
                    }
                })
                .show(getSupportFragmentManager());


    }
}
