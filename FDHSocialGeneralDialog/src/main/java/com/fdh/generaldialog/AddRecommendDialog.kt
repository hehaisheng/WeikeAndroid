package com.fdh.generaldialog

import android.app.Dialog
import android.content.Context
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.util.logging.Handler

/**
 * Created by LeoLu on 2017/10/26.
 */
public class AddRecommendDialog(context : Context , style : Int) : Dialog(context , style), View.OnClickListener,TextWatcher{


    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (TextUtils.isEmpty(content!!.text)) {
            submit!!.setBackgroundResource(R.drawable.shape_recommend_submit_btn);
            submit!!.isClickable = false;
        } else {
            submit!!.setBackgroundResource(R.drawable.shape_recommend_submit_sel_btn);
            submit!!.isClickable = true;
        }
    }

    init {
        init()
    }

    var listener : OnAddRecommendDialogListener ? = null

    var content : EditText ? = null;

    var submit : TextView? = null;

    var cancel : TextView ? = null

    var view : View? = null

    var text : String = ""

    fun init(){
        view = layoutInflater.inflate(R.layout.dialog_add_recommend_tag,null)
        content = view!!.findViewById(R.id.ed_input_text) as EditText
        submit = view!!.findViewById(R.id.btn_submit) as TextView
        cancel = view!!.findViewById(R.id.btn_cancel) as TextView

        submit!!.setOnClickListener (this)
        content!!.addTextChangedListener(this)
        cancel!!.setOnClickListener(this)
        setCancelable(false)
    }

    fun setOnAddRecommendListener(listener : OnAddRecommendDialogListener) {
        this.listener = listener;
    }

    fun showKeyBroad(v : View){
        val imm = getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED)

    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btn_submit ->{
                if (listener != null){
                    listener!!.onCall(content!!.text.toString(),content!!);
                }
                dismiss()
            }

            R.id.btn_cancel -> {
                dismiss()
            }
        }
        hideKeyboard(content!!)
    }


    override fun show() {
        addContentView(view!!, android.view.ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        super.show()
        content!!.setText(text)

        var handler: android.os.Handler = android.os.Handler()

        handler.postDelayed( Runnable {
            content!!.requestFocus()
            content!!.requestFocusFromTouch()
            showKeyBroad(content!!)
        },50)
        content!!.setSelection(content!!.text.length)
        resizeDialog()
    }

    override fun dismiss() {
        super.dismiss()
        Log.d("AddRecommentDialog","---> dismiss")
        hideKeyboard(content!!)

    }

    fun hideKeyboard(v: View) {
        val imm = v.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(v.applicationWindowToken, 0)
        }
    }


    private fun resizeDialog() {
        var params: WindowManager.LayoutParams = window.attributes;
        //TODO
        params.width = (DisplayUtil.getScreenWidth(context) * 0.8).toInt()
        window.attributes = params
    }


    interface OnAddRecommendDialogListener{

         fun onCall(str : String,view : EditText);
    }
}