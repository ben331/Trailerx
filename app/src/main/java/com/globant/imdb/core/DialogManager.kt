package com.globant.imdb.core

import android.app.AlertDialog
import android.content.Context
import com.globant.imdb.R
import javax.inject.Inject

class DialogManager @Inject constructor(){
    fun showAlert(context:Context, titleResource:Int, msgResource:Int){
        val title = context.getString(titleResource)
        val message = context.getString(msgResource)
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.accept, null)
        val dialog = builder.create()
        dialog.show()
    }

    fun showAlert(context:Context,title:String, message:String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.accept, null)
        val dialog = builder.create()
        dialog.show()
    }
}