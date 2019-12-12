package com.example.plan_a_trip.Activities

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*
import java.text.ParseException
import android.widget.Spinner
import com.example.plan_a_trip.R
import com.example.plan_a_trip.Globals.country
import com.example.plan_a_trip.Globals.countryID


class FlightSelectActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flight_select)

        val btn_viewList = findViewById<Button>(R.id.buttonViewList)

        var spinnerFrom = findViewById<Spinner>(R.id.spinnerFromCountry)
        var spinnerTo = findViewById<Spinner>(R.id.spinnerToCountry)

        setCountrySpinner(spinnerFrom)
        setCountrySpinner(spinnerTo)



        val textViewFromDate = findViewById<TextView>(R.id.departureTextView)
        getDate(textViewFromDate, this)
        val textViewFromTo = findViewById<TextView>(R.id.textViewArrivalTime)
        getDate(textViewFromTo, this)


        btn_viewList.setOnClickListener(){

            if (isValidDate(textViewFromDate.text.toString()) && isValidDate(textViewFromDate.text.toString())) {
                val date1 = SimpleDateFormat("dd/MM/yyyy").parse(textViewFromDate.text.toString())

                val date2 = SimpleDateFormat("dd/MM/yyyy").parse(textViewFromTo.text.toString())

                if (date1.before(date2)){
                    val intent = Intent(this,
                        FlightListActivity::class.java)
                    intent.putExtra("CountryFrom", countryID[spinnerFrom.firstVisiblePosition])
                    intent.putExtra("CountryTo", countryID[spinnerTo.firstVisiblePosition])
                    intent.putExtra("DateFrom",textViewFromDate.text.toString())
                    intent.putExtra("DateTo",textViewFromTo.text.toString())
                    startActivity(intent)
                } else{
                    Toast.makeText(applicationContext,"Warning: First date after second date",Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext,"Insert dates",Toast.LENGTH_SHORT).show()
            }



        }


    }

    fun getDate(textView: TextView, context: Context){

        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            textView.text = SimpleDateFormat("dd/MM/yyyy").format(cal.time)

        }

        textView.setOnClickListener {
            DatePickerDialog(context, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    fun setCountrySpinner(spinner: Spinner){
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            country
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.setAdapter(aa)

        spinner?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                Toast.makeText( applicationContext,"Selected country : "+ country[position],Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }
    }

    fun isValidDate(inDate: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        dateFormat.isLenient = false
        try {
            dateFormat.parse(inDate.trim { it <= ' ' })
        } catch (pe: ParseException) {
            return false
        }

        return true
    }
}

