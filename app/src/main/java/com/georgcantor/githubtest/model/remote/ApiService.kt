package com.georgcantor.githubtest.model.remote

import com.georgcantor.githubtest.model.data.UsersResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun getUsers(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Observable<UsersResponse>

}