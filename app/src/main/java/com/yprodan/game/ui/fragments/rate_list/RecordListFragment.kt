package com.yprodan.game.ui.fragments.rate_list

import com.yprodan.game.R
import com.yprodan.game.databinding.FragmentRecordListBinding
import com.yprodan.player.arch.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecordListFragment : BaseFragment<FragmentRecordListBinding>(R.layout.fragment_record_list) {
    override val viewModel: RecordListFragmentViewModel by viewModel()

    override fun setObservers() {
        binding.viewModel = viewModel
    }
}