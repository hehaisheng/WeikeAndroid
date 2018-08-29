package com.weike.data.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class DialogManager {




    public  static  void showDialog(final Activity activity,String content) {


        new AlertDialog.Builder(activity)
                .setTitle(content)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();


                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();

                    }
                }).show();
    }


    public static AlertDialog alertDialog;
    public static void showDialogByContent(final Activity activity,String content,String actionOne,String actionTwo) {


        alertDialog= new AlertDialog.Builder(activity)
                .setTitle(content)
                .setPositiveButton(actionOne, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();


                    }
                })
                .setNegativeButton(actionTwo, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();

                    }
                }).show();
    }

    public static  void miss(){
        alertDialog.dismiss();
    }
}
