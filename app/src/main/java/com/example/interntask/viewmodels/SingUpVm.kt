package com.example.interntask.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interntask.Resources.RagistationFaild
import com.example.interntask.Resources.Resourcesstate
import com.example.interntask.Resources.ValidateState
import com.example.interntask.Resources.Validationfaild
import com.example.interntask.Util.validateName
import com.example.interntask.Util.validatiomEmail
import com.example.interntask.Util.validcorrectPass
import com.example.interntask.Util.validpassword
import com.example.interntask.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
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
class SingUpVm @Inject constructor(val firebaseAuth: FirebaseAuth,val firebaseDatabase: FirebaseDatabase): ViewModel(){

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
    fun singUp(email:String, pass:String, pass2:String,name: String){

        val validemail= validatiomEmail(email)
        val validpass= validpassword(pass)
        val vaildcorrectpass= validcorrectPass(pass,pass2)

        if(validemail is ValidateState.Success && validpass is ValidateState.Success && vaildcorrectpass is ValidateState.Success && validateName(name) is ValidateState.Success){
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
                    _toast.emit("Unable to create user"+exception.message)
                }


            }
        }else{
          val ragisterInvalidInput=RagistationFaild(validatiomEmail(email),validpassword(pass),validcorrectPass(pass,pass2),validateName(name))
           viewModelScope.launch {
            _invalidInput.send(ragisterInvalidInput)
           }
        }

    }
    fun checkEmailVerification(userown: User) {
        _ragistationstate.value = Resourcesstate.Loading()
        val user = firebaseAuth.currentUser

        if (user == null) {
            viewModelScope.launch {
                _toast.emit("Unable to load user data ⚠️")
            }
            _ragistationstate.value = Resourcesstate.Unspecyfied()
            return
        }

        // Refresh user info from Firebase
        user.reload().addOnSuccessListener {
            if (user.isEmailVerified) {
                val uid = user.uid
                userown.id=uid
                // ✅ First, save data in Firebase Realtime Database
                firebaseDatabase.getReference("users")
                    .child(uid)
                    .setValue(userown)
                    .addOnSuccessListener {
                        viewModelScope.launch {
                            _toast.emit("User data saved successfully ✅")
                        }

                        // ✅ Now that everything succeeded — move to next screen
                        _ragistationstate.value = Resourcesstate.Success(user)
                        viewModelScope.launch {
                            _checkEmailVisible.value = false
                            _movetoDashboard.emit(true)
                            _toast.emit("Email verified✅")
                        }
                    }
                    .addOnFailureListener { e ->
                        viewModelScope.launch {
                            _toast.emit("Failed to save user: ${e.message}")
                        }
                        _ragistationstate.value = Resourcesstate.Error("Database write failed")
                    }

            } else {
                // ❌ Not verified yet
                _ragistationstate.value = Resourcesstate.Unspecyfied()
                viewModelScope.launch {
                    _toast.emit("You haven’t verified your email yet. Please check your inbox 📩")
                }
            }
        }.addOnFailureListener { e ->
            _ragistationstate.value = Resourcesstate.Unspecyfied()
            viewModelScope.launch {
                _toast.emit("Error checking verification: ${e.message}")
            }
        }
    }

}