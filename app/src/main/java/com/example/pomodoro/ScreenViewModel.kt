package com.example.pomodoro

import androidx.compose.runtime.currentCompositionErrors
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ScreenViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ScreenUiState())
    val uiState: StateFlow<ScreenUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun run() {
        if (uiState.value.seconds == 0 && uiState.value.minutes == 0) {
            _uiState.update { currentState ->
                currentState.copy(
                    isOver = true,
                    isRunning = false
                )
            }
        }
        if (uiState.value.isRunning) {
            timerJob?.cancel()
            timerJob = viewModelScope.launch {
                delay(1000)
                _uiState.update { currentState ->
                    currentState.copy(
                        uiState.value.seconds.dec()
                    )
                }
            }
        } else timerJob?.cancel()

        if (uiState.value.seconds == -1) {
            _uiState.update { currentState ->
                currentState.copy(
                    seconds = 59,
                    minutes = currentState.minutes.dec()
                )
            }
        }
    }

    fun start() {
        _uiState.update { currentState ->
            currentState.copy(
                isRunning = true
            )
        }
    }

    fun stop() {
        _uiState.update { currentState ->
            currentState.copy(
                isRunning = false
            )
        }
    }

    fun reset() {
        _uiState.value = ScreenUiState()
    }

    fun breakTime() {
        reset()
        _uiState.update { currentState ->
            currentState.copy(
                minutes = currentState.breakMinutes,
                isRunning = true,
                isBreak = true
            )
        }
    }

    fun longTimer() {
        if (!uiState.value.isLongTimer) {
            _uiState.update { currentState ->
                currentState.copy(
                    minutes = 50,
                    breakMinutes = 10,
                    isLongTimer = true
                )
            }
        } else reset()
    }
}