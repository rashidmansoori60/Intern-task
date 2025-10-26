package com.example.interntask.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.interntask.Resources.Resourcesstate
import com.example.interntask.Resources.ValidateState
import com.example.interntask.Resources.Validationfaild
import com.example.interntask.Util.validatiomEmail
import com.example.interntask.Util.validpassword
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.nio.file.Files.find
import javax.inject.Inject

@HiltViewModel
class LoginVm @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _resourcesvm = MutableStateFlow<Resourcesstate<FirebaseUser>>(Resourcesstate.Unspecyfied())
    val resourcesvm = _resourcesvm.asStateFlow()

    private val _movetoDashboard = MutableStateFlow(false)
    val movetoDashboard: StateFlow<Boolean> get() = _movetoDashboard.asStateFlow()

    private val _toast = MutableSharedFlow<String>()
    val toast = _toast.asSharedFlow()

    private val _invalidInput = Channel<Validationfaild>{}
    val invalidInput = _invalidInput.receiveAsFlow()


    private val _checkEmailVisible = MutableStateFlow(false)
    val checkEmailVisible: StateFlow<Boolean> get() = _checkEmailVisible.asStateFlow()

    fun signInUser(email: String, pass: String) {
        val emailValid = validatiomEmail(email)
        val passValid = validpassword(pass)

        if (emailValid is ValidateState.Success && passValid is ValidateState.Success) {
            _resourcesvm.value = Resourcesstate.Loading()

            firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    val user = firebaseAuth.currentUser
                    user?.reload()?.addOnSuccessListener {
                        if (user.isEmailVerified) {
                            viewModelScope.launch {
                                _resourcesvm.value = Resourcesstate.Success(user)
                                _movetoDashboard.value = true
                                _toast.emit("Verified email ✅")
                                _checkEmailVisible.value = false
                            }
                        } else {
                            user.sendEmailVerification()
                                .addOnSuccessListener {
                                    firebaseAuth.signOut()
                                    viewModelScope.launch {
                                        _resourcesvm.value = Resourcesstate.Unspecyfied()
                                        _toast.emit("Verification email sent ✅")
                                        _checkEmailVisible.value = true // 👈 show check button
                                    }
                                }
                                .addOnFailureListener { e ->
                                    viewModelScope.launch {
                                        _resourcesvm.value = Resourcesstate.Error(e.message.toString())
                                        _toast.emit("Failed to send verification link: ${e.message}")
                                        _checkEmailVisible.value = false
                                    }
                                }
                        }
                    }?.addOnFailureListener { e ->
                        viewModelScope.launch {
                            _resourcesvm.value = Resourcesstate.Error(e.message.toString())
                            _toast.emit("Failed to reload user: ${e.message}")
                        }
                    }
                }
                .addOnFailureListener { e ->
                    handleSignInError(e)
                    viewModelScope.launch {
                        _resourcesvm.value = Resourcesstate.Error(e.message.toString())
                    }
                }
        } else {
            viewModelScope.launch {
                _resourcesvm.value = Resourcesstate.Error("")
             val chekinvalid=Validationfaild(emailValid, passValid)
                _invalidInput.send(chekinvalid)
            }
        }
    }


    fun checkEmailVerification() {
        val user = firebaseAuth.currentUser
        user?.reload()?.addOnSuccessListener {
            if (user.isEmailVerified) {
                viewModelScope.launch {
                    _movetoDashboard.value = true
                    _toast.emit("Email verified ✅")
                    _checkEmailVisible.value = false
                }
            } else {
                viewModelScope.launch {
                    _toast.emit("You haven’t verified your email yet. Please check your inbox 📩")
                }
            }
        }?.addOnFailureListener { e ->
            viewModelScope.launch {
                _toast.emit("Error checking verification: ${e.message}")
            }
        }
    }
    private fun handleSignInError(e: Exception) {
        when (e) {
            is FirebaseAuthInvalidUserException -> {
                // User account exist nahi karta
                viewModelScope.launch {
                    _toast.emit("No account found for this email ❌")
                }
            }
            is FirebaseAuthInvalidCredentialsException -> {
                // Password galat hai ya email format invalid
                viewModelScope.launch {
                    _toast.emit("Invalid email or password ⚠️")
                }
            }
            is FirebaseNetworkException -> {
                // Internet issue
                viewModelScope.launch {
                    _toast.emit("No internet connection 🌐")
                }
            }
            else -> {
                // Koi unknown error
                viewModelScope.launch {
                    _toast.emit("Login failed: ${e.message}")
                }
            }
        }
    }

}
