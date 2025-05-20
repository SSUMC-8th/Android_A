package com.example.umc_8th

import android.content.Context

object SharedPreferencesHelper {
    private const val PREF_NAME = "umc_prefs"
    private const val KEY_USER_IDX = "id" // key는 "id"로 저장

    fun saveUserIdx(context: Context, userIdx: String?) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USER_IDX, userIdx).apply()
    }

    fun getUserIdx(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_USER_IDX, -1) // 없을 경우 -1 반환
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply()
    }
}