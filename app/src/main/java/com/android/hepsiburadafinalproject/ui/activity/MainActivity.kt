package com.android.hepsiburadafinalproject.ui.activity


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.hepsiburadafinalproject.R


class MainActivity : AppCompatActivity() {

    var preferences: SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferences=getSharedPreferences("com.android.hepsiburadafinalproject", Context.MODE_PRIVATE)
        preferences?.edit()?.remove("category")?.apply()
        preferences?.edit()?.remove("searchTerm")?.apply()

    }

}