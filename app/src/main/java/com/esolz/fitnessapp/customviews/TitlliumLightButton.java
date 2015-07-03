package com.esolz.fitnessapp.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by su on 24/6/15.
 */
public class TitlliumLightButton extends Button{

    public TitlliumLightButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitlliumLightButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TitlliumLightButton(Context context) {
        super(context);
        init();
    }

public  void init()


{
    super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
    "TitilliumWeb-Light.ttf"));
}




}
