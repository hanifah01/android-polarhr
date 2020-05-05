package com.example.polar.support.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.example.polar.R
import kotlinx.android.synthetic.main.dialog_loading.*

class DialogLoading(context: Context): Dialog(context) {
    init {
        setContentView(LayoutInflater.from(context).inflate(R.layout.dialog_loading, null, false))
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    fun showDialog(isLoading: Boolean ,message: String = "Loading") {
        txt_message_loading.text = message
        if (isLoading) show() else dismiss()
    }
}
