package com.ferranpons.spacehub.passTimes

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ferranpons.spacehub.R
import com.ferranpons.spacehub.issTracking.IssTrackingApiInterface
import org.joda.time.DateTime
import java.util.*
import java.util.concurrent.TimeUnit

class PassTimesAdapter(private val passTimes: List<IssTrackingApiInterface.PassTime>) : RecyclerView.Adapter<PassTimesAdapter.PassTimesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassTimesViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_pass_time, parent, false)
        return PassTimesViewHolder(v)
    }

    override fun getItemCount(): Int = passTimes.size

    override fun onBindViewHolder(holder: PassTimesViewHolder, position: Int) {
        val passTime = passTimes[position]
        if (passTime.riseTime > -1) {
            /*val date = DateTime(passTime.riseTime * 1000L)
            var sdf: SimpleDateFormat? = null
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            }
            if (sdf != null) {
                val riseTimeUnformatted = sdf.format(date).split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val riseTime = riseTimeUnformatted[0] + " at " + riseTimeUnformatted[1] + "h"
                holder.riseTime.text = riseTime
            }*/
        }
        if (passTime.duration > -1) {
            val duration = TimeUnit.SECONDS.toMinutes(passTime.duration.toLong())
            val durationTime: String
            durationTime = if (duration >= 1) {
                "during " + java.lang.Long.toString(duration) + " min"
            } else {
                "during " + Integer.toString(passTime.duration) + " secs"
            }
            holder.duration.text = durationTime
        }
    }

    inner class PassTimesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var riseTime = view.findViewById(R.id.riseTime) as TextView
        var duration = view.findViewById(R.id.duration) as TextView
    }
}
