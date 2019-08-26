package com.rahul.roomapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import com.rahul.roomapp.data.repository.local.UserDao
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rahul.roomapp.data.local.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class], version = 1)
  abstract class UserRoomDatabase : RoomDatabase() { //extends room DB

    abstract fun userDao(): UserDao //creates a function to call dao(methods to interact with entity)

    //Declaring members in companion object to directly access using their resp names
    //prevent having multiple instances of the database opened at the same time.

    companion object {
        @Volatile
        private var INSTANCE: UserRoomDatabase? = null

        //to also launch a coroutine scope
        fun getDatabase(context: Context, scope: CoroutineScope): UserRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                //instance to build DB
                val instance = Room.databaseBuilder(context.applicationContext,
                    UserRoomDatabase::class.java, "User_Database")
                    .addCallback(
                        UserDatabaseCallback(
                            scope
                        )
                    ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class UserDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.userDao())
                }
            }
        }
        fun populateDatabase(userDao: UserDao) {
            userDao.deleteAll()
//            var nameDB = User("Hello")
//            userDao.insert(nameDB)
//            nameDB = User("World!")
//            userDao.insert(nameDB)
        }
    }
}






