package com.rahul.roomapp.data.remote

class GetUser{
    var name: String? = null

    var email: String? = null

    var username: String? = null

    override fun toString() : String {
        return "DataClass [ name = $name , email = $email , username = $username ]"
    }
}