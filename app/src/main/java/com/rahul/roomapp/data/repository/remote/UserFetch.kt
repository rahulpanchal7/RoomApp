package com.rahul.roomapp.data.repository.remote

import com.rahul.roomapp.data.remote.GetUser
import retrofit2.Call
import retrofit2.http.GET

interface UserFetch {
    @GET("/users")
    fun getAllUsers() : Call<List<GetUser>>
}