package com.ferranpons.issposition.issTracking;

import io.reactivex.Observable;

public interface IssTrackingInteractorInterface {
  Observable<IssTrackingApiInterface.CurrentPositionResponse> getCurrentPosition();

  Observable<IssTrackingApiInterface.PassTimesResponse> getPassTimes(double latitude,
      double longitude);

  Observable<IssTrackingApiInterface.PeopleInSpaceResponse> getPeopleInSpace();
}
