package com.android.hepsiburadafinalproject.data.network

import com.android.hepsiburadafinalproject.data.iTunesAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{
        fun createInstance(baseUrl:String):Retrofit{
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()

            return retrofit
        }
    }
}