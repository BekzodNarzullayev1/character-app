package com.example.charactersapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.charactersapp.utils.EpisodeConverter
import com.example.charactersapp.utils.LocationConverter
import com.example.charactersapp.utils.OriginConverter
import kotlinx.parcelize.Parcelize

@Parcelize
@TypeConverters(LocationConverter::class,OriginConverter::class,EpisodeConverter::class)
@Entity(tableName = "characters_table")
data class Character(
    val created: String,
    val episode: List<String>,
    val gender: String,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val image: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String
) : Parcelable