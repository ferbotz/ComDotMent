package com.ferbotz.comments.utils

import android.os.SystemClock
import android.view.View

class DoubleClickListener(private val onDoubleClick: () -> Unit) : View.OnClickListener {
    private val DOUBLE_CLICK_TIME_DELTA: Long = 300 // Adjust this as needed

    private var lastClickTime: Long = 0

    override fun onClick(v: View) {
        val clickTime = SystemClock.elapsedRealtime()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            // Double-click detected, call the onDoubleClick function
            onDoubleClick.invoke()
        }
        lastClickTime = clickTime
    }
}