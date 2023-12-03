package com.ferbotz.comments.ui

import android.animation.Animator
import android.content.ClipData
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferbotz.comments.adapters.CommentsRecyclerViewAdapter
import com.ferbotz.comments.custom_views.CommentsView
import com.ferbotz.comments.databinding.FragmentCommentsBinding
import com.ferbotz.comments.modals.UserActionData
import com.ferbotz.comments.utils.ScreenUtils
import com.ferbotz.comments.utils.ScreenUtils.logVasi
import com.ferbotz.comments.utils.SwipeItemViewHelper
import com.ferbotz.comments.viewmodals.CommentViewModelFactory
import com.ferbotz.comments.viewmodals.CommentsViewModel
import com.ferbotz.comments.viewmodals.chuma

class CommentsFragment : Fragment() {

    private var _binding: FragmentCommentsBinding? = null
    val binding get() = _binding!!

    private lateinit var commentsViewModel: CommentsViewModel

    val MIME_TYPES = arrayOf("image/*", "video/*")

    private val commentsAdapter: CommentsRecyclerViewAdapter = CommentsRecyclerViewAdapter(
        userAction = {userActionData ->
            when(userActionData){
                is UserActionData.ReplyActionData -> {
                    commentsViewModel.focusedCommentId = userActionData.commentId
                    commentsViewModel.isComment = false
                    showReplyingToTab(userActionData.replyToUserName)
                    binding.commentEt.requestFocus()
                    ScreenUtils.showKeyboard(requireActivity())
                }
                is UserActionData.LikeCommentActionData -> {
                    "like action triggered...${userActionData.isLiked}".logVasi()
                    "inside rec view frag..item hash...${userActionData.comment.hashCode()}".logVasi()
                    commentsViewModel.likeComment(
                        isLiked = userActionData.isLiked,
                        comment = userActionData.comment
                    )
                }
                is UserActionData.LikeReplyActionData -> {
                    commentsViewModel.likeReply(
                        isLiked = userActionData.isLiked,
                        reply = userActionData.reply
                    )
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentsBinding.inflate(inflater, container, false)
        commentsViewModel = ViewModelProvider(requireActivity(), CommentViewModelFactory( chuma.repo!! ))[CommentsViewModel::class.java]
        ViewCompat.setOnReceiveContentListener(
            binding.commentEt,
            MIME_TYPES
        ) { view, payload ->
            val split = payload.partition { item: ClipData.Item -> item.uri != null }
            val remaining = split.second
            val clipData: ClipData = payload.clip
            if (clipData.itemCount > 0) {
                val item = clipData.getItemAt(0)
                val contentUri: Uri = item.uri
                if (commentsViewModel.isComment){
                    commentsViewModel.addNewComment(commentsViewModel.buildGifComment(contentUri.toString(), commentsViewModel.commentsViewAttribute.userProfile, commentsViewModel.commentsViewAttribute.postId))
                }
                else{
                    commentsViewModel.focusedCommentId?.let {
                        commentsViewModel.addNewReply(commentsViewModel.buildGifReply(contentUri.toString(), it, commentsViewModel.commentsViewAttribute.userProfile, commentsViewModel.commentsViewAttribute.postId))
                        commentsViewModel.removeReplyFocus()
                        hideReplyingToTab()
                    }
                }
                ScreenUtils.hideSoftKeyboard(requireActivity(), binding.commentEt)
                binding.commentEt.text!!.clear()
                binding.commentEt.clearFocus()
            }
            remaining
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    fun initComponents(){
        binding.apply {
            commentsRecyclerView.adapter = commentsAdapter
            commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            commentsViewModel.commentsViewAttribute.attributeInstructions.emptyViewBindListener?.let {listener->
                commentsAdapter.setEmptyViewHolderBinding { emptyViewHolderData, emptyView, position ->
                    listener.emptyViewHolderBind(
                        emptyViewHolderData,
                        emptyView,
                        position
                    )
                }
            }
            commentsViewModel.commentsViewAttribute.attributeInstructions.defaultCommentViewOverridingListener?.let { listener ->
                commentsAdapter.setDefaultCommentViewHolderModifyFunctions(listener)
            }

            val itemTouchHelper = ItemTouchHelper(SwipeItemViewHelper(commentsAdapter, commentsRecyclerView))
            itemTouchHelper.attachToRecyclerView(commentsRecyclerView)

            lifecycleScope.launchWhenResumed {
                commentsViewModel.commentDataList.collect{
                    "new comments list submitted".logVasi()
                    commentsAdapter.submitList(it)
                }
            }

            commentEt.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus){
                    commentsViewModel.removeReplyFocus()
                    hideReplyingToTab()
                    binding.commentEt.text?.clear()
                }
            }

            sendBtn.setOnClickListener {
                if(!binding.commentEt.text.isNullOrBlank()){
                    if (commentsViewModel.isComment){
                        commentsViewModel.addNewComment(commentsViewModel.buildTextComment(binding.commentEt.text.toString(), commentsViewModel.commentsViewAttribute.userProfile, commentsViewModel.commentsViewAttribute.postId))
                    }
                    else{
                        commentsViewModel.focusedCommentId?.let {
                            commentsViewModel.addNewReply(commentsViewModel.buildTextReply(binding.commentEt.text.toString(), it, commentsViewModel.commentsViewAttribute.userProfile, commentsViewModel.commentsViewAttribute.postId))
                            commentsViewModel.removeReplyFocus()
                            hideReplyingToTab()
                        }
                    }
                    ScreenUtils.hideSoftKeyboard(requireActivity(), binding.commentEt)
                    binding.commentEt.text!!.clear()
                    binding.commentEt.clearFocus()
                }
            }

            cancelReplyIv.setOnClickListener {
                commentsViewModel.removeReplyFocus()
                binding.commentEt.clearFocus()
                binding.commentEt.text?.clear()
                hideReplyingToTab()
            }

            binding.commentsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }
    }

    private fun showReplyingToTab(replyToUserName: String){
        binding.replyToTv.text = "Replying To: @${replyToUserName}"
        binding.replyingToLay.apply {
            visibility = View.VISIBLE
            translationY = height.toFloat()
            animate()
                .translationY(0F)
                .setDuration(400)
                .setListener(
                    object: Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {

                        }

                        override fun onAnimationEnd(animation: Animator) {
                        }

                        override fun onAnimationCancel(animation: Animator) {
                        }

                        override fun onAnimationRepeat(animation: Animator) {
                        }

                    }
                )
        }
    }


    private fun hideReplyingToTab(){
        binding.replyingToLay.apply {
            animate()
                .translationY(this.height.toFloat())
                .setDuration(400)
                .setListener(
                    object: Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {

                        }

                        override fun onAnimationEnd(animation: Animator) {
                            binding.replyingToLay.visibility = View.GONE
                        }

                        override fun onAnimationCancel(animation: Animator) {
                        }

                        override fun onAnimationRepeat(animation: Animator) {
                        }

                    }
                )
        }
    }




}