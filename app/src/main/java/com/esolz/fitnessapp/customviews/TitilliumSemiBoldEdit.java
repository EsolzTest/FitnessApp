package com.esolz.fitnessapp.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TitilliumSemiBoldEdit extends TextView {

	public TitilliumSemiBoldEdit(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public TitilliumSemiBoldEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public TitilliumSemiBoldEdit(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public void init() {

		super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
				"TitilliumWeb-SemiBold.ttf"));

	}

}
