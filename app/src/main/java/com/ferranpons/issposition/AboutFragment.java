package com.ferranpons.issposition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.samsung.android.sdk.imagefilter.SifImageFilter;

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
		ImageView issImage = (ImageView) view.findViewById(R.id.issImage);
		filterImage(issImage);
		projectUrl.setOnClickListener(view1 -> {
			if (getActivity() != null) {
				Intent intent =
					new Intent(getActivity().getApplicationContext(), WebViewActivity.class);
				startActivity(intent);
			}
		});
		return view;
	}

	private void filterImage(ImageView issImage) {
		try {
			Bitmap newBitmap = filterBitmap(issImage);
			if (newBitmap != null) {
				issImage.setImageBitmap(newBitmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Bitmap filterBitmap(ImageView issImage) {
		Bitmap bitmap = ((BitmapDrawable) issImage.getDrawable()).getBitmap();
		return SifImageFilter.filterImageCopy(bitmap, SifImageFilter.FILTER_CLASSIC,
			SifImageFilter.LEVEL_MEDIUM);
	}
}
