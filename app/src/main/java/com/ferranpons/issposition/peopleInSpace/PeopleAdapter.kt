package com.ferranpons.issposition.peopleInSpace

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ferranpons.issposition.R
import com.ferranpons.issposition.issTracking.IssTrackingApiInterface

class PeopleAdapter(context: Context, people: List<IssTrackingApiInterface.Person>) : ArrayAdapter<IssTrackingApiInterface.Person>(context, R.layout.row_person, people) {

    class ViewHolder(view: View) {
        internal var name: TextView
        internal var spaceCraft: TextView

        init {
            name = view.findViewById(R.id.name) as TextView
            spaceCraft = view.findViewById(R.id.spaceCraft) as TextView
        }
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        var customView = view
        if (customView == null) {
            customView = View.inflate(parent.context, R.layout.row_person, null)
            holder = ViewHolder(customView!!)
            customView.tag = holder
        } else {
            holder = customView.tag as ViewHolder
        }
        val person = getItem(position)
        holder.name.text = person!!.name
        holder.spaceCraft.text = person.spaceCraft
        return customView
    }
}
