package com.yprodan.game.ui.fragments.rate_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yprodan.game.domain.use_case.RecordListUseCase
import com.yprodan.game.ui.fragments.rate_list.adapter.RateListModel
import com.yprodan.player.arch.BaseViewModel

class RecordListFragmentViewModel(recordListUseCase: RecordListUseCase) :
    BaseViewModel() {

    private val _dataList = MutableLiveData<ArrayList<RateListModel>>()
    val dataList: LiveData<ArrayList<RateListModel>> = _dataList

    init {
        _dataList.postValue(recordListUseCase.getRecords())
    }
}