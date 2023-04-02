package com.yprodan.game.ui.fragments.rate_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.yprodan.game.arch.adapter.BaseRecyclerAdapter
import com.yprodan.game.arch.adapter.BindableViewHolder
import com.yprodan.game.databinding.ItemRecordListBinding

class RateListAdapter(adapterItems: ArrayList<RateListModel>) :
    BaseRecyclerAdapter<RateListModel, RateListViewHolder>(adapterItems) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateListViewHolder {
        return RateListViewHolder.from(parent)
    }
}

class RateListViewHolder private constructor(private val binding: ItemRecordListBinding) :
    BindableViewHolder<RateListModel>(binding.root) {
    override fun bind(model: RateListModel) {
        binding.model = model
    }

    companion object {
        fun from(parent: ViewGroup): RateListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                ItemRecordListBinding.inflate(layoutInflater, parent, false)
            return RateListViewHolder(binding)
        }
    }
}

data class RateListModel(val id: Int, val score: Int)