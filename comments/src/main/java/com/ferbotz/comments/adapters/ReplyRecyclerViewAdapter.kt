package com.ferbotz.comments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ferbotz.comments.R
import com.ferbotz.comments.databinding.GifReplyLayoutBinding
import com.ferbotz.comments.databinding.LoadMoreRepliesVhBinding
import com.ferbotz.comments.databinding.TextReplyLayoutBinding
import com.ferbotz.comments.modals.Reply
import com.ferbotz.comments.modals.ReplyAdapterViewType
import com.ferbotz.comments.modals.UserActionData
import com.ferbotz.comments.utils.ScreenUtils.logVasi
import com.ferbotz.comments.viewholder.GifReplyViewHolder
import com.ferbotz.comments.viewholder.LoadMoreRepliesViewHolder
import com.ferbotz.comments.viewholder.TextReplyViewHolder

class ReplyRecyclerViewAdapter(val userAction:(UserActionData) -> Unit): ListAdapter<ReplyAdapterViewType, RecyclerView.ViewHolder>(ReplyDiffUtil()) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when(item){
            is ReplyAdapterViewType.ReplyView{
                when(item.reply){
                    is Reply.TextReply ->{
                        1
                    }
                    is Reply.GifReply ->{
                        2
                    }
                }
            }
            is ReplyAdapterViewType.LoadMoreView ->{
                3
            }
            else -> {
                2
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            1 ->{
                TextReplyViewHolder( TextReplyLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), userAction)
            }
            2 ->{
                GifReplyViewHolder( GifReplyLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), userAction)
            }
            3 ->{
                LoadMoreRepliesViewHolder( LoadMoreRepliesVhBinding.inflate(LayoutInflater.from(parent.context), parent, false), userAction)
            }
            else -> {
                TextReplyViewHolder( TextReplyLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), userAction)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val reply = getItem(position)
        when(holder.itemViewType){
            1 ->{ (holder as TextReplyViewHolder).bindViews(reply as Reply.TextReply) }
            2 ->{ (holder as GifReplyViewHolder).bindViews(reply as Reply.GifReply) }
            3 ->{ (holder as LoadMoreRepliesViewHolder).bindViews(reply as ReplyAdapterViewType.LoadMoreView)}
        }
    }

    fun submitReplyList(replyList: List<Reply>){
        val tempList = mutableListOf<Reply>()
        tempList.addAll(replyList)
        tempList.distinctBy { it.replyId }.partition { it.replyPosition == null }.let { (nullPositionReplies, nonNullPositionReplies) ->
            val replyAdapterViewList = mutableListOf<ReplyAdapterViewType>()
            var repliesInorder = listOf<Reply>()
            nullPositionReplies.toMutableList().let { nullPositionReplies ->
                nonNullPositionReplies.sortedBy { it.replyPosition }.forEach {
                    if (it.replyPosition!! <= (nullPositionReplies.size + 1)){
                        nullPositionReplies.add(it.replyPosition!! - 1, it)
                        repliesInorder = nullPositionReplies.toList()
                    }else{
                        val lastElementReplyPosition = nonNullPositionReplies.last().replyPosition ?: nonNullPositionReplies.size
                        replyAdapterViewList.add(
                            ReplyAdapterViewType.LoadMoreView(
                                repliesBetweenCount = it.replyPosition!! - lastElementReplyPosition - 1,
                                repliesBetweenStartPosition = lastElementReplyPosition + 1,
                                repliesBetweenEndPosition = it.replyPosition!! - 1
                            )
                        )
                        replyAdapterViewList.add(
                            ReplyAdapterViewType.ReplyView(
                                it
                            )
                        )
                        nullPositionReplies.add(it)
                    }
                }
                replyAdapterViewList.addAll(0, repliesInorder.map { ReplyAdapterViewType.ReplyView(it) })
            }
        }
    }

}



class ReplyDiffUtil: DiffUtil.ItemCallback<ReplyAdapterViewType>() {
    override fun areItemsTheSame(oldItem: ReplyAdapterViewType, newItem: ReplyAdapterViewType): Boolean {
        return when(oldItem){
            is ReplyAdapterViewType.ReplyView{
                when(oldItem.reply){
                    is Reply.TextReply -> {
                        when(newItem){
                            is ReplyAdapterViewType.ReplyView -> {
                                when(newItem.reply){
                                    is Reply.TextReply ->{
                                        oldItem.reply.replyId == newItem.reply.replyId
                                    }
                                    is Reply.GifReply ->{
                                        false
                                    }
                                }

                            }
                            is ReplyAdapterViewType.LoadMoreView -> {
                                false
                            }
                        }
                    }
                    is Reply.GifReply -> {
                        when(newItem){
                            is Reply.TextReply ->{
                                false
                            }
                            is Reply.GifReply ->{
                                oldItem.replyId == newItem.replyId
                            }
                        }
                    }
                }
            }

        }
    }

    override fun areContentsTheSame(oldItem: Reply, newItem: Reply): Boolean {
        "old item liked...${oldItem.isCurrentUserLiked}....newitem liked...${newItem.isCurrentUserLiked}".logVasi()
        return oldItem.hashCode() == newItem.hashCode()
    }

}