package com.ferbotz.comments.modals

import java.util.Collections.emptyList

sealed class CommentsAdapterViewHolderDataTypes{
    data class EmptyView(val emptyViewData: Any? = null):CommentsAdapterViewHolderDataTypes()
    data class CommentView(val commentData: Comment):CommentsAdapterViewHolderDataTypes()
}

sealed class Comment(open val commentId: String, open val postId: String, open val user: UserProfile, open val replies: List<Reply>, open val likes: Int, open val timestamp: String, open var totalRepliesCount: Int, open val isCurrentUserLiked: Boolean, open val isPinned: Boolean, open val isUploading: Boolean){

    data class TextComment(
        override val commentId: String,
        override val postId: String,
        override val user: UserProfile,
        val commentText: String,
        override val likes: Int = 0,
        override var totalRepliesCount: Int = 0,
        override val replies: List<Reply> = emptyList(),
        override val timestamp: String,
        override val isCurrentUserLiked: Boolean = false,
        override val isPinned: Boolean = false,
        override val isUploading: Boolean = false,
        val extraParams: Any? = null
    ):Comment(commentId, postId, user, replies, likes, timestamp, totalRepliesCount, isCurrentUserLiked, isPinned, isUploading)


    data class GifComment(
        override val commentId: String,
        override val postId: String,
        override val user: UserProfile,
        val gifUrl: String,
        override val likes: Int = 0,
        override var totalRepliesCount: Int = 0,
        override val replies: List<Reply> = emptyList(),
        override val timestamp: String,
        override val isCurrentUserLiked: Boolean = false,
        override val isPinned: Boolean = false,
        override val isUploading: Boolean = false,
        val extraParams: Any? = null
    ):Comment(commentId, postId, user, replies, likes, timestamp, totalRepliesCount, isCurrentUserLiked, isPinned, isUploading)

}

