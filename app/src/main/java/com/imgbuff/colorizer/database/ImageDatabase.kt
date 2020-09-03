package com.imgbuff.colorizer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [DatabaseImage::class], version = 1)
abstract class ImageDatabase: RoomDatabase() {
    abstract fun imageDao(): ImageDao
}

private lateinit var INSTANCE: ImageDatabase

fun getInstance(context: Context): ImageDatabase {
    synchronized(ImageDatabase::class.java) {
        if(!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext, ImageDatabase::class.java, "image_database").build()
        }
    }
    return INSTANCE
}