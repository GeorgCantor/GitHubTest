package com.georgcantor.githubtest.viewmodel

import androidx.lifecycle.ViewModel
import com.georgcantor.githubtest.model.data.UserEntry
import com.georgcantor.githubtest.model.data.UsersResponse
import com.georgcantor.githubtest.repository.ApiRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class UsersViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    fun getUsers(
        query: String,
        page: Int,
        perPage: Int
    ): Observable<List<UserEntry>> =
        apiRepository.getUsers(query, page, perPage).map(UsersResponse::items)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}