package com.ferbotz.comments.data

import androidx.lifecycle.viewModelScope
import com.ferbotz.comments.interfaces.EssentialCommentActionListener
import com.ferbotz.comments.modals.*
import com.ferbotz.comments.utils.ScreenUtils.logVasi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CommentsRepository(val commentsViewAttribute: CommentsViewAttribute) {

    val commentsDataList: MutableSharedFlow<List<CommentsAdapterViewHolderDataTypes>> = MutableSharedFlow()

    val commentsList: MutableList<Comment> = mutableListOf()

    private val commentsDataAccessFunctions = CommentsDataAccessFunctions(::getCurrentCommentList, ::setCurrentCommentList)

    suspend fun likeComment(comment: Comment, isLiked: Boolean){
        commentsViewAttribute.essentialActionListener.onLikeComment(isLiked, comment, commentsDataAccessFunctions)?.let { comment ->
            commentsList.indexOfFirst { it.commentId == comment.commentId }.let { index->
                commentsList[index] = comment
                mergeCommentsAndEmptyCustomViewsAndUpdate()
            }
        }
    }

    suspend fun likeReply(isLiked: Boolean, reply: Reply){
        commentsViewAttribute.essentialActionListener.onLikeReply(isLiked, reply, commentsList.find { it.commentId == reply.commentId }!!, commentsDataAccessFunctions)?.let { reply ->
            commentsList.find { it.commentId == reply.commentId }?.let { comment->
                comment.replies.indexOfFirst { it.replyId == reply.replyId }?.let { replyIndex->
                    comment.replies[replyIndex] = reply
                }
                mergeCommentsAndEmptyCustomViewsAndUpdate()
            }
        }
    }

    suspend fun addNewComment(newComment: Comment){
        commentsViewAttribute.essentialActionListener.addNewComment(newComment, commentsDataAccessFunctions).let { comment->
            commentsList.add(comment)
            "new comment added...${comment.commentId}".logVasi()
            mergeCommentsAndEmptyCustomViewsAndUpdate()
        }
    }

    private suspend fun mergeCommentsAndEmptyCustomViewsAndUpdate(){
        val commentDataList = mutableListOf<CommentsAdapterViewHolderDataTypes>()
        commentsList.forEachIndexed { index, comment ->
            commentDataList.add(CommentsAdapterViewHolderDataTypes.CommentView(comment))
            commentsViewAttribute.attributeInstructions.emptyViewCondition?.let {emptyViewLambda ->
                if (emptyViewLambda(index)) commentDataList.add(CommentsAdapterViewHolderDataTypes.EmptyView())
            }
        }
        commentsDataList.emit(commentDataList.toList())
    }

    suspend fun addNewReply(newReply: Reply){
        commentsViewAttribute.essentialActionListener.addNewReply(
            newReply, commentsDataAccessFunctions
        ).let {reply->
            commentsList.find { it.commentId == reply.commentId }?.apply {
                totalRepliesCount += 1
                val temp = mutableListOf<Reply>()
                temp.addAll(replies)
                temp.add(reply)

//                TODO("have to create a abstract fun for comment class called copy")

                when(this){
                    is Comment.TextComment ->{
                        commentsList[commentsList.indexOfFirst { it.commentId == reply.commentId }] = (this as Comment.TextComment).copy(replies = temp)
                    }
                    is Comment.GifComment -> {
                        commentsList[commentsList.indexOfFirst { it.commentId == reply.commentId }] = (this as Comment.GifComment).copy(replies = temp)
                    }
                }
            }
        }
        mergeCommentsAndEmptyCustomViewsAndUpdate()
    }

    fun getCurrentCommentList(): List<Comment> = commentsList.toList()

    suspend fun setCurrentCommentList(list: List<Comment>){
        commentsList.clear()
        commentsList.addAll(list)
        mergeCommentsAndEmptyCustomViewsAndUpdate()
    }

    fun getNewReplyId(userId: String, commentId: String): String{
        var newId = UUID.randomUUID().toString()
        commentsList.find { it.commentId == commentId }?.let {
            while (it.replies.any { it.replyId == newId }){
                newId = UUID.randomUUID().toString()
            }
        }
        return userId + "_" + commentId + "_" + newId
    }



    fun getNewCommentId(userId: String): String{
        var newId = UUID.randomUUID().toString()
        while (commentsList.any{ it.commentId == newId }){
            newId = UUID.randomUUID().toString()
        }
        return userId + "_" + newId
    }

}