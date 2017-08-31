package com.ferranpons.issposition.schedule

import io.reactivex.functions.Consumer


interface ScheduleViewInterface {
    fun showPassTimes(list: List<String>)
    fun showPassTimesError(): Consumer<in Throwable>?
}