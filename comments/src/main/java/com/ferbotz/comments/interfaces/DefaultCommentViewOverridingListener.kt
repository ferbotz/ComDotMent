package com.ferbotz.comments.interfaces

import com.ferbotz.comments.databinding.GifCommentLayoutBinding
import com.ferbotz.comments.databinding.TextCommentLayoutBinding
import com.ferbotz.comments.modals.Comment

interface DefaultCommentViewOverridingListener {

    fun defaultTextCommentView(textComment: Comment.TextComment, binding: TextCommentLayoutBinding, position:Int)
    fun defaultGifCommentView(gifComment: Comment.GifComment, binding: GifCommentLayoutBinding, position:Int)

}