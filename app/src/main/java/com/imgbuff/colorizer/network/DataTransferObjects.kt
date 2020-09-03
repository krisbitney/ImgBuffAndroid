package com.imgbuff.colorizer.network

import com.imgbuff.colorizer.database.DatabaseImage
import com.imgbuff.colorizer.domain.ColorizedImage
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val formatter: DateTimeFormatter by lazy {
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault())
}

@JsonClass(generateAdapter = true)
data class NetworkImageContainer(val images: List<NetworkImage>)

@JsonClass(generateAdapter = true)
data class NetworkImage(
    val id: Int,
    @Json(name = "uri") val url: String,
    val rating: Int,
    @Json(name = "create_date") val createDateTime: String
)

fun NetworkImageContainer.asDomainModel(): List<ColorizedImage> {
    return images.map {
        ColorizedImage(
            id = it.id,
            url = it.url,
            rating = it.rating,
            createDateTime = ZonedDateTime.parse(it.createDateTime, formatter)
        )
    }
}

fun NetworkImage.asDomainModel(): ColorizedImage {
    return ColorizedImage(
        id = id,
        url = url,
        rating = rating,
        createDateTime = ZonedDateTime.parse(createDateTime, formatter)
    )
}

fun NetworkImageContainer.asDatabaseModel(): Array<DatabaseImage> {
    return Array(images.size) {
        DatabaseImage(
            id = images[it].id,
            url = images[it].url,
            rating = images[it].rating,
            createDateTime = images[it].createDateTime
        )
    }
}
