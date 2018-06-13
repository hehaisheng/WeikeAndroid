package com.fdh.generaldialog

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

/**
 * Created by LeoLu on 2017/8/31.
 */
object  BusinessDialogManager{

    /**
     * 创建成功的UI弹窗 打钩的
     * @param context
     * @param title
     * @param content
     * @return
     */
    @JvmStatic fun createSuccessDialog(context: android.content.Context, title:String, content : String): BusinessDialog {
        val dialog = BusinessDialog(context, R.style.whitDialog)
        dialog.setDialogTitle(title).setContent(content).setLeftBtnVis(android.view.View.GONE)
                .setLine1Vis(android.view.View.GONE).setTitleIcon( R.drawable.icon_chenggong).setRightBtnTextColor(android.graphics.Color.BLACK).setImageVisibility(android.view.View.VISIBLE).setRightBtnText("确定")
        return dialog;
    }

    /**
     * 失败弹窗
     * @param context
     * *
     * @param title
     * *
     * @param content
     * *
     * @return
     */
    @JvmStatic fun createFailedDialog(context: android.content.Context, title: String, content: String):BusinessDialog {
        val dialog = BusinessDialog(context, R.style.whitDialog)
        dialog.setDialogTitle(title).setContent(content).setLeftBtnVis(android.view.View.GONE).setLine1Vis(android.view.View.GONE).setRightBtnTextColor(android.graphics.Color.BLACK)
                .setTitleIcon( R.drawable.icon_shibai).setImageVisibility(android.view.View.VISIBLE).setRightBtnText("确定")
        return dialog
    }

    /**
     * 没有按钮的弹窗
     * @param context 上下文内容
     * *
     * @param title 标题
     * *
     * @param content 内容
     * *
     * @param iconId titleicon
     * *
     * @return
     */
    @JvmStatic fun createNotButtonDialog(context: android.content.Context, title: String, content: String, iconId: Int): BusinessDialog {
        val dialog = BusinessDialog(context, R.style.whitDialog)
        dialog.setDialogTitle(title).setContent(content).setLeftBtnVis(android.view.View.GONE).setRightBtnVis(android.view.View.GONE).setAllLineVis(android.view.View.GONE)
                .setTitleIcon(iconId).setImageVisibility(android.view.View.VISIBLE)
        return dialog
    }

    /**
     * 创建默认的弹窗 左边按钮灰色 右边按钮灰色 内容也是灰色
     * @param context
     * *
     * @param content
     * *
     * @return
     */
    @JvmStatic fun createStanderDialog(context: android.content.Context, content: String): BusinessDialog {
        val dialog = BusinessDialog(context, R.style.whitDialog)
        dialog.setLeftBtnVis(android.view.View.VISIBLE).setLeftBtnText("取消").setRightBtnText("确定").setLeftBtnTextColor(android.graphics.Color.parseColor("#808080")).setRightBtnTextColor(android.graphics.Color.parseColor("#808080"))
                .setImageVisibility(android.view.View.GONE).setContent(content).setDialogTitle("提示")
        return dialog
    }

    /**
     * 创建提示dialog 类似于warning 警告的弹窗
     * @param context
     * *
     * @param title
     * *
     * @param msg
     * *
     * @return
     */
    @JvmStatic fun createTipsDialog(context: android.content.Context, title: String, msg: String): BusinessDialog {
        val dialog = BusinessDialog(context, R.style.whitDialog)
        val textView = dialog.title as android.widget.TextView
        val drawable = ContextCompat.getDrawable(context, R.drawable.info_tips_logo)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        textView.setCompoundDrawables(drawable, null, null, null)
        dialog.setDialogTitle(title).setContent(msg).setImageVisibility(android.view.View.GONE).setLeftBtnText("取消").setRightBtnText("确定")
        return dialog
    }

    /**
     * 创建默认弹窗 左右按钮颜色可自定定义
     * 头顶没有任何图片
     * @param context
     * *
     * @param content
     * *
     * @param leftButtonColor
     * *
     * @param rightButtonColor
     * *
     * @return
     */
    @JvmStatic fun createStanderDialog(context: android.content.Context, title: String, content: String, leftButtonColor: Int, rightButtonColor: Int): BusinessDialog {
        val dialog = BusinessDialog(context, R.style.whitDialog)
        dialog.setLeftBtnVis(android.view.View.VISIBLE).setLeftBtnText("取消").setRightBtnText("确定").setLeftBtnTextColor(leftButtonColor).setRightBtnTextColor(rightButtonColor)
                .setImageVisibility(android.view.View.GONE).setContent(content).setDialogTitle(title)

        return dialog
    }

    /**
     * 创建没有图片没有按钮弹窗
     * 头顶没有任何图片
     * @param context
     * *
     * @param content
     * *
     * @param leftButtonColor
     * *
     * @param rightButtonColor
     * *
     * @return
     */
    @JvmStatic fun createTipDialog(context: android.content.Context, title: String, content: String): BusinessDialog {
        val dialog = BusinessDialog(context, R.style.whitDialog)
        dialog.setLeftBtnVis(android.view.View.GONE).setRightBtnVis(android.view.View.GONE).setLin2Vis(android.view.View.GONE)
                .setImageVisibility(android.view.View.GONE).setContent(content).setDialogTitle(title)

        return dialog
    }

    /**
     * 创建只有一个按钮的dialog
     * @param context 上下文
     * *
     * @param title 标题
     * *
     * @param content 内容
     * *
     * @param leftButtonColor 按钮颜色
     * *
     * @return
     */
    @JvmStatic fun createSingleButtonDialog(context: android.content.Context, title: String, content: String, leftButtonColor: Int, leftButtonText: String): BusinessDialog {
        val dialog = BusinessDialog(context, R.style.whitDialog)
        dialog.setRightBtnVis(android.view.View.GONE).setLeftBtnText(leftButtonText).setLeftBtnTextColor(leftButtonColor).setDialogTitle(title).setContent(content)
        dialog.setImageVisibility(android.view.View.GONE)
        dialog.setLine1Vis(android.view.View.GONE)
        return dialog
    }

    /**
     * @param context
     * *
     * @param content
     * *
     * @return
     */

    @JvmStatic fun createNewVersionNoticeDialog(context: Context, content: String): BusinessDialog {
        val dialog = BusinessDialog(context, R.style.whitDialog)
        dialog.setContent(content).setLeftBtnText("取消").setRightBtnText("立即更新")
        dialog.title!!.visibility = View.GONE
        dialog.setTitleIcon( R.drawable.bg_new_version)
        val imageView = dialog.titleIcon as android.widget.ImageView
        imageView.setImageResource( R.drawable.bg_new_version)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        val params = android.widget.LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, 0, 0, 0)
        imageView.layoutParams = params
        return dialog
    }
}