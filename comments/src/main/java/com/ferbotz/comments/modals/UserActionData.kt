package com.ferbotz.comments.modals

sealed class UserActionData{

    data class ReplyActionData(val commentId: String, val replyToUserName: String): UserActionData()

    data class LikeCommentActionData(val isLiked: Boolean, val comment: Comment): UserActionData()

    data class LikeReplyActionData(val isLiked: Boolean, val reply: Reply): UserActionData()

    object LoadNextPageComments:UserActionData()

}
