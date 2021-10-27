package com.ferranpons.spacehub.issTracking;

public interface IssTrackingPresenterInterface {
  void retrieveCurrentPosition();

  void retrievePassTimes(double latitude, double longitude);

  void retrievePeopleInSpace();

  void setView(IssTrackingView view);

  void stop();
}
