package com.ferranpons.issposition.issTracking;

import retrofit.RestAdapter;

public class IssTrackingApi {
	public static IssTrackingApiInterface getIssTrackingApi(String endPoint) {
		RestAdapter restAdapter =
			new RestAdapter.Builder()
				.setEndpoint(endPoint)
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.build();

		return restAdapter.create(IssTrackingApiInterface.class);
	}
}
