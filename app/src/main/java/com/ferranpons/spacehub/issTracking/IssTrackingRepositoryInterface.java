package com.ferranpons.spacehub.issTracking;

import io.reactivex.Observable;

public interface IssTrackingRepositoryInterface {
  Observable<IssTrackingApiInterface.CurrentPositionResponse> getCurrentPosition();

  Observable<IssTrackingApiInterface.PassTimesResponse> getPassTimes(double latitude,
      double longitude);

  Observable<IssTrackingApiInterface.PeopleInSpaceResponse> getPeopleInSpace();
}
