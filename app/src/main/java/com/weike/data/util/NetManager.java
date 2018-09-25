package com.weike.data.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetManager {

    public static  NetManager netManager;

    public static NetManager newInstance(){
        if(netManager==null){
            synchronized (NetManager.class){

                if(netManager==null){
                    netManager=new NetManager();
                }
            }
        }
        return netManager;

    }

    public boolean isNetworkConnected(Context context) {

        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
