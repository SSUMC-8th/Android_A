package com.example.umc_8th
// 파일: MusicPlayerState.kt

object MusicPlayerState {
    var isPlaying: Boolean = false
        private set

    private val listeners = mutableListOf<(Boolean) -> Unit>()

    fun togglePlay() {
        isPlaying = !isPlaying
        notifyListeners()
    }

    fun setPlayState(playing: Boolean) {
        isPlaying = playing
        notifyListeners()
    }

    private fun notifyListeners() {
        listeners.forEach { it(isPlaying) }
    }

    fun addListener(listener: (Boolean) -> Unit) {
        listeners.add(listener)
        listener(isPlaying) // 초기 상태도 반영
    }

    fun removeListener(listener: (Boolean) -> Unit) {
        listeners.remove(listener)
    }
}

