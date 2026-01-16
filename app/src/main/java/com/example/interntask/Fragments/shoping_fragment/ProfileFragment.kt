package com.example.interntask.Fragments.shoping_fragment

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.interntask.Activity.MainActivity
import com.example.interntask.R
import com.example.interntask.Resources.Resourcesstate
import com.example.interntask.databinding.FragmentProfileBinding
import com.example.interntask.model.User
import com.example.interntask.viewmodels.ProfileVm
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {


     var _binding: FragmentProfileBinding?=null
    private val binding get() = _binding!!

    val auth = FirebaseAuth.getInstance()

     val profileVm: ProfileVm by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("profile", "oncreate")
        _binding = FragmentProfileBinding.inflate(inflater,container,false)

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("profile", "oncreateview")

        binding.llLogout.setOnClickListener {
            auth.signOut()
            val intent= Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("openLogin", true)
            startActivity(intent)

        }

        lifecycleScope.launchWhenStarted {
            profileVm.userData.collect { it->
                when(it){

                    is Resourcesstate.Loading -> {
                        binding.profileProgress.visibility= View.VISIBLE
                    }
                    is Resourcesstate.Success ->{
                        loadProfileimage()
                        binding.profileProgress.visibility= View.GONE
                        setupView(it.data!!)
                    }
                    is Resourcesstate.Error -> {
                        binding.profileProgress.visibility= View.GONE
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Unit
                    }

                }
            }
        }


    }
    fun setupView(user: User){
       binding.tvUserName.text=user.name
        binding.tvUserEmail.text=user.email

    }
    private fun loadProfileimage(){
        Glide.with(this)
            .load("https://avatar.iran.liara.run/public/17")
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.imageProgress.visibility= View.GONE
                    return false
                }
                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable?>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.imageProgress.visibility= View.GONE
                    return false
                }
            })
            .into(binding.profileImage)
    }




    override fun onDestroyView() {
        Log.e("profile", "ondestroycall")
        super.onDestroyView()
        _binding = null
    }
    }


