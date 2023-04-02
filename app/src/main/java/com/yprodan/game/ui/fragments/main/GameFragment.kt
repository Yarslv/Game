package com.yprodan.game.ui.fragments.main

import com.yprodan.game.R
import com.yprodan.game.arch.ext.navigate
import com.yprodan.game.databinding.FragmentGameBinding
import com.yprodan.player.arch.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameFragment : BaseFragment<FragmentGameBinding>(R.layout.fragment_game) {
    override val viewModel: GameFragmentViewModel by viewModel()

    override fun setObservers() {
        binding.model = viewModel
        viewModel.navigateEvent.observe(viewLifecycleOwner) {
            viewModel.scoreText.value?.let { score ->
                navigate(
                    GameFragmentDirections.actionMainFragmentToResultFragment(
                        score
                    )
                )
            }
        }
    }
}