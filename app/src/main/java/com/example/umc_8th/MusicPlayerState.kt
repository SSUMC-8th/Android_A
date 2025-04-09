package com.example.umc_8th

object MusicPlayerState {
    var isPlaying: Boolean = false
        private set

    private val playListeners = mutableListOf<(Boolean) -> Unit>()

    var progress: Int = 0
        private set
    private val progressListeners = mutableListOf<(Int) -> Unit>()

    private var progressThread: Thread? = null

    fun togglePlay() {
        isPlaying = !isPlaying
        notifyPlayListeners()
        if (isPlaying) startProgressThread()
    }

    fun setPlayState(playing: Boolean) {
        isPlaying = playing
        notifyPlayListeners()
        if (isPlaying) startProgressThread()
    }

    private fun startProgressThread() {
        if (progressThread == null || !progressThread!!.isAlive) {
            progressThread = Thread {
                while (progress <= 100) {
                    if (!isPlaying) break
                    Thread.sleep(100)

                    progress++
                    notifyProgressListeners()
//                    if (progress >= 100) {
//                        isPlaying = false
//                        notifyPlayListeners()
//                        break
//                    }
                }
            }
            progressThread!!.start()
        }
    }

    fun setProgress(value: Int) {
        progress = value
        notifyProgressListeners()
    }


    private fun notifyPlayListeners() {
        playListeners.forEach { it(isPlaying) }
    }

    private fun notifyProgressListeners() {
        progressListeners.forEach { it(progress) }
    }

    fun addPlayListener(listener: (Boolean) -> Unit) {
        playListeners.add(listener)
        listener(isPlaying) // 초기 상태 반영
    }

    fun removePlayListener(listener: (Boolean) -> Unit) {
        playListeners.remove(listener)
    }

    fun addProgressListener(listener: (Int) -> Unit) {
        progressListeners.add(listener)
        listener(progress) // 초기 상태 반영
    }

    fun removeProgressListener(listener: (Int) -> Unit) {
        progressListeners.remove(listener)
    }
}

// 파일: MusicPlayerState.kt
//
//object MusicPlayerState {
//    var isPlaying: Boolean = false
//        private set
//
//    private val listeners = mutableListOf<(Boolean) -> Unit>()
//
//    fun togglePlay() {
//        isPlaying = !isPlaying
//        notifyListeners()
//    }
//
//    fun setPlayState(playing: Boolean) {
//        isPlaying = playing
//        notifyListeners()
//    }
//
//    private fun notifyListeners() {
//        listeners.forEach { it(isPlaying) }
//    }
//
//    fun addListener(listener: (Boolean) -> Unit) {
//        listeners.add(listener)
//        listener(isPlaying) // 초기 상태도 반영
//    }
//
//    fun removeListener(listener: (Boolean) -> Unit) {
//        listeners.remove(listener)
//    }
//}
//
