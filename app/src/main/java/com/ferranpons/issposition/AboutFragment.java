package com.ferranpons.issposition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends DialogFragment {

  public AboutFragment() {
    // Empty constructor required for DialogFragment
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NO_TITLE, 0);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_about, container);
    TextView projectUrl = (TextView) view.findViewById(R.id.projectUrl);
    projectUrl.setOnClickListener(view1 -> {
      if (getActivity() != null) {
        Intent intent = new Intent(getActivity().getApplicationContext(), WebViewActivity.class);
        startActivity(intent);
      }
    });
    return view;
  }
}
