package com.esolz.fitnessapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.esolz.fitnessapp.R;

//public class MeasurementDialogFragment extends DialogFragment {
//
//	int mYear, mMonth, mDay, mHour, mMinute;
//	LinearLayout llDone, llCancel, llPlus, llMinus;
//	TextView kiloWeight, parentWeight, parentDeadline;
//	String DATE = "", type = "", hour = "", min = "", WEIGHT = "";
//	DatePicker datePicker;
//	TimePicker timePicker;
//
//	SharedPreferences measurementSharedPreferences;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View rootView = inflater.inflate(R.layout.measurement_dialog,
//				container, false);
//		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//		setCancelable(false);
//
//		parentDeadline = (TextView) getActivity().findViewById(R.id.deadline);
//		parentWeight = (TextView) getActivity().findViewById(R.id.weight);
//
//		datePicker = (DatePicker) rootView.findViewById(R.id.date);
//		//timePicker = (TimePicker) rootView.findViewById(R.id.time);
//		llDone = (LinearLayout) rootView.findViewById(R.id.ll_done);
//		llCancel = (LinearLayout) rootView.findViewById(R.id.ll_cancel);
//		llPlus = (LinearLayout) rootView.findViewById(R.id.ll_plus);
//		llMinus = (LinearLayout) rootView.findViewById(R.id.ll_minus);
//		kiloWeight = (TextView) rootView.findViewById(R.id.kilo);
//
//		// -- Sharedpreference
//		measurementSharedPreferences = getActivity().getSharedPreferences(
//				"Measument", Context.MODE_PRIVATE);
//		// -- END
//
//		llPlus.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				kiloWeight.setText(""
//						+ (Integer.parseInt(kiloWeight.getText().toString()) + 1));
//			}
//		});
//
//		llMinus.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if ((Integer.parseInt(kiloWeight.getText().toString()) - 1) > 0) {
//					kiloWeight.setText(""
//							+ (Integer
//									.parseInt(kiloWeight.getText().toString()) - 1));
//				}
//
//			}
//		});
//
//		llDone.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
////				mDay = datePicker.getDayOfMonth();
////				mMonth = datePicker.getMonth() + 1;
////				mYear = datePicker.getYear();
////
////				mHour = timePicker.getCurrentHour();
////				mMinute = timePicker.getCurrentMinute();
////
////				if (mHour > 12) {
////					hour = "0" + (mHour - 12);
////					type = "pm";
////				} else {
////					hour = "0" + mHour;
////					type = "am";
////				}
////
////				DATE = mDay + "-" + mMonth + "-" + mYear;
////				WEIGHT = kiloWeight.getText().toString();
////				parentDeadline.setText("" + DATE);
////				parentWeight.setText("" + WEIGHT);
////
////				Editor editor = measurementSharedPreferences.edit();
////				editor.putString("DATE", DATE);
////				editor.putString("WEIGHT", WEIGHT);
////				editor.commit();
//
//				getDialog().dismiss();
//			}
//		});
//		llCancel.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Editor editor = measurementSharedPreferences.edit();
//				editor.clear();
//				editor.commit();
//				getDialog().dismiss();
//			}
//		});
//
//		return rootView;
//	}
//}
