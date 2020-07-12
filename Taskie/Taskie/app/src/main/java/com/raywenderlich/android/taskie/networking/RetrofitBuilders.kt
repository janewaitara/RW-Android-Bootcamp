package com.raywenderlich.android.taskie.networking

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.raywenderlich.android.taskie.App
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

//factory functions require to build retrofit clients and its API services here

//constant to represent authorization header
private const val HEADER_AUTHORIZATION = "Authorization"

fun buildClient(): OkHttpClient =
        OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY //want to log the body level of the request which mean little report of sent and received data
                })
                .addInterceptor(buildAuthorizationInterceptor())
                .build()

fun buildAuthorizationInterceptor() = object : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        //checking if there is a token saved in the app , if there isn't proceed with the original
        if (App.getToken().isBlank()) return chain.proceed(originalRequest)

        //if there is, create a new one from original one and add header in the runtime
        val new = originalRequest.newBuilder()
                .addHeader(HEADER_AUTHORIZATION, App.getToken())
                .build()

        return chain.proceed(new)
    }

}

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
