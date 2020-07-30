package com.raywenderlich.spacedaily.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    //Creates only one class no matter the times it is called
    single(named("name")){
    }
    //factory creates a new instance each time a variable is defined
}

val coroutines = module {
    single(named("Background")) { Dispatchers.Default }
    single(named("IO")) { Dispatchers.IO }
    single(named("UI")) { Dispatchers.Main }
}