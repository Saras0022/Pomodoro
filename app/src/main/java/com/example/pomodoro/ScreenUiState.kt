package com.example.pomodoro

data class ScreenUiState(
    val seconds: Int = 0,
    val minutes: Int = 25,
    val isRunning: Boolean = false,
    val isOver: Boolean = false,
    val isBreak: Boolean = false,
    val breakMinutes: Int = 5,
    val isLongTimer: Boolean = false
)
