package com.raywenderlich.android.foodmart.ui.categories

import android.support.v4.view.ViewPager
import android.view.View

class DepthPageTransformer: ViewPager.PageTransformer {

    companion object{
        private const val MIN_SCALE = 0.75f
    }
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width

        when{
            position < -1 -> //(-infinity, -1)
                //this page is way off-screen to the left
                page.alpha = 0f

            position <= 0 -> { //(-1, 0)
                //use the default slide transition when moving to the left
                page.alpha = 1f
                page.translationX = 0f
                page.scaleY = 1f
                page.scaleX = 1f
            }

            position <= 1 -> { //(0, 1)
                //Fade the page out
                page.alpha = 1f - position

                //Counteract the default slide transition
                page.translationX = pageWidth * -position

                //scale the page down( between MIN_SCALE and 1)
                val scaleFactor = MIN_SCALE * (1 - MIN_SCALE) * ( 1 - Math.abs(position))
                page.scaleX = scaleFactor
                page.scaleY = scaleFactor

            }
            else -> // (1, +infinity)
                //This page is way off-screen to the right
                page.alpha = 0f
        }
    }
}