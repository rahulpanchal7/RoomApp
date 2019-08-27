package com.rahul.roomapp.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Camera
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.rahul.roomapp.R
import com.rahul.roomapp.data.firebase.FireDatabaseHelper
import com.rahul.roomapp.data.firebase.FireUser
import com.rahul.roomapp.ui.userauth.UserRegistration
import kotlinx.android.synthetic.main.activity_add_user.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class AddUser : AppCompatActivity() {

    private val databaseHelper = FireDatabaseHelper()
    private var mAuth: FirebaseAuth? = null
    private var pref: SharedPreferences? = null

    companion object {
        val REQUEST_IMAGE_CAPTURE = 1
        const val EXTRA_REPLY1 = "REPLY1"
        const val EXTRA_REPLY2 = "REPLY2"
        const val EXTRA_REPLY3 = "REPLY3"
    }

    private lateinit var currentPhotoPath: String

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
        val editnameView = findViewById(R.id.edit_name) as EditText
        val editemailView = findViewById(R.id.edit_email) as EditText
        val editusernameView = findViewById(R.id.edit_username) as EditText
        val save_button = findViewById<Button>(R.id.button_save)


        pref = this.getSharedPreferences("RoomApp", Context.MODE_PRIVATE) ?: return

        Log.i("SPF", "UA3 <${pref?.getString("logged", "")}>")
        save_button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editnameView.text) && TextUtils.isEmpty(editemailView.text) && TextUtils.isEmpty(
                    editusernameView.text
                )
            ) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val name = editnameView.text.toString()
                val email = editemailView.text.toString()
                val username = editusernameView.text.toString()

                replyIntent.putExtra(EXTRA_REPLY1, name)
                Log.i("DB", "Name $name")

                replyIntent.putExtra(EXTRA_REPLY2, email)
                Log.i("DB", "email $email")

                replyIntent.putExtra(EXTRA_REPLY3, username)
                Log.i("DB", "username $username")

                databaseHelper.writeUser(name, email, username)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        button_camera.setOnClickListener {
            dispatchTakePictureIntent()
        }

        button_logout.setOnClickListener {
            mAuth?.signOut()
            pref?.edit()?.putString("logged", "false")?.apply()
            Log.i("SPF", "UA4 <${pref?.getString("logged", "")}>")

            val intent = Intent(applicationContext, UserRegistration::class.java)
            startActivity(intent)
        }

        //to read only data
        databaseHelper.readUser(object : FireDatabaseHelper.DataStatus {
            override fun DataIsLoaded(fireusers: MutableList<FireUser>) {
                Log.i("DATA_BASE", "Data is loaded")
            }

            override fun DataIsInserted() {
                Log.i("DATA_BASE", "Data is inserted")
            }
        })
    }

    private fun dispatchTakePictureIntent() {

        /* MediaStore.ACTION_IMAGE_CAPTURE is used to capture images or videos without directly using the Camera object*/
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
//                galleryAddPic()
                photoFile?.also {
                    val photoURI: Uri =
                        FileProvider.getUriForFile(this, "com.rahul.roomapp.fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//                }
                }
            }
        }

    }
    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "RoomApp_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }
}
