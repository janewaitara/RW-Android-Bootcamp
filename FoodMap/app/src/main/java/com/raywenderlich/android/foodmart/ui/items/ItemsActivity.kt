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

package com.raywenderlich.android.foodmart.ui.items

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Property
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.raywenderlich.android.foodmart.R
import com.raywenderlich.android.foodmart.model.Food
import com.raywenderlich.android.foodmart.model.events.CartEvent
import com.raywenderlich.android.foodmart.ui.Injection
import com.raywenderlich.android.foodmart.ui.cart.CartActivity
import com.raywenderlich.android.foodmart.ui.categories.CategoriesActivity
import com.raywenderlich.android.foodmart.ui.detail.FoodActivity
import kotlinx.android.synthetic.main.activity_items.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ItemsActivity : AppCompatActivity(), ItemsContract.View, ItemsAdapter.ItemsAdapterListener {

  override lateinit var presenter: ItemsContract.Presenter
  private val adapter = ItemsAdapter(mutableListOf(), this)

  private var itemCount: TextView? = null
  private var itemCountCircle: FrameLayout? = null

  companion object{
    const val DURATION = 500L //will be shared by animators in animator set
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_items)

    presenter = Injection.provideItemsPresenter(this)

    setupRecyclerView()
    setupCartIcon()
  }

  private fun setupCartIcon() {
    // Add an OnGlobalLayoutListener to allow creating cart icon correctly in lifecycle
    itemsRootView.viewTreeObserver.addOnGlobalLayoutListener {
      itemCount = findViewById(R.id.itemCount)
      itemCountCircle = findViewById(R.id.itemCountCircle)
      updateCartIcon()
    }
  }

  private fun setupRecyclerView() {
    itemsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    itemsRecyclerView.adapter = adapter
  }

  override fun onStart() {
    super.onStart()
    EventBus.getDefault().register(this)
  }

  override fun onStop() {
    super.onStop()
    EventBus.getDefault().unregister(this)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    super.onCreateOptionsMenu(menu)
    menuInflater.inflate(R.menu.activity_items, menu)
    val item = menu.findItem(R.id.cart_menu_item)
    item.actionView.setOnClickListener { menu.performIdentifierAction(item.itemId, 0) }
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.cart_menu_item -> startActivity(CartActivity.newIntent(this))
      R.id.add_all_menu_item -> presenter.addAllToCart()
      R.id.remove_all_menu_item -> presenter.clearCart()
      R.id.categories_menu_item -> showCategories()
    }
    return super.onOptionsItemSelected(item)
  }

  private fun showCategories() {
    startActivity(CategoriesActivity.newIntent(this))
  }

  private fun updateCartIcon() {
    val cartSize = presenter.cartSize()
    itemCount?.text = "$cartSize"
    itemCountCircle?.visibility = if (cartSize > 0) View.VISIBLE else View.INVISIBLE
  }

  override fun onResume() {
    super.onResume()
    presenter.start()
  }

  override fun showItems(items: List<Food>) {
    adapter.updateItems(items)
  }

  override fun removeItem(item: Food, cartButton: ImageView) {
    animateCartButton(cartButton, false)

    cartIconAnimatorSet().apply {
      addListener(object: AnimatorListenerAdapter(){
        override fun onAnimationEnd(animation: Animator?) {
          presenter.removeItem(item)
        }
      })
      start()
    }
  }

  override fun addItem(item: Food, foodImageView: ImageView, cartButton: ImageView) {
    //getting position of the food image and the cart count circle
    val foodImagePosition = getPositionOf(foodImageView)
    val itemCountCirclePosition = getPositionOf(itemCountCircle)

    //getting size of the food image
    val foodImageSize = resources.getDimension(R.dimen.list_item_height).toInt()

    //getting the view to animate
    val viewToAnimate = setupViewToAnimate(item,foodImageView,foodImageSize)

    //adding the viewToAnimate to the root view
    itemsRootView.addView(viewToAnimate)

    val xAnimator = objectAnimatorOfFloat(viewToAnimate, "x", foodImagePosition[0].toFloat(),
      itemCountCirclePosition[0].toFloat() - foodImageSize/2)
    val yAnimator = objectAnimatorOfFloat(viewToAnimate, "y", foodImagePosition[1].toFloat(),
      itemCountCirclePosition[1].toFloat() - foodImageSize)
    val alphaAnimator = objectAnimatorOfFloat(viewToAnimate, "alpha", 0f,1f)

    /**
     * Animator Set*/
    AnimatorSet().apply {
      play(xAnimator).with(yAnimator).with(alphaAnimator)

      cartButton.isEnabled = false
      /**
       * Animator Listener before starting the animator set*/
      addListener(object : AnimatorListenerAdapter(){

        override fun onAnimationEnd(p0: Animator?) {
          cartIconAnimatorSet().start()
          presenter.addItem(item)
          itemsRootView.removeView(viewToAnimate)
          cartButton.isEnabled = true
        }
      })
      start()
    }
    animateCartButton(cartButton, true)
  }
  /**
   * Animated Vector Drawable*/
  private fun animateCartButton(cartButton: ImageView, morphToDone: Boolean){
    cartButton.setImageResource(if (morphToDone) R.drawable.ic_morph else R.drawable.ic_morph_reverse)
    val animatable = cartButton.drawable as Animatable
    animatable.start()
  }

  /**
   * Activity transition(Shared Element Transition)*/
  override fun showFoodDetail(view: View, food: Food) {
    //create a pair for the image to be transitioned using the transition name attribute set
    val foodImage = view.findViewById<ImageView>(R.id.foodImage)
    val imagePair = Pair.create(foodImage as View, "tImage")

    //create a pair for the name to be transitioned using the transition name attribute set
    val foodName = view.findViewById<TextView>(R.id.name)
    val namePair = Pair.create(foodName as View, "tName")


    /**
     * To execute activity transition,
     * create the animation using makeSceneTransitionAnimation from ActivityOptionsCompat
     * then call start Activity from ActivityCompat*/
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imagePair,namePair )
    ActivityCompat.startActivity(this,FoodActivity.newIntent(this, food.id) ,options.toBundle())

  }

  override fun removeFavorite(item: Food) {
    presenter.removeFavorite(item)
  }

  override fun addFavorite(item: Food) {
    presenter.addFavorite(item)
  }

  //get position of helper
  private fun getPositionOf(view: View?): IntArray{
    val position = intArrayOf(0, 0)
    view?.getLocationOnScreen(position)
    return position
  }

  //helper method to setup extra view to animate
  private fun setupViewToAnimate(item: Food, imageView: ImageView, imageSize: Int): ImageView{

    val viewToAnimate = ImageView(this)
    viewToAnimate.setImageResource(resources.getIdentifier(item.thumbnail, null, packageName))

    val layoutParams = imageView.layoutParams
    layoutParams.height = imageSize
    layoutParams.width = imageSize

    viewToAnimate.layoutParams = layoutParams
    viewToAnimate.alpha = 0f

    return viewToAnimate
  }

  //helper method to return ObjectAnimator
  private fun objectAnimatorOfFloat(view: View, propertyName: String, startValue: Float, endValue: Float): ObjectAnimator{
    val animator = ObjectAnimator.ofFloat(view, propertyName, startValue, endValue)
    animator.interpolator = AccelerateDecelerateInterpolator()
    animator.duration = DURATION
    return animator
  }
  //helper method to return ObjectAnimator
  private fun objectAnimatorOfFloatForCountCircle(view: FrameLayout?, propertyName: Property<View, Float>, startValue: Float, endValue: Float): ObjectAnimator{
    val animator = ObjectAnimator.ofFloat(view, propertyName, startValue, endValue)
    //animator.interpolator = BounceInterpolator()
    animator.duration = 200L
    animator.repeatCount = 1
    animator.repeatMode = ObjectAnimator.REVERSE
    return animator
  }

  /**Animator Set*/
  private fun cartIconAnimatorSet(): AnimatorSet{

    val iconScaleAnimatorX = objectAnimatorOfFloatForCountCircle(itemCountCircle, View.SCALE_X,
            1f,1.5f)
    val iconScaleAnimatorY = objectAnimatorOfFloatForCountCircle(itemCountCircle, View.SCALE_Y,
            1f,1.5f)

    val iconAnimatorSet = AnimatorSet()
    iconAnimatorSet. play(iconScaleAnimatorX).with(iconScaleAnimatorY)
    return  iconAnimatorSet
  }


  @Suppress("UNUSED_PARAMETER")
  @Subscribe(threadMode = ThreadMode.MAIN)
  fun onCartEvent(event: CartEvent) {
    updateCartIcon()
    adapter.notifyDataSetChanged()
  }
}


