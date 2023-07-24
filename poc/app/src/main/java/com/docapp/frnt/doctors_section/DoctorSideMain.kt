package com.docapp.frnt.doctors_section

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.docapp.frnt.R
import com.docapp.frnt.data.companions.CommonFunctions.apiCallGetDocAppointments
import com.docapp.frnt.data.companions.CommonFunctions.getDocProfile
import com.docapp.frnt.data.companions.CommonFunctions.getSerializableExtra
import com.docapp.frnt.data.companions.Constants
import com.docapp.frnt.databinding.ActivityDoctorSideMainBinding
import com.docapp.frnt.databinding.NavHeaderDoctorSideMainBinding
import com.docapp.frnt.doctors_section.ui.home.HomeViewModel
import com.docapp.frnt.ui.login.LoginActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import java.math.BigInteger

class DoctorSideMain : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDoctorSideMainBinding
    private lateinit var sharedDetails: HomeViewModel
    private lateinit var userId: BigInteger
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDoctorSideMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarDoctorSideMain.toolbar)
        sharedDetails =
            ViewModelProvider(this)[HomeViewModel::class.java]
        binding.appBarDoctorSideMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.docDrawerLayout
        val navView: NavigationView = binding.doctorNavView
        val navHeaderMainBinding = NavHeaderDoctorSideMainBinding.bind(navView.getHeaderView(0))
        navHeaderMainBinding.docTextviewFirst.text = intent.getStringExtra(Constants.DISPLAY_NAME)
        navHeaderMainBinding.docTextviewSecond.text = intent.getStringExtra(Constants.USER_EMAIL)
        navView.viewTreeObserver.addOnGlobalLayoutListener {
            val k = ViewTreeObserver.OnGlobalLayoutListener {
                // Load the image using Glide or any other image loading library
                Glide.with(this@DoctorSideMain)
                    .asBitmap()
                    .load(
                        "${getString(R.string.dms_base)}/${getString(R.string.pic_get)}/${
                            intent.getStringExtra(
                                Constants.PROFILE_PIC_LOCATION
                            )
                        }"
                    )
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(navHeaderMainBinding.imageView)
            }
            navView.viewTreeObserver.removeOnGlobalLayoutListener(k)
            k.onGlobalLayout()

        }

        // Find the logout menu item
        val logoutMenuItem = navView.menu.findItem(R.id.nav_logout)

        // Set the click listener for the logout menu item
        logoutMenuItem.setOnMenuItemClickListener {
            // Handle logout logic here
            // For example, navigate to the login screen or clear user session

            // Navigate to the login screen and clear the activity stack
            val loginIntent = Intent(this@DoctorSideMain, LoginActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(loginIntent)

            true // Return true to indicate that the event has been handled
        }


        val navController = findNavController(R.id.nav_host_fragment_content_doctor_side_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        userId = getSerializableExtra(intent, Constants.UUID, BigInteger::class.java)!!
        sharedDetails.profileDetails.value = getDocProfile(this, userId)
        sharedDetails.appoList.value = apiCallGetDocAppointments(this, userId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.doctor_side_main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        sharedDetails.profileDetails.value = getDocProfile(this, userId)
        sharedDetails.appoList.value = apiCallGetDocAppointments(this, userId)
        val navController = findNavController(R.id.nav_host_fragment_content_doctor_side_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}