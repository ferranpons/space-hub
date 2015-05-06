package com.ferranpons.issposition;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.gesture.Sgesture;
import com.samsung.android.sdk.gesture.SgestureHand;

public class AboutFragment extends DialogFragment {

	public AboutFragment() {
		// Empty constructor required for DialogFragment
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, 0);

		try {
			Sgesture mGesture = new Sgesture();
			mGesture.initialize(getActivity().getApplicationContext());
			SgestureHand mGestureHand = new SgestureHand(Looper.getMainLooper(), mGesture);
			mGestureHand.start(0, changeListener);
		} catch (IllegalArgumentException | SsdkUnsupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_about, container);
		TextView projectUrl = (TextView) view.findViewById(R.id.projectUrl);
		projectUrl.setOnClickListener(view1 -> {
			if (getActivity() != null) {
				Intent intent =
					new Intent(getActivity().getApplicationContext(), WebViewActivity.class);
				startActivity(intent);
			}
		});
		return view;
	}

	private SgestureHand.ChangeListener changeListener = info -> {
		dismiss();
	};
}
