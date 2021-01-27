package com.example.view.util

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun Bitmap.blur(radius : Float, context: Context) : Bitmap {
    return Bitmap.createBitmap(this).also {
        val renderScript = RenderScript.create(context)
        val scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        val allocationIn = Allocation.createFromBitmap(renderScript, this)
        val allocationOut = Allocation.createFromBitmap(renderScript, it)
        scriptIntrinsicBlur.setRadius(radius)
        scriptIntrinsicBlur.setInput(allocationIn)
        scriptIntrinsicBlur.forEach(allocationOut)
        allocationOut.copyTo(it)
        renderScript.destroy()
    }
}