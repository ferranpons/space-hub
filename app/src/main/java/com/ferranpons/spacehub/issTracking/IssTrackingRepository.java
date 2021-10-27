package com.ferranpons.spacehub.issTracking;

import io.reactivex.Observable;

public class IssTrackingRepository implements IssTrackingRepositoryInterface {
  private final IssTrackingApiInterface api;

  public IssTrackingRepository(IssTrackingApiInterface api) {
    this.api = api;
  }

  @Override
  public Observable<IssTrackingApiInterface.CurrentPositionResponse> getCurrentPosition() {
    return api.getCurrentPosition();
  }

  @Override
  public Observable<IssTrackingApiInterface.PassTimesResponse> getPassTimes(double latitude,
      double longitude) {
    return api.getPassTimes(latitude, longitude);
  }

  @Override
  public Observable<IssTrackingApiInterface.PeopleInSpaceResponse> getPeopleInSpace() {
    return api.getPeopleInSpace();
  }
}
