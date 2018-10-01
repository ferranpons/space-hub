package com.ferranpons.spacehub.schedule

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ferranpons.spacehub.R
import com.ferranpons.spacehub.issTracking.IssTrackingApi
import com.ferranpons.spacehub.issTracking.IssTrackingApiInterface
import com.ferranpons.spacehub.issTracking.IssTrackingInteractor
import com.ferranpons.spacehub.issTracking.IssTrackingPresenter
import com.ferranpons.spacehub.issTracking.IssTrackingPresenterInterface
import com.ferranpons.spacehub.issTracking.IssTrackingViewInterface
import com.ferranpons.spacehub.passTimes.PassTimesAdapter

class ScheduleFragment : Fragment(), IssTrackingViewInterface {
    private lateinit var issTrackingPresenter: IssTrackingPresenterInterface
    private lateinit var recyclerView: RecyclerView

    companion object {
        const val OPEN_NOTIFY_API = "http://api.open-notify.org"
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        issTrackingPresenter = IssTrackingPresenter(IssTrackingInteractor(IssTrackingApi.getIssTrackingApi(OPEN_NOTIFY_API)))
        (issTrackingPresenter as IssTrackingPresenter).setView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(view.context)
        recyclerView = view.findViewById(R.id.schedule_recyclerview)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        issTrackingPresenter.retrieveCurrentPosition()
        issTrackingPresenter.retrievePeopleInSpace()
    }

    override fun showCurrentPositionError() {
    }

    override fun setIssPosition(position: IssTrackingApiInterface.IssPosition?) {
        issTrackingPresenter.retrievePassTimes(position?.latitude!!, position.longitude)
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
        val scheduleAdapter = PassTimesAdapter(passTimes!!)

        recyclerView.adapter = scheduleAdapter
        scheduleAdapter.notifyDataSetChanged()
    }

    override fun showPeopleInSpace(people: MutableList<IssTrackingApiInterface.Person>?) {
    }
}