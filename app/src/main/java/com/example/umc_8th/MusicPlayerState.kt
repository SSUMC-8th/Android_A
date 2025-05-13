object MusicPlayerState {
    var isPlaying: Boolean = false
        private set

    var currentSongId: Int? = null  // Long에서 Int로 변경
        private set

    var progress: Int = 0
        private set

    private val playListeners = mutableListOf<(Boolean) -> Unit>()
    private val progressListeners = mutableListOf<(Int) -> Unit>()
    private val songChangeListeners = mutableListOf<(Int?) -> Unit>()  // Long에서 Int로 변경

    private var progressThread: Thread? = null

    // ✅ 외부에서 현재 곡 ID를 설정할 수 있는 메서드 추가
    fun setCurrentSongId(id: Int) {  // Long에서 Int로 변경
        currentSongId = id
        notifySongChangeListeners()
    }

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

    fun playNewSong(songId: Int) {  // Long에서 Int로 변경
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

    fun addSongChangeListener(listener: (Int?) -> Unit) {  // Long에서 Int로 변경
        songChangeListeners.add(listener)
        listener(currentSongId)
    }

    fun removeSongChangeListener(listener: (Int?) -> Unit) {  // Long에서 Int로 변경
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
