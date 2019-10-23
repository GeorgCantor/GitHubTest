package com.georgcantor.githubtest.model.data

import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("total_count")
    val total: Int,
    @SerializedName("items")
    val items: List<UserEntry>
)