package com.raywenderlich.android.taskie.networking

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType

//factory functions require to build retrofit clients and its API services here

fun buildClient(): OkHttpClient =
        OkHttpClient.Builder()
                .build()

//retrofit client factory fun
fun buildRetrofit(): Retrofit{
    val contentType = "application/json".toMediaType()
    return Retrofit.Builder()
            .client(buildClient())
            .addConverterFactory(Json.nonstrict.asConverterFactory(contentType)) // automatically parses the json and gives it the type that you need and nonstrict gives a more forgiving parser
            .baseUrl(BASE_URL)
            .build()
}
//create an API service from the class you have defined
fun buildApiService(): RemoteApiService =
    buildRetrofit().create(RemoteApiService::class.java)
