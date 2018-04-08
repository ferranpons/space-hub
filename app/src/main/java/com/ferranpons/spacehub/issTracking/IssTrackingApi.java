package com.ferranpons.spacehub.issTracking;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class IssTrackingApi {
  public static IssTrackingApiInterface getIssTrackingApi(String endPoint) {
    Retrofit restAdapter = new Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(endPoint)
            .build();

    return restAdapter.create(IssTrackingApiInterface.class);
  }
}
