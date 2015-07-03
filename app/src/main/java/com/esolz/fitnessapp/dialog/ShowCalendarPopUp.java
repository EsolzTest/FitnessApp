package com.esolz.fitnessapp.dialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.TitilliumRegular;
import com.esolz.fitnessapp.helper.ReturnCalendarDetails;

public class ShowCalendarPopUp extends PopupWindow {

	Context context;
	TitilliumRegular[] textViewArray;
	LinearLayout[] llArray;
	TitilliumRegular txt_currentdatemonth, txt_currentyear;
	LinearLayout llHide;
	View popupView;

	// -- Calendar Instance
	Calendar calendar;
	int currentYear, currentMonth, currentDay, currentDate, firstDayPosition,
			currentMonthLength, previousDayPosition, nextDayPosition;

	int l;

	public ShowCalendarPopUp(final Context context) {
		super(context);

		this.context = context;

		setContentView(LayoutInflater.from(context).inflate(
				R.layout.popup_showcalender, null));
		setHeight(WindowManager.LayoutParams.MATCH_PARENT);
		setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		popupView = getContentView();
		setFocusable(true);

		llHide = (LinearLayout) popupView.findViewById(R.id.ll_hide);
		txt_currentdatemonth = (TitilliumRegular) popupView
				.findViewById(R.id.txt_currentdatemonth);
		// txt_currentyear = (TitilliumRegular) popupView
		// .findViewById(R.id.txt_currentyear);

		calendar = Calendar.getInstance(Locale.getDefault());

		currentDate = calendar.get(Calendar.DATE);
		currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		currentMonth = (calendar.get(Calendar.MONTH) + 1);
		currentYear = calendar.get(Calendar.YEAR);
		firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);

