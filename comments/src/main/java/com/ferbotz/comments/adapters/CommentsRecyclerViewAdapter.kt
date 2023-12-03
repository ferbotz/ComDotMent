package com.ferbotz.comments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ferbotz.comments.databinding.EmptyVhBinding
import com.ferbotz.comments.databinding.GifCommentLayoutBinding
import com.ferbotz.comments.databinding.TextCommentLayoutBinding
import com.ferbotz.comments.interfaces.DefaultCommentViewOverridingListener
import com.ferbotz.comments.modals.Comment
import com.ferbotz.comments.modals.CommentsAdapterViewHolderDataTypes
import com.ferbotz.comments.modals.UserActionData
import com.ferbotz.comments.utils.JsonUtils
import com.ferbotz.comments.utils.ScreenUtils.logVasi
import com.ferbotz.comments.viewholder.EmptyViewHolder
import com.ferbotz.comments.viewholder.GifCommentViewHolder
import com.ferbotz.comments.viewholder.TextCommentViewHolder

class CommentsRecyclerViewAdapter(val userAction:(UserActionData) -> Unit): ListAdapter<CommentsAdapterViewHolderDataTypes, RecyclerView.ViewHolder>(CommentsDiffUtil()) {

    private var emptyViewHolderBindFunction:((emptyViewHolderData: Any?, emptyView:EmptyVhBinding, position:Int ) -> Unit)? = null

    private var defaultCommentViewHolderModifyFunctions: DefaultCommentViewOverridingListener? = null

    fun setEmptyViewHolderBinding(emptyViewHolderBind:(emptyViewHolderData: Any?, emptyView:EmptyVhBinding, position:Int ) -> Unit){
        emptyViewHolderBindFunction = emptyViewHolderBind
    }

