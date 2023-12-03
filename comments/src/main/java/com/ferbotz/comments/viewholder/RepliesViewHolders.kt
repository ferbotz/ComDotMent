package com.ferbotz.comments.viewholder

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ferbotz.comments.R
import com.ferbotz.comments.adapters.ReplyRecyclerViewAdapter
import com.ferbotz.comments.databinding.GifCommentLayoutBinding
import com.ferbotz.comments.databinding.GifReplyLayoutBinding
import com.ferbotz.comments.databinding.TextCommentLayoutBinding
import com.ferbotz.comments.databinding.TextReplyLayoutBinding
import com.ferbotz.comments.modals.Comment
import com.ferbotz.comments.modals.Reply
import com.ferbotz.comments.modals.UserActionData
import com.ferbotz.comments.utils.ScreenUtils.logVasi
import com.ferbotz.comments.utils.setOnDoubleClickListener

class TextReplyViewHolder(val binding: TextReplyLayoutBinding, val userAction:(UserActionData) -> Unit): RecyclerView.ViewHolder(binding.root) {

    fun bindViews(textReply: Reply.TextReply) {
        binding.apply {
            replyTextTv.text = textReply.replyText
            profileNameTv.text = textReply.user.userName
            binding.likeCountTv.text = textReply.likes.toString()
            if (textReply.isCurrentUserLiked){
                binding.likeIv.setImageDrawable(ResourcesCompat.getDrawable(binding.root.resources, R.drawable.liked_icon, null))
            }
            else{
                binding.likeIv.setImageDrawable(ResourcesCompat.getDrawable(binding.root.resources, R.drawable.unliked_icon, null))

            }
            replyTv.setOnClickListener {
                userAction(
                    UserActionData.ReplyActionData(
                        textReply.commentId,
                        textReply.user.userName
                    )
                )
            }
            root.setOnDoubleClickListener {
                "reply double clicked".logVasi()
                textReply.isCurrentUserLiked = !textReply.isCurrentUserLiked
                if (textReply.isCurrentUserLiked){
                    binding.likeIv.setImageDrawable(ResourcesCompat.getDrawable(binding.root.resources, R.drawable.liked_icon, null))
                    textReply.likes += 1
                    binding.likeCountTv.text = textReply.likes.toString()
                }
                else{
                    binding.likeIv.setImageDrawable(ResourcesCompat.getDrawable(binding.root.resources, R.drawable.unliked_icon, null))
                    textReply.likes -= 1
                    binding.likeCountTv.text = textReply.likes.toString()
                }
                userAction(
                    UserActionData.LikeReplyActionData(
                        textReply.isCurrentUserLiked,
                        textReply
                    )
                )
            }
            likeIv.setOnClickListener {
                textReply.isCurrentUserLiked = !textReply.isCurrentUserLiked
                if (textReply.isCurrentUserLiked){
                    binding.likeIv.setImageDrawable(ResourcesCompat.getDrawable(binding.root.resources, R.drawable.liked_icon, null))
                    textReply.likes += 1
                    binding.likeCountTv.text = textReply.likes.toString()
                }
                else{
                    binding.likeIv.setImageDrawable(ResourcesCompat.getDrawable(binding.root.resources, R.drawable.unliked_icon, null))
                    textReply.likes -= 1
                    binding.likeCountTv.text = textReply.likes.toString()
                }
                userAction(
                    UserActionData.LikeReplyActionData(
                        !textReply.isCurrentUserLiked,
                        textReply.copy(isCurrentUserLiked = !textReply.isCurrentUserLiked)
                    )
                )
            }
        }
    }
}


class GifReplyViewHolder(val binding: GifReplyLayoutBinding, val userAction:(UserActionData) -> Unit): RecyclerView.ViewHolder(binding.root) {

    fun bindViews(gifReply: Reply.GifReply) {
        binding.apply {
            Glide.with(binding.root.context)
                .load(gifReply.replyGifUrl)
                .placeholder(R.drawable.reply_icon)
                .into(
                    replyGifIv)
            profileNameTv.text = gifReply.user.userName
            replyTv.setOnClickListener {
                userAction(
                    UserActionData.ReplyActionData(
                        gifReply.commentId,
                        gifReply.user.userName
                    )
                )
            }
        }
    }
}
