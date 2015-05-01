package com.ferranpons.issposition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.ferranpons.issposition.issTracking.IssTrackingApi;
import com.ferranpons.issposition.issTracking.IssTrackingApiInterface;
import com.ferranpons.issposition.issTracking.IssTrackingInteractor;
import com.ferranpons.issposition.issTracking.IssTrackingPresenter;
import com.ferranpons.issposition.issTracking.IssTrackingPresenterInterface;
import com.ferranpons.issposition.issTracking.IssTrackingViewInterface;
import com.ferranpons.issposition.peopleInSpace.PeopleAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IssTrackingViewInterface {

	private ListView peopleInSpaceListView;
	private GoogleMap map;
	private IssTrackingPresenterInterface issTrackingPresenter;
	private ProgressBar peopleInSpaceProgressBar;
	private ImageView peopleInSpaceCollapseButton;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpViews();
		issTrackingPresenter = new IssTrackingPresenter(new IssTrackingInteractor(IssTrackingApi.getIssTrackingApi("http://api.open-notify.org")));
		issTrackingPresenter.start(this);
		setUpMapIfNeeded();
	}

	private void setUpViews() {
		peopleInSpaceListView = (ListView) findViewById(R.id.peopleInSpaceListView);
		peopleInSpaceProgressBar = (ProgressBar) findViewById(R.id.progressPeopleInSpace);
		peopleInSpaceCollapseButton = (ImageView) findViewById(R.id.collapsePeople);
		peopleInSpaceCollapseButton.setOnClickListener(view -> {
			if (peopleInSpaceListView.getVisibility() == View.VISIBLE) {
				peopleInSpaceListView.setVisibility(View.GONE);
				flipPeopleInSpaceCollapseButton();
			} else {
				peopleInSpaceListView.setVisibility(View.VISIBLE);
				flipPeopleInSpaceCollapseButton();
			}
		});
	}

	@Override protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		issTrackingPresenter.retrieveCurrentPosition();
		issTrackingPresenter.retrievePeopleInSpace();
	}

	private void setUpMapIfNeeded() {
		Location location = getLocation();
		if (map == null) {
			map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
				R.id.map)).getMap();
			if (map != null && location != null) {
				issTrackingPresenter.retrievePassTimes(location.getLatitude(), location.getLongitude());
				setUpMap(location);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.about:
				FragmentManager fm = getSupportFragmentManager();
				AboutFragment aboutDialog = new AboutFragment();
				aboutDialog.show(fm, "fragment_edit_name");
				return true;
			case R.id.refreshPassTimes:
				issTrackingPresenter.retrieveCurrentPosition();
				Location location = getLocation();
				if (location != null) {
					issTrackingPresenter.retrievePassTimes(location.getLatitude(), location.getLongitude());
				}
				return true;
			default:
				return true;
		}
	}

	@Override
	public void onDestroy() {
		issTrackingPresenter.stop();
		super.onDestroy();
	}

	private void setUpMap(Location location) {
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(
			new LatLng(location.getLatitude(), location.getLongitude()), 13));
		CameraPosition cameraPosition = new CameraPosition.Builder()
			.target(new LatLng(location.getLatitude(), location.getLongitude()))
			.zoom(5)
			.bearing(0)
			.tilt(40)
			.build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		map.addMarker(new MarkerOptions().position(
			new LatLng(location.getLatitude(), location.getLongitude())).title("Marker"));
	}

	private Location getLocation() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
	}

	@Override
	public void willRetrieveCurrentPosition() {
		Log.d("***CURRENT_POSITION", "willRetrieveCurrentPosition");
	}

	@Override
	public void setIssPosition(IssTrackingApiInterface.IssPosition position) {
		if (map != null) {
			map.addMarker(new MarkerOptions().position(
				new LatLng(position.latitude, position.longitude)).title("Marker"));
		}
	}

	@Override public void didRetrieveCurrentPosition() {
		Log.d("***CURRENT_POSITION", "didRetrieveCurrentPosition");
	}

	@Override
	public void willRetrievePassTimes() {
		Log.d("***CURRENT_POSITION", "willRetrievePassTimes");
	}

	@Override
	public void showPassTimes(ArrayList<IssTrackingApiInterface.PassTime> passTimes) {
		Log.d("***CURRENT_POSITION", "SHOW PASS TIMES");
	}

	@Override
	public void didRetrievePassTimes() {
		Log.d("***CURRENT_POSITION", "didRetrievePassTimes");
	}

	@Override
	public void willRetrievePeopleInSpace() {
		peopleInSpaceProgressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void showPeopleInSpace(ArrayList<IssTrackingApiInterface.Person> people) {
		ListAdapter peopleAdapter = new PeopleAdapter(getApplicationContext(), people);
		peopleInSpaceListView.setAdapter(peopleAdapter);
	}

	@Override
	public void didRetrievePeopleInSpace() {
		peopleInSpaceProgressBar.setVisibility(View.GONE);
	}

	@Override
	public void showNetworkError() {
		Toast.makeText(getBaseContext(), R.string.toast_network_error, Toast.LENGTH_SHORT).show();
	}

	private void flipPeopleInSpaceCollapseButton() {
		Bitmap bitmap = ((BitmapDrawable)peopleInSpaceCollapseButton.getDrawable()).getBitmap();
		peopleInSpaceCollapseButton.setImageBitmap(flipVertical(bitmap));
	}

	public Bitmap flipVertical(Bitmap src) {
		Matrix matrix = new Matrix();
		matrix.preScale(1.0f, -1.0f);
		return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
	}
}
