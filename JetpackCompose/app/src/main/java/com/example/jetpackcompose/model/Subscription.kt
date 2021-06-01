package com.example.jetpackcompose.model

import com.example.jetpackcompose.R

data class Subscription(
    val title: String,
    val text: String,
    val subtext: String,
    val imgResource: Int,
)

val subscriptions = listOf(
    Subscription(
        title = "СберПрайм",
        text = "Платёж 9 июля",
        subtext = "199 ₽ в месяц",
        imgResource = R.drawable.ic_flag
    ),
    Subscription(
        title = "Переводы",
        text = "Автопродление 21 августа",
        subtext = "199 ₽ в месяц",
        imgResource = R.drawable.ic_percentage
    ),
    Subscription(
        title = "СберПрайм",
        text = "Платёж 9 июля",
        subtext = "199 ₽ в месяц",
        imgResource = R.drawable.ic_flag
    ),
    Subscription(
        title = "Переводы",
        text = "Автопродление 21 августа",
        subtext = "199 ₽ в месяц",
        imgResource = R.drawable.ic_percentage
    ),
)