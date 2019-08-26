package com.rahul.roomapp.ui.userauth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rahul.roomapp.ui.main.MainActivity
import com.rahul.roomapp.R
import kotlinx.android.synthetic.main.activity_user_registration.*

class UserRegistration: AppCompatActivity() {
    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private var pref: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase?.reference?.child("Users")
        mAuth = FirebaseAuth.getInstance()

        pref = this.getSharedPreferences("RoomApp", Context.MODE_PRIVATE) ?: return
        var spf = pref?.getString("logged","")
        Log.e("SPF","UA1 <$spf>")
        when(spf){
            "true" ->
            {
                val intent = Intent(this@UserRegistration, MainActivity::class.java)
                startActivity(intent)
                Log.e("SPF","UA2 $spf")
            }

        }

        buttonNewUser.setOnClickListener{
            newUser()
        }
        buttonLogin.setOnClickListener{
            existUser()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth?.currentUser
        updateUI(currentUser)
    }

    private fun newUser(){
        val email = user_reg_email.text.toString()
        val password = user_reg_password.text.toString()
        Log.i("Fire", email)
        Log.i("Fire", password)

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this
                ) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Auth", "createUserWithEmail:success")
                        val user = mAuth!!.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Auth", "createUserWithEmail:failure", task.getException())
                        updateUI(null)
                    }
                }

        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun  existUser(){
        val email = user_reg_email.text.toString()
        val password = user_reg_password.text.toString()

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SIGN", "signInWithEmail:success")
//                   val user = mAuth!!.currentUser

                    pref = this.getSharedPreferences("RoomApp", Context.MODE_PRIVATE)
                    pref?.edit()?.putString("logged","true")?.apply()

                    val intent = Intent(this@UserRegistration, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this@UserRegistration, "hello $email", Toast.LENGTH_SHORT).show()
                    Log.e("SPF","UA4 <${pref?.getString("logged","")}>")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SIGN", "signInWithEmail:failure", task.exception)
                    Toast.makeText(this@UserRegistration, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(user: FirebaseUser?) {

        if (user != null)
        {
            Toast.makeText(this@UserRegistration, " User Registered.", Toast.LENGTH_SHORT).show()
            user_reg_email.setText("")
            user_reg_password.setText("")
        }
        else
        {
            Toast.makeText(this@UserRegistration, " Authentication failed.", Toast.LENGTH_SHORT).show()
            user_reg_email.setText("")
            user_reg_password.setText("")
        }
    }
}
