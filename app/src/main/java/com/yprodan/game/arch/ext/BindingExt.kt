package com.yprodan.game.arch.ext

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yprodan.game.ui.custom.GameView
import com.yprodan.game.ui.fragments.rate_list.adapter.RateListAdapter
import com.yprodan.game.ui.fragments.rate_list.adapter.RateListModel
import com.yprodan.game.ui.fragments.result.ScoreText

@BindingAdapter("app:setScoreText")
fun AppCompatTextView.setScoreText(scoreText: ScoreText?) {
    scoreText?.let {
        text = when (it) {
            ScoreText.Lol -> "Lol"
            ScoreText.Good -> "Good!"
            ScoreText.Almost -> "Almost"
            ScoreText.NewBest -> "New best!"
        }
    }
}

@BindingAdapter("app:setBGColor")
fun GameView.setBGColor(color: Int?){
    color?.let {
        updateBG(it)
    }
}
@BindingAdapter("app:setItems")
fun RecyclerView.setItems(list: ArrayList<RateListModel>?){
    list?.let {
        if (adapter == null) adapter = RateListAdapter(it)
        else (adapter as RateListAdapter).setContent(it)
    }
}