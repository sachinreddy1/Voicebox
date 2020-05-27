package com.sachinreddy.feature.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sachinreddy.feature.data.User

@Dao
interface MyDao {

    @Query("SELECT * FROM userInfo")
    fun getUserInfo(): LiveData<List<User>>

    @Query("UPDATE userInfo SET artistName = :artistName, email= :email WHERE created_at = :createdAt")
    fun updateUserInfo(artistName: String, email: String, createdAt: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUserInfo(user: User)

//    @Delete
//    fun deleteItem(item: User)
//
//    @Query("SELECT * FROM tasks WHERE date = :day")
//    fun getItemsByDay(day: Int): LiveData<List<Item>>

}