package com.fdh.generaldialog

import android.app.Activity

import android.os.Build
import android.support.annotation.RequiresApi
import android.util.DisplayMetrics

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.LinearLayout

/**
 * Created by LeoLu on 2017/8/31.
 */
object DisplayUtil {

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变

     * @param pxValue （DisplayMetrics类中属性density）
     * *
     * @return
     */
    @JvmStatic fun px2dip(pxValue: Float, context: Context): Int {
        val scale = context.resources.displayMetrics.density

        return (pxValue / scale + 0.5f).toInt()

    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变

     * @param dipValue （DisplayMetrics类中属性density）
     * *
     * @return
     */
    @JvmStatic fun dip2px(dipValue: Float, context: Context): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * 根据分辨率从 dp 的单位 转成为 px(像素)
     */
    @JvmStatic fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变

     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * *
     * @return
     */
    @JvmStatic fun px2sp(pxValue: Float, context: Context): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变

     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * *
     * @return
     */
    @JvmStatic fun sp2px(spValue: Float, context: Context): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 直接获取控件的宽、高

     * @param view
     * *
     * @return int[]
     */
    @JvmStatic fun getWidgetWH(view:  View): IntArray {
        val vto2 = view.viewTreeObserver
        vto2.addOnGlobalLayoutListener(object :  ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.addOnGlobalLayoutListener(this)
            }
        })
        return intArrayOf(view.width, view.height)
    }

    /**
     * 直接获取控件的宽、高

     * @param view
     * *
     * @return int[]
     */
    @JvmStatic fun getViewHeight(view:  View): Int {
        val vto2 = view.viewTreeObserver
        vto2.addOnGlobalLayoutListener(object :  ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })
        return view.height
    }

    /**
     * 直接获取控件的宽、高

     * @param view
     * *
     * @return int[]
     */
    @JvmStatic fun getViewWidth(view:  View): Int {
        val vto2 = view.viewTreeObserver
        vto2.addOnGlobalLayoutListener(object :  ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })
        return view.width
    }

    /**
     * 获得屏幕宽度

     * @param context
     * *
     * @return
     */
    @JvmStatic fun getScreenWidth(context: Context): Int {
        val wm = context
                .getSystemService(android.content.Context.WINDOW_SERVICE) as  WindowManager
        val outMetrics = android.util.DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    /**
     * 获得屏幕高度

     * @param context
     * *
     * @return
     */
    @JvmStatic fun getScreenHeight(context: Context): Int {
        val wm = context
                .getSystemService(android.content.Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = android.util.DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    /**
     * 获得状态栏的高度
     * 注意：该方法只能在Activity类中使用，在测试模式下失败

     * @param context
     * *
     * @return
     */
    @JvmStatic fun getStatusBarHeight(context:Context): Int {
        var statusBarHeight = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(`object`).toString())
            statusBarHeight = context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusBarHeight
    }

    /**
     * 获取控件的宽

     * @param view
     * *
     * @return
     */
    @JvmStatic fun getWidgetWidth(view: View): Int {
        val w =  View.MeasureSpec.makeMeasureSpec(0,  View.MeasureSpec.UNSPECIFIED)
        val h =  View.MeasureSpec.makeMeasureSpec(0,  View.MeasureSpec.UNSPECIFIED)
        view.measure(w, h)//先度量
        val width = view.measuredWidth
        return width
    }

    /**
     * 获取控件的高

     * @param view
     * *
     * @return
     */
    @JvmStatic fun getWidgetHeight(view:  View): Int {
        val w =  View.MeasureSpec.makeMeasureSpec(0,  View.MeasureSpec.UNSPECIFIED)
        val h =  View.MeasureSpec.makeMeasureSpec(0,  View.MeasureSpec.UNSPECIFIED)
        view.measure(w, h)//先度量
        val height = view.measuredHeight
        return height
    }

    /**
     * 设置控件宽

     * @param view
     * *
     * @param width
     */
    @JvmStatic fun setWidgetWidth(view:  View, width: Int) {
        val params = view.layoutParams as  LinearLayout.LayoutParams
        params.width = width
        view.layoutParams = params
    }

    /**
     * 设置控件高

     * @param view
     * *
     * @param height
     */
    @JvmStatic fun setWidgetHeight(view:  View, height: Int) {
        val params = view.layoutParams as  LinearLayout.LayoutParams
        params.height = height
        view.layoutParams = params
    }


    //----------------------------------------------

    /**
     * 获取当前屏幕截图，包含状态栏（这个方法没测试通过）

     * @param activity
     * *
     * @return Bitmap
     */
    @JvmStatic fun snapShotWithStatusBar(activity: android.app.Activity):  Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val width = com.fdh.generaldialog.DisplayUtil.getScreenWidth(activity)
        val height = com.fdh.generaldialog.DisplayUtil.getScreenHeight(activity)
        var bp: Bitmap =  Bitmap.createBitmap(bmp, 0, 0, width, height)
        view.destroyDrawingCache()
        return bp
    }

    /**
     * 获取当前屏幕截图，不包含状态栏（这个方法没测试通过）

     * @param activity
     * *
     * @return Bitmap
     */
    @JvmStatic fun snapShotWithoutStatusBar(activity: android.app.Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val frame =  Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusBarHeight = frame.top

        val width = com.fdh.generaldialog.DisplayUtil.getScreenWidth(activity)
        val height = com.fdh.generaldialog.DisplayUtil.getScreenHeight(activity)
        var bp:  Bitmap? = null
        bp =  Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight)
        view.destroyDrawingCache()
        return bp
    }

    /**
     * 获得屏幕尺寸

     * @param context
     * *
     * @return
     */
    @RequiresApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @JvmStatic fun getScreenSize(context: Context): Point {
        val point =  Point()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as  WindowManager
        wm.defaultDisplay.getSize(point)
        return point
    }
}