package com.rahul.roomapp.data.repository.remote


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance {

    const val BASE_URL = "https://jsonplaceholder.typicode.com"

    private fun <T> builder(endpoint: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(endpoint)
    }

            fun getUserFetch(): UserFetch {
                return builder(UserFetch::class.java)
            }
        }