package com.ferranpons.issposition.issTracking;

import rx.Observable;

public class IssTrackingInteractor implements IssTrackingInteractorInterface {
	private final IssTrackingApiInterface api;

	public IssTrackingInteractor(IssTrackingApiInterface api) {
		this.api = api;
	}

	@Override
	public Observable<IssTrackingApiInterface.CurrentPositionResponse> getCurrentPosition() {
		return api.getCurrentPosition();
	}

	@Override public Observable<IssTrackingApiInterface.PassTimesResponse> getPassTimes(
		double latitude, double longitude) {
		return api.getPassTimes(latitude, longitude);
	}

	@Override public Observable<IssTrackingApiInterface.PeopleInSpaceResponse> getPeopleInSpace() {
		return api.getPeopleInSpace();
	}
}
