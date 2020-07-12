package com.raywenderlich.android.taskie.networking

import com.raywenderlich.android.taskie.model.Task
import com.raywenderlich.android.taskie.model.request.AddTaskRequest
import com.raywenderlich.android.taskie.model.request.UserDataRequest
import com.raywenderlich.android.taskie.model.response.*
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
    fun registerUser(@Body request: UserDataRequest): Call<RegisterResponse>

    @GET("/api/note")
    fun getNotes(): Call<GetTasksResponse>

    @POST("/api/login")
    fun loginUser(@Body request: UserDataRequest): Call<LoginResponse>

    @GET("/api/user/profile")
    fun getMyProfile(): Call<UserProfileResponse>

    @POST("/api/note/complete")
    fun completeTask(@Query("id") noteId: String) : Call<CompleteNoteResponse>

    @POST("/api/note")
    fun addTask(@Body request: AddTaskRequest):  Call<Task>

    @DELETE("/api/note")
    suspend fun deleteTask(@Query("id") noteId: String) : DeleteNoteResponse

}

