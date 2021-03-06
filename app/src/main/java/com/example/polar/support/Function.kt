package com.example.polar.support

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


val mtformat = SimpleDateFormat("HH:mm")
val mdformat = SimpleDateFormat("yyyy-MM-dd")

fun dateDialog(editText: TextView, context: Context) {
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
    datePickerDialog.show()
}

val mdformatHasil = SimpleDateFormat("E,dd-MM-yyyy")
fun dateDialogHasil(editText: TextView, context: Context) {
    val newCalendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val newDate = Calendar.getInstance()
            newDate[year, monthOfYear] = dayOfMonth
            editText.setText(mdformatHasil.format(newDate.time))
        },
        newCalendar[Calendar.YEAR],
        newCalendar[Calendar.MONTH],
        newCalendar[Calendar.DAY_OF_MONTH]
    )
    datePickerDialog.show()
}

fun requestDate(): String {
    val unixTime = System.currentTimeMillis()
    val mdformat = SimpleDateFormat("E,dd-MM-yyyy")
    return mdformat.format(Date(unixTime))
}

fun requestDate2(): String {
    val unixTime = System.currentTimeMillis()
    val mdformat = SimpleDateFormat("yyyy-MM-dd")
    return mdformat.format(Date(unixTime))
}

fun Context.intent(toClass: Class<*>) = Intent(this, toClass)

fun <T> JSONObject.toObject(classOfT: Class<T>): T = Gson().fromJson(this.toString(), classOfT)

