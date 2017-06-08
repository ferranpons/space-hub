package com.ferranpons.issposition.issTracking;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;

public class IssTrackingPresenter implements IssTrackingPresenterInterface {
  private final IssTrackingInteractorInterface issTrackingInteractorInterface;
  private IssTrackingViewInterface view;
  private final IssTrackingViewInterface nullView = new IssTrackingViewInterface.NullView();
  private Disposable timerSubscription;
  private CompositeDisposable compositeDisposable = new CompositeDisposable();
  private final Scheduler scheduler;
  private final Observable<Long> timer = Observable.timer(60, TimeUnit.SECONDS, AndroidSchedulers.mainThread());

  public IssTrackingPresenter(IssTrackingInteractorInterface issTrackingInteractorInterface) {
    this(issTrackingInteractorInterface, AndroidSchedulers.mainThread());
  }

  private IssTrackingPresenter(IssTrackingInteractorInterface issTrackingInteractorInterface, Scheduler scheduler) {
    this.issTrackingInteractorInterface = issTrackingInteractorInterface;
    this.view = nullView;
    this.scheduler = scheduler;
  }

  @Override
  public void setView(IssTrackingViewInterface view) {
    this.view = view;
  }

  @Override
  public void retrieveCurrentPosition() {
    Disposable currentPositionSubscription = issTrackingInteractorInterface.getCurrentPosition()
        .observeOn(scheduler)
        .subscribe(currentPositionResponse -> view.setIssPosition(currentPositionResponse.position),
            throwable -> view.showCurrentPositionError(), () -> {
              if (timerSubscription == null || timerSubscription.isDisposed()) {
                timerSubscription = timer.observeOn(scheduler)
                    .subscribe((numberOfTimes) -> retrieveCurrentPosition());
                compositeDisposable.add(timerSubscription);
              }
            });
    compositeDisposable.add(currentPositionSubscription);
  }

  @Override
  public void retrievePassTimes(double latitude, double longitude) {
    view.willRetrievePassTimes();
    Disposable passTimesSubscription = issTrackingInteractorInterface.getPassTimes(latitude, longitude)
        .observeOn(scheduler)
        .subscribe(passTimesResponse -> view.showPassTimes(passTimesResponse.passTimes),
            throwable -> view.showPassTimesError(), view::didRetrievePassTimes);
    compositeDisposable.add(passTimesSubscription);
  }

  @Override
  public void retrievePeopleInSpace() {
    view.willRetrievePeopleInSpace();
    Disposable peopleInSpaceSubscription = issTrackingInteractorInterface.getPeopleInSpace()
        .observeOn(scheduler)
        .subscribe(peopleInSpaceResponse -> view.showPeopleInSpace(peopleInSpaceResponse.people),
            throwable -> view.showPeopleInSpaceError(), view::didRetrievePeopleInSpace);
    compositeDisposable.add(peopleInSpaceSubscription);
  }

  @Override
  public void stop() {
    this.view = nullView;
    compositeDisposable.dispose();
  }
}
