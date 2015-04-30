package com.ferranpons.issposition.issTracking;

import java.util.ArrayList;

public interface IssTrackingViewInterface {
	void willRetrieveCurrentPosition();

	void didRetrieveCurrentPosition();

	void showNetworkError();

	void setIssPosition(IssTrackingApiInterface.IssPosition position);

	void willRetrievePassTimes();

	void didRetrievePassTimes();

	void willRetrievePeopleInSpace();

	void didRetrievePeopleInSpace();

	void showPassTimes(ArrayList<IssTrackingApiInterface.PassTime> passTimes);

	void showPeopleInSpace(ArrayList<IssTrackingApiInterface.Person> people);

	class NullView implements IssTrackingViewInterface {
		@Override public void willRetrieveCurrentPosition() {

		}

		@Override public void didRetrieveCurrentPosition() {

		}

		@Override public void showNetworkError() {

		}

		@Override public void setIssPosition(IssTrackingApiInterface.IssPosition position) {

		}

		@Override public void willRetrievePassTimes() {

		}

		@Override public void didRetrievePassTimes() {

		}

		@Override public void willRetrievePeopleInSpace() {

		}

		@Override public void didRetrievePeopleInSpace() {

		}

		@Override public void showPassTimes(ArrayList<IssTrackingApiInterface.PassTime> passTimes) {

		}

		@Override public void showPeopleInSpace(ArrayList<IssTrackingApiInterface.Person> people) {

		}
	}
}
