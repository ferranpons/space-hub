package com.ferranpons.issposition.issTracking;

import java.util.List;

public interface IssTrackingViewInterface {

  void showCurrentPositionError();

  void setIssPosition(IssTrackingApiInterface.IssPosition position);

  void willRetrievePassTimes();

  void didRetrievePassTimes();

  void showPassTimesError();

  void willRetrievePeopleInSpace();

  void didRetrievePeopleInSpace();

  void showPeopleInSpaceError();

  void showPassTimes(List<IssTrackingApiInterface.PassTime> passTimes);

  void showPeopleInSpace(List<IssTrackingApiInterface.Person> people);

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
    public void showPassTimes(List<IssTrackingApiInterface.PassTime> passTimes) {

    }

    @Override
    public void showPeopleInSpace(List<IssTrackingApiInterface.Person> people) {

    }
  }
}
