package com.raywenderlich.android.taskie.networking

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RemoteApiService {

    /**
     * @POST tells retrofit it will be a post request to that relative endpoint path
     * request Body @Body and tells retrofit to put it as the body parameter in the request
     * RequestBody is a specific type in okHttpclient which describes data you can send into the server
     * ResponseBody is a counterpart that describes data you received
     * Call is a Retrofit object which describes a prepared call
     * */

    @POST("/api/register")
    fun registerUser(@Body request: RequestBody): Call<ResponseBody>

    @GET("/api/note")
    fun getNotes(@Header("Authorization") token: String): Call<ResponseBody>

    @POST("/api/login")
    fun loginUser(@Body request: RequestBody): Call<ResponseBody>

    @GET("/api/user/profile")
    fun getMyProfile(@Header("Authorization") token: String): Call<ResponseBody>

    @POST("/api/note/complete")
    fun completeTask(@Header("Authorization") token: String,
                     @Query("id")noteId: String) : Call<ResponseBody>

    @POST("/api/note")
    fun addTask(@Header("Authorization") token: String,
                @Body request: RequestBody):  Call<ResponseBody>
}

