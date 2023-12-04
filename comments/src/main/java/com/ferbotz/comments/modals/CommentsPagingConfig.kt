package com.ferbotz.comments.modals

import android.view.View
import com.ferbotz.comments.databinding.LoadingFooterVhBinding

data class CommentsPagingConfig(
    val preLoadPositionOffset: Int,
    val pagingContentSource: CommentsPagingSource
)

interface CommentsPagingSource{

    suspend fun loadNextPageComments(page: Int): Pair<List<Comment>, Boolean>

    fun loadingFooterBind(loadingFooter: LoadingFooterVhBinding)

}
