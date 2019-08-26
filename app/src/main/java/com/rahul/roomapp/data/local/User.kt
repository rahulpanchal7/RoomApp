package com.rahul.roomapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table") //nameDB of table..defining as table..

data class User(
    @PrimaryKey
    @ColumnInfo(name = "nameDB") val nameDB: String,
    @ColumnInfo(name = "emailDB") val emailDB: String,
    @ColumnInfo(name = "usernameDB") val usernameDB: String
)









