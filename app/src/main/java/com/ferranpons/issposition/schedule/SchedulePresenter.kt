package com.ferranpons.issposition.schedule

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers


class SchedulePresenter(var view: ScheduleViewInterface, var useCase: PassTimesUseCase) {

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun getPassTimesFor(latitude: Double, longitude: Double) {
        val disposable: Disposable = useCase.getPassTimes(latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess({ view.showPassTimes(emptyList()) })
                .doOnError(view.showPassTimesError())
                .subscribe()
        compositeDisposable.add(disposable)
        view.showPassTimes(emptyList())
    }

    class NullView : ScheduleViewInterface {
        override fun showPassTimesError(): Consumer<in Throwable>? {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun showPassTimes(list: List<String>) {
        }
    }
}