package com.yprodan.game.domain.use_case

import com.yprodan.game.data.repository.SharedPreferencesRepository
import com.yprodan.game.ui.fragments.rate_list.adapter.RateListModel

class RecordListUseCase(private val sharedPreferencesStorage: SharedPreferencesRepository) {

    fun getRecords(): ArrayList<RateListModel> {
        return sharedPreferencesStorage.getRecordList()
    }
}