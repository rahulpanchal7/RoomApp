package com.rahul.roomapp.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.rahul.roomapp.R
import com.rahul.roomapp.data.firebase.FireDatabaseHelper
import com.rahul.roomapp.data.firebase.FireUser
import com.rahul.roomapp.ui.userauth.UserRegistration
import kotlinx.android.synthetic.main.activity_new_user.*

class AddUser : AppCompatActivity() {

    private val databaseHelper = FireDatabaseHelper()
    private var mAuth: FirebaseAuth? = null
    private var pref: SharedPreferences? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
        var editnameView = findViewById(R.id.edit_name) as EditText
        var editemailView = findViewById(R.id.edit_email) as EditText
        var editusernameView = findViewById(R.id.edit_username) as EditText
        val save_button = findViewById<Button>(R.id.button_save)

        pref = this.getSharedPreferences("RoomApp", Context.MODE_PRIVATE) ?: return

        Log.i("SPF","UA3 <${pref?.getString("logged","")}>")
        save_button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editnameView.text ) && TextUtils.isEmpty(editemailView.text ) && TextUtils.isEmpty(editusernameView.text )) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                var name= editnameView.text.toString()
                var email= editemailView.text.toString()
                var username = editusernameView.text.toString()

                replyIntent.putExtra(EXTRA_REPLY1,name)
                Log.i("DB","Name $name")

                replyIntent.putExtra(EXTRA_REPLY2, email)
                Log.i("DB","email $email")

                replyIntent.putExtra(EXTRA_REPLY3, username)
                Log.i("DB","username $username")

                databaseHelper.writeUser(name,email,username)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        button_logout.setOnClickListener{
            mAuth?.signOut()
            pref?.edit()?.putString("logged","false")?.apply()
            Log.i("SPF","UA4 <${pref?.getString("logged","")}>")

            val intent = Intent(applicationContext, UserRegistration::class.java)
            startActivity(intent)
        }

        //to read only data
        databaseHelper.readUser(object : FireDatabaseHelper.DataStatus {
            override fun DataIsLoaded(fireusers: MutableList<FireUser>) {
                Log.i("DATA_BASE","Data is loaded")
            }

            override fun DataIsInserted() {
                Log.i("DATA_BASE","Data is inserted")
            }
        })
    }

    companion object {
        const val EXTRA_REPLY1 = "REPLY1"
        const val EXTRA_REPLY2 = "REPLY2"
        const val EXTRA_REPLY3 = "REPLY3"
    }
}

