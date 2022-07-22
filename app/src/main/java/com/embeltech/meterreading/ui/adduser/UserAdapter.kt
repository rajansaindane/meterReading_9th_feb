package com.embeltech.meterreading.ui.adduser

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.embeltech.meterreading.R
import com.embeltech.meterreading.ui.device.model.User
import com.embeltech.meterreading.ui.device.model.newScreens.ResponseUserListItem
import java.util.ArrayList

class UserAdapter(
    private val context: Context,
    private val userList: ArrayList<ResponseUserListItem>,
    private val userRole: String
) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.single_user,parent,false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  userList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i("@Adapter", "adapter==========>"+ userList!![position])
        if (holder is UserViewHolder) {
            holder.name.text = userList!![position].firstname + " " +  userList!![position].lastname
            holder.mobile.text = userList!![position].contactNo.toString()
            holder.email.text = userList!![position].email.toString()

        }
    }

    fun updateData(newList: ArrayList<ResponseUserListItem>) {
        userList!!.clear()
        userList.addAll(newList)
//        notifyDataSetChanged()
    }

    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
       val  name=itemView.findViewById<AppCompatTextView>(R.id.userFullName)
        val  mobile=itemView.findViewById<AppCompatTextView>(R.id.userMobileNumber)
        val  email=itemView.findViewById<AppCompatTextView>(R.id.userMailId)
    }


}
