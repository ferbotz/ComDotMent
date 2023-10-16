package com.ferbotz.comments.ui

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferbotz.comments.R
import com.ferbotz.comments.adapters.CommentsRecyclerViewAdapter
import com.ferbotz.comments.custom_views.CommentsView
import com.ferbotz.comments.databinding.FragmentCommentsBottomSheetBinding
import com.ferbotz.comments.interfaces.DefaultCommentViewOverridingListener
import com.ferbotz.comments.interfaces.EmptyViewHolderBindListener
import com.ferbotz.comments.modals.CommentsViewAttribute
import com.ferbotz.comments.modals.UserProfile
import com.ferbotz.comments.utils.ScreenUtils
import com.ferbotz.comments.utils.ScreenUtils.logVasi
import com.ferbotz.comments.utils.SwipeItemViewHelper
import com.ferbotz.comments.viewmodals.CommentViewModelFactory
import com.ferbotz.comments.viewmodals.CommentsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*


class CommentsBottomSheet(private val attribute: CommentsViewAttribute) : BottomSheetDialogFragment() {

    private lateinit var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback

    private lateinit var commentsViewModel: CommentsViewModel

    private var _binding : FragmentCommentsBottomSheetBinding? = null
    val binding get() = _binding!!

    var bottomSheetBehaviour: BottomSheetBehavior<*>? = null

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        "on create called".logVasi()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentsBottomSheetBinding.inflate(inflater, container, false)
        binding.commentsView.buildCommentsView(
            requireActivity(),
            attribute,
            childFragmentManager
        )
        return binding.root
    }

    override fun show(manager: FragmentManager, tag: String?) {
        "on show s before called".logVasi()
        super.show(manager, tag)
        "on show s after called".logVasi()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents(){
        initBottomSheetCallback()
        this.dialog?.let { setWindowSettings(it) }
        setHeightOfSheet(binding.root, (ScreenUtils.getScreenHeight(requireActivity())/3))
    }




    private fun setWindowSettings(dialog: Dialog) {
        val window = dialog.window
        if (window != null) {
            val layoutParams = window.attributes
            layoutParams.dimAmount = 0.5f
            layoutParams.windowAnimations = R.style.DialogAnimationSlideUpSlideDown
            window.attributes = layoutParams
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }


    private fun initBottomSheetCallback() {
        bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        dismiss()
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.v("Vasi testing","onslide...${slideOffset}...${bottomSheetBehaviour?.peekHeight}")
            }
        }
    }

    private fun setHeightOfSheet(contentView: View, bottomSheetTopMargin: Int?) {
        var topMargin = bottomSheetTopMargin
        if (topMargin == null) {
            topMargin = 20
        }

        val screenHeight = ScreenUtils.getScreenHeight(requireActivity()) - topMargin
        val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior
        params.height = screenHeight
        (contentView.parent as View).layoutParams = params
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(bottomSheetCallback)
            behavior.peekHeight = screenHeight
            bottomSheetBehaviour = behavior
        }
    }
}