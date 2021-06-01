package com.example.jetpackcompose.model

import com.example.jetpackcompose.R

data class Operation(
    val title: String,
    val subtitle: String? = null,
    val imgResource: Int
)

val operations = listOf(
    Operation(
        title = "Изменить суточный лимит",
        subtitle = "На платежи и переводы",
        imgResource = R.drawable.ic_clock
    ),
    Operation(
        title = "Переводы без комиссии",
        subtitle = "Показать остаток в этом месяце",
        imgResource = R.drawable.ic_pure_percentage
    ),
    Operation(
        title = "Информация о тарифах\nи лимитах",
        imgResource = R.drawable.ic_arrows
    ),
)