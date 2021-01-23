package com.githubusers.android.data.sources.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

object ListConverter {

    @TypeConverter
    @JvmStatic
    fun fromStringToListInt(value: String?): List<Int> {
        val listType = object : TypeToken<ArrayList<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromListIntToString(list: List<Int>): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}