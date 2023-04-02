package com.yprodan.game.ui.fragments.start

import com.yprodan.game.R
import com.yprodan.game.arch.ext.navigate
import com.yprodan.game.databinding.FragmentStartBinding
import com.yprodan.player.arch.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartFragment : BaseFragment<FragmentStartBinding>(R.layout.fragment_start) {
    override val viewModel: StartFragmentViewModel by viewModel()

    override fun setObservers() {
        binding.viewModel = viewModel
        viewModel.navigationEvent.observe(viewLifecycleOwner) {
            when (it) {
                StartFragmentDestination.Game -> navigate(StartFragmentDirections.actionStartFragmentToMainFragment())
            }
        }
    }
}