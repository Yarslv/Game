package com.yprodan.game.arch.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yprodan.player.arch.adapter.AbstractDiffCallback

abstract class BaseRecyclerAdapter<M, VH : BindableViewHolder<M>>(
    private var adapterItems: ArrayList<M>
) : RecyclerView.Adapter<VH>() {

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(adapterItems[position])
    }

    override fun getItemCount() = adapterItems.size

    fun setContent(items: List<M>) {
        val diffResult = DiffUtil.calculateDiff(getDiffCallback(items))
        adapterItems.clear()
        adapterItems.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun getDiffCallback(newItems: List<M>): DiffUtil.Callback {
        return AbstractDiffCallback(newItems, adapterItems)
    }
}