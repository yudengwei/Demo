package com.example.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

class ViewAnimationSet private constructor(private var mDuration : Long,
                                           private var mStartDelay : Long,
                                           private var mInterpolator : Interpolator? = null,
                                           private var mRepeatCount : Int,
                                           private var mRepeatMode : Int,
                                           private var mIsNeedViewInflater : Boolean){

    private lateinit var mAnimatorSet: AnimatorSet

    private fun createAnimatorSet(build: AnimationSetBuild) : AnimatorSet{
        mAnimatorSet = build.getAnimators()
        for (animator in mAnimatorSet) {
            build.getInterpolator()
        }
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animators)

        animatorSet.duration = build.getDuration()

        build.getInterpolator()?.let {
            animatorSet.interpolator = it
        }


    }

    fun start(build : AnimationSetBuild){

    }


    open class AnimationSetBuild(private val view : View){

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

        private var mDuration = 2000L
        private var mStartDelay = 0L
        private var mInterpolator : Interpolator? = null
        private var mRepeatCount = 0
        private var mRepeatMode = ValueAnimator.RESTART
        private var mIsNeedViewInflater = false
        private val mAnimators = ArrayList<Animator>()

        private fun property(propertyName : String, vararg values : Float) : AnimationSetBuild{
            mAnimators.add(ObjectAnimator.ofFloat(view,propertyName,*values))
            return this
        }

        internal fun getAnimators() : List<Animator>{
            return mAnimators
        }

        internal fun getDuration() : Long{
            return mDuration
        }

        internal fun getStartDelay() : Long{
            return mStartDelay
        }

        internal fun getInterpolator() : Interpolator?{
            return mInterpolator
        }

        fun setDuration(duration : Long) : AnimationSetBuild{
            this.mDuration = duration
            return this
        }

        fun setStartDelay(startDelay : Long) : AnimationSetBuild{
            this.mStartDelay = startDelay
            return this
        }

        fun setInterpolator(interpolator : Interpolator) : AnimationSetBuild{
            this.mInterpolator = interpolator
            return this
        }

        fun setRepeatCount(repeatCount : Int) : AnimationSetBuild{
            this.mRepeatCount = repeatCount
            return this
        }

        fun setRepeatMode(repeatMode : Int) : AnimationSetBuild{
            this.mRepeatMode = repeatMode
            return this
        }

        fun setNeedViewInflater(needViewInflater : Boolean) : AnimationSetBuild{
            this.mIsNeedViewInflater = needViewInflater
            return this
        }

        fun translationY(vararg values : Float) : AnimationSetBuild{
            return property(TRANSLATION_Y,*values)
        }

        fun translationX(vararg values : Float) : AnimationSetBuild{
            return property(TRANSLATION_X,*values)
        }

        fun alpha(vararg values : Float) : AnimationSetBuild{
            return property(ALPHA,*values)
        }

        fun scaleX(vararg values : Float) : AnimationSetBuild{
            return property(SCALE_X,*values)
        }

        fun scaleY(vararg values : Float) : AnimationSetBuild{
            return property(SCALE_Y,*values)
        }

        fun pivotX(vararg values : Float) : AnimationSetBuild{
            return property(PIVOT_X,*values)
        }

        fun pivotY(vararg values : Float) : AnimationSetBuild{
            return property(PIVOT_Y,*values)
        }

        fun rotation(vararg values : Float) : AnimationSetBuild{
            return property(ROTATION,*values)
        }

        fun rotationX(vararg values : Float) : AnimationSetBuild{
            return property(ROTATION_X,*values)
        }

        fun rotationY(vararg values : Float) : AnimationSetBuild{
            return property(ROTATION_Y,*values)
        }

        fun build() : ViewAnimationSet{
            return ViewAnimationSet(
                mDuration,
                mStartDelay,
                mInterpolator,
                mRepeatCount,
                mRepeatMode,
                mIsNeedViewInflater
            )
        }
    }
}