import android.util.Log

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

    // 재생/일시 정지 상태를 전환
    fun togglePlay() {
        isPlaying = !isPlaying
        notifyPlayListeners()
        if (isPlaying) startProgressThread()
    }

    // 주어진 값으로 재생 상태를 설정
    fun setPlayState(playing: Boolean) {
        isPlaying = playing
        notifyPlayListeners()
        if (isPlaying) startProgressThread()
    }

    private fun startProgressThread() {
        if (progressThread == null || !progressThread!!.isAlive) {
            progressThread = Thread {
                try {
                    while (progress <= 100) {
                        if (!isPlaying) break
                        Thread.sleep(100)  // InterruptedException을 처리할 부분
                        progress++
                        notifyProgressListeners()
                    }
                } catch (e: InterruptedException) {
                    // 쓰레드가 중단되었을 때의 처리
                    Log.d("MusicPlayerState", "Progress thread interrupted")
                }
            }
            progressThread!!.start()
        }
    }


//    private fun startProgressThread() {
//        if (progressThread == null || !progressThread!!.isAlive) {
//            progressThread = Thread {
//                while (progress <= 100) {
//                    if (!isPlaying) break
//                    Thread.sleep(100)
//                    progress++
//                    notifyProgressListeners()
//                }
//            }
//            progressThread!!.start()
//        }
//    }

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


    private fun notifyPlayListeners() {
        playListeners.forEach { it(isPlaying) }
    }

    private fun notifyProgressListeners() {
        progressListeners.forEach { it(progress) }
    }

    private fun notifySongChangeListeners() {
        songChangeListeners.forEach { it(currentSongId) }
    }

    // 기존 스레드를 중지하는 함수
    fun stopProgressThread() {
        progressThread?.interrupt()
        progressThread = null
    }

    // 진행 상태를 0으로 초기화하고 새로 스레드를 시작하는 함수
    fun restartProgressThread() {
        stopProgressThread()
        progress = 0
        notifyProgressListeners()
        startProgressThread()
    }

    fun playNewSong(songId: Int) {
        currentSongId = songId
        notifySongChangeListeners()
        restartProgressThread()  // 진행률 초기화하고 새로 시작
        setPlayState(true)
    }


}
