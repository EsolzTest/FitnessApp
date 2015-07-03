package com.esolz.fitnessapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.esolz.fitnessapp.R;

public class AppointmantFragment extends Fragment {

	LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton,
			back;
	RelativeLayout llMessagebutton;

	View fView;
	FragmentTransaction fragmentTransaction;
	FragmentManager fragmentManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		fView = inflater.inflate(R.layout.frag_appointment, container, false);

		back = (LinearLayout) fView.findViewById(R.id.back);
		fragmentManager = getActivity().getSupportFragmentManager();

		/*
		 * llCalenderButton = (LinearLayout) getActivity().findViewById(
		 * R.id.calenderbutton); llBlockAppoinmentButton = (LinearLayout)
		 * getActivity().findViewById( R.id.blockappoinmentbutton);
		 * llProgressButton = (LinearLayout) getActivity().findViewById(
		 * R.id.progressbutton); llMessagebutton = (RelativeLayout)
		 * getActivity().findViewById( R.id.messagebutton);
		 * llCalenderButton.setClickable(true);
		 * llBlockAppoinmentButton.setClickable(false);
		 * llProgressButton.setClickable(true);
		 * llMessagebutton.setClickable(true);
		 */

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				fragmentTransaction = fragmentManager.beginTransaction();
				CalenderFragment cal_fragment = new CalenderFragment();
				fragmentTransaction.replace(R.id.fragment_container,
						cal_fragment);
				fragmentTransaction.commit();
			}
		});

		return fView;
	}

}
