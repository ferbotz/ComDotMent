package com.ferbotz.comments.modals

import com.ferbotz.comments.interfaces.DefaultCommentViewOverridingListener
import com.ferbotz.comments.interfaces.EmptyViewHolderBindListener
import com.ferbotz.comments.interfaces.EssentialCommentActionListener

data class CommentsViewAttribute private constructor(val userProfile: UserProfile, val postId: String, val attributeInstructions: CommentsViewAttributeBuilder, val essentialActionListener: EssentialCommentActionListener){

    class CommentsViewAttributeBuilder(private val userProfile: UserProfile, private val postId: String, private val essentialActionListener: EssentialCommentActionListener){

        var emptyViewOffset: Int? = null
        var emptyViewBindListener: EmptyViewHolderBindListener? = null
        var defaultCommentViewOverridingListener: DefaultCommentViewOverridingListener? = null

        fun addEmptyViewsWithOffset(offset: Int, listener: EmptyViewHolderBindListener) = apply{
            this.emptyViewOffset = offset
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
