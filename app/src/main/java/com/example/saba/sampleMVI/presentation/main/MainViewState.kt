package com.example.saba.sampleMVI.presentation.main


const val MAIN_VIEW_DRAW_ADDING_SCREEN_STATE: Int = 1
const val MAIN_VIEW_DRAW_RESULT_SCREEN_STATE: Int = 2

data class MainViewState(val state: Int)