    fun setDefaultCommentViewHolderModifyFunctions(listener: DefaultCommentViewOverridingListener){
        defaultCommentViewHolderModifyFunctions = listener
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when(item){
            is CommentsAdapterViewHolderDataTypes.CommentView ->{
                when(item.commentData){
                    is Comment.TextComment ->{
                        1
                    }
                    is Comment.GifComment -> {
                        2
                    }
                }
            }
            is CommentsAdapterViewHolderDataTypes.EmptyView -> {
                0
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            0 ->{
                EmptyViewHolder( EmptyVhBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            1 ->{
                TextCommentViewHolder( TextCommentLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), userAction)
            }
            2 ->{
                GifCommentViewHolder( GifCommentLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), userAction)
            }
            else -> {
                TextCommentViewHolder( TextCommentLayoutBinding.inflate(LayoutInflater.from(parent.context)), userAction)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val comment = getItem(position)
        when(holder.itemViewType){
            0 ->{
                emptyViewHolderBindFunction?.let {
                    it(
                        (comment as CommentsAdapterViewHolderDataTypes.EmptyView).emptyViewData,
                        (holder as EmptyViewHolder).binding,
                        position
                    )
                }
            }

            1 ->{
                (holder as TextCommentViewHolder).bindViews(
                    ((comment as CommentsAdapterViewHolderDataTypes.CommentView).commentData as Comment.TextComment),
                    holder.binding
                )
                defaultCommentViewHolderModifyFunctions?.let {
                    it.defaultTextCommentView(
                        (comment.commentData as Comment.TextComment),
                        holder.binding,
                        position
                    )
                }
            }
            2->{
                (holder as GifCommentViewHolder).bindViews(
                    ((comment as CommentsAdapterViewHolderDataTypes.CommentView).commentData as Comment.GifComment),
                    holder.binding
                )
                defaultCommentViewHolderModifyFunctions?.let {
                    it.defaultGifCommentView(
                        (comment.commentData as Comment.GifComment),
                        holder.binding,
                        position
                    )
                }
            }
        }
    }
}

class CommentsDiffUtil: DiffUtil.ItemCallback<CommentsAdapterViewHolderDataTypes>() {
    override fun areItemsTheSame(oldItem: CommentsAdapterViewHolderDataTypes, newItem: CommentsAdapterViewHolderDataTypes): Boolean {
        return when(oldItem){
            is CommentsAdapterViewHolderDataTypes.EmptyView ->{
                when(newItem){
                    is CommentsAdapterViewHolderDataTypes.EmptyView ->{
                        oldItem.hashCode() == newItem.hashCode()
                    }
                    is CommentsAdapterViewHolderDataTypes.CommentView->{
                        false
                    }
                }
            }
            is CommentsAdapterViewHolderDataTypes.CommentView ->{
                when(oldItem.commentData){
                    is Comment.TextComment ->{
                        when(newItem){
                            is CommentsAdapterViewHolderDataTypes.EmptyView ->{
                                false
                            }
                            is CommentsAdapterViewHolderDataTypes.CommentView->{
                                when(newItem.commentData){
                                    is Comment.TextComment->{
                                        oldItem.commentData.commentId == newItem.commentData.commentId
                                    }
                                    is Comment.GifComment ->{
                                        oldItem.commentData.commentId == newItem.commentData.commentId
                                    }
                                }
                            }
                        }
                    }
                    is Comment.GifComment -> {
                        when(newItem){
                            is CommentsAdapterViewHolderDataTypes.EmptyView ->{
                                false
                            }
                            is CommentsAdapterViewHolderDataTypes.CommentView->{
                                when(newItem.commentData){
                                    is Comment.TextComment->{
                                        oldItem.commentData.commentId == newItem.commentData.commentId
                                    }
                                    is Comment.GifComment ->{
                                        oldItem.commentData.commentId == newItem.commentData.commentId
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun areContentsTheSame(oldItem: CommentsAdapterViewHolderDataTypes, newItem: CommentsAdapterViewHolderDataTypes): Boolean {
        return when (oldItem) {
            is CommentsAdapterViewHolderDataTypes.EmptyView -> {
                when (newItem) {
                    is CommentsAdapterViewHolderDataTypes.EmptyView -> {
                        JsonUtils.jsonify(oldItem) == JsonUtils.jsonify(newItem)
                    }
                    is CommentsAdapterViewHolderDataTypes.CommentView -> {
                        false
                    }
                }
            }
            is CommentsAdapterViewHolderDataTypes.CommentView -> {
                when (oldItem.commentData) {
                    is Comment.TextComment -> {
                        when (newItem) {
                            is CommentsAdapterViewHolderDataTypes.EmptyView -> {
                                false
                            }
                            is CommentsAdapterViewHolderDataTypes.CommentView -> {
                                when (newItem.commentData) {
                                    is Comment.TextComment -> {
//                                        oldItem.hashCode() == newItem.hashCode()
                                        "diff util...${oldItem.hashCode()}...${newItem.hashCode()}".logVasi()
                                        oldItem.commentData.commentId == newItem.commentData.commentId && oldItem.commentData.totalRepliesCount == newItem.commentData.totalRepliesCount && oldItem.commentData.replies.size == newItem.commentData.replies.size
                                    }
                                    is Comment.GifComment -> {
                                        oldItem.commentData.commentId == newItem.commentData.commentId && oldItem.commentData.totalRepliesCount == newItem.commentData.totalRepliesCount && oldItem.commentData.replies.size == newItem.commentData.replies.size
                                    }
                                }
                            }
                        }
                    }
                    is Comment.GifComment -> {
                        when (newItem) {
                            is CommentsAdapterViewHolderDataTypes.EmptyView -> {
                                false
                            }
                            is CommentsAdapterViewHolderDataTypes.CommentView -> {
                                when (newItem.commentData) {
                                    is Comment.TextComment -> {
                                        oldItem.commentData.commentId == newItem.commentData.commentId && oldItem.commentData.totalRepliesCount == newItem.commentData.totalRepliesCount && oldItem.commentData.replies.size == newItem.commentData.replies.size
                                    }
                                    is Comment.GifComment -> {
                                        oldItem.commentData.commentId == newItem.commentData.commentId && oldItem.commentData.totalRepliesCount == newItem.commentData.totalRepliesCount && oldItem.commentData.replies.size == newItem.commentData.replies.size
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}