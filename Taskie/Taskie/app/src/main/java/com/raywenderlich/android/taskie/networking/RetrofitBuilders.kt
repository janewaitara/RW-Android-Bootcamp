package com.raywenderlich.android.taskie.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit

//factory functions require to build retrofit clients and its API services here

fun buildClient(): OkHttpClient =
        OkHttpClient.Builder()
                .build()

//retrofit client factory fun
fun buildRetrofit(): Retrofit{
    return Retrofit.Builder()
            .client(buildClient())
            .baseUrl(BASE_URL)
            .build()
}
//create an API service from the class you have defined
fun buildApiService(): RemoteApiService =
    buildRetrofit().create(RemoteApiService::class.java)
