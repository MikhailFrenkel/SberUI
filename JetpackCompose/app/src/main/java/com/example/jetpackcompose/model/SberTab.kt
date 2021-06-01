package com.example.jetpackcompose.model

import com.example.jetpackcompose.ui.theme.Lexem

enum class SberTab(
    val title: String
) {
    Profile(Lexem.Profile),
    Settings(Lexem.Settings);

    companion object {
        fun fromRoute(route: String?): SberTab =
            when(route?.substringBefore("/")){
                Profile.name -> Profile
                Settings.name -> Settings
                null -> Profile
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}