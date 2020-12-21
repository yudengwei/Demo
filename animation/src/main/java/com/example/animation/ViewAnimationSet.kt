package com.example.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Path
import android.graphics.PathMeasure
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Interpolator
import com.example.base.util.UiUtil

class ViewAnimationSet private constructor(
    private var mDuration: Long = DEFAULT_DURATION, private var mStartDelay: Long = 0,
    private var mInterpolator: Interpolator? = null,
    private var mRepeatCount: Int = 0,
    private var mRepeatMode: Int = ValueAnimator.RESTART
){

    private val mAnimationBuilders = mutableListOf<AnimationSetBuild>()
    private var prev : ViewAnimationSet? = null
    private var next : ViewAnimationSet? = null
    private var mWaitViewForDraw : View? = null

    private var onStart : (() -> Unit)? = null
    private var onStop : (() -> Unit)? = null
    private var onUpdate : ((view: View, value: Float) -> Unit)? = null

    private var mAnimatorSet : AnimatorSet? = null

    internal fun addAnimationBuilder(vararg views: View) : AnimationSetBuild{
        val build = AnimationSetBuild(this, *views)
        mAnimationBuilders.add(build)
        return build
    }

    internal fun thenAnimator(vararg views: View) : AnimationSetBuild{
        val viewAnimationSet = ViewAnimationSet()
        viewAnimationSet.prev = this
        this.next = viewAnimationSet
        return viewAnimationSet.addAnimationBuilder(*views)
    }

    private fun createAnimatorSet() : AnimatorSet {
        val animators = mutableListOf<Animator>()
        mAnimationBuilders.forEach { animationSetBuild ->
            val animatorList = animationSetBuild.mAnimators
            animationSetBuild.mSingleInterpolator?.let { singleInterpolator ->
                animatorList.forEach {
                    it.interpolator = singleInterpolator
                }
            }
            animators.addAll(animatorList)
        }
        for (mAnimationBuilder in mAnimationBuilders) {
            if (mAnimationBuilder.mWaitFirstViewForDrawFilter) {
                this.mWaitViewForDraw = mAnimationBuilder.getFirstView()
                break
            }
        }
        animators.forEach {
            if (it is ValueAnimator) {
                it.repeatCount = this.mRepeatCount
                it.repeatMode = this.mRepeatMode
            }
        }
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animators)
        animatorSet.duration = this.mDuration
        animatorSet.startDelay = this.mStartDelay
        this.mInterpolator?.let {
            animatorSet.interpolator = this.mInterpolator
        }
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                onStart?.let {
                    it
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
                onStop?.let {
                    it
                }
                next?.let {
                    it.prev = null
                    it.start()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
        return animatorSet
    }

    fun start(){
        if (prev != null) {
            prev?.start()
        } else {
            mAnimatorSet = createAnimatorSet()
            if (mWaitViewForDraw != null) {
                mWaitViewForDraw?.viewTreeObserver?.addOnPreDrawListener(object :
                    ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        mAnimatorSet?.start()
                        mWaitViewForDraw?.viewTreeObserver?.removeOnPreDrawListener(this)
                        return false
                    }
                })
            } else {
                mAnimatorSet?.start()
            }
        }
    }

    fun cancel() {
        mAnimatorSet?.cancel()
        next?.let {
            it.cancel()
        }
        next = null
    }

    companion object {
        fun animator(vararg views: View) : AnimationSetBuild {
            val viewAnimationSet = ViewAnimationSet()
            return viewAnimationSet.addAnimationBuilder(*views)
        }

        const val DEFAULT_DURATION = 3000L
    }

    class AnimationSetBuild(
        private val animatorSet: ViewAnimationSet,
        private vararg val views: View
    ){

        companion object{
            private const val TRANSLATION_Y = "translationY"
            private const val TRANSLATION_X = "translationX"
            private const val ALPHA = "alpha"
            private const val SCALE_X = "scaleX"
            private const val SCALE_Y = "scaleY"
            private const val PIVOT_X = "pivotX"
            private const val PIVOT_Y = "pivotY"
            private const val ROTATION_X = "rotationX"
            private const val ROTATION_Y = "rotationX"
            private const val ROTATION = "rotation"
        }

        internal var mStartDelay = 0L
        internal var mInterpolator : Interpolator? = null
        internal val mAnimators = mutableListOf<Animator>()
        internal var mSingleInterpolator : Interpolator? = null
        internal var mWaitFirstViewForDrawFilter = false
        internal var nextValueWillBeDp = false

        private fun property(propertyName: String, vararg values: Float) : AnimationSetBuild{
            views.forEach {
                val floatValues = getValues(*values)
                mAnimators.add(ObjectAnimator.ofFloat(it, propertyName, *floatValues))
            }
            return this
        }

        private fun getValues(vararg values: Float) : FloatArray{
            if (!nextValueWillBeDp) {
                return values
            }
            val floatValues = FloatArray(values.size)
            for (i in values.indices) {
                floatValues[i] = UiUtil.dp2px(views[0].context.resources, values[i])
            }
            return floatValues
        }

        private fun add(animator: Animator) {
            this.mAnimators.add(animator)
        }

        internal fun getFirstView() : View {
            return views[0]
        }

        fun waitFirstViewForDraw(wait: Boolean) : AnimationSetBuild{
            this.mWaitFirstViewForDrawFilter = wait
            return this
        }

        fun singleInterpolator(singleInterpolator: Interpolator) : AnimationSetBuild{
            this.mSingleInterpolator = singleInterpolator
            return this
        }

        fun dp() : AnimationSetBuild {
            this.nextValueWillBeDp = true
            return this
        }

        fun duration(duration: Long) : AnimationSetBuild{
            this.animatorSet.mDuration = duration
            return this
        }

        fun startDelay(startDelay: Long) : AnimationSetBuild{
            this.animatorSet.mStartDelay = startDelay
            return this
        }

        fun interpolator(interpolator: Interpolator) : AnimationSetBuild{
            this.animatorSet.mInterpolator = interpolator
            return this
        }

        fun repeatCount(repeatCount: Int) : AnimationSetBuild{
            this.animatorSet.mRepeatCount = repeatCount
            return this
        }

        fun repeatMode(repeatMode: Int) : AnimationSetBuild{
            this.animatorSet.mRepeatMode = repeatMode
            return this
        }

        fun thenAnimator(vararg views: View) : AnimationSetBuild{
            return this.animatorSet.thenAnimator(*views)
        }

        fun onStart(onStart: () -> Unit) {
            this.animatorSet.onStart = onStart
        }

        fun onEnd(onStop: () -> Unit) {
            this.animatorSet.onStop = onStop
        }

        fun translationY(vararg values: Float) : AnimationSetBuild {
            return property(TRANSLATION_Y, *values)
        }

        fun translationX(vararg values: Float) : AnimationSetBuild {
            return property(TRANSLATION_X, *values)
        }

        fun alpha(vararg values: Float) : AnimationSetBuild {
            return property(ALPHA, *values)
        }

        fun scaleX(vararg values: Float) : AnimationSetBuild {
            return property(SCALE_X, *values)
        }

        fun scaleY(vararg values: Float) : AnimationSetBuild {
            return property(SCALE_Y, *values)
        }

        fun pivotX(vararg values: Float) : AnimationSetBuild {
            return property(PIVOT_X, *getValues(*values))
        }

        fun pivotY(vararg values: Float) : AnimationSetBuild {
            return property(PIVOT_Y, *getValues(*values))
        }

        fun rotation(vararg values: Float) : AnimationSetBuild {
            return property(ROTATION, *values)
        }

        fun rotationX(vararg values: Float) : AnimationSetBuild {
            return property(ROTATION_X, *values)
        }

        fun rotationY(vararg values: Float) : AnimationSetBuild {
            return property(ROTATION_Y, *values)
        }

        fun custom(vararg values: Float, onUpdate: (View?, Float) -> Unit) : AnimationSetBuild {
            views.forEach { view ->
                val valueAnimator = ValueAnimator.ofFloat(*getValues(*values))
                valueAnimator.addUpdateListener {
                    onUpdate(view, it.animatedValue as Float)
                }
                add(valueAnimator)
            }
            return this
        }

        fun shake() : AnimationSetBuild {
            return translationX(0f, 100f, -100f, 100f, -100f, 100f, -100f, 100f, -100f, 0f)
        }

        fun bounce() : AnimationSetBuild {
            return translationY(0f, 100f, -100f, 100f, -100f, 100f, -100f, 0f)
        }

        fun bounceIn(): AnimationSetBuild {
            //alpha(0f, 1f, 0f, 1f,0f, 1f)
            scaleX(1.0f, 0.7f, 0.3f, 1.0f, 0.7f, 0.3f, 1.0f)
            //scaleY(0.7f,0.3f, 1.0f, 0.7f,0.3f, 1.0f)
            return this
        }

        fun bounceOut(): AnimationSetBuild {
            //scaleY(1f, 0.9f, 1.05f, 0.3f)
            scaleX(1f, 1.2f, 1.5f, 2.0f)
            //alpha(1f, 1f, 1f, 0f,)
            return this
        }

        fun rollIn(): AnimationSetBuild {
            rotation(0f, 360f)
            /*for (view in views) {
                alpha(0f, 1f)
                translationX(-(view.width - view.paddingLeft - view.paddingRight).toFloat(), 0f)
                rotation(0f,-120f, 0f)
            }*/
            return this
        }

        fun wave(): AnimationSetBuild {
            for (view in views) {
                val x = ((view.width - view.paddingLeft - view.paddingRight) / 2
                        + view.paddingLeft).toFloat()
                val y = (view.height - view.paddingBottom).toFloat()
                rotation(12f, -12f, 3f, -3f, 0f)
                pivotX(x, x, x, x, x)
                pivotY(y, y, y, y, y)
            }
            return this
        }

        fun fadeOut(): AnimationSetBuild {
            return alpha(1f, 0.75f, 0.5f, 0.25f, 0f)
        }

        fun path(path: Path?) : AnimationSetBuild {
            path?.let {
                val pathMeasure = PathMeasure(path, false)
                custom(0f, pathMeasure.length) { view, value ->
                    view?.let {
                        val currentPostion = FloatArray(2)
                        pathMeasure.getPosTan(value, currentPostion, null)
                        val x = currentPostion[0]
                        val y = currentPostion[1]
                        it.x = x
                        it.y = y
                    }
                }
            }
            return this
        }

        fun start() : ViewAnimationSet {
            animatorSet.start()
            return animatorSet
        }
    }
}