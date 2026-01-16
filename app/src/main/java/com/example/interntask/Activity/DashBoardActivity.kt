package com.example.interntask.Activity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.interntask.Fragments.shoping_fragment.CartFragment
import com.example.interntask.Fragments.shoping_fragment.HomeFragment
import com.example.interntask.Fragments.shoping_fragment.ProfileFragment
import com.example.interntask.Fragments.shoping_fragment.Top_dealsFragment
import com.example.interntask.R
import com.example.interntask.databinding.ActivityDashBoardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardActivity : AppCompatActivity() {

    private lateinit var homeFragment: HomeFragment
    private lateinit var topDealsfragment : Top_dealsFragment
    private lateinit var cartFragment: CartFragment
    private lateinit var profileFragment: ProfileFragment


    private var activeFragment: Fragment? = null
    private val binding by lazy {
        ActivityDashBoardBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        homeFragment = HomeFragment()
        topDealsfragment = Top_dealsFragment()
        cartFragment = CartFragment()
        profileFragment = ProfileFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.framlayout,homeFragment,"homefragment")
            .add(R.id.framlayout,topDealsfragment,"top_dealsfragment").hide(topDealsfragment)
            .add(R.id.framlayout,cartFragment,"cartfragment").hide(cartFragment)
            .add(R.id.framlayout,profileFragment,"profilefragment").hide(profileFragment)
            .commit()

            activeFragment=homeFragment





        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId)
            {
                R.id.homeFragment ->showfragment(homeFragment)

                R.id.top_dealsFragment->showfragment(topDealsfragment)

                R.id.cartFragment->showfragment(cartFragment)

                R.id.profileFragment->showfragment(profileFragment)
            }
            true
        }



    }

   private fun showfragment(fragment: Fragment){
        if (fragment == activeFragment) return
        supportFragmentManager.beginTransaction()
            .hide(activeFragment!!)
            .show(fragment)
            .commit()
        activeFragment = fragment
    }

}
