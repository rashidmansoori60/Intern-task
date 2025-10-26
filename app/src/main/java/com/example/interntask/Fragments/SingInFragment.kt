package com.example.interntask.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.interntask.Activity.DashBoardActivity
import com.example.interntask.R
import com.example.interntask.Resources.Resourcesstate
import com.example.interntask.Resources.ValidateState
import com.example.interntask.databinding.FragmentSingInBinding
import com.example.interntask.viewmodels.LoginVm
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SingInFragment : Fragment() {
    var _binding: FragmentSingInBinding?=null
    private val binding get() = _binding!!

    val loginVm : LoginVm by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSingInBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvGoToSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_singInFragment_to_singUpFragment,null,
                NavOptions.Builder().setPopUpTo(R.id.singInFragment,true).build())
        }

        binding.btnSignIn.setOnClickListener{
            val email=binding.edtEmail.text.toString().trim()
            val pass=binding.edtPassword.text.toString().trim()

            loginVm.signInUser(email,pass)

        }


        lifecycleScope.launchWhenStarted {
            loginVm.checkEmailVisible.collect { isVisible ->
                binding.btnSignInemailckek.visibility =
                    if (isVisible) View.VISIBLE else View.GONE
            }
        }
        lifecycleScope.launchWhenStarted {

             loginVm.resourcesvm.collect { it
                 when(it){
                     is Resourcesstate.Loading->{
                         binding.btnSignIn.visibility= View.GONE
                         binding.progSignin.visibility= View.VISIBLE
                     }
                     is Resourcesstate.Success->{
                         binding.btnSignIn.visibility= View.VISIBLE
                         binding.progSignin.visibility= View.GONE
                         val intent = Intent(requireContext(), DashBoardActivity::class.java)
                         startActivity(intent)
                         requireActivity().supportFragmentManager.popBackStack()

                     }
                     is Resourcesstate.Error->{
                         binding.btnSignIn.visibility= View.VISIBLE
                         binding.progSignin.visibility= View.GONE
                     }
                     else -> Unit

                 }

             }
        }
        lifecycleScope.launchWhenStarted {
            loginVm.invalidInput.collect { value ->
                if(value.email is ValidateState.Error){
                    binding.edtEmail.setError(value.email.message)
                }
                if(value.pass is ValidateState.Error){
                    binding.edtPassword.setError(value.pass.message)
                }
            }

        }
        lifecycleScope.launchWhenStarted {
            loginVm.toast.collect { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnSignInemailckek.setOnClickListener {
        lifecycleScope.launchWhenStarted { loginVm.checkEmailVisible.collect { it ->
            if(it){
                loginVm.checkEmailVerification()
            }

        }
        }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}