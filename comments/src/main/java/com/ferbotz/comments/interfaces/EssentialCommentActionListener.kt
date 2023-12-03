package com.ferbotz.comments.interfaces

import com.ferbotz.comments.modals.Comment
import com.ferbotz.comments.modals.CommentsDataAccessFunctions
import com.ferbotz.comments.modals.Reply

interface EssentialCommentActionListener {

    suspend fun addNewComment(newComment: Comment, commentsDataAccessFunctions: CommentsDataAccessFunctions): Comment{
        return newComment
    }

    suspend fun addNewReply(newReply: Reply, commentsDataAccessFunctions: CommentsDataAccessFunctions): Reply{
        return newReply
    }

    suspend fun onLikeComment(isLiked: Boolean, likedComment: Comment, commentsDataAccessFunctions: CommentsDataAccessFunctions): Comment{
        return likedComment
    }

    suspend fun onLikeReply(isLiked: Boolean, likedReply: Reply, parentComment: Comment, commentsDataAccessFunctions: CommentsDataAccessFunctions): Reply{
        return likedReply
    }

}