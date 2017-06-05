package com.ferranpons.issposition.passTimes

import android.annotation.TargetApi
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ferranpons.issposition.R
import com.ferranpons.issposition.issTracking.IssTrackingApiInterface
import java.util.Locale
import java.util.concurrent.TimeUnit
import org.joda.time.DateTime

class PassTimesAdapter(context: Context, passTimes: List<IssTrackingApiInterface.PassTime>) : ArrayAdapter<IssTrackingApiInterface.PassTime>(context, R.layout.row_pass_time, passTimes) {

    class ViewHolder(view: View) {
        internal var riseTime: TextView = view.findViewById(R.id.riseTime) as TextView
        internal var duration: TextView = view.findViewById(R.id.duration) as TextView

    }

    @TargetApi(24)
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        var customView = view
        if (view == null) {
            customView = View.inflate(parent.context, R.layout.row_pass_time, null)
            holder = ViewHolder(customView)
            customView.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }
        val passTime = getItem(position)
        if (passTime != null && passTime.riseTime > -1) {
            val date = DateTime(passTime.riseTime * 1000L)
            var sdf: SimpleDateFormat? = null
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            }
            var riseTimeUnformatted = arrayOfNulls<String>(0)
            if (sdf != null) {
                riseTimeUnformatted = sdf.format(date).split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }
            val riseTime = riseTimeUnformatted[0] + " at " + riseTimeUnformatted[1] + "h"
            holder.riseTime.text = riseTime
        }
        if (passTime != null && passTime.duration > -1) {
            val duration = TimeUnit.SECONDS.toMinutes(passTime.duration.toLong())
            val durationTime: String
            if (duration >= 1) {
                durationTime = "during " + java.lang.Long.toString(duration) + " min"
            } else {
                durationTime = "during " + Integer.toString(passTime.duration) + " secs"
            }
            holder.duration.text = durationTime
        }
        return customView!!
    }
}
