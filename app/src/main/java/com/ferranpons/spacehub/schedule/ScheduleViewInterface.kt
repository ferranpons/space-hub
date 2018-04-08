package com.ferranpons.spacehub.schedule

import io.reactivex.functions.Consumer

interface ScheduleViewInterface {
    fun showPassTimes(list: List<String>)
    fun showPassTimesError(): Consumer<in Throwable>?
}