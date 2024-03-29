package com.example.plan_a_trip.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCurrency {

    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://currency-exchange.p.rapidapi.com/"

    // create a retrofit instance, only if it has not been created yet.
    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit
        }
}