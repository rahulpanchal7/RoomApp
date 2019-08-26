package com.rahul.roomapp.data.firebase

data class FireUser (
    var name: String? = "",
    var email: String? = "",
    var username: String? = ""
)
//    constructor()
//
//    constructor(name : String, email : String, username : String) : this() {
//        this.name = name
//        this.email = email
//        this.username = username
//    }
//
//    fun toMap() : Map<String, Any> {
//        val result = mutableMapOf<String, Any>()
//        result["name"] = name!!
//        result["email"] = email!!
//        result["username"] = username!!
//        return result.toMap()
//    }
//
//    override fun toString(): String {
//        return name.toString() + " Shop: " + email.toString() + " Date: "  + username.toString()
//    }
