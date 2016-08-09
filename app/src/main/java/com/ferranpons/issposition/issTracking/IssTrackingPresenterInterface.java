package com.ferranpons.issposition.issTracking;

public interface IssTrackingPresenterInterface {
  void retrieveCurrentPosition();

  void retrievePassTimes(double latitude, double longitude);

  void retrievePeopleInSpace();

  void setView(IssTrackingViewInterface view);

  void stop();
}
