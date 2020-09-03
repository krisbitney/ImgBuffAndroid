package com.imgbuff.colorizer.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.io.InputStream

private const val BASE_URL: String = "http://10.0.2.2:8000"

// TODO: this is a temporary placeholder until I implement user authentication
private const val TEST_USERNAME: String = "test"

private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory()) // return result as Kotlin Deferred
    .build()

interface ImageApiService {
    @Multipart
    @POST("colorizeImageMobile/{username}")
    fun requestColorizeAsync(@Path("username") user: String, @Part image: MultipartBody.Part): Deferred<NetworkImage>

    @GET("myImages/{username}")
    fun requestColorizedImagesAsync(@Path("username") user: String): Deferred<NetworkImageContainer>
}

// service is accessible as a singleton with lazy initialization
object ImageApi {
    private val retrofitService: ImageApiService = retrofit.create(ImageApiService::class.java)

    fun colorizeAsync(imageStream: InputStream): Deferred<NetworkImage> {
        val requestBody = RequestBody.create(MediaType.parse("image/*"), imageStream.readBytes())
        val request = MultipartBody.Part.createFormData("image", "inputImage", requestBody)
        return retrofitService.requestColorizeAsync(TEST_USERNAME, request)
    }
}


                                               
