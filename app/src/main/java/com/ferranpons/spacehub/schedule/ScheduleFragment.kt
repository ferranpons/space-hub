package com.ferranpons.spacehub.schedule

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ferranpons.spacehub.R
import com.ferranpons.spacehub.issTracking.*


class ScheduleFragment: Fragment(), IssTrackingViewInterface {

    private lateinit var issTrackingPresenter: IssTrackingPresenterInterface

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        issTrackingPresenter = IssTrackingPresenter(IssTrackingInteractor(IssTrackingApi.getIssTrackingApi("http://api.open-notify.org")))
        (issTrackingPresenter as IssTrackingPresenter).setView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        issTrackingPresenter.retrieveCurrentPosition()
        issTrackingPresenter.retrievePeopleInSpace()
    }

    override fun showCurrentPositionError() {

    }

    override fun setIssPosition(position: IssTrackingApiInterface.IssPosition?) {

    }

    override fun willRetrievePassTimes() {

    }

    override fun didRetrievePassTimes() {

    }

    override fun showPassTimesError() {

    }

    override fun willRetrievePeopleInSpace() {

    }

    override fun didRetrievePeopleInSpace() {

    }

    override fun showPeopleInSpaceError() {

    }

    override fun showPassTimes(passTimes: MutableList<IssTrackingApiInterface.PassTime>?) {

    }

    override fun showPeopleInSpace(people: MutableList<IssTrackingApiInterface.Person>?) {

    }
}