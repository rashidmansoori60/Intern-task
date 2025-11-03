package com.example.interntask.Fragments.login_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.interntask.R
import com.example.interntask.Resources.Resourcesstate
import com.example.interntask.Resources.ValidateState
import com.example.interntask.databinding.FragmentSingUpBinding
import com.example.interntask.model.User
import com.example.interntask.viewmodels.SingUpVm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingUpFragment : Fragment() {

    var _binding: FragmentSingUpBinding?=null
    private val binding get() = _binding!!

    val singUpVm: SingUpVm by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSingUpBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvGoToSignIn.setOnClickListener {
            findNavController().navigate(
                R.id.action_singUpFragment_to_singInFragment,null,
                NavOptions.Builder().setPopUpTo(R.id.singInFragment,true).build())
        }



        binding.btnSignUp.setOnClickListener {
            val email=binding.edtEmail.text.toString().trim()
            val pass=binding.edtPassword.text.toString().trim()
            val name=binding.edtFirstName.text.toString().trim()
            val lastname=binding.edtLastName.text.toString().trim()
            val comformpss=binding.edtConfirmPassword.text.toString().trim()



            singUpVm.singUp(email,pass,comformpss,name)


        }

        binding.btnSignUpemailckek.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val name = binding.edtFirstName.text.toString().trim()
            val lastname = binding.edtLastName.text.toString().trim()
            val userown = User(email, name, lastname,"")

            singUpVm.checkEmailVerification(userown)
        }

        lifecycleScope.launchWhenStarted {
            singUpVm.movetoDashboard.collect { navigate ->
                if (navigate) {
                    val navController = findNavController()
                    if (navController.currentDestination?.id == R.id.singUpFragment) {
                        navController.navigate(
                            R.id.action_singUpFragment_to_singInFragment,
                            null,
                            NavOptions.Builder().setPopUpTo(R.id.singUpFragment, true).build()
                        )
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            singUpVm.checkEmailVisible.collect { isVisible ->
                binding.btnSignUpemailckek.visibility =
                    if (isVisible) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launchWhenStarted {
            singUpVm.invalidInput.collect { it->
            if(it.email is ValidateState.Error){
               binding.edtEmail.setError(it.email.message)
            }
                if(it.pass is ValidateState.Error){
                binding.edtPassword.setError(it.pass.message)

            }
                if(it.correctpass is ValidateState.Error){
                    binding.edtConfirmPassword.setError(it.correctpass.message)
                }
                if(it.name is ValidateState.Error){
                    binding.edtFirstName.setError(it.name.message)
                }
        }
        }

        lifecycleScope.launchWhenStarted {
            singUpVm.ragistatiomstate.collect { it
            when(it){
                is Resourcesstate.Loading ->{
                    binding.progSingUp.visibility= View.VISIBLE
                    binding.btnSignUp.visibility= View.GONE
                }

                is Resourcesstate.Success ->{
                    binding.progSingUp.visibility= View.GONE
                    binding.btnSignUp.visibility= View.VISIBLE

                }
                is Resourcesstate.Error ->{

                    binding.progSingUp.visibility= View.GONE
                    binding.btnSignUp.visibility= View.VISIBLE
                }
                else -> {binding.progSingUp.visibility= View.GONE
                }
            }

            }
        }
        lifecycleScope.launchWhenStarted {
            singUpVm.toast.collect { value ->
                Toast.makeText(requireContext(),value, Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}