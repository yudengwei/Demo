package com.example.view.bezier

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import androidx.core.animation.addListener
import com.example.base.util.LogUtil
import kotlin.math.sqrt
import kotlin.random.Random

class ParticleView @JvmOverloads constructor(context : Context, attributeSet: AttributeSet? = null, defStyleAttr : Int = 0) : View(context, attributeSet, defStyleAttr){

    private val maxRadius = 10f
    private val mParticleNum = 20

    private val mCenterPoint = Point()
    private var maxDistance = 0.toDouble()
    private var minDistance = 0.toDouble()
    private var mMoveDistance = 0f
    private var k = 0f

    private var mParticles = mutableListOf<Particle>()

    private var mMoveParticles = Array(mParticleNum){
        Particle(alpha = 0)
    }

    private var mInitParticles = Array(mParticleNum) {
        Particle()
    }

    private val mParticlePaint = Paint().also {
        it.isAntiAlias = true
        it.color = Color.WHITE
        it.style = Paint.Style.FILL
    }

    private var mIsOut = false

    private val animatorGetParticles = ValueAnimator.ofInt(0, mParticleNum).apply {
        this.duration = 2000
        this.addUpdateListener { value ->
            val count = value.animatedValue as Int
            val size = mParticles.size
            while (size < mParticleNum && count >= mParticles.size) {
                val moveParticle = mMoveParticles[size]
                val initParticle = mInitParticles[size]
                val particle = if (mIsOut) moveParticle.newParticle(radius = initParticle.radius, alpha = 255) else initParticle.newParticle()
                val endParticle = if (mIsOut) mInitParticles[size].newParticle(radius = 0f, alpha = 0) else mMoveParticles[size].newParticle()
                mParticles.add(particle)
                startMoveAnimator(particle, endParticle)
            }
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

    private fun startMoveAnimator(startParticle : Particle, endParticle : Particle) {
        val x = ValueAnimator.ofInt(startParticle.x, endParticle.x).apply {
            this.addUpdateListener { value ->
                startParticle.x = value.animatedValue as Int
                invalidate()
            }
        }
        val y = ValueAnimator.ofInt(startParticle.y, endParticle.y).apply {
            this.addUpdateListener { value ->
                startParticle.y = value.animatedValue as Int
                invalidate()
            }
        }
        val radius = ValueAnimator.ofFloat(startParticle.radius, endParticle.radius).apply {
            this.addUpdateListener { value ->
                startParticle.radius = value.animatedValue as Float
                invalidate()
            }
        }
        val alpha = ValueAnimator.ofInt(startParticle.alpha, endParticle.alpha).apply {
            this.addUpdateListener { value ->
                startParticle.alpha = value.animatedValue as Int
                invalidate()
            }
        }
        AnimatorSet().let {
            it.playTogether(x, y, radius, alpha)
            it.duration = 4000
            it.addListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {

                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

            })
            it.start()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setParam(w / 2,  500)
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
            val radius = Random.nextFloat() * 10
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
        maxDistance = mCenterPoint.x * 0.8
        minDistance = mCenterPoint.x * 0.4
        k = (maxRadius / maxDistance).toFloat()
        mMoveDistance = mCenterPoint.x * 0.2f
    }

    fun inParticle() {
        mIsOut = false
        mParticles.clear()
        getParticlePoint()
        animatorGetParticles.start()
        /*animator.setObjectValues(mInitParticles, mMoveParticles)
        animator.setEvaluator(pointEvaluator)
        animator.start()*/
    }
    
    fun outParticle() {
        mIsOut = true
        mParticles.clear()
        animatorGetParticles.start()
    }

    override fun onDraw(canvas: Canvas) {
        for (particle in mParticles) {
            mParticlePaint.alpha = particle.alpha
            canvas.drawCircle(particle.x.toFloat(), particle.y.toFloat(), particle.radius, mParticlePaint)
        }
    }

    data class Particle(var x : Int = 0, var y : Int = 0, var radius : Float = 0f, var alpha : Int = 255) {
        override fun toString(): String {
            return "Particle(x=$x, y=$y, radius=$radius, alpha=$alpha)"
        }

        fun newParticle(x : Int = this.x, y : Int = this.y, radius : Float = this.radius, alpha : Int = this.alpha) : Particle {
            return Particle(x, y, radius, alpha)
        }
    }
}