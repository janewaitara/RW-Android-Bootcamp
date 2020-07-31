package com.raywenderlich.spacedaily.di

import com.raywenderlich.spacedaily.network.NASAAPIInterface
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


/**
sample

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

factory (named("name")){

}
}*/

//building all variables to inject
val networkModule = module {
    single (named("BASE_URL")){
        "https://api.nasa.gov/plantary"
    }
    //Logging Interceptor
    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        interceptor //returning the interceptor
    }
    //OKHTTPClient class
    single {
        val clientBuilder = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
        if ( BuildConfig.DEBUG){
            clientBuilder.addInterceptor(get<HttpLoggingInterceptor>()) //using get with a type to let koin know which interceptor to get
        }
        clientBuilder.build()
    }
    //moshi instance
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())//adapts json so that it can convert any json into our model object
            .build()
    }
    //Retrofit class
    single {
        Retrofit.Builder().baseUrl(get<String>(named("BASE_URL")))
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .build()
    }
    //create NASA interface
    single {
        get<Retrofit>().create(NASAAPIInterface::class.java)
    }
}