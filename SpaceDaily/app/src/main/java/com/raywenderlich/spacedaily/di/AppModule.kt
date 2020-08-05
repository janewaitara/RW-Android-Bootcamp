package com.raywenderlich.spacedaily.di

import com.raywenderlich.spacedaily.ActivityRetriever
import com.raywenderlich.spacedaily.DefaultCurrentActivityListener
import org.koin.dsl.module

val appModule = module {
    single { DefaultCurrentActivityListener() }//provide default current activity
    single { ActivityRetriever(get()) } //we use the get function to inject the
}