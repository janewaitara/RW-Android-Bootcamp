/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.taskie.networking

import android.util.Log
import com.google.gson.Gson
import com.raywenderlich.android.taskie.App
import com.raywenderlich.android.taskie.model.Task
import com.raywenderlich.android.taskie.model.UserProfile
import com.raywenderlich.android.taskie.model.request.AddTaskRequest
import com.raywenderlich.android.taskie.model.request.UserDataRequest
import com.raywenderlich.android.taskie.model.response.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.NullPointerException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.Buffer

/**
 * Holds decoupled logic for all the API calls.
 */

const val BASE_URL = "https://taskie-rw.herokuapp.com"

//remote api service property which is a middleman between UI and actual API service
class RemoteApi(private val apiService: RemoteApiService) {

  private val gson = Gson()

  fun loginUser(userDataRequest: UserDataRequest, onUserLoggedIn: (String?, Throwable?) -> Unit) {

    apiService.loginUser(userDataRequest).enqueue(object: Callback<LoginResponse>{
      override fun onFailure(call: Call<LoginResponse>, error: Throwable) {
        onUserLoggedIn(null, error)
      }

      override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

        val loginResponse = response.body()
        if (loginResponse == null ||
                loginResponse.token.isNullOrBlank()){
          onUserLoggedIn(null, NullPointerException("No response body"))
        }else{
          onUserLoggedIn(loginResponse.token, null)
        }
      }

    })
  }

  fun registerUser(userDataRequest: UserDataRequest, onUserCreated: (String?, Throwable?) -> Unit) {
   /* Thread(Runnable {
      //to achieve a http url connection
      // /api/register is the end point path(unique combination of a rest method and a url path which holds unique functionality)
      val connection = URL("$BASE_URL/api/register").openConnection() as HttpURLConnection //open a connection
      connection.requestMethod = "POST"
      connection.setRequestProperty("Content-Type","application/json") //to use json format for
      connection.setRequestProperty("Accept","application/json")

      //describing time outs inCase server doesn't connect properly
      connection.readTimeout = 10000
      connection.connectTimeout = 10000
      //lets the connection perform data output or input
      connection.doOutput = true
      connection.doInput = true

      //to format registration data into json

      val body  = gson.toJson(userDataRequest)

      val bytes = body.toByteArray() //convert since json will be sent as a series of bytes

      //open an outPutStream to send the data and write the bytes to the API end point
      try {
        //sending the register data
       connection.outputStream.use { outputStream->  //we are using "use" to automatically close the stream once the operation is done
         outputStream.write(bytes)
       }

        //reading response
        val reader = InputStreamReader(connection.inputStream) //creating an input stream reader to read the response from connection input stream
        reader.use { input->
          val response = StringBuilder()
          val bufferedReader = BufferedReader(input) //reading the input one chunk at a time

          bufferedReader.useLines { lines->
            lines.forEach {
              response.append(it.trim())
            }
         }
          //parsing the register response
          val jsonObject = JSONObject(response.toString())

          onUserCreated(jsonObject.getString("message"), null)

        }

      }catch (error: Throwable) {
        onUserCreated(null, error)
      }
      connection.disconnect()

    }).start()*/

    //enqueue(unblocking - async) api call in the background
    apiService.registerUser(userDataRequest).enqueue(object : Callback<RegisterResponse>{
      /**
       * called when the request fails eg when:
       *  reaching an endpoint that doest exist
       *  lacking an Internet connection
       *  Timeout
       * */

      override fun onFailure(call: Call<RegisterResponse>, error: Throwable) {
        onUserCreated(null, error)
      }

      /**
       * when you receive a response from the server
       *  can be successful or an error
       * Response types:
       *  Positive with a nun null body- body()
       *  Negative with a nun null error body - errorBody()
       * */
      override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
       val message = response.body()?.message
        if (message == null){
          onUserCreated(null, NullPointerException("No response body"))
          return
        }

        onUserCreated(message,null)
      }

    })

  }

  fun getTasks(onTasksReceived: (List<Task>, Throwable?) -> Unit) {

    apiService.getNotes(App.getToken()).enqueue(object : Callback<GetTasksResponse>{
      override fun onFailure(call: Call<GetTasksResponse>, error: Throwable) {
        onTasksReceived(emptyList(), error)
      }

      override fun onResponse(call: Call<GetTasksResponse>, response: Response<GetTasksResponse>) {
        //if there is data, parse response using gson
        val data = response.body()

        if (data != null && data.notes.isNotEmpty()){
          onTasksReceived(data.notes.filter { !it.isCompleted }, null)

        }else{
          onTasksReceived(emptyList(), NullPointerException("No data available"))
        }
      }
    })
  }

  fun deleteTask(onTaskDeleted: (Throwable?) -> Unit) {
    onTaskDeleted(null)
  }

  fun completeTask(taskId: String, onTaskCompleted: (Throwable?) -> Unit) {
    apiService.completeTask(App.getToken(),taskId).enqueue(object: Callback<CompleteNoteResponse>{
      override fun onFailure(call: Call<CompleteNoteResponse>, error: Throwable) {
        onTaskCompleted(error)
      }

      override fun onResponse(call: Call<CompleteNoteResponse>, response: Response<CompleteNoteResponse>) {
        val completeNoteResponse = response.body()

        if (completeNoteResponse?.message == null){
          onTaskCompleted(NullPointerException("No response"))
        }else{
          onTaskCompleted(null)
        }

      }
    })

  }

  fun addTask(addTaskRequest: AddTaskRequest, onTaskCreated: (Task?, Throwable?) -> Unit) {
    apiService.addTask(App.getToken(),addTaskRequest).enqueue(object : Callback<Task>{
      override fun onFailure(call: Call<Task>, error: Throwable) {
        onTaskCreated(null,error)
      }

      override fun onResponse(call: Call<Task>, response: Response<Task>) {
        val taskResponse = response.body()
        if (taskResponse == null){
          onTaskCreated(null, NullPointerException("No response"))
        }else{
          onTaskCreated(taskResponse, null)
        }

      }

    })
  }

  fun getUserProfile(onUserProfileReceived: (UserProfile?, Throwable?) -> Unit) {

    //requesting the notes since the user profile contains the amount of notes I have
    getTasks { tasks, error ->
      /*if its an NPE, request the profile and return the of notes being 0 and if its an error, no need to request the profile*/
      if (error != null && error !is NullPointerException){
        onUserProfileReceived(null, error)
        return@getTasks
      }

      apiService.getMyProfile(App.getToken()).enqueue(object: Callback<ResponseBody>{
        override fun onFailure(call: Call<ResponseBody>, error: Throwable) {
          onUserProfileReceived(null, error)
        }

        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
          val jsonResponse = response.body()?.string()
          if (jsonResponse == null){
            onUserProfileReceived(null, error)
            return
          }
          val profileResponse = gson.fromJson(jsonResponse, UserProfileResponse::class.java )

          if (profileResponse.email == null || profileResponse.name == null ){
            onUserProfileReceived(null, error)
          }else{
            onUserProfileReceived(UserProfile(
                    profileResponse.email,
                    profileResponse.name,
                    tasks.size
            ), null )
          }
        }
      })

    }
  }
}