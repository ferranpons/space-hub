package com.ferranpons.issposition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.ferranpons.issposition.issTracking.IssTrackingApi;
import com.ferranpons.issposition.issTracking.IssTrackingApiInterface;
import com.ferranpons.issposition.issTracking.IssTrackingInteractor;
import com.ferranpons.issposition.issTracking.IssTrackingPresenter;
import com.ferranpons.issposition.issTracking.IssTrackingPresenterInterface;
import com.ferranpons.issposition.issTracking.IssTrackingViewInterface;
import com.ferranpons.issposition.passTimes.PassTimesAdapter;
import com.ferranpons.issposition.peopleInSpace.PeopleAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IssTrackingViewInterface {

  private GoogleMap map;
  private IssTrackingPresenterInterface issTrackingPresenter;

  @InjectView(R.id.peopleInSpaceListView) public ListView peopleInSpaceListView;

  @InjectView(R.id.progressPeopleInSpace) public ProgressBar peopleInSpaceProgressBar;

  @InjectView(R.id.collapsePeople) public ImageView peopleInSpaceCollapseImage;

  @InjectView(R.id.retryPeople) public ImageView peopleInSpaceRetryImage;

  @InjectView(R.id.collapseLayout) public LinearLayout peopleInSpaceCollapseButton;

  @InjectView(R.id.passTimesListView) public ListView passTimesListView;

  @InjectView(R.id.progressPassTimes) public ProgressBar passTimesProgressBar;

  @InjectView(R.id.collapsePassTimesLayout) public LinearLayout passTimesCollapseButton;

  @InjectView(R.id.collapsePassTimes) public ImageView passTimesCollapseImage;

  @InjectView(R.id.retryPassTimes) public ImageView passTimesRetryImage;

  private ConnectivityChange connectivityChange;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);
    flipPeopleInSpaceCollapseButton();
    flipPassTimesCollapseButton();
    issTrackingPresenter = new IssTrackingPresenter(
        new IssTrackingInteractor(IssTrackingApi.getIssTrackingApi("http://api.open-notify.org")));
    issTrackingPresenter.setView(this);
  }

  @OnClick(R.id.collapseLayout)
  public void collapsePeopleInSpaceView() {
    if (peopleInSpaceListView.getVisibility() == View.VISIBLE) {
      peopleInSpaceListView.setVisibility(View.GONE);
      flipPeopleInSpaceCollapseButton();
    } else {
      peopleInSpaceListView.setVisibility(View.VISIBLE);
      flipPeopleInSpaceCollapseButton();
    }
  }

  @OnClick(R.id.collapsePassTimesLayout)
  public void collapsePassTimesView() {
    if (passTimesListView.getVisibility() == View.VISIBLE) {
      passTimesListView.setVisibility(View.GONE);
      flipPassTimesCollapseButton();
    } else {
      passTimesListView.setVisibility(View.VISIBLE);
      flipPassTimesCollapseButton();
    }
  }

  @OnClick(R.id.retryPeople)
  public void retryRetrievePeopleInSpace() {
    issTrackingPresenter.retrievePeopleInSpace();
  }

  @Override
  protected void onResume() {
    super.onResume();
    loadContent();
    int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
    if (status == ConnectionResult.SERVICE_MISSING) {
      Toast.makeText(getBaseContext(), R.string.common_google_play_services_unsupported_text,
          Toast.LENGTH_SHORT).show();
    }
    connectivityChange = new ConnectivityChange();
    registerReceiver(connectivityChange, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
  }

  private void loadContent() {
    setUpMapIfNeeded();
    issTrackingPresenter.retrieveCurrentPosition();
    issTrackingPresenter.retrievePeopleInSpace();
  }

  public void setUpMapIfNeeded() {
    Location location = getLocation();
    if (map == null) {
      ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(
          googleMap -> {
            if (map != null && location != null) {
              issTrackingPresenter.retrievePassTimes(location.getLatitude(),
                  location.getLongitude());
              setUpMap(location);
            } else {
              showPassTimesError();
            }
          });
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
      case R.id.refreshCurrentPosition:
        issTrackingPresenter.retrieveCurrentPosition();
        return true;
      default:
        return true;
    }
  }

  @Override
  protected void onPause() {
    unregisterReceiver(connectivityChange);
    super.onPause();
  }

  @Override
  public void onDestroy() {
    issTrackingPresenter.stop();
    super.onDestroy();
  }

  private void setUpMap(Location location) {
    map.animateCamera(CameraUpdateFactory.newLatLngZoom(
        new LatLng(location.getLatitude(), location.getLongitude()), 13));
    CameraPosition cameraPosition = new CameraPosition.Builder().target(
        new LatLng(location.getLatitude(), location.getLongitude()))
        .zoom(3)
        .bearing(0)
        .tilt(40)
        .build();
    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    map.addMarker(
        new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()))
            .title("Marker"));
  }

  private Location getLocation() {
    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    Criteria criteria = new Criteria();

    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
      public void onLocationChanged(Location location) {
        issTrackingPresenter.retrievePassTimes(location.getLatitude(), location.getLongitude());
        setUpMap(location);
      }

      public void onStatusChanged(String provider, int status, Bundle extras) {
      }

      public void onProviderEnabled(String provider) {
      }

      public void onProviderDisabled(String provider) {
      }
    };

    // Register the listener with the Location Manager to receive location updates
    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this,
        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
      //    locationListener);
    }

    //return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
    return null;
  }

  @Override
  public void setIssPosition(IssTrackingApiInterface.IssPosition position) {
    if (map != null) {
      map.addMarker(
          new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.iss_marker))
              .position(new LatLng(position.latitude, position.longitude))
              .title("Marker"));
    }
  }

  @Override
  public void willRetrievePassTimes() {
    passTimesProgressBar.setVisibility(View.VISIBLE);
    passTimesCollapseImage.setVisibility(View.GONE);
    passTimesRetryImage.setVisibility(View.GONE);
  }

  @Override
  public void showPassTimes(List<IssTrackingApiInterface.PassTime> passTimes) {
    ListAdapter passTimesAdapter = new PassTimesAdapter(getApplicationContext(), passTimes);
    passTimesListView.setAdapter(passTimesAdapter);
  }

  @Override
  public void didRetrievePassTimes() {
    passTimesProgressBar.setVisibility(View.GONE);
    passTimesCollapseImage.setVisibility(View.VISIBLE);
  }

  @Override
  public void showPassTimesError() {
    passTimesProgressBar.setVisibility(View.GONE);
    passTimesRetryImage.setVisibility(View.VISIBLE);
    passTimesCollapseImage.setVisibility(View.GONE);
  }

  @Override
  public void willRetrievePeopleInSpace() {
    peopleInSpaceProgressBar.setVisibility(View.VISIBLE);
    peopleInSpaceCollapseImage.setVisibility(View.GONE);
    peopleInSpaceRetryImage.setVisibility(View.GONE);
  }

  @Override
  public void showPeopleInSpace(List<IssTrackingApiInterface.Person> people) {
    ListAdapter peopleAdapter = new PeopleAdapter(getApplicationContext(), people);
    peopleInSpaceListView.setAdapter(peopleAdapter);
  }

  @Override
  public void didRetrievePeopleInSpace() {
    peopleInSpaceProgressBar.setVisibility(View.GONE);
    peopleInSpaceCollapseImage.setVisibility(View.VISIBLE);
  }

  @Override
  public void showPeopleInSpaceError() {
    peopleInSpaceProgressBar.setVisibility(View.GONE);
    peopleInSpaceRetryImage.setVisibility(View.VISIBLE);
    peopleInSpaceCollapseImage.setVisibility(View.GONE);
  }

  @Override
  public void showCurrentPositionError() {
    Toast.makeText(getBaseContext(), R.string.toast_network_error, Toast.LENGTH_SHORT).show();
  }

  private void flipPeopleInSpaceCollapseButton() {
    Bitmap bitmap = ((BitmapDrawable) peopleInSpaceCollapseImage.getDrawable()).getBitmap();
    peopleInSpaceCollapseImage.setImageBitmap(flipVertical(bitmap));
  }

  private void flipPassTimesCollapseButton() {
    Bitmap bitmap = ((BitmapDrawable) passTimesCollapseImage.getDrawable()).getBitmap();
    passTimesCollapseImage.setImageBitmap(flipVertical(bitmap));
  }

  private Bitmap flipVertical(Bitmap src) {
    Matrix matrix = new Matrix();
    matrix.preScale(1.0f, -1.0f);
    return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
  }

  private class ConnectivityChange extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      ConnectivityManager cm =
          (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

      NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
      boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
      if (isConnected) {
        loadContent();
      }
    }
  }
}
