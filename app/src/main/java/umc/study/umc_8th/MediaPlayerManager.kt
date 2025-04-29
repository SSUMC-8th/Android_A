package umc.study.umc_8th

import android.content.Context
import android.media.MediaPlayer

object MediaPlayerManager {
    var mediaPlayer: MediaPlayer? = null
    var isPlaying = false

    fun initialize(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.goodnight_song)
        }
    }

    fun play() {
        mediaPlayer?.start()
        isPlaying = true
    }

    fun pause() {
        mediaPlayer?.pause()
        isPlaying = false
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }
}
