package com.globant.ui.helpers

import android.app.AlertDialog
import android.content.Context
import androidx.annotation.StringRes
import com.globant.ui.R
import javax.inject.Inject

class DialogManager @Inject constructor(){
    fun showAlert(context: Context, @StringRes titleResource:Int, msgResource:Int){
        val title = context.getString(titleResource)
        val message = context.getString(msgResource)
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.accept, null)
        val dialog = builder.create()
        dialog.show()
    }

    fun showAlert(context: Context, titleResource: Int, message: String){
        showAlert(context, context.getString(titleResource), message)
    }

    fun showAlert(context: Context, title:String, message:String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.accept, null)
        val dialog = builder.create()
        dialog.show()
    }
}