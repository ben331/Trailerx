package tech.benhack.ui.helpers

import android.util.Patterns

private const val MIN_CHARACTERS_PASSWORD = 8
class FormValidator {
    companion object {
        fun validateLogin(email:String, password: String): Boolean{
            return (
                    validateEmail(email)
                &&  validatePassword(password)
            )
        }

        fun validateSignUp(username:String, email:String, password:String): Boolean{
            return (
                    validateIsNotBlank(username)
                &&  validateEmail(email)
                &&  validatePassword(password)
            )
        }

        fun validateEmail(email:String): Boolean{
            val pattern = Patterns.EMAIL_ADDRESS
            return pattern.matcher(email).matches()
        }

        fun validatePassword(password:String):Boolean{
            return password.length >= MIN_CHARACTERS_PASSWORD
        }

        fun validateIsNotBlank(field:String):Boolean{
            return field.isNotBlank()
        }


    }
}

fun String?.toYear():String =
    if(this!=null && this.length >= 4)
        this.subSequence(0,4).toString()
    else "----"