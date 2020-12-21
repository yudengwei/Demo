package com.example.base.delegate

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PreferenceDelegate<T>(private val context: Context,private val name : String, private val default : T,private val prefName : String = "abiao")
    : ReadWriteProperty<Any?,T> {

    private var oldValue : T? =null

    private val prefs : SharedPreferences by lazy {
        context.getSharedPreferences(prefName,Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return oldValue ?: getProperty(name)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        setProperty(name,value)
    }

    private fun getProperty(key : String) : T{
        Log.d("MainActivity", "getProperty: $key")
        oldValue =  when(default){
            is String -> prefs.getString(key,default)
            is Int -> prefs.getInt(key,default)
            is Boolean -> prefs.getBoolean(key,default)!!
            is Float -> prefs.getFloat(key,default)!!
            is Long -> prefs.getLong(key,default)!!
            else -> throw IllegalArgumentException("UnKnown Type")
        } as T
        return oldValue!!
    }

    private fun setProperty(key : String,value : T) = with(prefs.edit()){
        oldValue = value
        when(value){
            is String -> putString(key,value)
            is Int -> putInt(key,value)
            is Boolean -> putBoolean(key,value)
            is Float -> putFloat(key,value)
            is Long -> putLong(key,value)
            else -> throw IllegalArgumentException("UnKnown Type")
        }
    }.apply()
}