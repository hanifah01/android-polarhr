package com.example.polar.support

import android.app.DatePickerDialog
import android.content.Context
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

//val mdformat = SimpleDateFormat("yyyy/MM/dd")
val mdformat = SimpleDateFormat("yyyy-MM-dd")

fun dateDialog(editText: EditText, context: Context) {
    val newCalendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val newDate = Calendar.getInstance()
            newDate[year, monthOfYear] = dayOfMonth
            editText.setText(mdformat.format(newDate.time))
        },
        newCalendar[Calendar.YEAR],
        newCalendar[Calendar.MONTH],
        newCalendar[Calendar.DAY_OF_MONTH]
    )
    datePickerDialog!!.show()
}