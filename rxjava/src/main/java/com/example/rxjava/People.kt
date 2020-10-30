package com.example.rxjava

class People(val name : String? = null,val age : Int = 0) {

    override fun equals(other: Any?): Boolean {
        return age == (other as People).age
    }

    override fun hashCode(): Int {
        return age.hashCode()
    }
}