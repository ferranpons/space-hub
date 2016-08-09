package com.ferranpons.issposition.issTracking;

import java.util.ArrayList;

public interface IssTrackingViewInterface {

  void showCurrentPositionError();

  void setIssPosition(IssTrackingApiInterface.IssPosition position);

  void willRetrievePassTimes();

  void didRetrievePassTimes();

  void showPassTimesError();

  void willRetrievePeopleInSpace();

  void didRetrievePeopleInSpace();

  void showPeopleInSpaceError();

  void showPassTimes(ArrayList<IssTrackingApiInterface.PassTime> passTimes);

  void showPeopleInSpace(ArrayList<IssTrackingApiInterface.Person> people);

  class NullView implements IssTrackingViewInterface {

    @Override
    public void showCurrentPositionError() {

    }

    @Override
    public void setIssPosition(IssTrackingApiInterface.IssPosition position) {

    }

    @Override
    public void willRetrievePassTimes() {

    }

    @Override
    public void didRetrievePassTimes() {

    }

    @Override
    public void showPassTimesError() {

    }

    @Override
    public void willRetrievePeopleInSpace() {

    }

    @Override
    public void didRetrievePeopleInSpace() {

    }

    @Override
    public void showPeopleInSpaceError() {

    }

    @Override
    public void showPassTimes(ArrayList<IssTrackingApiInterface.PassTime> passTimes) {

    }

    @Override
    public void showPeopleInSpace(ArrayList<IssTrackingApiInterface.Person> people) {

    }
  }
}
