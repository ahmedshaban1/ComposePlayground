package com.ahmed.composepayground.datastore

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val language: Language = Language.ARABIC
)

enum class Language{
    ENGLISH,GERMAN,ARABIC
}