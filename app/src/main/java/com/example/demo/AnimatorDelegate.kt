package com.example.demo

import android.animation.AnimatorSet
import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AnimatorDelegate(private val context : Context, private val resourceId : Int) : ReadOnlyProperty<Any?,Animation>{

    override fun getValue(thisRef: Any?, property: KProperty<*>): Animation {
        return AnimationUtils.loadAnimation(context,resourceId)
    }
}

