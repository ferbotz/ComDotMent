package com.ferbotz.comments.viewholder

import android.os.SystemClock
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.ferbotz.comments.R
import com.ferbotz.comments.adapters.ReplyRecyclerViewAdapter
import com.ferbotz.comments.databinding.EmptyVhBinding
import com.ferbotz.comments.databinding.GifCommentLayoutBinding
import com.ferbotz.comments.databinding.LoadingFooterVhBinding
import com.ferbotz.comments.databinding.TextCommentLayoutBinding
import com.ferbotz.comments.modals.Comment
import com.ferbotz.comments.modals.UserActionData
import com.ferbotz.comments.utils.DoubleClickListener
import com.ferbotz.comments.utils.ScreenUtils.logVasi
import com.ferbotz.comments.utils.Utils
import com.ferbotz.comments.utils.setOnDoubleClickListener

class TextCommentViewHolder(val binding: TextCommentLayoutBinding, val userAction:(UserActionData) -> Unit): RecyclerView.ViewHolder(binding.root) {

    var repliesAdapter: ReplyRecyclerViewAdapter = ReplyRecyclerViewAdapter(userAction)
    init {
        "text viewholder".logVasi()
    }

    fun bindViews(textComment: Comment.TextComment, binding: TextCommentLayoutBinding) {
        "bind text comment view".logVasi()
        binding.apply {
            commentTextTv.text = textComment.commentText
            profileNameTv.text = textComment.user.userName
            whenTv.text = Utils.formatTime(System.currentTimeMillis() - textComment.timestamp.toLong())
            repliesRv.adapter = repliesAdapter
            repliesRv.layoutManager = LinearLayoutManager(binding.root.context)
            repliesAdapter.submitList(textComment.replies)
            binding.likeCountTv.text = textComment.likes.toString()
            if (textComment.isCurrentUserLiked){
                binding.likeIv.setImageDrawable(ResourcesCompat.getDrawable(binding.root.resources, R.drawable.liked_icon, null))
            }
            else{
                binding.likeIv.setImageDrawable(ResourcesCompat.getDrawable(binding.root.resources, R.drawable.unliked_icon, null))
            }
            repliesRv.visibility = if (textComment.totalRepliesCount > 2) View.GONE else View.VISIBLE
            viewRepliesLay.visibility = if (textComment.totalRepliesCount > 2) View.VISIBLE else View.GONE





            replyTv.setOnClickListener {
                userAction(
                    UserActionData.ReplyActionData(
                        textComment.commentId,
                        textComment.user.userName
                    )
                )
            }
            root.setOnDoubleClickListener {
                textComment.isCurrentUserLiked = !textComment.isCurrentUserLiked
                if (textComment.isCurrentUserLiked){
                    binding.likeIv.setImageDrawable(ResourcesCompat.getDrawable(binding.root.resources, R.drawable.liked_icon, null))
                    textComment.likes += 1
                    binding.likeCountTv.text = textComment.likes.toString()
                }
                else{
                    binding.likeIv.setImageDrawable(ResourcesCompat.getDrawable(binding.root.resources, R.drawable.unliked_icon, null))
                    textComment.likes -= 1
                    binding.likeCountTv.text = textComment.likes.toString()
                }
                userAction(
                    UserActionData.LikeCommentActionData(
                        textComment.isCurrentUserLiked,
                        textComment
                    )
                )
            }

            likeIv.setOnClickListener {
                "like iv clicked".logVasi()
                textComment.isCurrentUserLiked = !textComment.isCurrentUserLiked
                if (textComment.isCurrentUserLiked){
                    binding.likeIv.setImageDrawable(ResourcesCompat.getDrawable(binding.root.resources, R.drawable.liked_icon, null))
                    textComment.likes += 1
                    binding.likeCountTv.text = textComment.likes.toString()
                }
                else{
                    binding.likeIv.setImageDrawable(ResourcesCompat.getDrawable(binding.root.resources, R.drawable.unliked_icon, null))
                    textComment.likes -= 1
                    binding.likeCountTv.text = textComment.likes.toString()
                }
                userAction(
                    UserActionData.LikeCommentActionData(
                        textComment.isCurrentUserLiked,
                        textComment
                    )
                )
            }
        }
    }
}

class GifCommentViewHolder(val binding: GifCommentLayoutBinding, val userAction:(UserActionData) -> Unit): RecyclerView.ViewHolder(binding.root) {

    var repliesAdapter: ReplyRecyclerViewAdapter = ReplyRecyclerViewAdapter(userAction)
    init {
        "text viewholder".logVasi()
    }

    fun bindViews(gifComment: Comment.GifComment, binding: GifCommentLayoutBinding) {
        "bind text comment view".logVasi()
        binding.apply {
            Glide.with(binding.root.context)
                .load(gifComment.gifUrl)
                .placeholder(R.drawable.reply_icon)
                .into(
                    commentGifIv)
            profileNameTv.text = gifComment.user.userName
            repliesRv.adapter = repliesAdapter
            repliesRv.layoutManager = LinearLayoutManager(binding.root.context)
            repliesAdapter.submitList(gifComment.replies)
            replyTv.setOnClickListener {
                userAction(
                    UserActionData.ReplyActionData(
                        gifComment.commentId,
                        gifComment.user.userName
                    )
                )
            }
        }
    }
}


class EmptyViewHolder(val binding: EmptyVhBinding): RecyclerView.ViewHolder(binding.root) {}

class LoadingFooterViewHolder(val binding: LoadingFooterVhBinding): RecyclerView.ViewHolder(binding.root) {}

