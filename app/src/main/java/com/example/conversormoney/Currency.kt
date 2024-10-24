package com.example.conversormoney

data class Currency(
    val imageResId: Int,
    val currencyName: String,
    val currencyRate: Double,
    var isSelected: Boolean = false
)