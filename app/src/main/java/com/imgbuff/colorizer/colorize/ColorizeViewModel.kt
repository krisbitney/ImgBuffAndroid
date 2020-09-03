package com.imgbuff.colorizer.colorize

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imgbuff.colorizer.domain.ColorizedImage
import com.imgbuff.colorizer.network.ImageApi
import com.imgbuff.colorizer.network.NetworkImage
import com.imgbuff.colorizer.network.asDomainModel
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

enum class HttpRequestStatus {
    LOADING, ERROR, DONE
}

class ColorizeViewModel: ViewModel() {

    // coroutines
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // stores status of most recent HTTP request
    private val _status = MutableLiveData<HttpRequestStatus>()
    val status: LiveData<HttpRequestStatus> get() = _status

    // colorized image
    private val _colorizedImage = MutableLiveData<ColorizedImage?>()
    val colorizedImage: LiveData<ColorizedImage?> get() = _colorizedImage

    // colorize image
    fun colorize(context: Context?, uri: Uri?) {
        if (uri == null) return
        _status.value = HttpRequestStatus.LOADING
        uiScope.launch {
            // get input image data on io thread
            val inputStream: InputStream? = withContext(Dispatchers.IO) {
                context?.contentResolver?.openInputStream(uri)?.use {
                    it.toByteArrayInputStream()
                }
            }
            // TODO: database insert should only happen when user chooses to save image
            if (inputStream == null) return@launch
            // start http request -> server hosts neural network used to colorize images
            val deferredNetworkImage: Deferred<NetworkImage> = ImageApi.colorizeAsync(inputStream)
            try {
                _colorizedImage.value = deferredNetworkImage.await().asDomainModel()
                _status.value = HttpRequestStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _colorizedImage.value = null
                _status.value = HttpRequestStatus.ERROR
            }
        }
    }

    private fun InputStream.toByteArrayInputStream(): ByteArrayInputStream {
        val outputStream = ByteArrayOutputStream()
        this.copyTo(outputStream)
        return ByteArrayInputStream(outputStream.toByteArray())
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}