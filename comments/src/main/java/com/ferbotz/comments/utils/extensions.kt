package com.ferbotz.comments.utils

import android.util.Log
import android.view.View

fun String.testingLog(){
    Log.v("comments logging",this)
}

fun View.setOnDoubleClickListener( onDoubleClick: () -> Unit){
    this.setOnClickListener(
        DoubleClickListener(onDoubleClick)
    )
}
