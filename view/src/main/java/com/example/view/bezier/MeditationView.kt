package com.example.view.bezier

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.example.view.R
import com.example.view.util.blur

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
class MeditationView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr : Int = 0) : FrameLayout(context, attributeSet, defStyleAttr) {

    private val mThreeView : ThreeView by lazy {
        ThreeView(context, attributeSet, defStyleAttr).also { threeView ->
            threeView.setListener(object : ThreeView.BigListener{
                override fun bigAnimatorEnd() {
                    mParticleView.outParticle()
                    ObjectAnimator.ofFloat(mDynamicImageView,"alpha",0f,1f).also {
                        it.duration = 4000
                        it.start()
                    }
                }
            })
        }
    }

    private val mParticleView : ParticleView by lazy {
        ParticleView(context, attributeSet, defStyleAttr)
    }

    private val mImageView : ImageView by lazy {
        ImageView(context, attributeSet, defStyleAttr).apply {
            this.scaleType = ImageView.ScaleType.FIT_XY
            val sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.nazha)
            this.setImageBitmap(sourceBitmap.blur(25f, context))
        }
    }

    private val mDynamicImageView : ImageView by lazy {
        ImageView(context, attributeSet, defStyleAttr).apply {
            this.scaleType = ImageView.ScaleType.FIT_XY
            val sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.nazha)
            this.setImageBitmap(sourceBitmap)
        }
    }

    init {
        addView(mImageView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        addView(mDynamicImageView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        addView(mThreeView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        addView(mParticleView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    fun start() {
        mThreeView.big()
        mParticleView.inParticle()
        ObjectAnimator.ofFloat(mDynamicImageView,"alpha",1f,0f).also {
            it.duration = 4000
            it.start()
        }
    }
}