package com.example.plan_a_trip.Activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.plan_a_trip.Globals.currencies
import com.example.plan_a_trip.Interfaces.CurrencyInterface
import com.example.plan_a_trip.R
import com.example.plan_a_trip.Retrofit.RetrofitCurrency
import kotlinx.android.synthetic.main.activity_currency_converter.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CurrencyConverterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter)
        var task_number = "0"

        setCurrencySpinner(spinnerFromCurrency)
        setCurrencySpinner(spinnerToCurrency)




        val buttonConverter = findViewById<Button>(R.id.buttonCurrencyConverter)

        buttonConverter.setOnClickListener{

            var fromCurrency = currencies[spinnerFromCurrency.firstVisiblePosition]
            var toCurrency = currencies[spinnerToCurrency.firstVisiblePosition]
            if (!editTextAmount.text.isEmpty()) {
                val service =
                    RetrofitCurrency.retrofitInstance?.create(CurrencyInterface::class.java)
                val dataCurrency = service?.getCurrencyOne(fromCurrency,toCurrency,editTextAmount.text.toString())
                dataCurrency?.enqueue(object : Callback<Double> {
                    override fun onFailure(call: Call<Double>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error parsing json", Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<Double>,
                        response: Response<Double>
                    ) {
                        val body = response.body()


                        val rate = body.toString()
                        textViewResult.text = rate


                    }

                })
            }else {
                Toast.makeText(applicationContext,"Please insert amount", Toast.LENGTH_LONG).show()
            }
        }


    }

    fun setCurrencySpinner(spinner: Spinner){
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            currencies
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.setAdapter(aa)

        spinner?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                //Toast.makeText( applicationContext,"Selected currency : "+ currencies[position],Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }
    }

}
