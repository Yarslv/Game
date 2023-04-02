package com.yprodan.game.data.repository

import android.content.Context
import com.yprodan.game.Constants
import com.yprodan.game.R
import com.yprodan.game.ui.fragments.rate_list.adapter.RateListModel

interface SharedPreferencesRepository {
    fun getScore(): Set<Int>
    fun getRecordList(): ArrayList<RateListModel>
    fun getMaxScore(): Int
    fun setScore(newScore: Int)
    fun getColor(): Int
    fun updateColor()
}

class SharedPreferencesRepositoryImpl(private val context: Context) : SharedPreferencesRepository {

    private val pref = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME_TAG, Context.MODE_PRIVATE)
    private val editor = pref.edit()


    override fun getScore(): Set<Int> {
        val oldSet = pref.getStringSet(Constants.SCORE_SET_TAG, setOf("0")) ?: setOf("0")
        return oldSet.map { it.toInt() }.toSet()
    }

    override fun getRecordList(): ArrayList<RateListModel> {
        val scores = getScore().sortedDescending()
        val result = scores.mapIndexed { index, i -> RateListModel(index + 1, i) }
        return result as ArrayList
    }

    override fun getMaxScore(): Int {
        val scores = getScore()
        return scores.max()
    }

    override fun setScore(newScore: Int) {
        val score = getScore().toMutableSet()
        val max = getMaxScore()

        score.drop(max)
        score.add(newScore)

        editor.remove(Constants.SCORE_SET_TAG)
        editor.putStringSet(Constants.SCORE_SET_TAG, score.map { it.toString() }.toSet()).commit()
    }

    override fun getColor(): Int {
        return pref.getInt(Constants.BACKGROUND_COLOR_TAG, (0xff13131d).toInt())
    }

    override fun updateColor() {
        val colorArr = context.resources.getIntArray(R.array.game_background_colors)
        val random = (colorArr.indices).random()
        editor.putInt(Constants.BACKGROUND_COLOR_TAG, colorArr[random]).commit()
    }
}