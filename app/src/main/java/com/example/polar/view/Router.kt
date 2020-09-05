package com.example.polar.view

import android.content.Context
import com.example.polar.model.DataLatihan
import com.example.polar.support.KEY_DATA
import com.example.polar.support.intent
import com.example.polar.view.hasillatihan.DetailLatihan
import com.example.polar.view.latihan.Latihan
import com.example.polar.view.latihan.SimpanHasil
import maes.tech.intentanim.CustomIntent

class Router {
    fun toHasil(context: Context?, data: DataLatihan) {
        context?.startActivity(context.intent(SimpanHasil::class.java).apply { putExtra(KEY_DATA, data) })
        CustomIntent.customType(context, "left-to-right")
    }

    fun toLatihan(context: Context?, data: String) {
        context?.startActivity(context.intent(Latihan::class.java).apply { putExtra(KEY_DATA, data) })
        CustomIntent.customType(context, "left-to-right")
    }

    fun toDetailHasil(context: Context?, data: DataLatihan) {
        context?.startActivity(context.intent(DetailLatihan::class.java).apply { putExtra(KEY_DATA, data) })
        CustomIntent.customType(context, "left-to-right")
    }
}