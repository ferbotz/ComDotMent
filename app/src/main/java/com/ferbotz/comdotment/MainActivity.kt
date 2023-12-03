package com.ferbotz.comdotment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.ferbotz.comments.databinding.EmptyVhBinding
import com.ferbotz.comments.databinding.GifCommentLayoutBinding
import com.ferbotz.comments.databinding.TextCommentLayoutBinding
import com.ferbotz.comments.interfaces.DefaultCommentViewOverridingListener
import com.ferbotz.comments.interfaces.EmptyViewHolderBindListener
import com.ferbotz.comments.interfaces.EssentialCommentActionListener
import com.ferbotz.comments.modals.*
import com.ferbotz.comments.ui.CommentsBottomSheet
import com.ferbotz.comments.utils.ScreenUtils.logVasi
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

//    val bottomSheet = CommentsBottomSheet(
//        UserProfile(
//            "user1",
//            userName = "vasi"
//        ),
//        postId = "12345",
//        CommentsBottomSheet.CommentBottomSheetBuilder(UserProfile(
//            "user1",
//            userName = "vasi"
//        ),
//            postId = "12345")
//    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.chumaBtn)
        btn.setOnClickListener {
            val eActListener = object : EssentialCommentActionListener{
                override suspend fun addNewComment(
                    newComment: Comment,
                    commentsDataAccessFunctions: CommentsDataAccessFunctions
                ): Comment {
                    "new comment listener called".logVasi()
                    return newComment
                }

                override suspend fun addNewReply(
                    newReply: Reply,
                    commentsDataAccessFunctions: CommentsDataAccessFunctions
                ): Reply {
                    "new reply listener called".logVasi()
                    return newReply
                }

                override suspend fun onLikeComment(
                    isLiked: Boolean,
                    likedComment: Comment,
                    commentsDataAccessFunctions: CommentsDataAccessFunctions
                ): Comment {
                    return likedComment
                }

                override suspend fun onLikeReply(
                    isLiked: Boolean,
                    likedReply: Reply,
                    parentComment: Comment,
                    commentsDataAccessFunctions: CommentsDataAccessFunctions
                ): Reply {
                    return likedReply
                }


            }


            val btmSheet = CommentsBottomSheet(
                CommentsViewAttribute.CommentsViewAttributeBuilder(
                    UserProfile("user1", userName = "vasi"),
                    postId = "12345",
                    eActListener
                ).addEmptyViewsWithOffset({ it % 2 == 0 },
                    object : EmptyViewHolderBindListener {
                        override fun emptyViewHolderBind(
                            emptyViewHolderData: Any?,
                            emptyView: EmptyVhBinding,
                            position: Int
                        ) {
                            emptyView.emptyRoot.background = ColorDrawable(Color.parseColor("#00FF00"))
                            emptyView.emptyRoot.setOnClickListener {
                                emptyView.emptyRoot.background = ColorDrawable(Color.parseColor("#FF6600"))
                                Toast.makeText(this@MainActivity, "empty view clicked", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                ).build()
            )
            btmSheet.show(supportFragmentManager, "comments")
        }
    }

    override fun onStop() {
        super.onStop()
    }
}