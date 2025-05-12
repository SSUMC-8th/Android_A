package com.example.umc_8th.flo_project

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.SnackbarBinding
import com.google.android.material.snackbar.Snackbar

class Snackbar(view: View, private val message: String){
       companion object{
           fun make(view: View, message: String) = Snackbar(view, message)
       }
    private val context = view.context
    private val snackbar = Snackbar.make(view, "", 5000)
    @SuppressLint("RestrictedApi")
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    private val inflater = LayoutInflater.from(context)
    private val snackbarBinding: SnackbarBinding = DataBindingUtil.inflate(inflater, R.layout.snackbar, null, false)

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackbarLayout) {
            removeAllViews()
            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackbarBinding.root, 0)
        }
    }

    private fun initData() {
        snackbarBinding.snackbarTv.text = message
        snackbarBinding.snackbarBtn.setOnClickListener {
        }
    }

    fun show() {
        snackbar.show()
    }




}