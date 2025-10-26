package com.example.interntask.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interntask.Resources.RagistationFaild
import com.example.interntask.Resources.Resourcesstate
import com.example.interntask.Resources.ValidateState
import com.example.interntask.Resources.Validationfaild
import com.example.interntask.Util.validatiomEmail
import com.example.interntask.Util.validcorrectPass
import com.example.interntask.Util.validpassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingUpVm @Inject constructor(val firebaseAuth: FirebaseAuth): ViewModel(){

    private val _toast = MutableSharedFlow<String>()
    val toast = _toast.asSharedFlow()

    private val _invalidInput = Channel<RagistationFaild>{}
    val invalidInput = _invalidInput.receiveAsFlow()


    private val _checkEmailVisible = MutableStateFlow(false)
    val checkEmailVisible: StateFlow<Boolean> get() = _checkEmailVisible.asStateFlow()

    private val _movetoDashboard = MutableSharedFlow<Boolean>()
    val movetoDashboard: SharedFlow<Boolean> get() = _movetoDashboard.asSharedFlow()
    private val _ragistationstate = MutableStateFlow<Resourcesstate<FirebaseUser>>(Resourcesstate.Unspecyfied())
    val ragistatiomstate = _ragistationstate.asStateFlow()

    @SuppressLint("SuspiciousIndentation")
    fun singUp(email:String, pass:String, pass2:String){

        val validemail= validatiomEmail(email)
        val validpass= validpassword(pass)
        val vaildcorrectpass= validcorrectPass(pass,pass2)

        if(validemail is ValidateState.Success && validpass is ValidateState.Success && vaildcorrectpass is ValidateState.Success){
            _ragistationstate.value= Resourcesstate.Loading()
            firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener {
                val user=firebaseAuth.currentUser

                user?.let { user ->
                    user.reload().addOnSuccessListener {
                        if(user.isEmailVerified){
                            viewModelScope.launch {
                            _movetoDashboard.emit(true)
                            }

                            _ragistationstate.value= Resourcesstate.Success(user)
                        }
                        else{
                            user.sendEmailVerification().addOnSuccessListener {
                                viewModelScope.launch {
                                    _toast.emit("Verified email sent✅")
                                    _ragistationstate.value= Resourcesstate.Unspecyfied()
                                }
                                _checkEmailVisible.value=true


                            }.addOnFailureListener {e->
                                _ragistationstate.value= Resourcesstate.Error("")
                                _checkEmailVisible.value=false
                                viewModelScope.launch { _toast.emit( "Failed to send verification link:+$e.message")

                                }

                            }
                        }
                    }.addOnFailureListener {
                        _ragistationstate.value= Resourcesstate.Error("")
                        viewModelScope.launch {
                            _toast.emit("Unable to load user")
                        }
                    }

                }


            }.addOnFailureListener {exception ->
                _ragistationstate.value= Resourcesstate.Error("")
                viewModelScope.launch {
                    _toast.emit("Unable to load create"+exception.message)
                }


            }
        }else{
          val ragisterInvalidInput=RagistationFaild(validatiomEmail(email),validpassword(pass),validcorrectPass(pass,pass2))
           viewModelScope.launch {
            _invalidInput.send(ragisterInvalidInput)
           }
        }

    }
    fun checkEmailVerification() {
        _ragistationstate.value= Resourcesstate.Loading()
        val user = firebaseAuth.currentUser
        user?.reload()?.addOnSuccessListener {
            if (user.isEmailVerified) {
                _ragistationstate.value= Resourcesstate.Success(user)
                viewModelScope.launch {
                    _movetoDashboard.emit(true)
                    _toast.emit("Email verified ✅")
                    _checkEmailVisible.value = false
                }
            } else {
                _ragistationstate.value= Resourcesstate.Unspecyfied()
                viewModelScope.launch {
                    _toast.emit("You haven’t verified your email yet. Please check your inbox 📩")
                }
            }
        }?.addOnFailureListener { e ->
            _ragistationstate.value= Resourcesstate.Unspecyfied()
            viewModelScope.launch {
                _toast.emit("Error checking verification: ${e.message}")
            }
        }
    }
}