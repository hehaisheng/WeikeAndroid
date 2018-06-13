package com.fdh.generaldialog

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

import android.widget.Button
import android.widget.EditText
import android.widget.TextView

/**
 * Created by LeoLu on 2017/10/10.
 */
public class EditInputDialog : android.app.Dialog, View.OnClickListener {


    /**
     * 构造函数
     */
    constructor(context: Context, style: Int) : super(context, style) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    /**
     * 左边按钮
     */
    private var left: TextView? = null

    private var right: TextView? = null

    private var inputContent: EditText? = null

    private var title: TextView? = null

    private var view: View? = null

    private var listener: EditInputDialog.OnInputDialogListener? = null

    fun setOnInputDialogListener(listener: OnInputDialogListener) {
        this.listener = listener
    }

    init {
        init()
    }

    override fun show() {
        addContentView(view!!, android.view.ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        super.show()
     //   resizeDialog()
    }

    private fun resizeDialog() {
        var params: WindowManager.LayoutParams = window.attributes;
        //TODO
        params.width = (DisplayUtil.getScreenWidth(context) * 0.8).toInt()
        window.attributes = params
    }

    fun init() {
        view = layoutInflater.inflate(R.layout.dialog_include_inputtext, null)
        left = view!!.findViewById(R.id.btn_dialog_apply_create_company_confirm_leftbutton) as TextView?
        right = view!!.findViewById(R.id.btn_dialog_apply_create_company_confirm_rightbutton) as TextView?
        title = view!!.findViewById(R.id.tv_include_inputext_title) as TextView
        inputContent = view!!.findViewById(R.id.ed_input_text) as EditText

        left!!.setOnClickListener(this)
        right!!.setOnClickListener(this)


        setCancelable(false)
    }

    /**
     * 设置标题
     */
    fun setDialogTitle(title: String) {
        this.title!!.text = title;
    }

    /**
     * 设置输入提示
     */
    fun setInputHind(hint: String) {
        inputContent!!.hint = hint
    }

    override fun onClick(v: android.view.View) {
        if (v.id == R.id.btn_dialog_apply_create_company_confirm_rightbutton) {
            if (listener != null) {
                listener!!.onClick(inputContent!!.text.toString(), TYPE_OF_COMFIRM)
            }
        } else {
            if (listener != null) {
                listener!!.onClick(inputContent!!.text.toString(), TYPE_OF_CANCEL)
            }
        }

    }


    companion object {
        @JvmField val TYPE_OF_CANCEL: Int = 1;
        @JvmField val TYPE_OF_COMFIRM: Int = 2;
    }

    interface OnInputDialogListener {
        /**
         *
         */
        fun onClick(content: String, type: Int)
    }
}