package com.yprodan.game.arch.ext

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(direction: NavDirections){
    findNavController().navigate(direction)
}

