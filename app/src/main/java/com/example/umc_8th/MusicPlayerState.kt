package com.example.umc_8th

object MusicPlayerState {
    var isPlaying: Boolean = false
        private set

    var currentSongId: Long? = null
        private set

    var progress: Int = 0
        private set

    private val playListeners = mutableListOf<(Boolean) -> Unit>()
    private val progressListeners = mutableListOf<(Int) -> Unit>()
    private val songChangeListeners = mutableListOf<(Long?) -> Unit>()

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

    fun playNewSong(songId: Long) {
        // 다른 곡으로 바뀔 경우 초기화
        if (songId != currentSongId) {
            currentSongId = songId
            progress = 0
            notifySongChangeListeners()
            notifyProgressListeners()
        }

        setPlayState(true)
    }

    private fun startProgressThread() {
        if (progressThread == null || !progressThread!!.isAlive) {
            progressThread = Thread {
                while (progress <= 100) {
                    if (!isPlaying) break
                    Thread.sleep(100)
                    progress++
                    notifyProgressListeners()
                }
            }
            progressThread!!.start()
        }
    }

    fun setProgress(value: Int) {
        progress = value
        notifyProgressListeners()
    }

    fun addPlayListener(listener: (Boolean) -> Unit) {
        playListeners.add(listener)
        listener(isPlaying)
    }

    fun removePlayListener(listener: (Boolean) -> Unit) {
        playListeners.remove(listener)
    }

    fun addProgressListener(listener: (Int) -> Unit) {
        progressListeners.add(listener)
        listener(progress)
    }

    fun removeProgressListener(listener: (Int) -> Unit) {
        progressListeners.remove(listener)
    }

    fun addSongChangeListener(listener: (Long?) -> Unit) {
        songChangeListeners.add(listener)
        listener(currentSongId)
    }

    fun removeSongChangeListener(listener: (Long?) -> Unit) {
        songChangeListeners.remove(listener)
    }

    private fun notifyPlayListeners() {
        playListeners.forEach { it(isPlaying) }
    }

    private fun notifyProgressListeners() {
        progressListeners.forEach { it(progress) }
    }

    private fun notifySongChangeListeners() {
        songChangeListeners.forEach { it(currentSongId) }
    }
}

//
//object MusicPlayerState {
//    var isPlaying: Boolean = false
//        private set
//
//    private val playListeners = mutableListOf<(Boolean) -> Unit>()
//
//    var progress: Int = 0
//        private set
//    private val progressListeners = mutableListOf<(Int) -> Unit>()
//
//    private var progressThread: Thread? = null
//
//    fun togglePlay() {
//        isPlaying = !isPlaying
//        notifyPlayListeners()
//        if (isPlaying) startProgressThread()
//    }
//
//    fun setPlayState(playing: Boolean) {
//        isPlaying = playing
//        notifyPlayListeners()
//        if (isPlaying) startProgressThread()
//    }
//
//    private fun startProgressThread() {
//        if (progressThread == null || !progressThread!!.isAlive) {
//            progressThread = Thread {
//                while (progress <= 100) {
//                    if (!isPlaying) break
//                    Thread.sleep(100)
//
//                    progress++
//                    notifyProgressListeners()
////                    if (progress >= 100) {
////                        isPlaying = false
////                        notifyPlayListeners()
////                        break
////                    }
//                }
//            }
//            progressThread!!.start()
//        }
//    }
//
//    fun setProgress(value: Int) {
//        progress = value
//        notifyProgressListeners()
//    }
//
//
//    private fun notifyPlayListeners() {
//        playListeners.forEach { it(isPlaying) }
//    }
//
//    private fun notifyProgressListeners() {
//        progressListeners.forEach { it(progress) }
//    }
//
//    fun addPlayListener(listener: (Boolean) -> Unit) {
//        playListeners.add(listener)
//        listener(isPlaying) // 초기 상태 반영
//    }
//
//    fun removePlayListener(listener: (Boolean) -> Unit) {
//        playListeners.remove(listener)
//    }
//
//    fun addProgressListener(listener: (Int) -> Unit) {
//        progressListeners.add(listener)
//        listener(progress) // 초기 상태 반영
//    }
//
//    fun removeProgressListener(listener: (Int) -> Unit) {
//        progressListeners.remove(listener)
//    }
//}