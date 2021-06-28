package com.venza.androidcodingtest

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*
import kotlin.collections.ArrayList
import com.venza.androidcodingtest.databinding.ItemUserBinding

class RecyclerViewAdapter(private var userList : ArrayList<UserModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    //var userList = ArrayList<UserModel>()
    var userFilterList = ArrayList<UserModel>()

    lateinit var mContext: Context

    class UserHolder(var viewBinding: ItemUserBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    init {
        userFilterList = userList
    }

//    fun setData(list: ArrayList<UserModel>) {
//        userFilterList = list
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val sch = UserHolder(binding)
        mContext = parent.context
        return sch
    }

    override fun getItemCount(): Int {
        return userFilterList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val userHolder = holder as UserHolder
        userHolder.viewBinding.txtUsername.text = userFilterList[position].login
        Glide.with(mContext)
            .load(userFilterList[position].avatar_url)
            .apply(RequestOptions.circleCropTransform())
            .into(userHolder.viewBinding.imgPhoto)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    userFilterList = userList
                } else {
                    val resultList = ArrayList<UserModel>()
                    for (row in userList) {
                        if (row.login.lowercase().contains(charSearch.lowercase())) {
                            resultList.add(row)
                        }
                    }
                    userFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = userFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                userFilterList = results?.values as ArrayList<UserModel>
                notifyDataSetChanged()
            }

        }
    }
}