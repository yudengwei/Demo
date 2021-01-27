package com.example.demo

import android.animation.Animator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.PathUtils
import com.example.animation.ViewAnimationSet
import com.example.base.util.UiUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Time
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var bubbleLayout: ViewGroup


    companion object{
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bubbleLayout = findViewById<FrameLayout>(R.id.animation_heart)
        bubbleLayout.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    fun onDialog(view: View) {
        bubbleLayout.removeAllViews()
        for (animator in animators) {
            animator.cancel()
        }
        animators.clear()
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                bubbleLayout.post { addHeart() }
            }
        }, 0, 200)

    }

    fun onShake(view: View) {
        ViewAnimationSet.animator(view, animation_image)
            .rollIn()
            .duration(1000 * 5)
            .interpolator(AccelerateDecelerateInterpolator())
            .interpolator(AccelerateInterpolator())
                //一直匀速显示
            .interpolator(LinearInterpolator())
                //开始快后面慢
            .interpolator(DecelerateInterpolator())
                //开始有个往后甩的动作，再正常开始
            .interpolator(AnticipateInterpolator())
                //开始有个往后甩的动作，结束时有个往前甩的动作，再到结束的地方
            .interpolator(AnticipateOvershootInterpolator())
                //结束时往前甩，再回到结束的地方
            .interpolator(OvershootInterpolator())
                //结束时会回弹几次
            .interpolator(BounceInterpolator())
                //循环播放数次
            .interpolator(CycleInterpolator(5f))
            .start()

    }

    fun onBounce(view: View) {
        ViewAnimationSet.animator(view, animation_image)
            .bounce()
            .start()
    }


    fun onBounceIn(view: View) {
        ViewAnimationSet.animator(view, animation_image)
            .bounceIn()
            .start()
    }

    fun onBounceOut(view: View) {
        ViewAnimationSet.animator(view, animation_image)
            .bounceOut()
            .start()
    }

    fun onFlash(view: View) {

    }

    fun onFlipHorizontal(view: View) {

    }

    fun onFlipVertical(view: View) {

    }

    fun onWave(view: View) {
        ViewAnimationSet.animator(animation_image)
            .wave()
            .duration(5000)
            .start()
    }

    fun onTada(view: View) {

    }

    fun onPulse(view: View) {

    }

    fun onStandUp(view: View) {

    }

    fun onSwing(view: View) {

    }

    fun onWobble(view: View) {

    }

    fun onFall(view: View) {

    }

    fun onNewsPaper(view: View) {

    }

    fun onSlit(view: View) {

    }

    fun onSlideLeftIn(view: View) {

    }

    fun onSlideRightIn(view: View) {

    }

    fun onSlideTopIn(view: View) {

    }

    fun onZoomIn(view: View) {

    }

    fun onRollIn(view: View) {

    }

    fun onFadeIn(view: View) {

    }

    fun onRubber(view: View) {

    }

    fun onPath(view: View) {

    }

    fun onSvgPath(view: View) {

    }

    fun onFollower(view: View) {

    }

    var animators: ArrayList<ViewAnimationSet> = ArrayList()
    var timer: Timer? = null

    fun onBubble(view: View?) {

    }

    private fun addHeart() {
        val metrics = UiUtil.displayMetrics(this)
        if (animators.size == 50) {
            timer?.cancel()
            timer = null
            return
        }
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.MATRIX
        imageView.setImageResource(R.mipmap.heart)
        bubbleLayout.addView(imageView)
        val builder = ViewAnimationSet.animator(imageView)
        builder.path(PathUtils.createBubble(metrics.widthPixels, metrics.heightPixels))
        builder.fadeOut()
        builder.duration(5000)
        builder.repeatCount(Animation.INFINITE)
        builder.interpolator(LinearInterpolator())
        val animator= builder.start()
        animators.add(animator)
    }
}