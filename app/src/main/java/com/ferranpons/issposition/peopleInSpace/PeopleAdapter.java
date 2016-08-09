package com.ferranpons.issposition.peopleInSpace;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ferranpons.issposition.R;
import com.ferranpons.issposition.issTracking.IssTrackingApiInterface;
import java.util.ArrayList;

public class PeopleAdapter extends ArrayAdapter<IssTrackingApiInterface.Person> {

  public PeopleAdapter(Context context, ArrayList<IssTrackingApiInterface.Person> people) {
    super(context, R.layout.row_person, people);
  }

  public class ViewHolder {
    TextView name;
    TextView spaceCraft;

    public ViewHolder(View view) {
      name = (TextView) view.findViewById(R.id.name);
      spaceCraft = (TextView) view.findViewById(R.id.spaceCraft);
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
    final IssTrackingApiInterface.Person person = getItem(position);
    holder.name.setText(person.name);
    holder.spaceCraft.setText(person.spaceCraft);
    return view;
  }
}
