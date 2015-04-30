package com.ferranpons.issposition;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.util.Log;
import com.ferranpons.issposition.issTracking.IssTrackingApi;
import com.ferranpons.issposition.issTracking.IssTrackingApiInterface;
import com.ferranpons.issposition.issTracking.IssTrackingInteractor;
import com.ferranpons.issposition.issTracking.IssTrackingPresenter;
import com.ferranpons.issposition.issTracking.IssTrackingPresenterInterface;
import com.ferranpons.issposition.issTracking.IssTrackingViewInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements IssTrackingViewInterface {

	private GoogleMap mMap;
	private IssTrackingPresenterInterface issTrackingPresenter;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		issTrackingPresenter = new IssTrackingPresenter(new IssTrackingInteractor(IssTrackingApi.getIssTrackingApi("http://api.open-notify.org")));
		issTrackingPresenter.start(this);
		setUpMapIfNeeded();
	}

	@Override protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		issTrackingPresenter.retrieveCurrentPosition();
		issTrackingPresenter.retrievePeopleInSpace();
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
				R.id.map)).getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	@Override
	public void onDestroy() {
		issTrackingPresenter.stop();
		super.onDestroy();
	}

	private void setUpMap() {
		//mMap.setMyLocationEnabled(true);
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();

		Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
		if (location != null)
		{
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
				new LatLng(location.getLatitude(), location.getLongitude()), 13));

			CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
				.zoom(5)                   // Sets the zoom
				.bearing(0)                // Sets the orientation of the camera to east
				.tilt(40)                   // Sets the tilt of the camera to 30 degrees
				.build();                   // Creates a CameraPosition from the builder
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			mMap.addMarker(new MarkerOptions().position(
				new LatLng(location.getLatitude(), location.getLongitude())).title("Marker"));
			issTrackingPresenter.retrievePassTimes(location.getLatitude(), location.getLongitude());
		}
		//mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
	}

	@Override public void willRetrieveCurrentPosition() {
		Log.d("***CURRENT_POSITION", "willRetrieveCurrentPosition");
	}

	@Override public void didRetrieveCurrentPosition() {
		Log.d("***CURRENT_POSITION", "didRetrieveCurrentPosition");
	}

	@Override public void showNetworkError() {
		Log.d("***CURRENT_POSITION", "NETWORK ERROR");
	}

	@Override public void setIssPosition(IssTrackingApiInterface.IssPosition position) {
		Log.d("***CURRENT_POSITION", "SET ISS POSITION");
	}

	@Override public void willRetrievePassTimes() {
		Log.d("***CURRENT_POSITION", "willRetrievePassTimes");
	}

	@Override public void didRetrievePassTimes() {
		Log.d("***CURRENT_POSITION", "didRetrievePassTimes");
	}

	@Override public void willRetrievePeopleInSpace() {
		Log.d("***CURRENT_POSITION", "willRetrievePeopleInSpace");
	}

	@Override public void didRetrievePeopleInSpace() {
		Log.d("***CURRENT_POSITION", "didRetrievePeopleInSpace");
	}

	@Override public void showPassTimes(ArrayList<IssTrackingApiInterface.PassTime> passTimes) {
		Log.d("***CURRENT_POSITION", "SHOW PASS TIMES");
	}

	@Override public void showPeopleInSpace(ArrayList<IssTrackingApiInterface.Person> people) {
		Log.d("***CURRENT_POSITION", "SHOW PEOPLE IN SPACE");
	}
}
