package com.rahul.roomapp.ui.component

import androidx.lifecycle.LiveData
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.roomapp.data.local.User
import com.rahul.roomapp.data.local.room.UserRoomDatabase
import com.rahul.roomapp.data.repository.local.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//create a view model class
//never pass context,activity,frgmnt..etc in view model..to prevent data leak

class UserViewModel(application: Application) : AndroidViewModel(application) {
//declare member variables for referencing
private val repository: UserRepository
    val allUsers: LiveData<List<User>>

    init {

        val usersDao = UserRoomDatabase.getDatabase(application, viewModelScope).userDao()
        repository = UserRepository(usersDao)
        allUsers = repository.allUsers
    }
//cancel an insertion to the DB when you navigate back from a screen
    fun insert(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(user)
    }
}