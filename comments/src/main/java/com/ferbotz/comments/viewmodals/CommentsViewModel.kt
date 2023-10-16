package com.ferbotz.comments.viewmodals

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ferbotz.comments.custom_views.CommentsView
import com.ferbotz.comments.interfaces.EmptyViewHolderBindListener
import com.ferbotz.comments.modals.*
import com.ferbotz.comments.utils.ScreenUtils
import kotlinx.coroutines.launch
import java.util.*

class CommentsViewModel():ViewModel() {

    lateinit var commentsViewAttribute: CommentsViewAttribute

    val commentDataList: MutableLiveData<List<CommentsAdapterViewHolderDataTypes>> = MutableLiveData()

    private val commentAdapterDataList: MutableList<CommentsAdapterViewHolderDataTypes> = mutableListOf()

    private val commentsList: MutableList<Comment> = mutableListOf()

    var focusedCommentId: String? = null
    var isComment = true
    var emptyViewOffset: Int? = null

    val commentsDataAccessFunctions = CommentsDataAccessFunctions(::getCurrentCommentList, ::setCurrentCommentList)


    private fun mergeCommentsAndEmptyCustomViewsAndUpdate(){

        commentAdapterDataList.clear()
        commentsList.forEachIndexed { index, comment ->
            commentAdapterDataList.add(CommentsAdapterViewHolderDataTypes.CommentView(comment))
            emptyViewOffset?.let {
                if ((index + 1) % it == 0) commentAdapterDataList.add(CommentsAdapterViewHolderDataTypes.EmptyView())
            }
        }
        commentDataList.value = commentAdapterDataList.toList()
    }

    fun removeReplyFocus(){
        focusedCommentId = null
        isComment = true
    }

    fun getCurrentCommentList(): kotlin.collections.List<Comment> = commentsList.toList()

    fun setCurrentCommentList(list: kotlin.collections.List<Comment>){
        commentsList.clear()
        commentsList.addAll(list)
        mergeCommentsAndEmptyCustomViewsAndUpdate()
    }


    fun likeComment(comment: Comment, isLiked: Boolean){
        viewModelScope.launch {
            when(comment){
                is Comment.TextComment -> {
                    commentsViewAttribute.essentialActionListener.onLikeComment(
                        isLiked,
                        comment.copy(
                            isCurrentUserLiked = isLiked
                        ),
                        commentsDataAccessFunctions
                    )?.let { comment ->
                        commentsList.indexOfFirst { it.commentId == comment.commentId }.let { index->
                            commentsList[index] = comment
                            mergeCommentsAndEmptyCustomViewsAndUpdate()
                        }
                    }
                }
                is Comment.GifComment -> {
                    commentsViewAttribute.essentialActionListener.onLikeComment(
                        isLiked,
                        comment.copy(
                            isCurrentUserLiked = isLiked
                        ),
                        commentsDataAccessFunctions
                    )?.let { comment ->
                        commentsList.indexOfFirst { it.commentId == comment.commentId }.let { index->
                            commentsList[index] = comment
                            mergeCommentsAndEmptyCustomViewsAndUpdate()
                        }
                    }
                }
            }
        }
    }


    fun addNewComment(newComment: Comment){
        viewModelScope.launch {
            commentsViewAttribute.essentialActionListener.addNewComment(
                newComment,
                commentsDataAccessFunctions
            )?.let {comment->
                commentsList.add(
                    comment
                )
            }
            mergeCommentsAndEmptyCustomViewsAndUpdate()
        }
    }

    fun addNewReply(newReply: Reply){
        viewModelScope.launch {
            commentsViewAttribute.essentialActionListener.addNewReply(
                newReply, commentsDataAccessFunctions
            ).let {reply->
                commentsList.find { it.commentId == reply.commentId }?.apply {
                    totalRepliesCount += 1
                    val temp = mutableListOf<Reply>()
                    temp.addAll(replies)
                    temp.add(reply)
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
        }
        mergeCommentsAndEmptyCustomViewsAndUpdate()
    }

    fun addEmptyViewsWithOffset(offset: Int){
        emptyViewOffset = offset
        mergeCommentsAndEmptyCustomViewsAndUpdate()
    }

    fun buildTextReply(replyText: String, commentId: String, userProfile: UserProfile, postId: String): Reply.TextReply{
        return Reply.TextReply(
            commentId = commentId,
            replyId = getNewReplyId(userId = userProfile.userId, commentId = commentId),
            user = userProfile,
            postId = postId,
            replyText = replyText,

            ).also {
            Log.v("Vasi testing","new comment...${it}")
        }
    }

    fun buildGifReply(replyGifUrl: String, commentId: String, userProfile: UserProfile, postId: String): Reply.GifReply{
        return Reply.GifReply(
            commentId = commentId,
            replyId = getNewReplyId(userId = userProfile.userId, commentId = commentId),
            user = userProfile,
            postId = postId,
            replyGifUrl = replyGifUrl
        ).also {
            Log.v("Vasi testing","new comment...${it}")
        }
    }


    fun buildTextComment(commentText: String, userProfile: UserProfile, postId: String): Comment.TextComment{
        return Comment.TextComment(
            commentId = getNewCommentId(userProfile.userId),
            user = userProfile,
            commentText = commentText,
            postId = postId,
            timestamp = System.currentTimeMillis().toString(),

            ).also {
            Log.v("Vasi testing","new comment...${it}")
        }
    }

    fun buildGifComment(gifUrl: String, userProfile: UserProfile, postId: String): Comment.GifComment{
        return Comment.GifComment(
            commentId = getNewCommentId(userProfile.userId),
            user = userProfile,
            gifUrl = gifUrl,
            postId = postId,
            timestamp = System.currentTimeMillis().toString(),

            ).also {
            Log.v("Vasi testing","new comment...${it}")
        }
    }

    private fun getNewReplyId(userId: String, commentId: String): String{
        var newId = UUID.randomUUID().toString()
        commentsList.find { it.commentId == commentId }?.let {
            while (it.replies.any { it.replyId == newId }){
                newId = UUID.randomUUID().toString()
            }
        }
        return userId + "_" + commentId + "_" + newId
    }



    private fun getNewCommentId(userId: String): String{
        var newId = UUID.randomUUID().toString()
        while (commentsList.any{ it.commentId == newId }){
            newId = UUID.randomUUID().toString()
        }
        return userId + "_" + newId
    }


}


class CommentViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommentsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommentsViewModel() as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}