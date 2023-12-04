package com.ferbotz.comments.modals

import com.ferbotz.comments.interfaces.DefaultCommentViewOverridingListener
import com.ferbotz.comments.interfaces.EmptyViewHolderBindListener
import com.ferbotz.comments.interfaces.EssentialCommentActionListener

data class CommentsViewAttribute internal constructor(val userProfile: UserProfile, val postId: String, val attributeInstructions: CommentsViewAttributeBuilder, val essentialActionListener: EssentialCommentActionListener){

    class CommentsViewAttributeBuilder(private val userProfile: UserProfile, private val postId: String, private val essentialActionListener: EssentialCommentActionListener){

        var emptyViewCondition: ((Int)-> Boolean)? = null
        var emptyViewBindListener: EmptyViewHolderBindListener? = null
        var defaultCommentViewOverridingListener: DefaultCommentViewOverridingListener? = null
        var commentsPagingConfig: CommentsPagingConfig? = null

        fun enableCommentsPaging(commentsPagingConfig: CommentsPagingConfig)= apply{
            this.commentsPagingConfig = commentsPagingConfig
        }

        fun addEmptyViewsWithOffset(condition: ((Int)-> Boolean), listener: EmptyViewHolderBindListener) = apply{
            this.emptyViewCondition = condition
            this.emptyViewBindListener = listener
        }

        fun modifyDefaultCommentsView(listener: DefaultCommentViewOverridingListener) = apply{
            defaultCommentViewOverridingListener = listener
        }

        fun build(): CommentsViewAttribute{
            return CommentsViewAttribute(userProfile, postId, this, essentialActionListener)
        }
    }



}
