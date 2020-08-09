/*
 * Copyright (c) 2018 Razeware LLC
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
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.raywenderlich.android.foodmart.ui.checkout

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.animation.DynamicAnimation
import android.support.animation.FlingAnimation
import android.support.animation.SpringAnimation
import android.support.animation.SpringForce
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import com.raywenderlich.android.foodmart.R
import com.raywenderlich.android.foodmart.app.toast
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : AppCompatActivity() {

  /**
   * Physics Based animation:
   *    Spring Animation*/
  //property to measure the x and y displacement
  private var xPositionDiff = 0f
  private var yPositionDiff = 0f

  //SpringForce takes a final position in its constructor
  private  val springForce by lazy{
    SpringForce(0f).apply{
      stiffness = SpringForce.STIFFNESS_LOW
      dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
    }
  }
  //Spring Animation constructor takes an object to act on and an associated property
  // which in this case is translationX for horizontal displacement
  private val springAnimationX: SpringAnimation by lazy {
    SpringAnimation(donut, DynamicAnimation.TRANSLATION_X).setSpring(springForce)
  }

  private val springAnimationY : SpringAnimation by lazy {
    SpringAnimation(donut, DynamicAnimation.TRANSLATION_Y).setSpring(springForce)
  }

  //respond to a user's touch to start the animation by setting up a touch listener on the donut
  private fun setupTouchListener(){

    donut.setOnTouchListener { view, motionEvent ->

      when(motionEvent?.action){

        MotionEvent.ACTION_DOWN -> {

          xPositionDiff = motionEvent.rawX - view.x
          yPositionDiff = motionEvent.rawY - view.y
          springAnimationX.cancel()
          springAnimationY.cancel()
        }

        MotionEvent.ACTION_MOVE-> {
          donut.x = motionEvent.rawX - xPositionDiff
          donut.y = motionEvent.rawY - yPositionDiff
        }

        MotionEvent.ACTION_UP  -> {
          springAnimationX.start()
          springAnimationY.start()
        }
      }
      true
    }
  }

  /**
   * Physics Based animation:
   *    Fling Animation*/
  private var donutFlingCount = 0f
  private var cookieFlingCount = 0f

  //gestureListener for the donut
  private val donutGestureListener = object: GestureDetector.SimpleOnGestureListener(){
    override fun onDown(e: MotionEvent?) = true

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
      //limiting the user to one fling
      if (donutFlingCount < 1){
        donutFlingAnimationX.setStartVelocity(velocityX)
        donutFlingAnimationY.setStartVelocity(velocityY)

        donutFlingAnimationX.start()
        donutFlingAnimationY.start()
      }
      return true
    }
  }

  //gestureListener for the cookie
  private val cookieGestureListener = object: GestureDetector.SimpleOnGestureListener(){
    override fun onDown(e: MotionEvent?) = true

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
      //limiting the user to one fling
      if (cookieFlingCount < 1){
        cookieFlingAnimationX.setStartVelocity(velocityX)
        cookieFlingAnimationY.setStartVelocity(velocityY)

        cookieFlingAnimationX.start()
        cookieFlingAnimationY.start()
      }
      return true
    }
  }

  private val donutGestureDetector: GestureDetector by lazy {
    GestureDetector(this, donutGestureListener)
  }

  private val cookieGestureDetector: GestureDetector by lazy {
    GestureDetector(this, cookieGestureListener)
  }

  private val donutFlingAnimationX: FlingAnimation by lazy {
   FlingAnimation(donut, DynamicAnimation.X).setFriction(1f)
  }
  private val donutFlingAnimationY : FlingAnimation by lazy {
    FlingAnimation(donut, DynamicAnimation.Y).setFriction(1f)
  }

  private val cookieFlingAnimationX: FlingAnimation by lazy {
    FlingAnimation(cookie, DynamicAnimation.X).setFriction(1f)
  }
  private val cookieFlingAnimationY : FlingAnimation by lazy {
    FlingAnimation(cookie, DynamicAnimation.Y).setFriction(1f)
  }

  //animating the block to move back and forth
  private fun setUpAnimatingBlock(){
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)

    val width = displayMetrics.widthPixels

    ObjectAnimator.ofFloat(block, "translationX", 0f,
            width.toFloat()-resources.getDimension(R.dimen.block_width)).apply {
      interpolator = AccelerateDecelerateInterpolator()
      duration = 1000L
      repeatCount = ObjectAnimator.INFINITE
      repeatMode = ObjectAnimator.REVERSE
      start()
    }
  }

  //limiting the motion of the donut to stay in the view by adding a globalLayoutListener to the donut viewTreeObserver
  private fun setupTreeObserver(){

    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)

    val width = displayMetrics.widthPixels
    val height = displayMetrics.heightPixels

    donut.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
      override fun onGlobalLayout() {
        donutFlingAnimationX.setMinValue(0f).setMaxValue((width - donut.width).toFloat())
        donutFlingAnimationY.setMinValue(0f).setMaxValue((height - 2 * donut.height).toFloat())

        donut.viewTreeObserver.removeOnGlobalLayoutListener(this)

      }
    })

    cookie.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
      override fun onGlobalLayout() {
        cookieFlingAnimationX.setMinValue(0f).setMaxValue((width - cookie.width).toFloat())
        cookieFlingAnimationY.setMinValue(0f).setMaxValue((height - 2 * cookie.height).toFloat())

        cookie.viewTreeObserver.removeOnGlobalLayoutListener(this)

      }
    })
  }

  //helper method to determine if two views are overlapping by checking if the corresponding HitRects intersect
  private fun isViewOverLapping(v1: View, v2: View): Boolean{
    val rect1 = Rect()
    v1.getHitRect(rect1)

    val rect2 = Rect()
    v2.getHitRect(rect2)

    return Rect.intersects(rect1,rect2)
  }

  //helper method to increment the flingCount and cast for a successful fling when the flingAnimation ends
  private fun setUpEndListener(){
    donutFlingAnimationX.addEndListener { _, _, _, _ ->
      donutFlingCount += 1
      //if the donut overlaps the block alert by a toast
      if (isViewOverLapping(donut,block)){
        toast(getString(R.string.free_donuts))
      }
    }

    cookieFlingAnimationX.addEndListener { _, _, _, _ ->
      cookieFlingCount += 1
      //if the donut overlaps the block alert by a toast
      if (isViewOverLapping(cookie,block)){
        toast(getString(R.string.free_cookies))
      }
    }
  }

  //passing touches on the donut to our gestureDetector
  private fun setupTouchListenerByFling(){
    donut.setOnTouchListener { _, motionEvent ->
      donutGestureDetector.onTouchEvent(motionEvent)
    }
    cookie.setOnTouchListener { _, motionEvent ->
      cookieGestureDetector.onTouchEvent(motionEvent)
    }
  }

  companion object {
    fun newIntent(context: Context) = Intent(context, CheckoutActivity::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_checkout)

    title = getString(R.string.checkout_title)

    setUpAnimatingBlock()
    setupTouchListenerByFling()
    setupTreeObserver()
    setUpEndListener()

  }




}
