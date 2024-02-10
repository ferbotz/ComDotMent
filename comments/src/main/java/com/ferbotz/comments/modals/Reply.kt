package com.ferbotz.comments.modals


sealed class ReplyAdapterViewType(){
    data class ReplyView(val reply: Reply): ReplyAdapterViewType()
    data class LoadMoreView(val repliesBetweenCount: Int, val repliesBetweenStartPosition: Int, val repliesBetweenEndPosition: Int): ReplyAdapterViewType()
}


sealed class Reply(open val commentId: String, open val replyId: String, open val postId: String, open val user: UserProfile, open var likes: Int, open var isCurrentUserLiked: Boolean, open var replyPosition: Int?){

    data class TextReply(
        override val commentId: String,
        override val replyId: String,
        override val postId: String,
        override val user: UserProfile,
        val replyText: String,
        override var likes: Int = 0,
        override var isCurrentUserLiked: Boolean = false,
        override var replyPosition: Int? = null,
        val extraParams: Any? = null
    ):Reply(commentId, replyId, postId, user, likes, isCurrentUserLiked, replyPosition)

    data class GifReply(
        override val commentId: String,
        override val replyId: String,
        override val postId: String,
        override val user: UserProfile,
        val replyGifUrl: String,
        override var likes: Int = 0,
        override var isCurrentUserLiked: Boolean = false,
        override var replyPosition: Int? = null,
        val extraParams: Any? = null
    ):Reply(commentId, replyId, postId, user, likes, isCurrentUserLiked, replyPosition)

}
