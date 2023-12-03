package com.ferbotz.comments.modals

sealed class Reply(open val commentId: String, open val replyId: String, open val postId: String, open val user: UserProfile, open var likes: Int, open var isCurrentUserLiked: Boolean){

    data class TextReply(
        override val commentId: String,
        override val replyId: String,
        override val postId: String,
        override val user: UserProfile,
        val replyText: String,
        override var likes: Int = 0,
        override var isCurrentUserLiked: Boolean = false,
        val extraParams: Any? = null
    ):Reply(commentId, replyId, postId, user, likes, isCurrentUserLiked)

    data class GifReply(
        override val commentId: String,
        override val replyId: String,
        override val postId: String,
        override val user: UserProfile,
        val replyGifUrl: String,
        override var likes: Int = 0,
        override var isCurrentUserLiked: Boolean = false,
        val extraParams: Any? = null
    ):Reply(commentId, replyId, postId, user, likes, isCurrentUserLiked)

}
