package com.imgbuff.colorizer.domain

import java.time.ZonedDateTime


data class ColorizedImage(
    val id: Int,
    val url: String,
    val rating: Int,
    val createDateTime: ZonedDateTime
)