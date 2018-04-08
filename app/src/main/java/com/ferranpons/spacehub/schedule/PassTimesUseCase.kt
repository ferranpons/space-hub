package com.ferranpons.spacehub.schedule

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable


class PassTimesUseCase {
    fun getPassTimes(latitude: Double, longitude: Double): Single<List<String>> {
        return Single.just(ArrayList<String>())
    }


}