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

import com.google.gson.Gson
import com.raywenderlich.android.taskie.App
import com.raywenderlich.android.taskie.model.Task
import com.raywenderlich.android.taskie.model.UserProfile
import com.raywenderlich.android.taskie.model.request.AddTaskRequest
import com.raywenderlich.android.taskie.model.request.UserDataRequest
import com.raywenderlich.android.taskie.model.response.GetTasksResponse
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.Buffer

/**
 * Holds decoupled logic for all the API calls.
 */

const val BASE_URL = "https://taskie-rw.herokuapp.com"

class RemoteApi {

  private val gson = Gson()

  fun loginUser(userDataRequest: UserDataRequest, onUserLoggedIn: (String?, Throwable?) -> Unit) {

    Thread(Runnable {
      val connection = URL("$BASE_URL/api/login").openConnection() as HttpURLConnection
      connection.requestMethod = "POST"
      connection.setRequestProperty("Content-Type","application/json") //to use json format for
      connection.setRequestProperty("Accept","application/json")
      connection.readTimeout = 10000
      connection.connectTimeout = 10000
      connection.doOutput = true
      connection.doInput = true

      val body  = gson.toJson(userDataRequest)

      val bytes = body.toByteArray()

      try {
        connection.outputStream.use { outPutStream->
          outPutStream.write(bytes)
        }

        val reader = InputStreamReader(connection.inputStream)
        reader.use { input->

          val response = StringBuilder()
          val bufferedReader = BufferedReader(input)

          bufferedReader.useLines { lines->
            lines.forEach {
              response.append(it.trim())
            }
          }
          //parsing the json response by accessing the token property within and use jsonObject to parse the token
          val jsonObject = JSONObject(response.toString())
          val token = jsonObject.getString("token") //selecting the token field from the jsonObject to send to the callback

          onUserLoggedIn(token, null)

        }

      }catch (error: Throwable){
        onUserLoggedIn(null, error)
      }

      connection.disconnect()
    }).start()

  }

  fun registerUser(userDataRequest: UserDataRequest, onUserCreated: (String?, Throwable?) -> Unit) {
    Thread(Runnable {
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

    }).start()
  }

  fun getTasks(onTasksReceived: (List<Task>, Throwable?) -> Unit) {

    Thread(Runnable {
      val connection = URL("$BASE_URL/api/note").openConnection() as HttpURLConnection
      connection.requestMethod = "GET"
      connection.setRequestProperty("Content-Type","application/json") //to use json format for
      connection.setRequestProperty("Accept","application/json")
      connection.setRequestProperty("Authorization", App.getToken())
      connection.connectTimeout = 10000
      connection.readTimeout = 10000
      connection.doInput = true

      try {
        val reader = InputStreamReader(connection.inputStream)

        reader.use { input->

          val response = StringBuilder()
          val bufferedReader = BufferedReader(input)

          bufferedReader.useLines { lines->
            lines.forEach {
              response.append(it.trim())
            }
          }

          val tasksResponse = gson.fromJson(response.toString(), GetTasksResponse::class.java) //parse the response as a string into GetTasksResponse
          onTasksReceived(tasksResponse.notes ?: listOf(), null)
        }
      }catch (error: Throwable){
        onTasksReceived(emptyList(), error)
      }

      connection.disconnect()

    }).start()

  }

  fun deleteTask(onTaskDeleted: (Throwable?) -> Unit) {
    onTaskDeleted(null)
  }

  fun completeTask(onTaskCompleted: (Throwable?) -> Unit) {
    onTaskCompleted(null)
  }

  fun addTask(addTaskRequest: AddTaskRequest, onTaskCreated: (Task?, Throwable?) -> Unit) {

    Thread(Runnable {
      val connection = URL("$BASE_URL/api/note").openConnection() as HttpURLConnection
      connection.requestMethod = "POST"
      connection.setRequestProperty("Content-Type","application/json") //to use json format for
      connection.setRequestProperty("Accept","application/json")
      connection.setRequestProperty("Authorization", App.getToken())
      connection.connectTimeout = 10000
      connection.readTimeout = 10000
      connection.doOutput = true
      connection.doInput = true

   /*   val request = JSONObject()
      request.put("title", addTaskRequest.title)
      request.put("content", addTaskRequest.content)
      request.put("taskPriority", addTaskRequest.taskPriority)

      val body = request.toString()*/
      val body  = gson.toJson(addTaskRequest)

      val bytes = body.toByteArray()

      try {
        //sending the register data
        connection.outputStream.use {outputStream ->
          outputStream.write(bytes)
        }

        val reader = InputStreamReader(connection.inputStream)
        reader.use { input->
          val response = StringBuilder()
          val bufferedReader = BufferedReader(input)

          bufferedReader.useLines { lines->
            lines.forEach {
              response.append(it.trim())
            }
          }

          val jsonObject = JSONObject(response.toString())
          val task = Task(
               jsonObject.getString("id"),
                  jsonObject.getString("title"),
                  jsonObject.getString("content"),
                  jsonObject.getBoolean("isCompleted"),
                  jsonObject.getInt("taskPriority")
          )
          onTaskCreated(task,null)
        }
      }catch (error: Throwable){
        onTaskCreated(null, error)
      }

      connection.disconnect()

    }).start()
  }

  fun getUserProfile(onUserProfileReceived: (UserProfile?, Throwable?) -> Unit) {
    onUserProfileReceived(UserProfile("mail@mail.com", "Filip", 10), null)
  }
}