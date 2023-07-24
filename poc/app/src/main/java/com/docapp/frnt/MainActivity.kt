package com.docapp.frnt

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
import com.docapp.frnt.data.companions.CommonFunctions.apiCallGetDocuDetails
import com.docapp.frnt.data.companions.CommonFunctions.apiCallGetPresDetails
import com.docapp.frnt.data.companions.CommonFunctions.getSerializableExtra
import com.docapp.frnt.data.companions.Constants.DISPLAY_NAME
import com.docapp.frnt.data.companions.Constants.PROFILE_PIC_LOCATION
import com.docapp.frnt.data.companions.Constants.USER_EMAIL
import com.docapp.frnt.data.companions.Constants.UUID
import com.docapp.frnt.data.model.SharedDocuList
import com.docapp.frnt.databinding.ActivityMainBinding
import com.docapp.frnt.databinding.NavHeaderMainBinding
import com.docapp.frnt.ui.login.LoginActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import java.math.BigInteger

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedDocuList: SharedDocuList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedDocuList = ViewModelProvider(this)[SharedDocuList::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(
                view,
                "Hey!! ${intent.getStringExtra(DISPLAY_NAME)}", Snackbar.LENGTH_LONG
            ).setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navHeaderMainBinding = NavHeaderMainBinding.bind(navView.getHeaderView(0))
        navHeaderMainBinding.textviewFirst.text = intent.getStringExtra(DISPLAY_NAME)
        navHeaderMainBinding.textviewSecond.text = intent.getStringExtra(USER_EMAIL)
        navView.viewTreeObserver.addOnGlobalLayoutListener {
            val k = ViewTreeObserver.OnGlobalLayoutListener {
                // Load the image using Glide or any other image loading library
                Glide.with(this@MainActivity)
                    .asBitmap()
                    .load(
                        "${getString(R.string.dms_base)}/${getString(R.string.pic_get)}/${
                            intent.getStringExtra(
                                PROFILE_PIC_LOCATION
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
            val loginIntent = Intent(this@MainActivity, LoginActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(loginIntent)

            true // Return true to indicate that the event has been handled
        }
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.graph.findNode(R.id.FirstFragment)?.label =
            getString(R.string.welcome, intent.getStringExtra("displayName"))
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        sharedDocuList.docuList.value = apiCallGetDocuDetails(
            this,
            patientId = getSerializableExtra(intent, UUID, BigInteger::class.java)!!
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.settings_button, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        sharedDocuList.docuList.value = apiCallGetDocuDetails(
            this,
            patientId = getSerializableExtra(intent, UUID, BigInteger::class.java)!!
        )
        sharedDocuList.appoList.value = apiCallGetPresDetails(
            this, patientId = getSerializableExtra(
                intent,
                UUID, BigInteger::class.java
            )!!
        )
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}