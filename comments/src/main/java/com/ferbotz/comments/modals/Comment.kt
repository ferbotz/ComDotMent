package com.ferbotz.comments.modals

import java.util.Collections.emptyList

sealed class CommentsAdapterViewHolderDataTypes{
    data class EmptyView(val emptyViewData: Any? = null):CommentsAdapterViewHolderDataTypes()
    data class CommentView(val commentData: Comment):CommentsAdapterViewHolderDataTypes()
    object LoadingFooter: CommentsAdapterViewHolderDataTypes()
}

sealed class Comment(open val commentId: String, open val postId: String, open val user: UserProfile, open var replies: MutableList<Reply>, open var likes: Int, open val timestamp: String, open var totalRepliesCount: Int, open var isCurrentUserLiked: Boolean, open val isPinned: Boolean, open val isUploading: Boolean){

//    abstract fun <T> dataCopy(comment:T): T
    data class TextComment(
        override val commentId: String,
        override val postId: String,
        override val user: UserProfile,
        val commentText: String,
        override var likes: Int = 0,
        override var totalRepliesCount: Int = 0,
        override var replies: MutableList<Reply> = emptyList(),
        override val timestamp: String,
        override var isCurrentUserLiked: Boolean = false,
        override val isPinned: Boolean = false,
        override val isUploading: Boolean = false,
        val extraParams: Any? = null
    ):Comment(commentId, postId, user, replies, likes, timestamp, totalRepliesCount, isCurrentUserLiked, isPinned, isUploading)

    data class GifComment(
        override val commentId: String,
        override val postId: String,
        override val user: UserProfile,
        val gifUrl: String,
        override var likes: Int = 0,
        override var totalRepliesCount: Int = 0,
        override var replies: MutableList<Reply> = emptyList(),
        override val timestamp: String,
        override var isCurrentUserLiked: Boolean = false,
        override val isPinned: Boolean = false,
        override val isUploading: Boolean = false,
        val extraParams: Any? = null
    ):Comment(commentId, postId, user, replies, likes, timestamp, totalRepliesCount, isCurrentUserLiked, isPinned, isUploading)

}