		getLayouts();

	}

	public void getCalendar(int currentMonth, int indexOfDayOne, int curyear) {

		int i = 0, j = 0, k = 0, day = 1;
		int currentMonthLength = ReturnCalendarDetails.getMonthLenth(
				currentMonth, curyear);

		Calendar current = (Calendar) calendar.clone();

		current.set(Calendar.MONTH, currentMonth - 1);
		current.set(Calendar.YEAR, curyear);
		current.set(Calendar.DATE, currentDate);

		SimpleDateFormat sformat = new SimpleDateFormat("MMMM yyyy");

		txt_currentdatemonth.setText("" + sformat.format(current.getTime()));
		// txt_currentyear.setText("" + curyear);

		int today = current.get(Calendar.DATE);

		// --- Set Current Month
		for (i = indexOfDayOne; i < (currentMonthLength + indexOfDayOne); i++) {
			textViewArray[i].setText("" + day);
			textViewArray[i].setTextColor(Color.BLACK);
			day++;

			if (currentMonthLength > today) {

				if (textViewArray[(today + indexOfDayOne) - 1].getText()
						.toString().equals("" + today)) {
					llArray[(today + indexOfDayOne) - 1]
							.setBackgroundResource(R.drawable.selected_day);
					textViewArray[(today + indexOfDayOne) - 1]
							.setTextColor(Color.parseColor("#22A7F0"));
				}
			} else {

				if (textViewArray[(today + indexOfDayOne) - 1].getText()
						.toString().equals("" + today)) {
					llArray[(today + indexOfDayOne) - 2]
							.setBackgroundResource(R.drawable.selected_day);
					textViewArray[(today + indexOfDayOne) - 1]
							.setTextColor(Color.parseColor("#22A7F0"));
				}
			}
		}
		day = 1;

		// --- Set Next Month

		for (j = i; j < textViewArray.length; j++) {
			textViewArray[j].setText("" + day);
			textViewArray[j].setTextColor(Color.parseColor("#BDBDBD"));
			day++;
		}
		day = 1;

		if (currentMonth == 1) {
			currentMonth = 12;
			curyear--;
		} else {
			currentMonth--;
		}
		int tempcurrentMonthLength = ReturnCalendarDetails.getMonthLenth(
				currentMonth, curyear);

		if (indexOfDayOne != 0) {
			// --- Set Previous Month
			for (k = indexOfDayOne - 1; k >= 0; k--) {
				textViewArray[k].setText("" + tempcurrentMonthLength);
				textViewArray[k].setTextColor(Color.parseColor("#BDBDBD"));
				tempcurrentMonthLength--;
			}
		}

	}

	public void getLayouts() {
		llArray = new LinearLayout[42];
		llArray[0] = (LinearLayout) popupView.findViewById(R.id.ll7);
		llArray[1] = (LinearLayout) popupView.findViewById(R.id.ll8);
		llArray[2] = (LinearLayout) popupView.findViewById(R.id.ll9);
		llArray[3] = (LinearLayout) popupView.findViewById(R.id.ll10);
		llArray[4] = (LinearLayout) popupView.findViewById(R.id.ll11);
		llArray[5] = (LinearLayout) popupView.findViewById(R.id.ll12);
		llArray[6] = (LinearLayout) popupView.findViewById(R.id.ll13);
		llArray[7] = (LinearLayout) popupView.findViewById(R.id.ll14);
		llArray[8] = (LinearLayout) popupView.findViewById(R.id.ll15);
		llArray[9] = (LinearLayout) popupView.findViewById(R.id.ll16);
		llArray[10] = (LinearLayout) popupView.findViewById(R.id.ll17);
		llArray[11] = (LinearLayout) popupView.findViewById(R.id.ll18);
		llArray[12] = (LinearLayout) popupView.findViewById(R.id.ll19);
		llArray[13] = (LinearLayout) popupView.findViewById(R.id.ll20);
		llArray[14] = (LinearLayout) popupView.findViewById(R.id.ll21);
		llArray[15] = (LinearLayout) popupView.findViewById(R.id.ll22);
		llArray[16] = (LinearLayout) popupView.findViewById(R.id.ll23);
		llArray[17] = (LinearLayout) popupView.findViewById(R.id.ll24);
		llArray[18] = (LinearLayout) popupView.findViewById(R.id.ll25);
		llArray[19] = (LinearLayout) popupView.findViewById(R.id.ll26);
		llArray[20] = (LinearLayout) popupView.findViewById(R.id.ll27);
		llArray[21] = (LinearLayout) popupView.findViewById(R.id.ll28);
		llArray[22] = (LinearLayout) popupView.findViewById(R.id.ll29);
		llArray[23] = (LinearLayout) popupView.findViewById(R.id.ll30);
		llArray[24] = (LinearLayout) popupView.findViewById(R.id.ll31);
		llArray[25] = (LinearLayout) popupView.findViewById(R.id.ll32);
		llArray[26] = (LinearLayout) popupView.findViewById(R.id.ll33);
		llArray[27] = (LinearLayout) popupView.findViewById(R.id.ll34);
		llArray[28] = (LinearLayout) popupView.findViewById(R.id.ll35);
		llArray[29] = (LinearLayout) popupView.findViewById(R.id.ll36);
		llArray[30] = (LinearLayout) popupView.findViewById(R.id.ll37);
		llArray[31] = (LinearLayout) popupView.findViewById(R.id.ll38);
		llArray[32] = (LinearLayout) popupView.findViewById(R.id.ll39);
		llArray[33] = (LinearLayout) popupView.findViewById(R.id.ll40);
		llArray[34] = (LinearLayout) popupView.findViewById(R.id.ll41);
		llArray[35] = (LinearLayout) popupView.findViewById(R.id.ll42);
		llArray[36] = (LinearLayout) popupView.findViewById(R.id.ll43);
		llArray[37] = (LinearLayout) popupView.findViewById(R.id.ll44);
		llArray[38] = (LinearLayout) popupView.findViewById(R.id.ll45);
		llArray[39] = (LinearLayout) popupView.findViewById(R.id.ll46);
		llArray[40] = (LinearLayout) popupView.findViewById(R.id.ll47);
		llArray[41] = (LinearLayout) popupView.findViewById(R.id.ll48);

		for (int a = 0; a < llArray.length; a++) {
			llArray[a].setBackgroundColor(Color.TRANSPARENT);
		}

		textViewArray = new TitilliumRegular[42];
		textViewArray[0] = (TitilliumRegular) popupView.findViewById(R.id.txt7);
		textViewArray[1] = (TitilliumRegular) popupView.findViewById(R.id.txt8);
		textViewArray[2] = (TitilliumRegular) popupView.findViewById(R.id.txt9);
		textViewArray[3] = (TitilliumRegular) popupView
				.findViewById(R.id.txt10);
		textViewArray[4] = (TitilliumRegular) popupView
				.findViewById(R.id.txt11);
		textViewArray[5] = (TitilliumRegular) popupView
				.findViewById(R.id.txt12);
		textViewArray[6] = (TitilliumRegular) popupView
				.findViewById(R.id.txt13);
		textViewArray[7] = (TitilliumRegular) popupView
				.findViewById(R.id.txt14);
		textViewArray[8] = (TitilliumRegular) popupView
				.findViewById(R.id.txt15);
		textViewArray[9] = (TitilliumRegular) popupView
				.findViewById(R.id.txt16);
		textViewArray[10] = (TitilliumRegular) popupView
				.findViewById(R.id.txt17);
		textViewArray[11] = (TitilliumRegular) popupView
				.findViewById(R.id.txt18);
		textViewArray[12] = (TitilliumRegular) popupView
				.findViewById(R.id.txt19);
		textViewArray[13] = (TitilliumRegular) popupView
				.findViewById(R.id.txt20);
		textViewArray[14] = (TitilliumRegular) popupView
				.findViewById(R.id.txt21);
		textViewArray[15] = (TitilliumRegular) popupView
				.findViewById(R.id.txt22);
		textViewArray[16] = (TitilliumRegular) popupView
				.findViewById(R.id.txt23);
		textViewArray[17] = (TitilliumRegular) popupView
				.findViewById(R.id.txt24);
		textViewArray[18] = (TitilliumRegular) popupView
				.findViewById(R.id.txt25);
		textViewArray[19] = (TitilliumRegular) popupView
				.findViewById(R.id.txt26);
		textViewArray[20] = (TitilliumRegular) popupView
				.findViewById(R.id.txt27);
		textViewArray[21] = (TitilliumRegular) popupView
				.findViewById(R.id.txt28);
		textViewArray[22] = (TitilliumRegular) popupView
				.findViewById(R.id.txt29);
		textViewArray[23] = (TitilliumRegular) popupView
				.findViewById(R.id.txt30);
		textViewArray[24] = (TitilliumRegular) popupView
				.findViewById(R.id.txt31);
		textViewArray[25] = (TitilliumRegular) popupView
				.findViewById(R.id.txt32);
		textViewArray[26] = (TitilliumRegular) popupView
				.findViewById(R.id.txt33);
		textViewArray[27] = (TitilliumRegular) popupView
				.findViewById(R.id.txt34);
		textViewArray[28] = (TitilliumRegular) popupView
				.findViewById(R.id.txt35);
		textViewArray[29] = (TitilliumRegular) popupView
				.findViewById(R.id.txt36);
		textViewArray[30] = (TitilliumRegular) popupView
				.findViewById(R.id.txt37);
		textViewArray[31] = (TitilliumRegular) popupView
				.findViewById(R.id.txt38);
		textViewArray[32] = (TitilliumRegular) popupView
				.findViewById(R.id.txt39);
		textViewArray[33] = (TitilliumRegular) popupView
				.findViewById(R.id.txt40);
		textViewArray[34] = (TitilliumRegular) popupView
				.findViewById(R.id.txt41);
		textViewArray[35] = (TitilliumRegular) popupView
				.findViewById(R.id.txt42);
		textViewArray[36] = (TitilliumRegular) popupView
				.findViewById(R.id.txt43);
		textViewArray[37] = (TitilliumRegular) popupView
				.findViewById(R.id.txt44);
		textViewArray[38] = (TitilliumRegular) popupView
				.findViewById(R.id.txt45);
		textViewArray[39] = (TitilliumRegular) popupView
				.findViewById(R.id.txt46);
		textViewArray[40] = (TitilliumRegular) popupView
				.findViewById(R.id.txt47);
		textViewArray[41] = (TitilliumRegular) popupView
				.findViewById(R.id.txt48);
	}
}