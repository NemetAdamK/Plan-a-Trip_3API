package com.example.plan_a_trip.Interfaces

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CurrencyInterface {

    @Headers("x-rapidapi-host: currency-exchange.p.rapidapi.com","x-rapidapi-key: 6bd3e5e03emsh4d190350ffb644dp122c61jsnd18f751c9e61" )
        @GET("exchange")

        fun getCurrencyOne(@Query("from") from: String,@Query("to") to: String,@Query("q") q: String): Call<Double>


}