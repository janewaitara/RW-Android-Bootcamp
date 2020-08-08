package com.raywenderlich.android.foodmart.ui.categories

import android.support.v4.view.ViewPager
import android.view.View

class ZoomOutPageTransformer: ViewPager.PageTransformer {

    companion object{
        private const val MIN_SCALE = 0.85f
        private const val MIN_ALPHA = 0.5f
    }
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val pageHeight = page.height

        when{
            position < -1 -> //(-infinity, -1)
                //this page is way off-screen to the left
                page.alpha = 0f

            position <= 1 -> { //(-1, 1)
                //Modify the default slide transition to shrink the page as well
                val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                val verticalMargin = pageHeight*(1 - scaleFactor) / 2
                val horizontalMargin = pageWidth*(1 - scaleFactor) / 2

                if (position < 0){
                    page.translationX = horizontalMargin - verticalMargin / 2
                }else{
                    page.translationX = horizontalMargin + verticalMargin / 2
                }

                //scale the page down( between MIN_SCALE and 1)
                page.scaleX = scaleFactor
                page.scaleY = scaleFactor

                //Fade the page relative to its size
                page.alpha = MIN_ALPHA + (scaleFactor - MIN_SCALE ) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
            }
            else -> // (1, +infinity)
                //This page is way off-screen to the right
            page.alpha = 0f
        }
    }

}