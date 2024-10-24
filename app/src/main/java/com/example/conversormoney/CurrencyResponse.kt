package com.example.conversormoney

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("Realtime Currency Exchange Rate")
    val exchangeRateInfo: ExchangeRateInfo
)

data class ExchangeRateInfo(
    @SerializedName("1. From_Currency Code")
    val fromCurrencyCode: String,

    @SerializedName("2. From_Currency Name")
    val fromCurrencyName: String,

    @SerializedName("3. To_Currency Code")
    val toCurrencyCode: String,

    @SerializedName("4. To_Currency Name")
    val toCurrencyName: String,

    @SerializedName("5. Exchange Rate")
    val exchangeRate: String,

    @SerializedName("6. Last Refreshed")
    val lastRefreshed: String,

    @SerializedName("7. Time Zone")
    val timeZone: String
)
