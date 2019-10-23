package com.georgcantor.githubtest.viewmodel

import androidx.lifecycle.ViewModel
import com.georgcantor.githubtest.model.data.UsersResponse
import com.georgcantor.githubtest.repository.ApiRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class MainViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    fun getUsers(
        query: String,
        page: Int,
        perPage: Int
    ): Observable<Response<UsersResponse>> = apiRepository.getUsers(query, page, perPage)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

}