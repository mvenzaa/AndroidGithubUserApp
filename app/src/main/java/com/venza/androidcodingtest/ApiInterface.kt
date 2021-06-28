package com.venza.androidcodingtest

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("users")
    fun getAllUsers(): Call<List<UserModel>>
}