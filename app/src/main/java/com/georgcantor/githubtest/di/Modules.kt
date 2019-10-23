package com.georgcantor.githubtest.di

import com.georgcantor.githubtest.model.remote.ApiClient
import com.georgcantor.githubtest.repository.ApiRepository
import com.georgcantor.githubtest.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { ApiRepository(get()) }
}

val viewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
}

val appModule = module {
    single { ApiClient.create(get()) }
}