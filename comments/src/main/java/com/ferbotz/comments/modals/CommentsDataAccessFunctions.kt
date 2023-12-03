package com.ferbotz.comments.modals

data class CommentsDataAccessFunctions(
    val getCurrentCommentList: () -> List<Comment> ,
    val setCurrentCommentList: suspend (List<Comment>) -> Unit,
)
