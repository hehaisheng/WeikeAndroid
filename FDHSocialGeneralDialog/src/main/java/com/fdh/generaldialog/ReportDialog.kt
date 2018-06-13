package com.fdh.generaldialog

import android.content.Context
import android.view.View
import android.view.ViewGroup

import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.TextView

/**
 * Created by LeoLu on 2017/8/31.
 */
public class ReportDialog: android.app.Dialog, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    constructor(context : Context,style :Int):super(context ,style){
        init()
    }

    constructor(context : Context):super(context) {
        init()
    }

    private var left:  TextView? = null

    private var right:  TextView? = null

    private var cb_locationError:  CheckBox? = null

    private var cb_phoneError:  CheckBox? = null

    private var cb_shopNotExits:  CheckBox? = null

    private var type: Int = 0

    private var listener:  ReportDialog.OnErrorCheckListener? = null

    private var reportContent:  EditText? = null

    private var view: android.view.View? = null

    init {
        init()
    }



    private fun init() {
        view = layoutInflater.inflate( R.layout.dialog_report_error, null)
        left = view!!.findViewById( R.id.btn_dialog_apply_create_company_confirm_leftbutton) as  TextView
        right = view!!.findViewById( R.id.btn_dialog_apply_create_company_confirm_rightbutton) as  TextView
        left!!.setOnClickListener(this)
        right!!.setOnClickListener(this)

        cb_locationError = view!!.findViewById( R.id.cb_location_error) as  CheckBox
        cb_shopNotExits = view!!.findViewById( R.id.cb_shop_not_exisi) as  CheckBox
        cb_phoneError = view!!.findViewById( R.id.cb_shop_phone_error) as  CheckBox

        cb_phoneError!!.setOnCheckedChangeListener(this)
        cb_locationError!!.setOnCheckedChangeListener(this)
        cb_shopNotExits!!.setOnCheckedChangeListener(this)

        reportContent = view!!.findViewById( R.id.ed_reoprt_error) as  EditText

        setCancelable(true)

    }

    override fun show() {
        addContentView(view!!, android.view.ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        super.show()

    }

    override fun onClick(v: android.view.View) {
        if (v.id ==  R.id.btn_dialog_apply_create_company_confirm_rightbutton) {
            if (listener != null) {
                listener!!.onCheck(type, reportContent!!.text.toString())
            }
        }
        cancel()
    }


    private fun resizeDialog() {
        val params = window.attributes
        params.width = (DisplayUtil.getScreenWidth(context) * 0.5).toInt()
        window.attributes = params
    }

    fun setOnErrorCheckListener(listener:  ReportDialog.OnErrorCheckListener) {
        this.listener = listener
    }

    override fun onProvideKeyboardShortcuts(data: List<android.view.KeyboardShortcutGroup>, menu: android.view.Menu?, deviceId: Int) {

    }


    override fun onCheckedChanged(buttonView:  CompoundButton, isChecked: Boolean) {
        android.util.Log.d("reportDialog", "id:" + buttonView.id + ",isCheck:" + isChecked)
        if (isChecked) {
            if (buttonView.id ==  R.id.cb_location_error) {
                type = 1
                cb_phoneError!!.isChecked = false
                cb_shopNotExits!!.isChecked = false
            } else if (buttonView.id ==  R.id.cb_shop_not_exisi) {
                type = 2
                cb_locationError!!.isChecked = false
                cb_phoneError!!.isChecked = false
            } else if (buttonView.id ==  R.id.cb_shop_phone_error) {
                type = 3
                cb_locationError!!.isChecked = false
                cb_shopNotExits!!.isChecked = false
            }
        }

    }

    interface OnErrorCheckListener {
        /*
         *
         */
        fun onCheck(type: Int, content: String)
    }
}