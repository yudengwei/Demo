package com.example.view.indicator

import android.content.Context
import android.util.AttributeSet
import android.view.View

class SelectTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyle: Int = 0
) : View(context, attributeSet, defaultStyle) {

    private var mText: String? = null
    private var mTextColor = 0
    private var mClipColor = 0
}