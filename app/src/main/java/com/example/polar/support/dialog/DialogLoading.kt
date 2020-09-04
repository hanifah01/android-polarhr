package com.example.polar.support.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.example.polar.R
import kotlinx.android.synthetic.main.dialog_loading.*

class DialogLoading(context: Context): Dialog(context) {
    init {
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.dialog_loading)
    }

    fun show(isShown: Boolean, isCancelable: Boolean = false) {
        setCancelable(isCancelable)

        if (isShown) super.show()
        else super.dismiss()
    }

    override fun show() {
        show(true)
    }
}
