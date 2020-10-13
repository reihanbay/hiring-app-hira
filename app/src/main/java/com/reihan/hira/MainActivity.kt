package com.reihan.hira

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.reihan.hira.databinding.ActivityMainBinding
import com.reihan.hira.home.HomeFragment
import com.reihan.hira.profile.ProfileFragment
import com.reihan.hira.search.SearchFragment
import com.reihan.hira.project.ProjectFragment

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId())

        val Home = HomeFragment()
        val Search = SearchFragment()
        val Project = ProjectFragment()
        val Profile = ProfileFragment()

        currentNavigation(Home)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_homeFragment -> currentNavigation(Home)
                R.id.ic_searchFragment -> currentNavigation(Search)
                R.id.ic_projectFragment -> currentNavigation(Project)
                R.id.ic_profileFragment -> currentNavigation(Profile)
                else -> false
            }
        }
    }

    fun currentNavigation(fragment: Fragment): Boolean {
        if (fragment != null) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, fragment)
                    .commit()
            }
            return true
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(this, "Onstart()", Toast.LENGTH_LONG).show()
        Log.d("LifecycleMainActivity", "Onstart()")
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(this, "OnStop()", Toast.LENGTH_LONG).show()
        Log.d("LifecycleMainActivity", "OnStop()")
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "OnResume()", Toast.LENGTH_LONG).show()
        Log.d("LifecycleMainActivity", "OnResume()")
    }

    override fun onRestart() {
        super.onRestart()
        Toast.makeText(this, "OnRestart()", Toast.LENGTH_LONG).show()
        Log.d("LifecycleMainActivity", "OnRestart()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "OnDestroy()", Toast.LENGTH_LONG).show()
        Log.d("LifecycleMainActivity", "OnDestroy()")
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this, "OnPause()", Toast.LENGTH_LONG).show()
        Log.d("LifecycleMainActivity", "OnPause()")
    }
}