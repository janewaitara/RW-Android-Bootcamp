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

package com.raywenderlich.android.memories.ui.main

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.raywenderlich.android.memories.R
import com.raywenderlich.android.memories.service.ACTION_IMAGES_SYNCHRONIZED
import com.raywenderlich.android.memories.service.DownloadService
import com.raywenderlich.android.memories.service.SynchronizeImagesReceiver
import com.raywenderlich.android.memories.utils.toast
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Main Screen, represents different fragments for each section of the app.
 */
class MainActivity : AppCompatActivity() {

  private val pagerAdapter by lazy { MainPagerAdapter(supportFragmentManager) }

  private val receiver by lazy {
    SynchronizeImagesReceiver{
      toast("Images synchronized")
    }
  }

  companion object {
    fun getIntent(context: Context): Intent {
      val intent = Intent(context, MainActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      return intent
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.AppTheme)

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    initUi()
  }

  override fun onStart() {
    super.onStart()
    //register a receiver with an intent filter that listens to the action you defined
    registerReceiver(receiver, IntentFilter().apply {
      addAction(ACTION_IMAGES_SYNCHRONIZED)
    })
  }

  private fun initUi() {
    tabs.setupWithViewPager(fragmentPager)
    fragmentPager.adapter = pagerAdapter
  }

  override fun onStop() {
    val intent = Intent(this, DownloadService::class.java)
    stopService(intent) //frees up the resources taken by the services
    unregisterReceiver(receiver)//unregister the receiver once the activity stops
    super.onStop()
  }
}
