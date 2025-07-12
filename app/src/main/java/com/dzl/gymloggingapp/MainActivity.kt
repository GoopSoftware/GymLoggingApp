package com.dzl.gymloggingapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.dzl.gymloggingapp.cardio.CardioFragment
import com.dzl.gymloggingapp.databinding.ActivityMainBinding
import com.dzl.gymloggingapp.home.HomeFragment
import com.dzl.gymloggingapp.lifting.LiftingFragment
import com.dzl.gymloggingapp.logs.LogsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener(this)

    }


    private fun onHomeClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, HomeFragment())
        }
    }

    private fun onLiftingClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, LiftingFragment())
        }
    }

    private fun onCardioClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, CardioFragment())
        }
    }

    private fun onLogsClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, LogsFragment())
        }

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_home -> onHomeClicked()
            R.id.nav_lifting_log -> onLiftingClicked()
            R.id.nav_cardio_log -> onCardioClicked()
            R.id.nav_logs -> onLogsClicked()
            else -> return false
        }

        return true
    }


}