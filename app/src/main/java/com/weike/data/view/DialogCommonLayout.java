package com.weike.data.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weike.data.R;

public class DialogCommonLayout extends RelativeLayout {


    public RelativeLayout fatherLayout;
    public TextView cancelText;
    public TextView sureText;
    public TextView topText;
    public TextView bottomText;
    public TextView dialogContent;


    public DialogCommonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    public DialogCommonLayout(Context context) {
        super(context);
        init();
    }

    public void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_common, this);
        fatherLayout=view.findViewById(R.id.relative_layout);
         cancelText=view.findViewById(R.id.dialog_cancel);
        sureText=view.findViewById(R.id.dialog_sure);
        bottomText=view.findViewById(R.id.dialog_bottom);
        topText=view.findViewById(R.id.dialog_top);
        dialogContent=view.findViewById(R.id.dialog_content);

    }

    public void show(){
        fatherLayout.setVisibility(View.VISIBLE);
    }

    public void gone(){
        fatherLayout.setVisibility(View.GONE);
    }
    public void setContentAndListener(String content,DialogListener dialogListener){
        fatherLayout.setVisibility(View.VISIBLE);
        dialogContent.setText(content);
        bottomText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialogListener.handle("gone");
               gone();
            }
        });
        topText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogListener.handle("gone");
                gone();
            }
        });
        sureText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gone();
                dialogListener.handle("handle");
            }
        });

        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gone();
                dialogListener.handle("finish");
            }
        });

    }

    public interface  DialogListener{
        void handle(String model);
    }


}
