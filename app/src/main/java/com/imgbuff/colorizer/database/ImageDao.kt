package com.imgbuff.colorizer.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ImageDao {

    @Query("SELECT * FROM image_table")
    fun getImages(): LiveData<List<DatabaseImage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg images: DatabaseImage)

    @Delete
    fun delete(image: DatabaseImage)

}