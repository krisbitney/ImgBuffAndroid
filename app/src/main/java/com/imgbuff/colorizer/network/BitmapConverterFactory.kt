package com.imgbuff.colorizer.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.ByteArrayOutputStream
import java.lang.reflect.Type


class BitmapConverterFactory(): Converter.Factory() {

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, Bitmap>? {
        return Converter {
            val byteArray = it.bytes()
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }

    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<Annotation>, methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<Bitmap, RequestBody>? {
        return Converter {
            val byteArrayOutputStream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val imageBytes = byteArrayOutputStream.toByteArray()
            val imageRequest = RequestBody.create(MediaType.parse("multipart/form-data"), imageBytes)
            MultipartBody.Part.createFormData("image", "inputImage", imageRequest).body()
        }
    }
}