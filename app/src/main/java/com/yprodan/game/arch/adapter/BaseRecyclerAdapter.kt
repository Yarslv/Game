package com.yprodan.player.arch.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<M, VH : BindableViewHolder<M>>(
    private var adapterItems: ArrayList<M>,
    private val callback: (M) -> Unit
) : RecyclerView.Adapter<VH>() {

    private var currentComposition = -1

    fun getCurrent() = adapterItems[currentComposition]

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(adapterItems[position])
        holder.itemView.setOnClickListener {
            currentComposition = holder.adapterPosition
            callback.invoke(adapterItems[position])
        }
    }

    override fun getItemCount() = adapterItems.size

    fun setContent(items: List<M>) {
        val diffResult = DiffUtil.calculateDiff(getDiffCallback(items))
        adapterItems.clear()
        adapterItems.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getNext() {
        when (currentComposition) {
            adapterItems.lastIndex -> {
                currentComposition = 0
                callback.invoke(adapterItems[currentComposition])
            }
            -1 -> {}
            else -> {
                currentComposition++
                return callback.invoke(adapterItems[currentComposition])
            }
        }
    }

    fun getPrevious() {
        when (currentComposition) {
            0 -> {
                currentComposition = adapterItems.lastIndex
                callback(adapterItems[currentComposition])
            }
            -1 -> {}
            else -> {
                currentComposition--
                callback(adapterItems[currentComposition])
            }
        }
    }

    private fun getDiffCallback(newItems: List<M>): DiffUtil.Callback {
        return AbstractDiffCallback(newItems, adapterItems)
    }
}