package com.android.hepsiburadafinalproject.model


import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val results: ArrayList<Result>
)