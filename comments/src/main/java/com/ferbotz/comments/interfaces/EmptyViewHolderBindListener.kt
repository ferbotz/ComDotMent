package com.ferbotz.comments.interfaces

import com.ferbotz.comments.databinding.EmptyVhBinding

interface EmptyViewHolderBindListener {
    fun emptyViewHolderBind(emptyViewHolderData: Any?, emptyView: EmptyVhBinding, position:Int)
}