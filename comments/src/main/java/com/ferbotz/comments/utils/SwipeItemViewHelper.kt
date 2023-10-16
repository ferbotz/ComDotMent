package com.ferbotz.comments.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ferbotz.comments.R
import com.ferbotz.comments.adapters.CommentsRecyclerViewAdapter
import com.ferbotz.comments.viewholder.EmptyViewHolder
import java.lang.Math.abs
import java.lang.Math.min


class SwipeItemViewHelper(private val adapter: CommentsRecyclerViewAdapter, private val recyclerView: RecyclerView) :
    ItemTouchHelper.Callback() {

    private val SWIPE_THRESHOLD = 0.4f // Set the swipe threshold to 25%
    private var isSwipeBack = false


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (viewHolder is EmptyViewHolder){
            return makeMovementFlags(0, 0)
        }
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun getAnimationDuration(
        recyclerView: RecyclerView,
        animationType: Int,
        animateDx: Float,
        animateDy: Float
    ): Long {
        return super.getAnimationDuration(recyclerView,  animationType, animateDx, animateDy)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Handle the custom action when an item is swiped
        val position = viewHolder.adapterPosition
        Toast.makeText(viewHolder.itemView.context, "hellooo...${position}..${direction} ", Toast.LENGTH_SHORT).show()

        if (direction == ItemTouchHelper.RIGHT){
            val animationDuration = 100L // Adjust the duration as needed
            val itemView = viewHolder.itemView
            val animator = ObjectAnimator.ofFloat(itemView, "translationX", itemView.translationX, 0f)
            animator.duration = animationDuration
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    isSwipeBack = false
                    viewHolder.itemView.translationX = 0F
                    adapter.notifyItemChanged(position)
                }
            })
            animator.start()
        }
        else if (direction == ItemTouchHelper.LEFT){

        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        Log.v("Vasi testing","onchild draw called...${actionState}...${isCurrentlyActive}..${dX}")
        val itemView = viewHolder.itemView
        val swipeFraction = Math.abs(dX) / itemView.width.toFloat()
        val maxSwipeDistance = itemView.width * SWIPE_THRESHOLD
        val clampedDx = dX.coerceIn(-maxSwipeDistance, maxSwipeDistance)
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isCurrentlyActive) {

            if (swipeFraction >= SWIPE_THRESHOLD) {
                isSwipeBack = true
            }
            if (clampedDx > 0f){
                val colorDrawable = ColorDrawable(Color.CYAN)
                colorDrawable.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
                colorDrawable.draw(c)
                if (dX > 0.05 * itemView.width){
                    val iconRad = ScreenUtils.dpToPx(10f, viewHolder.itemView.context)
                    val progRad = ScreenUtils.dpToPx(10f, viewHolder.itemView.context) + 20
                    val arcPaint = Paint()
                    arcPaint.color = Color.BLUE
                    arcPaint.style = Paint.Style.STROKE
                    arcPaint.strokeWidth = 10f
                    arcPaint.isAntiAlias = true
                    if (dX < 0.2 * itemView.width){
                        val icon = itemView.context.resources.getDrawable(R.drawable.reply_icon)
                        icon.setBounds(((clampedDx - (0.1 * itemView.width)) - (iconRad)).toInt(), ((itemView.height/2) - iconRad), ((clampedDx - (0.1 * itemView.width)) + (iconRad)).toInt() ,(itemView.height/2) + iconRad)
                        icon.draw(c)
                        val rectF = RectF()
                        rectF.set(((clampedDx - (0.1 * itemView.width)) - progRad).toFloat(), ((itemView.height/2) - progRad).toFloat(), ((clampedDx - (0.1 * itemView.width)) + progRad).toFloat(), ((itemView.height/2) + progRad).toFloat());
                        c.drawArc(rectF, -90f, (360 * dX/(itemView.width * SWIPE_THRESHOLD)), false, arcPaint);
                    }
                    else{
                        val icon = itemView.context.resources.getDrawable(R.drawable.reply_icon)
                        icon.setBounds(((clampedDx/2) - (iconRad)).toInt(), ((itemView.height/2) - iconRad), ((clampedDx/2) + (iconRad)).toInt() , (itemView.height/2) + iconRad)
                        icon.draw(c)
                        val rectF = RectF()
                        rectF.set(((clampedDx/2) - progRad), ((itemView.height/2) - progRad).toFloat(), ((clampedDx/2) + progRad), ((itemView.height/2) + progRad).toFloat())
                        c.drawArc(rectF, -90f, (360 * dX/(itemView.width * SWIPE_THRESHOLD)) , false, arcPaint);
                    }
                }
            }
            else{
//                val colorDrawable = ColorDrawable(Color.MAGENTA)
//                colorDrawable.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
//                colorDrawable.draw(c)

                val totalWidthAvailable = (itemView.width * SWIPE_THRESHOLD) - ScreenUtils.dpToPx(4f, itemView.context)
                val eachButtonWidth = totalWidthAvailable/3
                val top = (ScreenUtils.dpToPx(3f, itemView.context)).toFloat()
                val bottom = (itemView.height - top)
                val cornerRadius = ScreenUtils.dpToPx(24f, itemView.context).toFloat()
                val end = clampedDx + itemView.width + (itemView.width * SWIPE_THRESHOLD)


                val cancelBtnPath = drawRoundedBox(
                    Path(),
                    end - ScreenUtils.dpToPx(4f, itemView.context) - (eachButtonWidth + cornerRadius),
                    (end - ScreenUtils.dpToPx(4f, itemView.context)).toFloat(),
                    top,
                    bottom,
                    cornerRadius
                )
                val cancelBoxPaint = Paint()
                cancelBoxPaint.color = Color.parseColor("#FF0000")
                c.drawPath(cancelBtnPath, cancelBoxPaint)



                val reportBtnPath = drawRoundedBox(
                    Path(),
                    end - ScreenUtils.dpToPx(4f, itemView.context) - (eachButtonWidth * 2) - cornerRadius ,
                    end - ScreenUtils.dpToPx(4f, itemView.context) - eachButtonWidth ,
                    top,
                    bottom,
                    cornerRadius
                )
                val reportBoxPaint = Paint()
                reportBoxPaint.color = Color.parseColor("#00FF00")
                c.drawPath(reportBtnPath, reportBoxPaint)


                val shareBtnPath = drawRoundedBox(
                    Path(),
                    end - ScreenUtils.dpToPx(4f, itemView.context) - (eachButtonWidth * 3) - cornerRadius ,
                    end - ScreenUtils.dpToPx(4f, itemView.context) - (eachButtonWidth * 2) ,
                    top,
                    bottom,
                    cornerRadius
                )
                val shareBoxPaint = Paint()
                reportBoxPaint.color = Color.parseColor("#0000FF")
                c.drawPath(shareBtnPath, shareBoxPaint)

            }
            super.onChildDraw(c, recyclerView, viewHolder, clampedDx, dY, actionState, isCurrentlyActive)
        }
        else{
            if (!isCurrentlyActive && dX < 0f){
//                val colorDrawable = ColorDrawable(Color.MAGENTA)
//                colorDrawable.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
//                colorDrawable.draw(c)

                val totalWidthAvailable = (itemView.width * SWIPE_THRESHOLD) - ScreenUtils.dpToPx(4f, itemView.context)
                val eachButtonWidth = totalWidthAvailable/3
                val top = (ScreenUtils.dpToPx(3f, itemView.context)).toFloat()
                val bottom = (itemView.height - top)
                val cornerRadius = ScreenUtils.dpToPx(24f, itemView.context).toFloat()
                val end = clampedDx + itemView.width + (itemView.width * SWIPE_THRESHOLD)


                val cancelBtnPath = drawRoundedBox(
                    Path(),
                    end - ScreenUtils.dpToPx(4f, itemView.context) - (eachButtonWidth + cornerRadius),
                    (end - ScreenUtils.dpToPx(4f, itemView.context)).toFloat(),
                    top,
                    bottom,
                    cornerRadius
                )
                val cancelBoxPaint = Paint()
                cancelBoxPaint.color = Color.parseColor("#FF0000")
                c.drawPath(cancelBtnPath, cancelBoxPaint)



                val reportBtnPath = drawRoundedBox(
                    Path(),
                    end - ScreenUtils.dpToPx(4f, itemView.context) - (eachButtonWidth * 2) - cornerRadius ,
                    end - ScreenUtils.dpToPx(4f, itemView.context) - eachButtonWidth ,
                    top,
                    bottom,
                    cornerRadius
                )
                val reportBoxPaint = Paint()
                reportBoxPaint.color = Color.parseColor("#00FF00")
                c.drawPath(reportBtnPath, reportBoxPaint)


                val shareBtnPath = drawRoundedBox(
                    Path(),
                    end - ScreenUtils.dpToPx(4f, itemView.context) - (eachButtonWidth * 3) - cornerRadius ,
                    end - ScreenUtils.dpToPx(4f, itemView.context) - (eachButtonWidth * 2) ,
                    top,
                    bottom,
                    cornerRadius
                )
                val shareBoxPaint = Paint()
                reportBoxPaint.color = Color.parseColor("#0000FF")
                c.drawPath(shareBtnPath, shareBoxPaint)

            }
            super.onChildDraw(c, recyclerView, viewHolder, clampedDx, dY, actionState, isCurrentlyActive)
            Log.v("Vasi testing","on child draw else...${actionState}...${isCurrentlyActive}..${dX}")
        }
    }

    private fun drawRoundedBox(path:Path, left:Float, right:Float, top:Float, bottom:Float, cornerRadius:Float):Path{
        path.moveTo(left, top)
        path.lineTo(right - cornerRadius, top)
        path.arcTo(right - 2 * cornerRadius, top, right, top + 2 * cornerRadius, -90f, 90f, false)
        path.lineTo(right, bottom - cornerRadius)
        path.arcTo(right - 2 * cornerRadius, bottom - 2 * cornerRadius, right, bottom, 0f, 90f, false)
        path.lineTo(left, bottom)
        path.lineTo(left, top)
        return path
    }
}