package com.embeltech.meterreading.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.embeltech.meterreading.R
import com.embeltech.meterreading.config.BaseActivity
import com.embeltech.meterreading.ui.login.LoginActivity
import com.embeltech.meterreading.ui.scanbeacon.ScanBeaconActivity
import com.google.android.material.navigation.NavigationView


class MainActivity : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    var drawerLayout: DrawerLayout? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent.inject(this)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val userName = navView.findViewById<AppCompatTextView>(R.id.userNameNavView)
        val navController = findNavController(R.id.nav_host_fragment)
        //navView.findViewById<AppCompatTextView>(R.id.userNameNavView).setText(appPreferences.getUserName().toString())

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_devices, R.id.nav_report,R.id.nav_device,R.id.nav_add_user, R.id.nav_billing,R.id.nav_issue, R.id.nav_statistics,R.id.nav_logout
            ), drawerLayout
        )

        val role = appPreferences.getUserRole()!!
        val menus = navView.menu
        Log.i("@main", "role============>$role")
        when (role) {
            "super-admin" -> {
                menus.findItem(R.id.nav_billing).isVisible = true
                menus.findItem(R.id.nav_add_user).isVisible = true
            }
            "user" -> {
                menus.findItem(R.id.nav_billing).isVisible = false
                menus.findItem(R.id.nav_add_user).isVisible = false
                menus.findItem(R.id.nav_issue).isVisible = false
            }
//            "admin" -> {
//                menus.findItem(R.id.nav_billing).isVisible = false
//                menus.findItem(R.id.nav_add_user).isVisible = false
//            }

            // Log.i("@main","==========>"+userName.text.toString())
            // userName.text = appPreferences.getUserName()
            // setNavigationViewListener()
        }
        val header=navView.getHeaderView(0)
        header.findViewById<AppCompatTextView>(R.id.userNameNavView).setText(appPreferences.getUserName()+"\n"+appPreferences.getUserRole()!!.toUpperCase())
       // Log.i("@main","==========>"+userName.text.toString())
       // userName.text = appPreferences.getUserName()
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
       // setNavigationViewListener()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_scan_device -> {
                val intent = Intent(this, ScanBeaconActivity::class.java)
                startActivity(intent)
            }

        }

        return false
    }




    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    }
    private fun setNavigationViewListener() {
        val navigationView =
            findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                finish()
                startActivity(Intent(this,LoginActivity::class.java))
            }
        }
        //close navigation drawer
        drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }
}