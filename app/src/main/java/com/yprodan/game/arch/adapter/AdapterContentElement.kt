package com.yprodan.player.arch.adapter

interface AdapterContentElement {

    fun areContentsTheSame(other: AdapterContentElement): Boolean
}