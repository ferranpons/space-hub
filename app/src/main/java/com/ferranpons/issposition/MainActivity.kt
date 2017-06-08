package com.ferranpons.issposition

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import butterknife.ButterKnife
import com.ferranpons.issposition.issLive.IssLiveFragment
import com.ferranpons.issposition.schedule.ScheduleFragment
import com.ferranpons.issposition.settings.SettingsFragment
import com.ferranpons.issposition.upcomingLaunches.UpcomingLaunchesFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.roughike.bottombar.BottomBar

class MainActivity : AppCompatActivity() {

    private var connectivityChange: ConnectivityChange? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val scheduleFragment: ScheduleFragment = ScheduleFragment()
        fragmentTransaction.replace(R.id.content_frame, scheduleFragment, scheduleFragment.tag).commit()

        setupBottomBar()
    }

    private fun setupBottomBar() {
        val bottomBar: BottomBar = this.findViewById(R.id.bottomBar) as BottomBar
        bottomBar.setOnTabSelectListener({
            tabId ->
            run {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                if (tabId == R.id.tab_schedule) {
                    val scheduleFragment: ScheduleFragment = ScheduleFragment()
                    fragmentTransaction.replace(R.id.content_frame, scheduleFragment, scheduleFragment.tag)
                } else if (tabId == R.id.tab_iss_live) {
                    val issLiveFragment: IssLiveFragment = IssLiveFragment()
                    fragmentTransaction.replace(R.id.content_frame, issLiveFragment, issLiveFragment.tag)
                } else if (tabId == R.id.tab_upcoming_launches) {
                    val upcomingLaunchesFragment: UpcomingLaunchesFragment = UpcomingLaunchesFragment()
                    fragmentTransaction.replace(R.id.content_frame, upcomingLaunchesFragment, upcomingLaunchesFragment.tag)
                } else if (tabId == R.id.tab_settings) {
                    val settingsFragment: SettingsFragment = SettingsFragment()
                    fragmentTransaction.replace(R.id.content_frame, settingsFragment, settingsFragment.tag)
                }
                fragmentTransaction.commit()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(applicationContext)
        if (status == ConnectionResult.SERVICE_MISSING) {
            Toast.makeText(baseContext, R.string.common_google_play_services_unsupported_text, Toast.LENGTH_SHORT).show()
        }
        connectivityChange = ConnectivityChange()
        registerReceiver(connectivityChange, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about -> {
                val fm = supportFragmentManager
                val aboutDialog = AboutFragment()
                aboutDialog.show(fm, "fragment_edit_name")
                return true
            }
            R.id.refreshCurrentPosition -> {
                return true
            }
            else -> return true
        }
    }

    override fun onPause() {
        unregisterReceiver(connectivityChange)
        super.onPause()
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    private inner class ConnectivityChange : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
            if (isConnected) {
            }
        }
    }
}
