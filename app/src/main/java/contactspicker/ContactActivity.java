package contactspicker;

import android.Manifest;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.business.client.ClientFragment;
import com.weike.data.config.Config;
import com.weike.data.model.req.AddClientListReq;
import com.weike.data.model.req.SearchDifferentPhoneReq;
import com.weike.data.model.resp.GetClientListResp;
import com.weike.data.model.resp.SearchDifferentPhoneResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.DialogUtil;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;
import com.weike.data.view.citypicker.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import contactspicker.widget.RBaseViewHolder;
import contactspicker.widget.RGroupItemDecoration;
import contactspicker.widget.RModelAdapter;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ContactActivity extends BaseActivity {

    private   contactspicker.widget.RRecyclerView mRecyclerView;
    private RModelAdapter<contactspicker.util.ContactsPickerHelper.ContactsInfo> mModelAdapter;

    private DialogFragment dialog;
    private SideBar sideBar;
    public TextView dialogText;

    private static List<  contactspicker.util.ContactsPickerHelper.ContactsInfo> sort(List<  contactspicker.util.ContactsPickerHelper.ContactsInfo> list) {
        Collections.sort(list, new Comparator<  contactspicker.util.ContactsPickerHelper.ContactsInfo>() {
            @Override
            public int compare(  contactspicker.util.ContactsPickerHelper.ContactsInfo o1,   contactspicker.util.ContactsPickerHelper.ContactsInfo o2) {
                return o1.letter.compareTo(o2.letter);
            }
        });
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_data);
        sideBar = findViewById(R.id.sidrbar);
        dialogText=findViewById(R.id.dialog);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s,boolean toShow) {

                if(toShow){

                    if (dialogText != null) {

                        dialogText.setText(s);
                        dialogText.setVisibility(View.VISIBLE);
                    }

                }else{
                    dialogText.setVisibility(View.GONE);
                }
                scrollToLetter(s);
            }
        });

        setCenterText("");
        setLeftText("联系人");
        setRightText("完成");
        initView();
    }

    @Override
    public void onRightClick() {
        super.onRightClick();

        StringBuffer name = new StringBuffer();
        List<GetClientListResp.ClientListSub> clientList = SpUtil.getInstance().getUser().clietList;



        final List<  contactspicker.util.ContactsPickerHelper.ContactsInfo> selectorData = mModelAdapter.getSelectorData();
        if (selectorData.size() == 0) {
            ToastUtil.showToast("您没有选中联系人");
            return;
        } else {


            ArrayList<String> phoneNum = new ArrayList<>();

            for(int i = 0 ; i < selectorData.size();i++){
                phoneNum.add(selectorData.get(i).phone);
            }

            SearchDifferentPhoneReq req = new SearchDifferentPhoneReq();
            //req.phoneNumber = JsonUtil.GsonString(phoneNum).replace("\"","").replace("[","").replace("]","");
            req.phoneNumber = "" + JsonUtil.GsonString(phoneNum) + "";
            req.sign = SignUtil.signData(req);
            LogUtil.d("test","导入"+JsonUtil.GsonString(req));





            RetrofitFactory.getInstance().getService().postAnything(req, Config.INSPECT_DIFFERENT_CLIENT)
                    .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<SearchDifferentPhoneResp>>() {

                    })).subscribe(new BaseObserver<BaseResp<SearchDifferentPhoneResp>>() {
                @Override
                protected void onSuccess(BaseResp<SearchDifferentPhoneResp> searchDifferentPhoneResp) throws Exception {
                        String dialogContent = "包含相同的客户:";
                        if(searchDifferentPhoneResp.getDatas().userNameList.length > 0){
                            for(int i = 0 ; i < searchDifferentPhoneResp.getDatas().userNameList.length;i++){
                                dialogContent+= searchDifferentPhoneResp.getDatas().userNameList[i] + ",";

                            }
                            dialogContent+= "是否添加?";
                        } else{
                            dialogContent = "是否导入以下客户?";
                        }

                    DialogUtil.showButtonDialog(getSupportFragmentManager(), "提示", dialogContent, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addClientList(selectorData);
                        }
                    });
                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

                }
            });

        }

       /* if(TextUtils.isEmpty(name.toString())) {
            name.append("是否导入以下客户?");
        } else {
            name.append(name.toString() + "包含同样的电话号码,是否导入?");
        }


        DialogUtil.showButtonDialog(getSupportFragmentManager(), "提示", name.toString(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final List<  contactspicker.util.ContactsPickerHelper.ContactsInfo> selectorData = mModelAdapter.getSelectorData();
                if (selectorData.size() == 0) {
                    ToastUtil.showToast("您没有选中联系人");
                    return;
                } else {
                    addClientList(selectorData);
                }
            }
        });*/


    }



    private void addClientList(List< contactspicker.util.ContactsPickerHelper.ContactsInfo> selectorData){

        List<AddClientListReq.ClientListAttr> attrs = new ArrayList<>();

        for(int i =0;i < selectorData.size();i++){
            AddClientListReq.ClientListAttr attr = new AddClientListReq.ClientListAttr();
            attr.phoneNumber = selectorData.get(i).phone;
            attr.userName = selectorData.get(i).name;
            attrs.add(attr);
        }

        AddClientListReq req = new AddClientListReq();
        req.clientArr = "" + JsonUtil.GsonString(attrs) + "";
        req.sign = SignUtil.signData(req);
        boolean  isToLong=false;

        for(int i=0;i<attrs.size()&&!isToLong;i++){
            if(attrs.get(i).userName.length()>15){
                isToLong=true;
            }
        }
        if(isToLong){
            DialogUtil.showButtonDialog(getSupportFragmentManager(), "提示", "联系人名字长度不能超过15", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }else{
            RetrofitFactory.getInstance().getService().postAnything(req, Config.ADD_CLIENT_LIST)
                    .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp>() {

                    })).subscribe(new BaseObserver<BaseResp>() {
                @Override
                protected void onSuccess(BaseResp getClientMsgDetailListRespBaseResp) throws Exception {

                    WKBaseApplication.getInstance().isShow=true;
                    finish();
                    sendBroadcast(new Intent(ClientFragment.ACTION_UPDATE_CLIENT));

                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

                }
            });
        }





    }

    private void scrollToLetter(String letter) {
        if (TextUtils.equals(letter, "#")) {
            ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
            return;
        }
        for (int i = 0; i < mModelAdapter.getAllDatas().size(); i++) {
            if (TextUtils.equals(letter, mModelAdapter.getAllDatas().get(i).letter)) {
                ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                break;
            }
        }
    }

    private void initView() {
        dialog = DialogUtil.showLoadingDialog(getSupportFragmentManager(),"正在加载..");
        mRecyclerView = (  contactspicker.widget.RRecyclerView) findViewById(R.id.recycler_view);
        mModelAdapter = new RModelAdapter<  contactspicker.util.ContactsPickerHelper.ContactsInfo>(this) {

            @Override
            protected int getItemLayoutId(int viewType) {
                return R.layout.item_contacts_layout;
            }

            @Override
            protected void onBindCommonView(final RBaseViewHolder holder, final int position, final   contactspicker.util.ContactsPickerHelper.ContactsInfo bean) {
                holder.fillView(bean);
                holder.v(R.id.item_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSelectorPosition(position, (CompoundButton) holder.v(R.id.checkbox));
                    }
                });
                holder.cV(R.id.checkbox).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSelectorPosition(position);
                    }
                });
                Glide.with(ContactActivity.this)
                        .load(  contactspicker.util.ContactsPickerHelper.getPhotoByte(ContactActivity.this, bean.contactId))
