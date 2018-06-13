package com.fdh.generaldialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView

import java.net.URL

/**
 * Created by LeoLu on 2017/8/31.
 * 把旧的dialog库 以kotlin的形式写了一遍
 */
public class BusinessDialog(context: Context,style: Int) : Dialog(context,style), View.OnClickListener {


    var title: TextView? = null

    var content: TextView? = null

    var btn_left: TextView? = null

    var btn_right: TextView? = null

    var listener: OnBusinessDialogListener? = null

    var titleIcon: ImageView? = null

    var view: View? = null

    var line1: View? = null

    var line2: View? = null

    init {
        init()
    }



    /**
     * 初始化所有的控件
     */
    private fun init() {
        view = layoutInflater.inflate(R.layout.dialog_apply_create_company_comfirm_orginal_2, null)
        title = view!!.findViewById(R.id.tv_dialog_apply_create_company_confirm_title) as TextView
        content = view!!.findViewById(R.id.tv_dialog_apply_create_company_confirm_content) as TextView
        btn_left = view!!.findViewById(R.id.btn_dialog_apply_create_company_confirm_leftbutton) as TextView
        btn_right = view!!.findViewById(R.id.btn_dialog_apply_create_company_confirm_rightbutton) as TextView
        line1 = view!!.findViewById(R.id.view_dialog_apply_create_company_confirm_line)
        line2 = view!!.findViewById(R.id.view_dialog_apply_create_company_confirm_line_2)
        titleIcon = view!!.findViewById(R.id.iv_dialog_apply_create_company_confirm_title_img) as ImageView


        btn_left!!.setOnClickListener(this)
        btn_right!!.setOnClickListener(this)
        setCancelable(true)
    }

    /**
     * 点击事件处理
     */
    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_dialog_apply_create_company_confirm_leftbutton) {
            if (listener != null) {
                listener!!.click(v, BUTTON_LEFT)
            }
        } else if (v?.id == R.id.btn_dialog_apply_create_company_confirm_rightbutton) {
            if (listener != null)
                listener!!.click(v, BUTTON_RIGHT)
        }
        dismiss()
    }

    /**
     * 改变dialog的宽度 变小一点
     */
    private fun resizeDialog() {
        var params: WindowManager.LayoutParams = window.attributes;
        //TODO
        params.width = (DisplayUtil.getScreenWidth(context) * 0.6).toInt()
        window.attributes = params
    }

    /**
     * 设置标题字体
     * @param title
     * @return
     */
    public fun setDialogTitle(title: String): BusinessDialog {
        this.title!!.text = title
        return this;
    }

    /**
     * 设置左边按钮的字体
     * @param text
     * @return
     */
    public fun setLeftBtnText(text: String): BusinessDialog {
        btn_left!!.text = text
        return this;
    }

    /**
     * 设置左边按钮的字体颜色
     * @param sourceId
     * @return
     */
    public fun setLeftBtnTextColor(sourceId: Int): BusinessDialog {
        btn_left!!.setTextColor(sourceId)
        return this;
    }

    /**
     * 设置右边按钮的字体颜色
     * @param sourceId
     * @return
     */
    public fun setRightBtnTextColor(sourceId: Int): BusinessDialog {
        btn_right!!.setTextColor(sourceId)
        return this;
    }

    /**
     * 设置右边按钮字体
     */
    public fun setRightBtnText(text: String): BusinessDialog {
        btn_right!!.text = text
        return this;
    }

    /**
     * 设置右边按钮的显示状态
     * @param status
     * @return
     */
    public fun setRightBtnVis(status: Int): BusinessDialog {
        btn_right!!.visibility = status
        return this;
    }

    override fun show() {
        addContentView(view, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        super.show()
        resizeDialog()
    }

    /**
     * 设置左边的按钮的显示状态
     * @param status
     * @return
     */
    public fun setLeftBtnVis(status: Int): BusinessDialog {
        btn_left!!.visibility = status
        return this;
    }

    /**
     * 设置字体内容
     * @param content
     * @return
     */
    public fun setContent(content: String): BusinessDialog {
        this.content!!.text = content
        return this;
    }

    /**
     * 设置监听
     */
    public fun setOnBusinessDialogListener(listener: OnBusinessDialogListener) {
        this.listener = listener;
    }


    /**
     * set dialog icon from your source id.
     * @param id
     * @return
     */
    public fun setTitleIcon(id: Int): BusinessDialog {
        titleIcon!!.setImageResource(id)
        return this;
    }

    /**
     * @param status
     * @return
     */
    public fun setImageVisibility(status: Int): BusinessDialog {
        titleIcon!!.visibility = status;
        return this;
    }

    /**
     * @param status
     * @return
     */
    public fun setLine1Vis(status: Int): BusinessDialog {
        line2!!.visibility = status
        return this;
    }

    public fun setLin2Vis(status: Int): BusinessDialog {
        line1!!.visibility = status;
        return this;
    }

    /**
     * @param status
     * @return
     */
    public fun setAllLineVis(status: Int): BusinessDialog {
        line1!!.visibility = status
        line2!!.visibility = status
        return this;
    }

    /**
     * BusinessDialog listener
     */
    public interface OnBusinessDialogListener {
        fun click(view: View, which: Int)

    }


    companion object {

        @JvmField val BUTTON_LEFT: Int = 1;
        @JvmField val BUTTON_RIGHT: Int = 2;

    }
}
