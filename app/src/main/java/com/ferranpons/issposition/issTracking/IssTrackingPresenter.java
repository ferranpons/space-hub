package com.ferranpons.issposition.issTracking;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class IssTrackingPresenter implements IssTrackingPresenterInterface {
	private final IssTrackingInteractorInterface issTrackingInteractorInterface;
	private IssTrackingViewInterface view;
	private final IssTrackingViewInterface nullView = new IssTrackingViewInterface.NullView();
	private Subscription currentPositionSubscription;
	private Subscription passTimesSubscription;
	private Subscription peopleInSpaceSubscription;
	private Subscription timerSubscription;
	private final Scheduler scheduler;
	private final Observable<Long> timer = Observable.timer(60, 60, TimeUnit.SECONDS);

	public IssTrackingPresenter(IssTrackingInteractorInterface issTrackingInteractorInterface) {
		this(issTrackingInteractorInterface, AndroidSchedulers.mainThread());
	}

	public IssTrackingPresenter(IssTrackingInteractorInterface issTrackingInteractorInterface, Scheduler scheduler) {
		this.issTrackingInteractorInterface = issTrackingInteractorInterface;
		this.view = nullView;
		this.scheduler = scheduler;
	}

	@Override
	public void start(IssTrackingViewInterface view) {
		this.view = view;
	}

	@Override
	public void retrieveCurrentPosition() {
		view.willRetrieveCurrentPosition();
		currentPositionSubscription = issTrackingInteractorInterface.getCurrentPosition()
			.observeOn(scheduler)
			.subscribe(
				currentPositionResponse -> view.setIssPosition(currentPositionResponse.position)
				, throwable -> view.showNetworkError()
				, () -> {
					view.didRetrieveCurrentPosition();
					if (timerSubscription == null || timerSubscription.isUnsubscribed()) {
						timerSubscription = timer.observeOn(scheduler)
							.subscribe((numberOfTimes) -> retrieveCurrentPosition());
					}
				}
			);
	}

	@Override
	public void retrievePassTimes(double latitude, double longitude) {
		view.willRetrievePassTimes();
		passTimesSubscription = issTrackingInteractorInterface.getPassTimes(latitude, longitude)
			.observeOn(scheduler)
			.subscribe(
				passTimesResponse -> view.showPassTimes(passTimesResponse.passTimes)
				, throwable -> view.showNetworkError()
				, view::didRetrievePassTimes
			);
	}

	@Override
	public void retrievePeopleInSpace() {
		view.willRetrievePeopleInSpace();
		peopleInSpaceSubscription = issTrackingInteractorInterface.getPeopleInSpace()
			.observeOn(scheduler)
			.subscribe(
				peopleInSpaceResponse -> view.showPeopleInSpace(peopleInSpaceResponse.people)
				, throwable -> view.showNetworkError()
				, view::didRetrievePeopleInSpace
			);
	}

	@Override
	public void stop() {
		this.view = nullView;
		if (currentPositionSubscription != null) {
			currentPositionSubscription.unsubscribe();
		}
		if (passTimesSubscription != null) {
			passTimesSubscription.unsubscribe();
		}
		if (peopleInSpaceSubscription != null) {
			peopleInSpaceSubscription.unsubscribe();
		}
		if (timerSubscription != null) {
			timerSubscription.unsubscribe();
		}
	}
}
