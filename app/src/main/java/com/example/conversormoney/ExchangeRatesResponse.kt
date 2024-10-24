package com.example.conversormoney

data class ExchangeRatesResponse(
    val base: String,
    val rates: Map<String, Double>
)

