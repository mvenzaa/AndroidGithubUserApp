package com.venza.androidcodingtest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class UserViewModel (application: Application): AndroidViewModel(application){

    private var userRepository:UserRepository?=null
    var postModelListLiveData : LiveData<List<UserModel>>?=null

    init {
        userRepository = UserRepository()
        postModelListLiveData = MutableLiveData()
    }

    fun getAllUsers(){
        postModelListLiveData = userRepository?.getAllUsers()
    }

}