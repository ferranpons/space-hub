package com.ferranpons.issposition.passTimes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ferranpons.issposition.R;
import com.ferranpons.issposition.issTracking.IssTrackingApiInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

public class PassTimesAdapter extends ArrayAdapter<IssTrackingApiInterface.PassTime> {

	public PassTimesAdapter(Context context, ArrayList<IssTrackingApiInterface.PassTime> passTimes) {
		super(context, R.layout.row_pass_time, passTimes);
	}

	public class ViewHolder {
		TextView riseTime;
		TextView duration;

		public ViewHolder(View view) {
			riseTime = (TextView) view.findViewById(R.id.riseTime);
			duration = (TextView) view.findViewById(R.id.duration);
		}
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			view = View.inflate(parent.getContext(), R.layout.row_pass_time, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final IssTrackingApiInterface.PassTime passTime = getItem(position);
		if (passTime != null && passTime.riseTime > -1) {
			Date date = new Date(passTime.riseTime * 1000L);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
			String[] riseTimeUnformatted = sdf.format(date).split(" ");
			String riseTime = riseTimeUnformatted[0] + " at " + riseTimeUnformatted[1] + "h";
			holder.riseTime.setText(riseTime);
		}
		if (passTime != null && passTime.duration > -1) {
			long duration = TimeUnit.SECONDS.toMinutes(passTime.duration);
			if (duration >= 1) {
				holder.duration.setText("during " + String.valueOf(duration) + " min");
			} else {
				holder.duration.setText("during " + String.valueOf(passTime.duration) + " secs");
			}
		}
		return view;
	}
}
