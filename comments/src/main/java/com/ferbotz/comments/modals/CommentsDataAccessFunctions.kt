package com.ferbotz.comments.modals

data class CommentsDataAccessFunctions(
    val getCurrentCommentList: () -> List<Comment> ,
    val setCurrentCommentList: (List<Comment>) -> Unit,
)
