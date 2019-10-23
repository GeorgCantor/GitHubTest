package com.georgcantor.githubtest.model.data

import com.google.gson.annotations.SerializedName

data class UserEntry(
    val login: String,
    val id: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String
)
