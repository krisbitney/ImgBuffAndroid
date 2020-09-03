package com.imgbuff.colorizer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.imgbuff.colorizer.domain.ColorizedImage
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@Entity(tableName = "image_table")
data class DatabaseImage(
    @PrimaryKey val id: Int,
    @ColumnInfo(name="url") val url: String,
    @ColumnInfo(name="rating") val rating: Int,
    @ColumnInfo(name="create_datetime") val createDateTime: String
)

fun List<DatabaseImage>.asDomainModel(): List<ColorizedImage> {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX")
    return this.map {
        ColorizedImage(
            id = it.id,
            url = it.url,
            rating = it.rating,
            createDateTime = ZonedDateTime
                .parse(it.createDateTime, formatter)
                .withZoneSameInstant(ZoneId.systemDefault())
        )
    }
}