package com.rahul.roomapp.ui.component

import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rahul.roomapp.R
import com.rahul.roomapp.data.local.User

//private class UserListAdapter(context: Context) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {
class UserListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var users = emptyList<User>() // Cached copy of words

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameItemView: TextView = itemView.findViewById(R.id.txt_name)
        val emailItemView: TextView = itemView.findViewById(R.id.txt_email)
        val usernameItemView: TextView = itemView.findViewById(R.id.txt_username)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val current = users[position]
        holder.nameItemView.text = current.nameDB
        holder.emailItemView.text = current.emailDB
        holder.usernameItemView.text = current.usernameDB
    }

    internal fun setWords(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun getItemCount() = users.size

    // Filter Class
    fun setItems(users: List<User>) {
        this.users = users
    }
}

