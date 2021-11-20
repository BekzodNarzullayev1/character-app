package com.example.charactersapp.utils

import androidx.room.TypeConverter
import com.example.charactersapp.models.Location
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class EpisodeConverter {

    private val gson = Gson()

    private val type: Type = object : TypeToken<List<String>?>() {}.type

    @TypeConverter
    fun from(team: List<String>?): String? {
        if (team == null) {
            return null
        }

        return gson.toJson(team, type)
    }

    @TypeConverter
    fun to(teamString: String?): List<String>? {
        if (teamString == null) {
            return null
        }
        return gson.fromJson(teamString, type)
    }

}