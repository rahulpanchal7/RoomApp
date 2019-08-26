package com.rahul.roomapp.ui.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rahul.roomapp.ui.component.UserListAdapter
import com.rahul.roomapp.ui.component.UserViewModel
import com.rahul.roomapp.data.repository.remote.RetrofitClientInstance
import com.rahul.roomapp.data.repository.local.UserDao
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import kotlin.coroutines.CoroutineContext
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rahul.roomapp.R
import kotlin.NullPointerException
import com.rahul.roomapp.data.local.User
import com.rahul.roomapp.data.local.room.UserRoomDatabase
import com.rahul.roomapp.data.remote.GetUser
import com.rahul.roomapp.data.firebase.FireDatabaseHelper

class MainActivity : AppCompatActivity(),CoroutineScope {

    companion object { const val newWordActivityRequestCode = 1 }

    private val job by lazy { Job() }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var prgbar : ProgressBar? = null

    lateinit var user : User
    private val users: MutableList<User> = mutableListOf()
    private lateinit var mUserViewModel: UserViewModel
    private lateinit var mUserDao : UserDao

    private val mDatabaseHelper = FireDatabaseHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        prgbar  = findViewById(R.id.progressBar)
        mUserViewModel= ViewModelProviders.of(this).get(UserViewModel::class.java)
        mUserDao = UserRoomDatabase.getDatabase(this, this).userDao()

        try {
            prgbar!!.visibility = View.VISIBLE
        } catch (e: NullPointerException) {
            Log.i("MyGetTask",""+e.printStackTrace())
        }

        val floatBtn = findViewById<FloatingActionButton>(R.id.fab)
        floatBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, AddUser::class.java)
            startActivityForResult(intent,
                newWordActivityRequestCode
            )
        }

        val service = RetrofitClientInstance
        service.getUserFetch().getAllUsers().enqueue(object : retrofit2.Callback<List<GetUser>> {
            override fun onFailure(call: Call<List<GetUser>>, t: Throwable) {
                Log.e("MyGetTask", "Fail 1")
            }
            override fun onResponse(call: Call<List<GetUser>>, response: Response<List<GetUser>>) {
                Log.i("MyGetTask", "Success 1")
                try {
                    prgbar!!.visibility = View.GONE
                } catch (e: Exception) {
                    Log.i("MyGetTask",""+e.printStackTrace())
                }
                var userlist = response.body()!!
                userlist.forEach {
                    users.add(
                        User(
                            it.name ?: "",
                            it.email ?: "",
                            it.username ?: ""
                        )
                    )
                    mDatabaseHelper.writeUser(it.name?:"",it.email?:"", it.username?:"")
                }
                 mUserDao.insertAll(users)
            }
        })

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = UserListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //observes any change in live data
        mUserViewModel.allUsers.observe(this, Observer { users ->
            // Update the cached copy of the words in the adapter.
            users?.let { adapter.setWords(it) }
            Log.i("DB","User_list $users ")
        })

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query:String?):Boolean {
                Log.i("Filter","submit $query")
//                val  text1 = query.let { it } ?: ""
//                var list = mUserDao.getFilter(text1)
//                list.siz
                return false
            }

            override fun onQueryTextChange(newText : String?):Boolean {
                Log.i("Filter","change $newText")
                val  text2 = newText.let { it } ?: ""
                var list = mUserDao.getFilter(text2)
                list.size
                //to refresh adapter
                adapter.setItems(list)
                adapter.notifyDataSetChanged()
                return false
            }
        })
    }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.let {
                 user = User(
                     it.getStringExtra(AddUser.EXTRA_REPLY1),
                     it.getStringExtra(AddUser.EXTRA_REPLY2),
                     it.getStringExtra(AddUser.EXTRA_REPLY3)
                 )
                Log.i("DB","User1[ $user ]")

                mUserViewModel.insert(user)
            }
        } else {
             Toast.makeText(
             applicationContext,
                 R.string.empty_not_saved,
             Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
