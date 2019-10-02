package com.ferranpons.spacehub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.ferranpons.spacehub.issLive.IssLiveFragment
import com.ferranpons.spacehub.schedule.ScheduleFragment
import com.ferranpons.spacehub.settings.SettingsFragment
import com.ferranpons.spacehub.upcomingLaunches.UpcomingLaunchesFragment
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.ashokvarma.bottomnavigation.BottomNavigationBar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val scheduleFragment = ScheduleFragment()
        fragmentTransaction.replace(R.id.content_frame, scheduleFragment, scheduleFragment.tag).commit()

        setupBottomBar()
    }

    private fun setupBottomBar() {
        val bottomNavigationBar = findViewById<View>(R.id.bottom_navigation_bar) as BottomNavigationBar

        bottomNavigationBar
                .addItem(BottomNavigationItem(R.drawable.ic_date_range_black_24dp, getString(R.string.iss_schedule_bottom_bar_button)))
                .addItem(BottomNavigationItem(R.drawable.ic_live_tv_black_24dp, getString(R.string.iss_live_bottom_bar_button)))
                .addItem(BottomNavigationItem(R.drawable.ic_flight_takeoff_black_24dp, getString(R.string.upcoming_launches_bottom_bar_button)))
                .addItem(BottomNavigationItem(R.drawable.ic_rss_feed_black_24dp, getString(R.string.nasa_news_bottom_bar_button)))
                .addItem(BottomNavigationItem(R.drawable.ic_settings_black_24dp, getString(R.string.settings_bottom_bar_button)))
                .initialise()

        bottomNavigationBar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                when (position) {
                    0 -> {
                        val scheduleFragment = ScheduleFragment()
                        fragmentTransaction.replace(R.id.content_frame, scheduleFragment, scheduleFragment.tag)
                    }
                    1 -> {
                        val issLiveFragment = IssLiveFragment()
                        fragmentTransaction.replace(R.id.content_frame, issLiveFragment, issLiveFragment.tag)
                    }
                    2 -> {
                        val upcomingLaunchesFragment = UpcomingLaunchesFragment()
                        fragmentTransaction.replace(R.id.content_frame, upcomingLaunchesFragment, upcomingLaunchesFragment.tag)
                    }
                    3 -> {
                        val settingsFragment = SettingsFragment()
                        fragmentTransaction.replace(R.id.content_frame, settingsFragment, settingsFragment.tag)
                    }
                }
                fragmentTransaction.commit()
            }
            override fun onTabUnselected(position: Int) {}
            override fun onTabReselected(position: Int) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about -> {
                val fm = supportFragmentManager
                val aboutDialog = AboutFragment()
                aboutDialog.show(fm, "fragment_edit_name")
                true
            }
            R.id.refreshCurrentPosition -> {
                true
            }
            else -> true
        }
    }
}
