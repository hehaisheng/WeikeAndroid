package com.weike.data.business.client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigInput;
import com.mylhyl.circledialog.params.InputParams;
import com.mylhyl.circledialog.view.listener.OnInputClickListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.weike.data.R;
import com.weike.data.adapter.CloudDataAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.listener.AdapterClickListener;
import com.weike.data.model.req.CloudDataRequest;
import com.weike.data.model.req.DeleteFileRequest;
import com.weike.data.model.req.RenameRequest;
import com.weike.data.model.resp.CloudDataResp;
import com.weike.data.model.resp.RenameResponse;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.payment.wechat.WXRegister;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.BitmapUtil;
import com.weike.data.util.ComSchedulers;
import com.weike.data.util.CompressBitmapManager;
import com.weike.data.util.Constants;
import com.weike.data.util.DownloadManager;
import com.weike.data.util.LogUtil;
import com.weike.data.util.ScrollManager;
import com.weike.data.util.SignUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;
import com.weike.data.view.DialogCommonLayout;
import com.weike.data.wxapi.WXEntryActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import imageselector.utils.ImageSelector;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;

public class CloudDataFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, DownloadManager.ProgressListener {


    public CloudDataAdapter cloudDataAdapter;
    public DialogFragment dialogFragment;



    RecyclerView cloudDataRecycler;

    SwipeRefreshLayout swipeLayout;

    TextView uploadImage;

    TextView uploadWord;

    LinearLayout uploadLinear;

    ImageView downImage;

    TextView downText;

    ImageView shareImage;

    TextView shareText;

    View middle;

    ImageView renameImage;

    TextView renameText;

    ImageView deleteImage;

    TextView deleteText;

    RelativeLayout handleLinear;

    DialogCommonLayout commonLayout;

    ProgressBar progress;

    TextView content;

    RelativeLayout relativeLayout;








    private Bitmap myBitmap;
    public List<CloudDataResp.SubCloudDataResp> subCloudDataRespList = new ArrayList<>();
    public List<CloudDataResp.SubCloudDataResp> selectSubCloudDataRespList = new ArrayList<>();

    public String clientId;
    public AddClientActivity addClientActivity;
    public int page = 1;
    public boolean isRefresh = false;
    public boolean isLoad = false;
    public int doneCount = 0;


    private static final int REQUEST_CODE = 0x00000011;


    public List<String> paths = new ArrayList<>();
    public Map<String, RequestBody> requestBodyMap;

    public File tempFile;

    public Map<String, MultipartBody.Part> partMap = new HashMap<>();

