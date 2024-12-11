package com.cevdetkilickeser.learnconnect

import android.app.Activity
import android.content.Context


fun setScreenOrientation(context: Context, orientation: Int) {
    val activity = context as? Activity
    activity?.requestedOrientation = orientation
}