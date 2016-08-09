package com.ferranpons.issposition.issTracking;

import retrofit.RestAdapter;

public class IssTrackingApi {
  public static IssTrackingApiInterface getIssTrackingApi(String endPoint) {
    RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(endPoint).build();

    return restAdapter.create(IssTrackingApiInterface.class);
  }
}
