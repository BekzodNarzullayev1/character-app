package com.example.charactersapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Characters (

    val info:Info,

    @SerializedName("results")
    val characterList: List<Character>

):Parcelable