//                        .load(ContactsPickerHelper.getPhoto(getContentResolver(), bean.contactId))
                        .placeholder(R.mipmap.icon_normal_3)
                        .into(holder.imgV(R.id.image_view));

//                Observable.just("")
//                        .map(new Func1<Object, Bitmap>() {
//                            @Override
//                            public Bitmap call(Object o) {
//                                return ContactsPickerHelper.getPhoto(getContentResolver(), bean.contactId);
//                            }
//                        })
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Action1<Bitmap>() {
//                            @Override
//                            public void call(Bitmap bitmap) {
//                                holder.imageView(R.id.image_view).setImageBitmap(bitmap);
//                            }
//                        });
            }

            @Override
            protected void onBindModelView(int model, boolean isSelector, RBaseViewHolder holder, int position,   contactspicker.util.ContactsPickerHelper.ContactsInfo bean) {
                ((CompoundButton) holder.v(R.id.checkbox)).setChecked(isSelector);
            }

            @Override
            protected void onBindNormalView(RBaseViewHolder holder, int position,   contactspicker.util.ContactsPickerHelper.ContactsInfo bean) {

            }
        };

        mModelAdapter.setModel(RModelAdapter.MODEL_MULTI);//多选模式
        mRecyclerView.setAdapter(mModelAdapter);



        final TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(getResources().getDisplayMetrics().scaledDensity * 20);
        final RectF rectF = new RectF();
        final Rect rect = new Rect();
        mRecyclerView.addItemDecoration(new RGroupItemDecoration(new RGroupItemDecoration.GroupCallBack() {
            @Override
            public int getGroupHeight() {
                return dp2px(20);
            }

            @Override
            public String getGroupText(int position) {
                //空分组测试

                return mModelAdapter.getAllDatas().get(position).letter;
            }

            @Override
            public void onGroupDraw(Canvas canvas, View view, int position) {
                paint.setColor(Color.parseColor("#f5f5f5"));

                if (isHorizontal()) {
                    rectF.set(view.getLeft() - getGroupHeight(), view.getTop(), view.getLeft(), view.getBottom());
                } else {
                    rectF.set(view.getLeft(), view.getTop() - getGroupHeight(), view.getRight(), view.getTop());
                }

                canvas.drawRoundRect(rectF, dp2px(2), dp2px(2), paint);
                paint.setColor(Color.parseColor("#bebebe"));

                final String letter = mModelAdapter.getAllDatas().get(position).letter;
                paint.getTextBounds(letter, 0, letter.length(), rect);

                if (isHorizontal()) {
                    canvas.drawText(letter, view.getLeft() - getGroupHeight() / 2 - rect.width() / 2, view.getBottom() - dp2px(10), paint);
                } else {
                    canvas.drawText(letter, view.getLeft() + dp2px(10), view.getTop() - (getGroupHeight() - rect.height()) / 2, paint);
                }
            }

            @Override
            public void onGroupOverDraw(Canvas canvas, View view, int position, int offset) {
                paint.setColor(Color.parseColor("#f5f5f5"));

                if (isHorizontal()) {
                    rectF.set(-offset, view.getTop(), getGroupHeight() - offset, view.getBottom());
                } else {
                    rectF.set(view.getLeft(), -offset, view.getRight(), getGroupHeight() - offset);
                }

                canvas.drawRoundRect(rectF, dp2px(2), dp2px(2), paint);
                paint.setColor(Color.parseColor("#bebebe"));

                final String letter = mModelAdapter.getAllDatas().get(position).letter;
                paint.getTextBounds(letter, 0, letter.length(), rect);

                if (isHorizontal()) {
                    canvas.drawText(letter, (getGroupHeight() - rect.width()) / 2 - offset, view.getBottom() - dp2px(10), paint);
                } else {
                    canvas.drawText(letter, view.getLeft() + dp2px(10), (getGroupHeight() + rect.height()) / 2 - offset, paint);
                }
            }
        }));
    }

    private boolean isHorizontal() {
        return ((LinearLayoutManager) mRecyclerView.getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL;
    }

    private int dp2px(int dp) {
        return (int) (getResources().getDisplayMetrics().density) * dp;
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        startLoading();
    }

    private void startLoading() {
        new RxPermissions(ContactActivity.this)
                .request(Manifest.permission.WRITE_CONTACTS)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
//                                    ContactsPickerHelper.getContactsList(ContactActivity.this);
                            //imageView.setImageBitmap(getPhoto(getContentResolver(), "517"));
//                                    Glide.with(ContactActivity.this).load(getPhotoByte(getContentResolver(), "518"));

                              contactspicker.util.ContactsPickerHelper
                                    .getContactsListObservable(ContactActivity.this)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<List<  contactspicker.util.ContactsPickerHelper.ContactsInfo>>() {

                                        @Override
                                        public void onStart() {
                                            super.onStart();
                                            //T2.show(ContactActivity.this, "开始扫描联系人");
                                        }

                                        @Override
                                        public void onCompleted() {
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(List<  contactspicker.util.ContactsPickerHelper.ContactsInfo> contactsInfos) {
                                            dialog.dismiss();
                                            mModelAdapter.resetData(sort(contactsInfos));
                                       //     T2.show(ContactActivity.this, "扫描完成:共" + contactsInfos.size() + "个联系人");
                                        }
                                    });
                        }
                    }
                });
    }
}
