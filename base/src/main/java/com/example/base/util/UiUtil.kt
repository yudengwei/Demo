package com.example.base.util

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import androidx.annotation.RequiresApi

object UiUtil {

    fun dp2px(resources: Resources, dpValue: Float) : Float{
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            resources.displayMetrics
        )
    }

    fun sp2px(resources: Resources, spValue: Float) : Float{
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spValue,
            resources.displayMetrics
        )
    }

    /**
     * Display metrics display metrics.
     *
     * @param context the context
     * @return the display metrics
     */
    fun displayMetrics(context: Context): DisplayMetrics {
        val dm = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(dm)
        //LogUtils.debug("screen width=" + dm.widthPixels + "px, screen height=" + dm.heightPixels
        //        + "px, densityDpi=" + dm.densityDpi + ", density=" + dm.density);
        return dm
    }
}