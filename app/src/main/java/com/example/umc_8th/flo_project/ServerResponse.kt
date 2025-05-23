package com.example.umc_8th.flo_project

import com.google.gson.annotations.SerializedName

data class ServerResponse (
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Timestamp
)

data class Timestamp(
    val memberId: Int,
    val createdAt: String,
    val updatedAt: String,
)