package com.ferbotz.comments.modals

sealed class Reply(open val commentId: String, open val replyId: String, open val postId: String, open val user: UserProfile, open val likes: Int, open val isCurrentUserLiked: Boolean){

    data class TextReply(
        override val commentId: String,
        override val replyId: String,
        override val postId: String,
        override val user: UserProfile,
        val replyText: String,
        override val likes: Int = 0,
        override val isCurrentUserLiked: Boolean = false,
        val extraParams: Any? = null
    ):Reply(commentId, replyId, postId, user, likes, isCurrentUserLiked)

    data class GifReply(
        override val commentId: String,
        override val replyId: String,
        override val postId: String,
        override val user: UserProfile,
        val replyGifUrl: String,
        override val likes: Int = 0,
        override val isCurrentUserLiked: Boolean = false,
        val extraParams: Any? = null
    ):Reply(commentId, replyId, postId, user, likes, isCurrentUserLiked)

}
