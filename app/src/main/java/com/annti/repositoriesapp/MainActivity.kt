package com.annti.repositoriesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.annti.repositoriesapp.databinding.ActivityMainBinding
import com.annti.repositoriesapp.presentation.download.DownloadFragment
import com.annti.repositoriesapp.presentation.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCurrentFragment(MainFragment(), "Search")

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mainFragment -> {
                    setCurrentFragment(MainFragment(), "Search")
                }
                R.id.downloadFragment -> {
                    setCurrentFragment(DownloadFragment(), "Downloads")
                }
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }
        binding.toolbar.title = title
    }

}