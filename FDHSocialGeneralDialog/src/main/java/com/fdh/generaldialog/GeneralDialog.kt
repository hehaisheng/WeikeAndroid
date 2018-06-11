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
 * Created by lwq on 2018.04.10
 */
public class GeneralDialog : android.app.Dialog, View.OnClickListener {


    /**
     * 构造函数
     */
    constructor(context: Context, style: Int) : super(context, style) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private var title: TextView? = null

    private var view: View? = null

    private var content : TextView ? = null;



    init {
        init()
    }

    override fun show() {
        addContentView(view!!, android.view.ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        super.show()
        resizeDialog()
    }

    private fun resizeDialog() {
        var params: WindowManager.LayoutParams = window.attributes;
        //TODO
        params.width = (DisplayUtil.getScreenWidth(context) * 0.6).toInt()
        window.attributes = params
    }

    fun init() {
        view = layoutInflater.inflate(R.layout.dialog_orginal_2, null)
        title = view!!.findViewById(R.id.tv_dialog_confirm_title) as TextView
        content = view!!.findViewById(R.id.tv_dialog_confirm_content) as TextView

        setCanceledOnTouchOutside(true)
    }

    /**
     * 设置标题
     */
    fun setDialogTitle(title: String) {
        this.title!!.text = title;
    }

    /**
     * 设置字体内容
     * @param content
     * @return
     */
     fun setContent(content: String) {
        this.content!!.text = content
    }


    override fun onClick(v: android.view.View) {
        dismiss()
    }
}