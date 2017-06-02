package com.ferranpons.issposition

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
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
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.ferranpons.issposition.issTracking.*
import com.ferranpons.issposition.passTimes.PassTimesAdapter
import com.ferranpons.issposition.peopleInSpace.PeopleAdapter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), IssTrackingViewInterface {

    private val map: GoogleMap? = null
    private var issTrackingPresenter: IssTrackingPresenterInterface? = null

    @BindView(R.id.peopleInSpaceListView) var peopleInSpaceListView: ListView? = null

    @BindView(R.id.progressPeopleInSpace) var peopleInSpaceProgressBar: ProgressBar? = null

    @BindView(R.id.collapsePeople) var peopleInSpaceCollapseImage: ImageView? = null

    @BindView(R.id.retryPeople) var peopleInSpaceRetryImage: ImageView? = null

    @BindView(R.id.collapseLayout) var peopleInSpaceCollapseButton: LinearLayout? = null

    @BindView(R.id.passTimesListView) var passTimesListView: ListView? = null

    @BindView(R.id.progressPassTimes) var passTimesProgressBar: ProgressBar? = null

    @BindView(R.id.collapsePassTimesLayout) var passTimesCollapseButton: LinearLayout? = null

    @BindView(R.id.collapsePassTimes) var passTimesCollapseImage: ImageView? = null

    @BindView(R.id.retryPassTimes) var passTimesRetryImage: ImageView? = null

    private var connectivityChange: ConnectivityChange? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        //flipPeopleInSpaceCollapseButton()
        //flipPassTimesCollapseButton()
        issTrackingPresenter = IssTrackingPresenter(IssTrackingInteractor(IssTrackingApi.getIssTrackingApi("http://api.open-notify.org")))
        (issTrackingPresenter as IssTrackingPresenter).setView(this)
    }

    @OnClick(R.id.collapseLayout)
    fun collapsePeopleInSpaceView() {
        if (peopleInSpaceListView?.visibility == View.VISIBLE) {
            peopleInSpaceListView?.visibility = View.GONE
            //flipPeopleInSpaceCollapseButton()
        } else {
            peopleInSpaceListView?.visibility = View.VISIBLE
            //flipPeopleInSpaceCollapseButton()
        }
    }

    @OnClick(R.id.collapsePassTimesLayout)
    fun collapsePassTimesView() {
        if (passTimesListView?.visibility == View.VISIBLE) {
            passTimesListView?.visibility = View.GONE
            //flipPassTimesCollapseButton()
        } else {
            passTimesListView?.visibility = View.VISIBLE
            //flipPassTimesCollapseButton()
        }
    }

    @OnClick(R.id.retryPeople)
    fun retryRetrievePeopleInSpace() {
        issTrackingPresenter!!.retrievePeopleInSpace()
    }

    override fun onResume() {
        super.onResume()
        loadContent()
        val status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(applicationContext)
        if (status == ConnectionResult.SERVICE_MISSING) {
            Toast.makeText(baseContext, R.string.common_google_play_services_unsupported_text, Toast.LENGTH_SHORT).show()
        }
        connectivityChange = ConnectivityChange()
        registerReceiver(connectivityChange, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun loadContent() {
        setUpMapIfNeeded()
        issTrackingPresenter?.retrieveCurrentPosition()
        issTrackingPresenter?.retrievePeopleInSpace()
    }

    fun setUpMapIfNeeded() {
        val location = location
        if (map == null) {
            (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync { googleMap ->
                if (map != null && location != null) {
                    issTrackingPresenter!!.retrievePassTimes(location.latitude, location.longitude)
                    setUpMap(location)
                } else {
                    showPassTimesError()
                }
            }
        }
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
                issTrackingPresenter!!.retrieveCurrentPosition()
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
        issTrackingPresenter!!.stop()
        super.onDestroy()
    }

    private fun setUpMap(location: Location) {
        map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 13f))
        val cameraPosition = CameraPosition.Builder().target(LatLng(location.latitude, location.longitude))
                .zoom(3f)
                .bearing(0f)
                .tilt(40f)
                .build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        map.addMarker(MarkerOptions().position(LatLng(location.latitude, location.longitude)).title("Marker"))
    }

    private // Define a listener that responds to location updates
            // Register the listener with the Location Manager to receive location updates
    val location: Location?
        get() {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            val locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    issTrackingPresenter!!.retrievePassTimes(location.latitude, location.longitude)
                    setUpMap(location)
                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

                override fun onProviderEnabled(provider: String) {}

                override fun onProviderDisabled(provider: String) {}
            }
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                try {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
                } catch (ignore: SecurityException) {
                }

            }

            try {
                return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false))
            } catch (e: IllegalArgumentException) {
                return null
            }

        }

    override fun setIssPosition(position: IssTrackingApiInterface.IssPosition) {
        map?.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.iss_marker))
                .position(LatLng(position.latitude, position.longitude))
                .title("Marker"))
    }

    override fun willRetrievePassTimes() {
        passTimesProgressBar?.visibility = View.VISIBLE
        passTimesCollapseImage?.visibility = View.GONE
        passTimesRetryImage?.visibility = View.GONE
    }

    override fun showPassTimes(passTimes: List<IssTrackingApiInterface.PassTime>) {
        val passTimesAdapter = PassTimesAdapter(applicationContext, passTimes)
        passTimesListView?.adapter = passTimesAdapter
    }

    override fun didRetrievePassTimes() {
        passTimesProgressBar?.visibility = View.GONE
        passTimesCollapseImage?.visibility = View.VISIBLE
    }

    override fun showPassTimesError() {
        passTimesProgressBar?.visibility = View.GONE
        passTimesRetryImage?.visibility = View.VISIBLE
        passTimesCollapseImage?.visibility = View.GONE
    }

    override fun willRetrievePeopleInSpace() {
        peopleInSpaceProgressBar?.visibility = View.VISIBLE
        peopleInSpaceCollapseImage?.visibility = View.GONE
        peopleInSpaceRetryImage?.visibility = View.GONE
    }

    override fun showPeopleInSpace(people: List<IssTrackingApiInterface.Person>) {
        val peopleAdapter = PeopleAdapter(applicationContext, people)
        peopleInSpaceListView!!.adapter = peopleAdapter
    }

    override fun didRetrievePeopleInSpace() {
        peopleInSpaceProgressBar?.visibility = View.GONE
        peopleInSpaceCollapseImage?.visibility = View.VISIBLE
    }

    override fun showPeopleInSpaceError() {
        peopleInSpaceProgressBar?.visibility = View.GONE
        peopleInSpaceRetryImage?.visibility = View.VISIBLE
        peopleInSpaceCollapseImage?.visibility = View.GONE
    }

    override fun showCurrentPositionError() {
        Toast.makeText(baseContext, R.string.toast_network_error, Toast.LENGTH_SHORT).show()
    }

    private fun flipPeopleInSpaceCollapseButton() {
        val bitmap = (peopleInSpaceCollapseImage?.drawable as BitmapDrawable).bitmap
        peopleInSpaceCollapseImage?.setImageBitmap(flipVertical(bitmap))
    }

    private fun flipPassTimesCollapseButton() {
        val bitmap = (passTimesCollapseImage?.drawable as BitmapDrawable).bitmap
        passTimesCollapseImage?.setImageBitmap(flipVertical(bitmap))
    }

    private fun flipVertical(src: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.preScale(1.0f, -1.0f)
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }

    private inner class ConnectivityChange : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
            if (isConnected) {
                loadContent()
            }
        }
    }
}
