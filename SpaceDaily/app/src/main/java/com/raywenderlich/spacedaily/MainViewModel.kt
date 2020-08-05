/*
 * Copyright (c) 2019 Razeware LLC
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
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.spacedaily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raywenderlich.spacedaily.network.NASAAPIInterface
import com.raywenderlich.spacedaily.network.PhotoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

interface MainView {
  fun setDailyPhoto(dailyPhoto: PhotoResponse)
}

/**
 *Implement KoinComponent interface as a way to tell koin that it can inject fields into this class
 * (MainViewModel which is not a lifeCycle Class)*/

class MainViewModel : ViewModel(), KoinComponent {
  val nasaApiInterface: NASAAPIInterface by inject()
  var view: MainView? = null

  fun getDailyPhoto() {
    viewModelScope.launch(Dispatchers.IO) {
      val dailyPhoto = nasaApiInterface.getDailyPhoto()
      withContext(Dispatchers.Main) {
        //tell view to show photo
        view?.setDailyPhoto(dailyPhoto)
      }
    }
  }
}