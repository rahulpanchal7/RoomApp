package com.rahul.roomapp.data.firebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FireDatabaseHelper {
    val user = FireUser()
    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.reference
    private val fireuserlist = mutableListOf<FireUser>()

    fun readUser(status : DataStatus) {
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("DATABASE_ERROR", "Error while reading appointments", error.toException())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                fireuserlist.clear()
                for (data in dataSnapshot.children) {
                    fireuserlist.add(data.getValue(FireUser::class.java)!!)
                }
                status.DataIsLoaded(fireuserlist)
            }

        })
    }
    fun writeUser(name : String, email : String, username : String) {
        //Create a new instance in Database/appointments
        Log.i("DATA_BASE","data write $name $email $username")
        val key = userRef.push().key
        //Add my appointment object
        userRef.child(key!!).setValue(FireUser(name, email, username))
    }
    interface DataStatus {
        fun DataIsLoaded(fireusers: MutableList<FireUser>)
        fun DataIsInserted()
    }
}
