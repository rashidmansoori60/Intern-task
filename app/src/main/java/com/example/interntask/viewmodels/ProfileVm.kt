package com.example.interntask.viewmodels

import androidx.lifecycle.ViewModel
import com.example.interntask.Resources.Resourcesstate
import com.example.interntask.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
@HiltViewModel
class ProfileVm @Inject constructor(private val firebaseDatabase: FirebaseDatabase,private val firebaseAuth: FirebaseAuth) : ViewModel(){

    private val _userData = MutableStateFlow<Resourcesstate<User>>(Resourcesstate.Unspecyfied())
    val userData  = _userData.asStateFlow()


    init {
      loaduser()
    }

    private fun loaduser(){
        _userData.value= Resourcesstate.Loading()
         val uid=firebaseAuth.currentUser?.uid

        if(uid==null){
            _userData.value= Resourcesstate.Error("unable to load user")
        }
        else{

            firebaseDatabase.getReference("users").child(uid).addListenerForSingleValueEvent(object :
                ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val name = snapshot.child("name").getValue(String::class.java) ?: ""
                        val email = snapshot.child("email").getValue(String::class.java) ?: ""
                        val lastname = snapshot.child("lastname").getValue(String::class.java) ?: ""

                        val user = User(uid, email, name, lastname)
                        _userData.value = Resourcesstate.Success(user)
                    } else {
                        _userData.value = Resourcesstate.Error("User data not found")
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                    _userData.value= Resourcesstate.Error(error.message)
                }
            })

        }
    }





}