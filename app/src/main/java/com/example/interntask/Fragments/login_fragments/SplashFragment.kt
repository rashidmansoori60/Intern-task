package com.example.interntask.Fragments.login_fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.interntask.Activity.DashBoardActivity
import com.example.interntask.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       viewLifecycleOwner.lifecycleScope.launch {
           delay(1000)
           if(FirebaseAuth.getInstance().currentUser!=null){
               val intent= Intent(requireContext(), DashBoardActivity::class.java)
               intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
               startActivity(intent)
           }else{
               findNavController().navigate(R.id.action_splashFragment_to_singInFragment,null,
                   navOptions {
                       popUpTo(R.id.splashFragment) {
                           inclusive = true
                       }
                   }
                   )
           }
       }
    }
}