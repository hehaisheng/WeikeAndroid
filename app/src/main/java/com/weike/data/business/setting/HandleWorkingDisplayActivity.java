package com.weike.data.business.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import com.weike.data.WKBaseApplication;
import com.weike.data.base.BaseActivity;
import com.weike.data.model.business.User;
import com.weike.data.util.FileCacheUtils;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private User user;

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

        if (user.handleWorkingType == TYPE_OF_WEEK) {
            radio_week.setChecked(true);
        } else if (user.handleWorkingType == TYPE_OF_MONTH) {
            radio_month.setChecked(true);
        } else if (user.handleWorkingType == TYPE_OF_QUARTER) {
            radio_quarter.setChecked(true);
        } else if (user.handleWorkingType == TYPE_OF_YEAR) {
            radio_year.setChecked(true);
        } else {
            radio_custom.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radiobutton_recent_week) {
                    user.handleWorkingType = TYPE_OF_WEEK;
                } else  if (checkedId ==R.id.radiobutton_recent_month) {
                    user.handleWorkingType = TYPE_OF_MONTH;
                } else if (checkedId == R.id.radiobutton_recent_quarter) {
                    user.handleWorkingType = TYPE_OF_QUARTER;
                } else if (checkedId == R.id.radiobutton_recent_year) {
                    user.handleWorkingType = TYPE_OF_YEAR;
                } else if (checkedId == R.id.radiobutton_custom) {

                }
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

                        SpUtil.getInstance().saveNewsUser(user);
                        finish();
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
