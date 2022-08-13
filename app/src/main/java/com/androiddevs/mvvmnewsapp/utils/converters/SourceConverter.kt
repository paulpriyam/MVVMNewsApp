package com.androiddevs.mvvmnewsapp.utils.converters

import androidx.room.TypeConverter
import com.androiddevs.mvvmnewsapp.model.Source
import java.util.*

class SourceConverter {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(UUID.randomUUID().toString(), name)
    }
}