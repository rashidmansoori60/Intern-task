package com.example.interntask.Util

import android.provider.ContactsContract
import android.util.Patterns
import com.example.interntask.Resources.ValidateState

 fun validatiomEmail(email:String): ValidateState{
    if(email.isEmpty()){
        return ValidateState.Error("Please fill email")
    }
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return ValidateState.Error("please fill valid email")
    }

    return ValidateState.Success
}

fun validpassword(pass:String): ValidateState{
    if(pass.length<6){
      return  ValidateState.Error("Password should contain atleast 6 charactors")
    }
    if(pass.isEmpty()){
        return ValidateState.Error("Please enter password")
    }

    return ValidateState.Success
 }

fun validcorrectPass(pass1:String,pass2:String): ValidateState{
    if (pass2.equals(pass1)){
       return ValidateState.Success
    }
    if(pass2.isEmpty()){
        return ValidateState.Error("Fill password!")
    }

   return ValidateState.Error("invalid")
}
fun validateName(name: String): ValidateState{
    if(name.isEmpty()){
        return ValidateState.Error("Mandatory to fill this requirement")
    }
    return ValidateState.Success
}