package com.yprodan.game.ui.fragments.start

import com.yprodan.game.arch.event.SingleLiveEvent
import com.yprodan.player.arch.BaseViewModel

class StartFragmentViewModel: BaseViewModel() {

    val navigationEvent = SingleLiveEvent<StartFragmentDestination>()

    fun startClick(){
        navigationEvent.postValue(StartFragmentDestination.Game)
    }
}
enum class StartFragmentDestination { Game }