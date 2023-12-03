package com.ferbotz.comments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ferbotz.comments.databinding.GifReplyLayoutBinding
import com.ferbotz.comments.databinding.TextReplyLayoutBinding
import com.ferbotz.comments.modals.Reply
import com.ferbotz.comments.modals.UserActionData
import com.ferbotz.comments.utils.ScreenUtils.logVasi
import com.ferbotz.comments.viewholder.GifReplyViewHolder
import com.ferbotz.comments.viewholder.TextReplyViewHolder

class ReplyRecyclerViewAdapter(val userAction:(UserActionData) -> Unit): ListAdapter<Reply, RecyclerView.ViewHolder>(ReplyDiffUtil()) {

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is Reply.TextReply ->{
                1
            }
            is Reply.GifReply ->{
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
        }
    }
}



class ReplyDiffUtil: DiffUtil.ItemCallback<Reply>() {
    override fun areItemsTheSame(oldItem: Reply, newItem: Reply): Boolean {
        return when(oldItem){
            is Reply.TextReply -> {
                when(newItem){
                    is Reply.TextReply ->{
                        oldItem.replyId == newItem.replyId
                    }
                    is Reply.GifReply ->{
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

    override fun areContentsTheSame(oldItem: Reply, newItem: Reply): Boolean {
        "old item liked...${oldItem.isCurrentUserLiked}....newitem liked...${newItem.isCurrentUserLiked}".logVasi()
        return oldItem.hashCode() == newItem.hashCode()
    }

}