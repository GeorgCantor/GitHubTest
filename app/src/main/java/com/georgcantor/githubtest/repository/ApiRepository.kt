package com.georgcantor.githubtest.repository

import com.georgcantor.githubtest.model.data.UsersResponse
import com.georgcantor.githubtest.model.remote.ApiService
import io.reactivex.Observable

class ApiRepository(private val apiService: ApiService) {

    fun getUsers(
        query: String,
        page: Int,
        perPage: Int
    ): Observable<UsersResponse> = apiService.getUsers(query, page, perPage)

}