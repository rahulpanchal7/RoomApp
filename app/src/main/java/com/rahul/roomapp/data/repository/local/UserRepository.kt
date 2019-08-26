package com.rahul.roomapp.data.repository.local

import androidx.lifecycle.LiveData
import androidx.annotation.WorkerThread
import com.rahul.roomapp.data.local.User

//Executing the queries
class UserRepository(private val userDao: UserDao) {

    val allUsers: LiveData<List<User>> = userDao.getAllUsers()
    @WorkerThread //ensures that no long-running operations on the main thread, blocking the UI
    //suspend - tell the compiler that this needs to be called from a coroutine or another suspending function.
    fun insert(user: User) {
        userDao.insert(user)
    }


}