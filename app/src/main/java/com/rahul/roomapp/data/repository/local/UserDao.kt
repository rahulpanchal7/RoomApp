package com.rahul.roomapp.data.repository.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rahul.roomapp.data.local.User

@Dao
interface UserDao {

    @Query(" SELECT * from user_table") //to get the table
    fun getAllUsers() : LiveData<List<User>> //LiveData is used to update DB values..also runs on backgrnd thread async

    @Insert(onConflict = OnConflictStrategy.REPLACE) //onConflict used to replace fields/words with same primary key
    fun insert(user: User) //insert a word

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users : List<User>)

    @Query("DELETE FROM user_table")
    fun deleteAll() //delete words

    @Query("SELECT * FROM user_table WHERE nameDB || emailDB || usernameDB LIKE '%' || :value  || '%'") //filtering using search view
    fun getFilter(value : String): List<User>

}