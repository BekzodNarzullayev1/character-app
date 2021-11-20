package com.example.charactersapp.utils

import androidx.room.TypeConverter
import com.example.charactersapp.models.Location
import com.example.charactersapp.models.Origin
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class OriginConverter {

    private val gson = Gson()

    private val type: Type = object : TypeToken<Origin?>() {}.type

    @TypeConverter
    fun from(team: Origin?): String? {
        if (team == null) {
            return null
        }

        return gson.toJson(team, type)
    }

    @TypeConverter
    fun to(teamString: String?): Origin? {
        if (teamString == null) {
            return null
        }
        return gson.fromJson(teamString, type)
    }
}