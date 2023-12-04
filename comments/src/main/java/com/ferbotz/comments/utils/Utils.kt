package com.ferbotz.comments.utils

import kotlin.random.Random

object Utils {

    fun formatTime(milliseconds: Long): String {
        val seconds = (milliseconds / 1000).toInt()

        return when {
            seconds < 10 -> "Just now"
            seconds < 60 -> "$seconds s"
            seconds < 600 -> "${seconds / 60} m"
            seconds < 3600 -> "${(seconds / 600) * 10} m"
            seconds < 86400 -> "${seconds / 3600} h"
            else -> "${seconds / 86400} d"
        }
    }

    fun getRandomTextWithLength(length: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') // You can customize the character pool

        return (1..length)
            .map { charPool[Random.nextInt(0, charPool.size)] }
            .joinToString("")
    }

}