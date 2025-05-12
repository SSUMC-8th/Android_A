package com.example.umc_8th

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.umc_8th.database.SongDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SongViewModel(application: Application) : AndroidViewModel(application) {

    private val songDatabase = SongDatabase.getDatabase(application)
    val isLiked = MutableLiveData<Boolean>()

    // Song 데이터를 로딩하고, 좋아요 상태를 업데이트
    fun loadSongData(songId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val song = songDatabase.songDao().getSongById(songId)
            isLiked.postValue(song?.isLiked ?: false)
        }
    }

    // 좋아요 상태를 변경
    fun toggleLikeStatus(songId: Int) {
        val newIsLiked = !(isLiked.value ?: false)
        isLiked.postValue(newIsLiked)
        updateIsLikedInDatabase(songId, newIsLiked)
    }

    // 데이터베이스에서 좋아요 상태를 업데이트
    private fun updateIsLikedInDatabase(songId: Int, isLiked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            songDatabase.songDao().updateIsLiked(songId, isLiked)
        }
    }
}
