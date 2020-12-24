package com.abiao.libjetpackdemo.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "shoe", indices = [Index("id")])
class Shoe(@PrimaryKey(autoGenerate = false) val id : Int, @Bindable var name : String, val description : String,
           val price : Float, val brand : String, val imageUrl : String) : BaseObservable(){

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Shoe) return false
        if (other.id != this.id) return false
        if (other.description != this.description) return false
        if (other.brand != this.brand) return false
        if (other.price != this.price) return false
        if (other.imageUrl != this.imageUrl) return false
        if (other.name != this.name) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + brand.hashCode()
        result = 31 * result + imageUrl.hashCode()
        return result
    }

    fun clone(): Shoe {
        return Shoe(id, name, description, price, brand, imageUrl)
    }

    @JvmName("setName1")
    fun setName(name : String) {
        this.name = name
        notifyPropertyChanged(com.abiao.libjetpackdemo.BR.name)
    }
}