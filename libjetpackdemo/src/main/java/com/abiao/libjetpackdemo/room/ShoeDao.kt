package com.abiao.libjetpackdemo.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.abiao.libjetpackdemo.model.Shoe

@Dao
interface ShoeDao {

    @Insert(entity = Shoe::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertShoe(shoe : Shoe)

    @Insert(entity = Shoe::class, onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    fun insertShoes(shoes : List<Shoe>)

    @Query("select * from shoe")
    fun getAllShoes() : List<Shoe>

    @Query("select * from shoe where id = :id")
    fun getShoeById(id : Int) : Shoe

    @Delete(entity = Shoe::class)
    fun delete(shoe : Shoe)

    @Delete
    fun delete(shoes : List<Shoe>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(shoe : Shoe)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(shoes : List<Shoe>)
}