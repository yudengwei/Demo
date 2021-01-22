package com.example.view.bezier

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import com.example.base.util.LogUtil
import kotlin.math.sqrt
import kotlin.random.Random

class ParticleView @JvmOverloads constructor(context : Context, attributeSet: AttributeSet? = null, defStyleAttr : Int = 0) : View(context, attributeSet, defStyleAttr){

    private val maxRadius = 20f

    private val mCenterPoint = Point()
    private var maxDistance = 0.toDouble()
    private var minDistance = 0.toDouble()
    private var k = 0f

    private var mParticles : Array<Particle>? = null
    private var mInitParticles : Array<Particle>? = null

    private val mParticlePaint = Paint().also {
        it.isAntiAlias = true
    }

    private val mAnimator = ValueAnimator.ofFloat(0f, 0.2f).also {
        it.duration = 4000
        it.addUpdateListener { value ->
            mParticles?.let { particles->
                for (i in particles.indices) {
                    val particle = particles[i]
                    val initParticle = mInitParticles!![i]
                    val realValue = value.animatedValue as Float
                    particle.x = (initParticle.x * realValue).toInt()
                    particle.y = (initParticle.y * realValue).toInt()
                    mParticlePaint.alpha = (255 * (0.2f - (value.animatedValue as Float))).toInt()
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setParam(w / 2, h / 2)
    }

    private fun getParticlePoint() : Array<Particle> {
        return Array(50) {
            val x = Random.nextInt(0, width)
            val x2x = (x - maxDistance) * (x - maxDistance)
            val maxY = (sqrt(maxDistance * maxDistance - x2x)).toInt() + mCenterPoint.y
            val minY = (sqrt(minDistance * minDistance -x2x)).toInt() + mCenterPoint.y
            var y = Random.nextInt(minY, maxY)
            y = if (Random.nextInt(2) > 0) y else height - y
            val distance = sqrt(x2x + (y - mCenterPoint.y) * (y - mCenterPoint.y)).toFloat()
            val radius = distance * k
            Particle(x, y, radius)
        }
    }

    fun setParam(x : Int, y : Int) {
        mCenterPoint.x = x
        mCenterPoint.y = y
        maxDistance = mCenterPoint.x.toDouble()
        minDistance = maxDistance * 0.8
        k = (maxRadius / maxDistance).toFloat()
    }

    fun showOrHideParticle() {
        mParticles = if (mParticles == null) {
            getParticlePoint()
        } else {
            null
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        mParticles?.let {
            mParticlePaint.color = Color.RED
            mParticlePaint.style = Paint.Style.STROKE
            canvas.drawCircle(mCenterPoint.x.toFloat(), mCenterPoint.y.toFloat(), mCenterPoint.x.toFloat(), mParticlePaint)
            canvas.drawCircle(mCenterPoint.x.toFloat(), mCenterPoint.y.toFloat(), (mCenterPoint.x.toFloat() * 0.8).toFloat(), mParticlePaint)
            mParticlePaint.color = Color.BLACK
            mParticlePaint.style = Paint.Style.FILL
            for (particle in it) {
                canvas.drawCircle(particle.x.toFloat(), particle.y.toFloat(), particle.radius, mParticlePaint)
            }
        }
    }

    data class Particle(var x : Int, var y : Int, val radius : Float)
}