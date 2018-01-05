package com.ferranpons.issposition

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import butterknife.ButterKnife
import com.ferranpons.issposition.issLive.IssLiveFragment
import com.ferranpons.issposition.schedule.ScheduleFragment
import com.ferranpons.issposition.settings.SettingsFragment
import com.ferranpons.issposition.upcomingLaunches.UpcomingLaunchesFragment

class MainActivity : AppCompatActivity() {

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
        /*val bottomBar: BottomBar = this.findViewById(R.id.bottomBar) as BottomBar
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
        })*/
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
}
