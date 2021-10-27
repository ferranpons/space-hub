package com.ferranpons.spacehub.schedule

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ferranpons.spacehub.R
import com.ferranpons.spacehub.issTracking.IssTrackingApi
import com.ferranpons.spacehub.issTracking.IssTrackingApiInterface
import com.ferranpons.spacehub.issTracking.IssTrackingPresenterInterface
import com.ferranpons.spacehub.issTracking.IssTrackingView
import com.ferranpons.spacehub.issTracking.IssTrackingPresenter
import com.ferranpons.spacehub.issTracking.IssTrackingRepository

class ScheduleFragment : Fragment(), IssTrackingView {
    private lateinit var issTrackingPresenter: IssTrackingPresenterInterface

    override fun onAttach(context: Context) {
        super.onAttach(context)

        issTrackingPresenter = IssTrackingPresenter(
            IssTrackingRepository(
                IssTrackingApi.getIssTrackingApi("http://api.open-notify.org")
            )
        )
        (issTrackingPresenter as IssTrackingPresenter).setView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
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