package com.yprodan.player.arch.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BindableViewHolder<M>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(model: M)
}