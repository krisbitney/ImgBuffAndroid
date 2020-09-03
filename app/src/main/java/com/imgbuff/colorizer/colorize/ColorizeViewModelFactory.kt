package com.imgbuff.colorizer.colorize

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ColorizeViewModelFactory: ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ColorizeViewModel::class.java)) {
            return ColorizeViewModel() as T
        }
        throw ClassCastException("Cannot cast ColorizeViewModel to ${modelClass.toString()}")
    }
}