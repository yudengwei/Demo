package com.example.view.bezier

import android.animation.Animator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import com.example.base.util.LogUtil
import com.example.view.interpolator.*
import kotlin.math.sqrt
import kotlin.random.Random

class ParticleView @JvmOverloads constructor(context : Context, attributeSet: AttributeSet? = null, defStyleAttr : Int = 0) : View(context, attributeSet, defStyleAttr){

    private val maxRadius = 5f

    private val mCenterPoint = Point()
    private var maxDistance = 0.toDouble()
    private var minDistance = 0.toDouble()
    private var mMoveDistance = 0f
    private var k = 0f

    private var mParticles = mutableListOf<Particle>()

    private var mMoveParticles = Array(50){
        Particle(alpha = 0)
    }

    private var mInitParticles = Array(50) {
        Particle()
    }

    private val mParticlePaint = Paint().also {
        it.isAntiAlias = true
        it.color = Color.WHITE
        it.style = Paint.Style.FILL
    }

    private val pointEvaluator = PointEvaluator()

    private var mIsOut = false

    private val animator = ValueAnimator().also {
        it.duration = 4000
       // it.interpolator = LinearInterpolator()
        it.addUpdateListener { value->
            val particleValues = value.animatedValue as Array<Particle>
            for(i in mParticles.indices) {
                val p = mParticles[i]
                val pp = particleValues[i]
                p.x = pp.x
                p.y = pp.y
                p.radius = pp.radius
                p.alpha = pp.alpha
            }
            invalidate()
        }
        it.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                mIsOut = true
                mParticles.clear()
                animatorGetParticles.start()
                /*animatorOut.setObjectValues(mMoveParticles, mInitParticles)
                animatorOut.setEvaluator(pointEvaluator)
                animatorOut.start()*/
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
    }

    private val animatorOut = ValueAnimator().also {
        it.duration = 4000
        it.addUpdateListener { value->
            val particleValues = value.animatedValue as Array<Particle>
            for(i in mParticles.indices) {
                val p = mParticles[i]
                val pp = particleValues[i]
                p.x = pp.x
                p.y = pp.y
                p.radius = pp.radius
                p.alpha = pp.alpha
            }
            invalidate()
        }
        it.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                invalidate()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
    }

    private val animatorGetParticles = ValueAnimator.ofInt(0, 50).apply {
        this.duration = 800
        this.addUpdateListener { value ->
            val count = value.animatedValue as Int
            val size = mParticles.size
            while (size < 50 && count >= mParticles.size) {
                mParticles.add(if (mIsOut) mMoveParticles[size] else mInitParticles[size])
            }
            invalidate()
        }
        this.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setParam(w / 2,  w / 2)
    }

    private fun getParticlePoint(){
        for (i in mInitParticles.indices) {
            val particle = mInitParticles[i]
            val moveParticle = mMoveParticles[i]
            val x = Random.nextInt(0, width)
            val x2x = (x - maxDistance) * (x - maxDistance)
            val maxY = (sqrt(maxDistance * maxDistance - x2x)).toInt() + mCenterPoint.y
            val minY = (sqrt(minDistance * minDistance -x2x)).toInt() + mCenterPoint.y
            var y = if (maxY == minY) maxY else Random.nextInt(minY, maxY)
            y = if (Random.nextInt(2) > 0 || mCenterPoint.y * 2 - y < 0) y else mCenterPoint.y * 2 - y
            val distance = sqrt(x2x + (y - mCenterPoint.y) * (y - mCenterPoint.y)).toFloat()
            val radius = distance * k
            val sin = if (y > mCenterPoint.y) (y - mCenterPoint.y).toFloat() / distance else (mCenterPoint.y - y).toFloat() / distance
            val cos = if (x > mCenterPoint.x) (x - mCenterPoint.x).toFloat() / distance else (mCenterPoint.x - x).toFloat() / distance
            val moveY = (sin * mMoveDistance).toInt()
            val moveX = (cos * mMoveDistance).toInt()
            particle.x = x
            particle.y = y
            particle.radius = radius
            moveParticle.x = if (x > mCenterPoint.x) mCenterPoint.x + moveX else mCenterPoint.x - moveX
            moveParticle.y = if (y > mCenterPoint.y) mCenterPoint.y + moveY else mCenterPoint.y - moveY
        }
    }

    fun setParam(x : Int, y : Int) {
        mCenterPoint.x = x
        mCenterPoint.y = y
        maxDistance = mCenterPoint.x.toDouble()
        minDistance = maxDistance * 0.8
        k = (maxRadius / maxDistance).toFloat()
        mMoveDistance = mCenterPoint.x * 0.2f
    }

    fun showParticle() {
        getParticlePoint()
        animatorGetParticles.start()
        /*animator.setObjectValues(mInitParticles, mMoveParticles)
        animator.setEvaluator(pointEvaluator)
        animator.start()*/
    }

    override fun onDraw(canvas: Canvas) {
        for (particle in mParticles) {
            mParticlePaint.alpha = particle.alpha
            canvas.drawCircle(particle.x.toFloat(), particle.y.toFloat(), particle.radius, mParticlePaint)
        }
    }

    data class Particle(var x : Int = 0, var y : Int = 0, var radius : Float = 0f, var alpha : Int = 255)

    class PointEvaluator : TypeEvaluator<Array<Particle>> {

        override fun evaluate(fraction: Float, startValues: Array<Particle>, endValues: Array<Particle>): Array<Particle> {
            return Array(50) {
                val startValue = startValues[it]
                val endValue = endValues[it]
                val x = (startValue.x + fraction * (endValue.x - startValue.x)).toInt()
                val y = (startValue.y + fraction * (endValue.y - startValue.y)).toInt()
                val alpha = (startValue.alpha + fraction * (endValue.alpha - startValue.alpha)).toInt()
                val radius = startValue.radius + fraction * (endValue.radius - startValue.radius)
                Particle(x, y, radius = radius, alpha = alpha)
            }
        }
    }
}