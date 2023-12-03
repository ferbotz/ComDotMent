package com.ferbotz.comments.viewmodals

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ferbotz.comments.custom_views.CommentsView
import com.ferbotz.comments.data.CommentsRepository
import com.ferbotz.comments.interfaces.EmptyViewHolderBindListener
import com.ferbotz.comments.modals.*
import com.ferbotz.comments.utils.ScreenUtils
import com.ferbotz.comments.utils.ScreenUtils.logVasi
import com.ferbotz.comments.utils.testingLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

class CommentsViewModel(private val commentsRepository: CommentsRepository):ViewModel() {

    val commentsViewAttribute: CommentsViewAttribute = commentsRepository.commentsViewAttribute

    val commentDataList: MutableStateFlow<List<CommentsAdapterViewHolderDataTypes>> = MutableStateFlow(emptyList())

    var focusedCommentId: String? = null
    var isComment = true

    init {
        viewModelScope.launch {
            commentsRepository.commentsDataList.collect{
                "viewmodel state collected".logVasi()
                commentDataList.emit(it)
            }
        }
        "comments viewmodel initialised".testingLog()
    }

    fun removeReplyFocus(){
        focusedCommentId = null
        isComment = true
    }


    fun likeComment(comment: Comment, isLiked: Boolean){
        viewModelScope.launch {
            commentsRepository.likeComment(comment, isLiked)
        }
    }

    fun likeReply(isLiked: Boolean, reply: Reply){
        viewModelScope.launch {
            commentsRepository.likeReply(isLiked, reply)
        }
    }


    fun addNewComment(newComment: Comment){
        viewModelScope.launch {
            commentsRepository.addNewComment(newComment)
        }
    }

    fun addNewReply(newReply: Reply){
        viewModelScope.launch {
            commentsRepository.addNewReply(newReply)
        }
    }

    fun buildTextReply(replyText: String, commentId: String, userProfile: UserProfile, postId: String): Reply.TextReply{
        return Reply.TextReply(
            commentId = commentId,
            replyId = commentsRepository.getNewReplyId(userId = userProfile.userId, commentId = commentId),
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
            replyId = commentsRepository.getNewReplyId(userId = userProfile.userId, commentId = commentId),
            user = userProfile,
            postId = postId,
            replyGifUrl = replyGifUrl
        ).also {
            Log.v("Vasi testing","new comment...${it}")
        }
    }


    fun buildTextComment(commentText: String, userProfile: UserProfile, postId: String): Comment.TextComment{
        return Comment.TextComment(
            commentId = commentsRepository.getNewCommentId(userProfile.userId),
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
            commentId = commentsRepository.getNewCommentId(userProfile.userId),
            user = userProfile,
            gifUrl = gifUrl,
            postId = postId,
            timestamp = System.currentTimeMillis().toString(),

            ).also {
            Log.v("Vasi testing","new comment...${it}")
        }
    }

}


class CommentViewModelFactory(val commentsRepository: CommentsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommentsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommentsViewModel(commentsRepository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}

object chuma {
    var repo : CommentsRepository? = null
}