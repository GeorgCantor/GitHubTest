package com.georgcantor.githubtest.di

import org.koin.dsl.module

val repositoryModule = module {
//    single { ApiRepository(get()) }
}

val viewModelModule = module {
//    viewModel {
//        MainViewModel(get())
//    }
}

val appModule = module {
//    single { ApiClient.create(get()) }
}