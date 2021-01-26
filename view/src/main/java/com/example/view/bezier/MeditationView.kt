package com.example.view.bezier

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.widget.FrameLayout

class MeditationView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr : Int = 0) : FrameLayout(context, attributeSet, defStyleAttr) {

    private val mThreeView : ThreeView by lazy {
        ThreeView(context, attributeSet, defStyleAttr)
    }

    private val mParticleView : ParticleView by lazy {
        ParticleView(context, attributeSet, defStyleAttr)
    }

    init {
        background = ColorDrawable(Color.BLACK)
        addView(mThreeView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        addView(mParticleView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    fun start() {
        mThreeView.big()
        mParticleView.showParticle()
    }
}