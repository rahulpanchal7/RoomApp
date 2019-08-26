package com.rahul.roomapp

import com.rahul.roomapp.data.remote.GetUser

interface CallCompleted {
    fun onCallComplete(result: List<GetUser>?)
}