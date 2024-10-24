package com.example.conversormoney

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var spinnerFromCurrency: Spinner
    private lateinit var spinnerToCurrency: Spinner
    private lateinit var etAmount: EditText
    private lateinit var tvConvertedAmount: TextView
    private lateinit var tvExchangeRate: TextView
    private lateinit var btnSwap: ImageButton
    private lateinit var btnClear: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Inicializando views
        spinnerFromCurrency = findViewById(R.id.spinner_from_currency)
        spinnerToCurrency = findViewById(R.id.spinner_to_currency)
        etAmount = findViewById(R.id.et_amount)
        tvConvertedAmount = findViewById(R.id.tv_converted_amount)
        tvExchangeRate = findViewById(R.id.tv_exchange_rate)
        btnSwap = findViewById(R.id.btn_swap)
        btnClear = findViewById(R.id.btn_clear)
        progressBar = findViewById(R.id.progress_bar)
        // Configurar o spinner com a lista de moedas
        val currencies = arrayOf("USD", "EUR", "BRL", "SGD")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFromCurrency.adapter = adapter
        spinnerToCurrency.adapter = adapter
        // Listener para o botão de swap
        btnSwap.setOnClickListener {
            val fromPosition = spinnerFromCurrency.selectedItemPosition
            spinnerFromCurrency.setSelection(spinnerToCurrency.selectedItemPosition)
            spinnerToCurrency.setSelection(fromPosition)
            performConversion() // Realiza a conversão após o swap
        }
        // Listener para limpar os campos
        btnClear.setOnClickListener {
            etAmount.text.clear()
            tvConvertedAmount.text = ""
            tvExchangeRate.text = ""
        }
        // Listener para converter o valor assim que o usuário digitar
        etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performConversion()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    // Função para realizar a conversão
    private fun performConversion() {
        progressBar.visibility = View.VISIBLE
        val baseCurrency = spinnerFromCurrency.selectedItem.toString()
        val targetCurrency = spinnerToCurrency.selectedItem.toString()
        val amountStr = etAmount.text.toString()

        if (amountStr.isNotEmpty()) {
            val amount = amountStr.toDouble()
            // Chamada da API
            RetrofitClient.api.getExchangeRate(fromCurrency = baseCurrency, toCurrency = targetCurrency)
                .enqueue(object : Callback<CurrencyResponse> {
                    override fun onResponse(call: Call<CurrencyResponse>, response: Response<CurrencyResponse>) {
                        progressBar.visibility = View.GONE
                        if (response.isSuccessful) {
                            val exchangeRateInfo = response.body()?.exchangeRateInfo
                            if (exchangeRateInfo != null) {
                                val exchangeRate = exchangeRateInfo.exchangeRate.toDouble()
                                val convertedAmount = amount * exchangeRate
                                tvConvertedAmount.text = String.format("%.2f", convertedAmount)
                                tvExchangeRate.text = String.format("1 %s = %.2f %s", baseCurrency, exchangeRate, targetCurrency)
                            } else {
                                tvConvertedAmount.text = "Erro: Taxa de câmbio não encontrada."
                            }
                        } else {
                            tvConvertedAmount.text = "Erro: ${response.message()}"
                        }
                    }

                    override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                        progressBar.visibility = View.GONE
                        tvConvertedAmount.text = "Erro na API: ${t.message}"
                    }
                })
        } else {
            tvConvertedAmount.text = ""
            tvExchangeRate.text = ""
            progressBar.visibility = View.GONE
        }
    }
}
