package com.ferranpons.spacehub.issTracking;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class IssTrackingPresenter implements IssTrackingPresenterInterface {
  private final IssTrackingRepositoryInterface issTrackingRepositoryInterface;
  private IssTrackingView view;
  private final IssTrackingView nullView = new IssTrackingView.NullView();
  private Disposable timerSubscription;
  private final CompositeDisposable compositeDisposable = new CompositeDisposable();
  private final Scheduler scheduler;
  private final Observable<Long> timer = Observable.timer(60, TimeUnit.SECONDS, AndroidSchedulers.mainThread());

  public IssTrackingPresenter(IssTrackingRepositoryInterface issTrackingRepositoryInterface) {
    this(issTrackingRepositoryInterface, AndroidSchedulers.mainThread());
  }

  private IssTrackingPresenter(IssTrackingRepositoryInterface issTrackingRepositoryInterface, Scheduler scheduler) {
    this.issTrackingRepositoryInterface = issTrackingRepositoryInterface;
    this.view = nullView;
    this.scheduler = scheduler;
  }

  @Override
  public void setView(IssTrackingView view) {
    this.view = view;
  }

  @Override
  public void retrieveCurrentPosition() {
    Disposable currentPositionSubscription = issTrackingRepositoryInterface.getCurrentPosition()
        .subscribeOn(Schedulers.io())
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
    Disposable passTimesSubscription = issTrackingRepositoryInterface.getPassTimes(latitude, longitude)
        .subscribeOn(Schedulers.io())
        .observeOn(scheduler)
        .subscribe(passTimesResponse -> view.showPassTimes(passTimesResponse.passTimes),
            throwable -> view.showPassTimesError(), view::didRetrievePassTimes);
    compositeDisposable.add(passTimesSubscription);
  }

  @Override
  public void retrievePeopleInSpace() {
    view.willRetrievePeopleInSpace();
    Disposable peopleInSpaceSubscription = issTrackingRepositoryInterface.getPeopleInSpace()
        .subscribeOn(Schedulers.io())
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
