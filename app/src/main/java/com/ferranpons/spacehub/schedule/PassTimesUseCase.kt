package com.ferranpons.spacehub.schedule

import io.reactivex.Single

class PassTimesUseCase {
    fun getPassTimes(latitude: Double, longitude: Double): Single<List<String>> =
            Single.just(ArrayList<String>())
}