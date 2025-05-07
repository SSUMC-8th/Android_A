// com.example.umc_8th.viewmodel.SavedViewModel.kt

package com.example.umc_8th.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.umc_8th.R
import com.example.umc_8th.SavedData

class SavedViewModel : ViewModel() {

    private val _savedList = MutableLiveData<MutableList<SavedData>>().apply {
        value = mutableListOf(
            SavedData(
                savedImg = R.drawable.img_album_lovewinsall,
                savedName = "love wins all",
                savedArtist = "아이유"
            ),
            SavedData(
                savedImg = R.drawable.img_album_supernova,
                savedName = "supernova",
                savedArtist = "aespa"
            ),
            SavedData(
                savedImg = R.drawable.img_album_drama,
                savedName = "drama",
                savedArtist = "aespa"
            ),
            SavedData(
                savedImg = R.drawable.img_album_exp3,
                savedName = "next level",
                savedArtist = "aespa"
            ),
            SavedData(
                savedImg = R.drawable.img_album_exp6,
                savedName = "weekend",
                savedArtist = "태연"
            ),
            SavedData(
                savedImg = R.drawable.img_album_lovewinsall,
                savedName = "love wins all",
                savedArtist = "아이유"
            ),
            SavedData(
                savedImg = R.drawable.img_album_supernova,
                savedName = "supernova",
                savedArtist = "aespa"
            ),
            SavedData(
                savedImg = R.drawable.img_album_drama,
                savedName = "drama",
                savedArtist = "aespa"
            ),
            SavedData(
                savedImg = R.drawable.img_album_exp3,
                savedName = "next level",
                savedArtist = "aespa"
            ),
            SavedData(
                savedImg = R.drawable.img_album_exp6,
                savedName = "weekend",
                savedArtist = "태연"
            )
        )
    }

    val savedList: LiveData<MutableList<SavedData>> = _savedList

//    fun removeItem(position: Int) {
//        _savedList.value?.let {
//            if (position in it.indices) {
//                it.removeAt(position)
//                _savedList.value = it.toMutableList() // 트리거를 위해 새 리스트로 할당
//            }
//        }
//    }
    fun removeItem(position: Int) {
        val updatedList = _savedList.value?.toMutableList()
        updatedList?.removeAt(position)
        _savedList.value = updatedList
    }

}
