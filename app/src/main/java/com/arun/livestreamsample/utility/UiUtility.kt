package com.arun.livestreamsample.utility

import android.content.Context

object UiUtility {
    fun calculateNoOfColumns(context: Context?, columnWidthDp: Float): Int {
        var noOfColumns = 1
        context?.let {
            val displayMetrics = context.resources.displayMetrics
            val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
            noOfColumns = (screenWidthDp / columnWidthDp + 0.5).toInt()
        }
       return noOfColumns
    }
}