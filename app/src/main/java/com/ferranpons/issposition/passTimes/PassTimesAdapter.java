package com.ferranpons.issposition.passTimes;

import android.annotation.TargetApi;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ferranpons.issposition.R;
import com.ferranpons.issposition.issTracking.IssTrackingApiInterface;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;

public class PassTimesAdapter extends ArrayAdapter<IssTrackingApiInterface.PassTime> {

  public PassTimesAdapter(Context context, List<IssTrackingApiInterface.PassTime> passTimes) {
    super(context, R.layout.row_pass_time, passTimes);
  }

  public static class ViewHolder {
    TextView riseTime;
    TextView duration;

    public ViewHolder(View view) {
      riseTime = (TextView) view.findViewById(R.id.riseTime);
      duration = (TextView) view.findViewById(R.id.duration);
    }
  }

  @TargetApi(24)
  @Override
  public View getView(final int position, View view, ViewGroup parent) {
    final ViewHolder holder;
    View customView = view;
    if (view == null) {
      customView = View.inflate(parent.getContext(), R.layout.row_pass_time, null);
      holder = new ViewHolder(customView);
      customView.setTag(holder);
    } else {
      holder = (ViewHolder) view.getTag();
    }
    final IssTrackingApiInterface.PassTime passTime = getItem(position);
    if (passTime != null && passTime.riseTime > -1) {
      DateTime date = new DateTime(passTime.riseTime * 1000L);
      SimpleDateFormat sdf = null;
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
      }
      String[] riseTimeUnformatted = new String[0];
      if (sdf != null) {
        riseTimeUnformatted = sdf.format(date).split(" ");
      }
      String riseTime = riseTimeUnformatted[0] + " at " + riseTimeUnformatted[1] + "h";
      holder.riseTime.setText(riseTime);
    }
    if (passTime != null && passTime.duration > -1) {
      long duration = TimeUnit.SECONDS.toMinutes(passTime.duration);
      String durationTime;
      if (duration >= 1) {
        durationTime = "during " + Long.toString(duration) + " min";
      } else {
        durationTime = "during " + Integer.toString(passTime.duration) + " secs";
      }
      holder.duration.setText(durationTime);
    }
    return customView;
  }
}