    public List<File> fileList = new ArrayList<>();


    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_cloud_data;
    }

    @Override
    protected void loadFinish(View view) {

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        initInstance();

        initEvent();
        initData();
    }

    public void initInstance() {

        LogUtil.d(Constants.LOG_DATA, "initInstance");
        addClientActivity = (AddClientActivity) getActivity();


    }


    public void initData() {

        isLoad = true;
        loadData();
    }


    public void initEvent() {

        uploadImage = addClientActivity.findViewById(R.id.upload_image);
        uploadWord = addClientActivity.findViewById(R.id.upload_word);
        shareText = addClientActivity.findViewById(R.id.share_text);
        shareImage = addClientActivity.findViewById(R.id.share_image);
        downText = addClientActivity.findViewById(R.id.down_text);
        downImage = addClientActivity.findViewById(R.id.down_image);
        deleteText = addClientActivity.findViewById(R.id.delete_text);
        deleteImage = addClientActivity.findViewById(R.id.delete_image);
        renameText = addClientActivity.findViewById(R.id.rename_text);
        renameImage = addClientActivity.findViewById(R.id.rename_image);
        swipeLayout=addClientActivity.findViewById(R.id.swipeLayout);
        cloudDataRecycler=addClientActivity.findViewById(R.id.cloud_data_recycler);
        uploadLinear=addClientActivity.findViewById(R.id.upload_linear);
        handleLinear=addClientActivity.findViewById(R.id.handle_linear);
        commonLayout=addClientActivity.findViewById(R.id.common_layout);
        content=addClientActivity.findViewById(R.id.content);
        relativeLayout=addClientActivity.findViewById(R.id.relative_layout);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        cloudDataRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        cloudDataRecycler = ScrollManager.newInstance().setScrollListener(cloudDataRecycler, getActivity());
        ScrollManager.newInstance().setListener(new ScrollManager.ScrollListener() {
            @Override
            public void scroll() {

                if (!isRefresh) {
                    isLoad = true;
                    page = page + 1;
                    loadData();
                } else {
                    ToastUtil.showToast("刷新中...");
                }


            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(Constants.LOG_DATA, "点击");
                toSelectImage();
            }
        });


        uploadWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivitySkipUtil.skipAnotherAct(getActivity(),UploadFileActivity.class);
            }
        });
        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareFile();

            }
        });
        shareText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFile();
            }
        });
        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downFile();
            }
        });
        downText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downFile();
            }
        });
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        renameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renameUtil();
            }
        });
        renameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renameUtil();
            }
        });


    }

    @Override
    public void onRefresh() {

        if (!isLoad && !isRefresh) {
            page = 1;
            swipeLayout.setRefreshing(true);
            isRefresh = true;
            loadData();
        }

    }

    public void loadData() {

        if (addClientActivity.clientId != null) {

            if (isLoad) {
                relativeLayout.setVisibility(View.VISIBLE);

            }

            CloudDataRequest req = new CloudDataRequest();
            req.page = page;
            req.sign = SignUtil.signData(req);

            RetrofitFactory.getInstance().getService().postAnything(req, Config.FETCH_IMAGE)
                    .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<CloudDataResp>>() {

                    })).subscribe(new BaseObserver<BaseResp<CloudDataResp>>() {
                @Override
                protected void onSuccess(BaseResp<CloudDataResp> cloudDataResp) throws Exception {

                    if (isLoad) {
                        isLoad = false;
                        relativeLayout.setVisibility(View.GONE);
                    }
                    if (isRefresh) {
                        isRefresh = false;
                        swipeLayout.setRefreshing(false);
                    }

                    if (page == 1) {
                        subCloudDataRespList.clear();

                    } else if (page > 1 && cloudDataResp.getDatas().subCloudDataRespList.size() == 0) {
                        ToastUtil.showToast("暂无更多");
                        page = page - 1;
                        return;
                    }


                    subCloudDataRespList.addAll(cloudDataResp.getDatas().subCloudDataRespList);
                    cloudDataAdapter = new CloudDataAdapter(getActivity(), R.layout.cloud_data_item, subCloudDataRespList);

                    cloudDataRecycler.setAdapter(cloudDataAdapter);
                    setCloudDataAdapter();


                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                    if (isLoad) {
                        isLoad = false;
                        relativeLayout.setVisibility(View.GONE);
                    }
                    if (isRefresh) {
                        isRefresh = false;
                        swipeLayout.setRefreshing(false);
                    }
                    ToastUtil.showToast("获取失败,请重新刷新");
                }
            });
        }

    }


    public void setCloudDataAdapter() {
        cloudDataAdapter.setListener(new AdapterClickListener() {
            @Override
            public void handleClick(String handleModel, Object object) {
                CloudDataResp.SubCloudDataResp subCloudDataResp = (CloudDataResp.SubCloudDataResp) object;

                if (handleModel.equals("image")) {


                    //跳转到预览图片的界面
                    BigPicActivity.startActivity(getActivity(),subCloudDataResp.getUrl());

                } else if (handleModel.equals("text")) {
                    //跳转到预览文件的界面
                    Intent intent=new Intent(getActivity(),ReviewActivity.class);
                    intent.putExtra("url",subCloudDataResp.getUrl());
                    startActivity(intent);


                } else if (handleModel.equals("select")) {
                    if (subCloudDataResp.isSelect()) {
                        selectSubCloudDataRespList.add(subCloudDataResp);

                    } else {

                        for (int i = 0; i < selectSubCloudDataRespList.size(); i++) {
                            if (selectSubCloudDataRespList.get(i).getUrl().equals(subCloudDataResp.getUrl())) {
                                selectSubCloudDataRespList.remove(i);
                            }
                        }
                    }
                    if (selectSubCloudDataRespList == null || selectSubCloudDataRespList.size() == 0) {
                        uploadLinear.setVisibility(View.VISIBLE);
                        handleLinear.setVisibility(View.GONE);
                    } else {
                        uploadLinear.setVisibility(View.GONE);
                        handleLinear.setVisibility(View.VISIBLE);
                    }


                }
            }
        });


    }


    @Override
    public void onClick(View v) {


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && data != null) {

            paths = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            postImage();


        }

    }


    public void toSelectImage() {
        if (paths != null && paths.size() > 0) {
            ImageSelector.builder()
                    .useCamera(true) // 设置是否使用拍照
                    .setSingle(false)  //设置是否单选
                    .setViewImage(true)
                    .setMaxSelectCount(9)
                    .setSelected((ArrayList<String>) paths)// 图片的最大选择数量，小于等于0时，不限数量。
                    .start(getActivity(), REQUEST_CODE); // 打开相册
        } else {
            ImageSelector.builder()
                    .useCamera(true) // 设置是否使用拍照
                    .setSingle(false)  //设置是否单选
                    .setViewImage(true)
                    .setMaxSelectCount(9) // 图片的最大选择数量，小于等于0时，不限数量。
                    .start(getActivity(), REQUEST_CODE); // 打开相册
        }
    }

    public void renameUtil() {
        dialogFragment = new CircleDialog.Builder()
                .setCanceledOnTouchOutside(false)
                .setCancelable(true)
                .setInputManualClose(true)
                .setTitle("重命名")
                .setInputHint("重命名")
                .setInputText(selectSubCloudDataRespList.get(0).getFileName())
                .configInput(new ConfigInput() {
                    @Override
                    public void onConfig(InputParams params) {
                        params.padding = new int[]{20, 20, 20, 20};
                        params.inputType = InputType.TYPE_CLASS_TEXT;
                    }
                })
                .setNegative("取消", null)
                .setPositiveInput("确定", new OnInputClickListener() {
                    @Override
                    public void onClick(String text, View v) {
                        if (TextUtils.isEmpty(text)) {
                            ToastUtil.showToast("文件名不能为空");
                            return;
                        }
                        rename(selectSubCloudDataRespList.get(0), text);

                    }
                })
                .show(CloudDataFragment.this.getFragmentManager());
    }

    public void rename(CloudDataResp.SubCloudDataResp subCloudDataResp, String newName) {


        RenameRequest renameRequest = new RenameRequest();
        renameRequest.id = subCloudDataResp.getId();
        renameRequest.newName = newName;
        renameRequest.sign = SignUtil.signData(renameRequest);

        RetrofitFactory.getInstance().getService().postAnything(renameRequest, Config.RENAME_FILE)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<RenameResponse>>() {

                })).subscribe(new BaseObserver<BaseResp<RenameResponse>>() {
            @Override
            protected void onSuccess(BaseResp<RenameResponse> renameResponse) throws Exception {


                int index = cloudDataAdapter.fetchIndex(subCloudDataResp.getUrl());


                subCloudDataResp.setFileName(newName);
                cloudDataAdapter.cloudDataRespList.set(index, subCloudDataResp);
                cloudDataAdapter.notifyItemChanged(index, subCloudDataResp);
                selectSubCloudDataRespList.clear();
                uploadLinear.setVisibility(View.VISIBLE);
                handleLinear.setVisibility(View.GONE);

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });


    }

    public void delete() {

        String clientString = "";
        String keyString = "";
        DeleteFileRequest deleteFileRequest = new DeleteFileRequest();

        if (selectSubCloudDataRespList.size() > 0) {
            for (int i = 0; i < selectSubCloudDataRespList.size(); i++) {
                if (i == 0) {
                    clientString = String.valueOf(selectSubCloudDataRespList.get(i).getUserId());
                    keyString = String.valueOf(selectSubCloudDataRespList.get(i).getUrl());
                } else {
                    clientString = String.format("%s,%s", clientString, String.valueOf(selectSubCloudDataRespList.get(i).getUserId()));
                    keyString = String.format("%s,%s", keyString, String.valueOf(selectSubCloudDataRespList.get(i).getUrl()));
                }
            }
            deleteFileRequest.ids = clientString;
            deleteFileRequest.keys = keyString;
            deleteFileRequest.sign = SignUtil.signData(deleteFileRequest);

            RetrofitFactory.getInstance().getService().postAnything(deleteFileRequest, Config.DELETE_FILE)
                    .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<RenameResponse>>() {

                    })).subscribe(new BaseObserver<BaseResp<RenameResponse>>() {
                @Override
                protected void onSuccess(BaseResp<RenameResponse> renameResponse) throws Exception {


                    for (int i = 0; i < selectSubCloudDataRespList.size(); i++) {
                        int index = cloudDataAdapter.fetchIndex(selectSubCloudDataRespList.get(i).getUrl());
                        cloudDataAdapter.cloudDataRespList.remove(index);
                        cloudDataAdapter.notifyItemRemoved(index);
                    }
                    selectSubCloudDataRespList.clear();
                    uploadLinear.setVisibility(View.VISIBLE);
                    handleLinear.setVisibility(View.GONE);


                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

                }
            });
        } else {
            ToastUtil.showToast("请选择要删除的文件");
        }

    }


    public void downFile() {
        if (selectSubCloudDataRespList.size() > 0) {
            if (selectSubCloudDataRespList.size() > 8) {

                ToastUtil.showToast("文件数量不能超过8个");
            } else {
                relativeLayout.setVisibility(View.VISIBLE);
                content.setText("下载中...");
                for (int i = 0; i < selectSubCloudDataRespList.size(); i++) {
                    DownloadManager downloadManager = DownloadManager.newInstance(selectSubCloudDataRespList.get(i).getFileName() + "." + selectSubCloudDataRespList.get(i).getFileType());
                    downloadManager.start(selectSubCloudDataRespList.get(i).getUrl());
                    downloadManager.setProgressListener(this);
                }
            }
        } else {
            ToastUtil.showToast("请选择要下载的文件");
        }
    }


    public void shareFile() {
        if (selectSubCloudDataRespList != null && selectSubCloudDataRespList.size() > 0) {
            boolean allImage = true;
            int imageCount = 0;
            int shareIndex=0;
            for (int i = 0; i < selectSubCloudDataRespList.size(); i++) {
                if (!selectSubCloudDataRespList.get(i).getFileType().equals("img")) {
                    allImage = false;
                } else {
                    imageCount = imageCount + 1;
                    shareIndex = i;
                }
            }
            if (!allImage) {
                ToastUtil.showToast("只能分享图片");
            } else {
                if (imageCount > 1) {
                    ToastUtil.showToast("一次只能分享一张图片");
                } else if (imageCount == 0) {
                    ToastUtil.showToast("请选择要分享的图片");
                } else {
                    shared(false,selectSubCloudDataRespList.get(shareIndex).getUrl());

                }

            }
        } else {
            ToastUtil.showToast("请先选择分享的文件");
        }
    }

    public void shared(boolean isTimelineCb, String url) {


        new Thread() {
            public void run() {

                myBitmap = BitmapUtil.GetImageInputStream(url);
                WXImageObject imageObject = new WXImageObject(myBitmap);
                //这个构造方法中自动把传入的bitmap转化为2进制数据,或者你直接传入byte[]也行
                //注意传入的数据不能大于10M,开发文档上写的

                WXMediaMessage msg = new WXMediaMessage();  //这个对象是用来包裹发送信息的对象
                msg.mediaObject = imageObject;
                //msg.mediaObject实际上是个IMediaObject对象,
                //它有很多实现类,每一种实现类对应一种发送的信息,
                //比如WXTextObject对应发送的信息是文字,想要发送文字直接传入WXTextObject对象就行


                Bitmap thumbBitmap = Bitmap.createScaledBitmap(myBitmap, 150, 150, true);

                msg.setThumbImage(thumbBitmap);
                //在这设置缩略图
                //官方文档介绍这个bitmap不能超过32kb
                //如果一个像素是8bit的话换算成正方形的bitmap则边长不超过181像素,边长设置成150是比较保险的
                //或者使用msg.setThumbImage(thumbBitmap);省去自己转换二进制数据的过程
                //如果超过32kb则抛异常


                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = WXEntryActivity.WX_SHAERD;
                req.message = msg;
                req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                WXRegister.getApi().sendReq(req);


            }
        }.start();


    }


    public void postImage() {
        requestBodyMap = new HashMap<>();
        for (int i = 0; i < paths.size(); i++) {
            compress(paths.get(i));
        }

        uploadAvatar();
    }


    public void compress(String path) {


        File file = new File(path);
        tempFile = file;
        fileList.add(file);
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                try {
                    size = fis.available();

                    if (size > 1024 * 1024 * 3) {

                        boolean success = CompressBitmapManager.compressBitmap(path, 1024 * 3, path);
                        size = fis.available();
                        File newFile = new File(path);
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), newFile);
                        requestBodyMap.put("files\"; filename=" + newFile.getName(), requestFile);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("files", tempFile.getName(), requestFile);


                        partMap.put(newFile.getName(), part);
                    } else {
                        File newFile = new File(path);
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), newFile);
                        requestBodyMap.put("files\"; filename=" + newFile.getName(), requestFile);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("files", tempFile.getName(), requestFile);


                        partMap.put(newFile.getName(), part);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadAvatar() {

        relativeLayout.setVisibility(View.VISIBLE);
        content.setText("上传中...");


        List<MultipartBody.Part> partList = new ArrayList<>();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), tempFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("files", tempFile.getName(), requestFile);

        partList.add(part);

        MultipartBody.Part[] newPart = new MultipartBody.Part[fileList.size()];
        for (int i = 0; i < fileList.size(); i++) {

            RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), fileList.get(i));
            MultipartBody.Part part1 = MultipartBody.Part.createFormData("files", fileList.get(i).getName(), requestFile1);

            newPart[i] = part1;
        }


        RetrofitFactory.getInstance().getService().uploadImages3(newPart)
                .compose(ComSchedulers.applyIoSchedulers()).subscribe(new Observer<BaseResp<RenameResponse>>() {
            @Override
            public void onCompleted() {

                relativeLayout.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {

                paths.clear();


            }

            @Override
            public void onNext(BaseResp<RenameResponse> responseBody) {


                LogUtil.d(Constants.LOG_DATA, "结果" + responseBody.getResult());

                LogUtil.d(Constants.LOG_DATA, "" + "信息" + responseBody.getMessage());


                paths.clear();


            }
        });

    }

    @Override
    public void progressChanged(long read, long contentLength, boolean done) {
        if (doneCount == 8) {
            //下载完成

            relativeLayout.setVisibility(View.GONE);

        } else {
            doneCount = doneCount + 1;

        }
    }





}
