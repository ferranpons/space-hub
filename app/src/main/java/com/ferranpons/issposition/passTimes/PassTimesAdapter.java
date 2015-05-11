package com.ferranpons.issposition.passTimes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ferranpons.issposition.R;
import com.ferranpons.issposition.issTracking.IssTrackingApiInterface;
import java.util.ArrayList;

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
			view = View.inflate(parent.getContext(), R.layout.row_person, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final IssTrackingApiInterface.PassTime passTime = getItem(position);
		holder.riseTime.setText(String.valueOf(passTime.riseTime));
		holder.duration.setText(passTime.duration);
		return view;
	}
}
