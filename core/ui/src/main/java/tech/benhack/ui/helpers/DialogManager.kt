package tech.benhack.ui.helpers

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import tech.benhack.ui.R
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

    fun showDialog(
        context: Context,
        titleResource: Int,
        msgResource: Int,
        accept:()->Unit
    ){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(titleResource))
        builder.setMessage(context.getString(msgResource))
        builder.setPositiveButton(R.string.accept) { dialog, _ ->
            accept()
            dialog.dismiss()
        }
        builder.setNegativeButton(context.getString(R.string.cancel), null)
        val dialog = builder.create()
        dialog.show()
    }

    fun showDialog(
        context: Context,
        titleResource: Int,
        msgResource:Int,
        accept:()->Unit,
        cancel:()->Unit
    ){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(titleResource))
        builder.setMessage(context.getString(msgResource))
        builder.setPositiveButton(R.string.accept) { dialog, _ ->
            accept()
            dialog.dismiss()
        }
        builder.setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
            cancel()
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}