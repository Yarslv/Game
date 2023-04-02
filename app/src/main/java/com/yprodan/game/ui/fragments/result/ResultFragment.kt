package com.yprodan.game.ui.fragments.result

import androidx.navigation.fragment.navArgs
import com.yprodan.game.R
import com.yprodan.game.arch.ext.navigate
import com.yprodan.game.databinding.FragmentResultBinding
import com.yprodan.player.arch.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResultFragment : BaseFragment<FragmentResultBinding>(R.layout.fragment_result) {
    private val args: ResultFragmentArgs by navArgs()
    override val viewModel: ResultFragmentViewModel by viewModel()
    override fun setObservers() {
        binding.viewModel = viewModel
        viewModel.setScore(args.score)

        viewModel.navigateEvent.observe(viewLifecycleOwner) {
            when (it) {
                ResultFragmentDestination.Game -> navigate(
                    ResultFragmentDirections.actionResultFragmentToMainFragment()
                )
                ResultFragmentDestination.Rate -> navigate(
                    ResultFragmentDirections.actionResultFragmentToRecordListFragment()
                )
            }
        }
    }
}

