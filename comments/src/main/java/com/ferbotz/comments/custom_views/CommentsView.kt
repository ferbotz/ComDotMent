package com.ferbotz.comments.custom_views

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide.init
import com.ferbotz.comments.R
import com.ferbotz.comments.databinding.CommentsViewLayoutBinding
import com.ferbotz.comments.interfaces.DefaultCommentViewOverridingListener
import com.ferbotz.comments.interfaces.EmptyViewHolderBindListener
import com.ferbotz.comments.modals.CommentsViewAttribute
import com.ferbotz.comments.modals.UserProfile
import com.ferbotz.comments.ui.CommentsBottomSheet
import com.ferbotz.comments.ui.CommentsFragment
import com.ferbotz.comments.viewmodals.CommentViewModelFactory
import com.ferbotz.comments.viewmodals.CommentsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentsView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private var _binding: CommentsViewLayoutBinding? = null
    val binding get() = _binding!!
    lateinit var navController: NavController
    lateinit var commentsViewModel: CommentsViewModel


    init {
        Log.v("Vasi testing","comment view initilaised....")
    }


    fun buildCommentsView(activity: FragmentActivity, commentsViewAttribute: CommentsViewAttribute , childFragmentManager: FragmentManager){
        commentsViewModel = ViewModelProvider(activity, CommentViewModelFactory())[CommentsViewModel::class.java]
        commentsViewModel.commentsViewAttribute = commentsViewAttribute
        _binding =  CommentsViewLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val fragment = CommentsFragment()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()

    }

}