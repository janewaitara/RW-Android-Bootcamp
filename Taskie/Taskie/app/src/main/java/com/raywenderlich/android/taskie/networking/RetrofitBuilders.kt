package com.raywenderlich.android.taskie.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

//factory functions require to build retrofit clients and its API services here

fun buildClient(): OkHttpClient =
        OkHttpClient.Builder()
                .build()

//retrofit client factory fun
fun buildRetrofit(): Retrofit{
    return Retrofit.Builder()
            .client(buildClient())
            .addConverterFactory(MoshiConverterFactory.create().asLenient()) //add moshi converter to retrofit which  automatically parses the json and gives it the type that you need
            .baseUrl(BASE_URL)
            .build()
}
//create an API service from the class you have defined
fun buildApiService(): RemoteApiService =
    buildRetrofit().create(RemoteApiService::class.java)
