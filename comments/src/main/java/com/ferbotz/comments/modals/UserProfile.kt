package com.ferbotz.comments.modals

data class UserProfile(
    val userId: String,
    val userName: String,
    val profilePictureUrl: String? = null,
)
