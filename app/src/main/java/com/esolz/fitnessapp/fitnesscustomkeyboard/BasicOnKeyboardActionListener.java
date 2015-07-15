package com.esolz.fitnessapp.fitnesscustomkeyboard;

import android.app.Activity;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;

/**
 * Created by ltp on 15/07/15.
 */
public class BasicOnKeyboardActionListener implements KeyboardView.OnKeyboardActionListener {

    private Activity mTargetActivity;

    /**
     * @param targetActivity Activity a cui deve essere girato l'evento
     *                       "pressione di un tasto sulla tastiera"
     */
    public BasicOnKeyboardActionListener(Activity targetActivity) {
        mTargetActivity = targetActivity;
    }

    @Override
    public void swipeUp() {
        // TODO Auto-generated method stub

    }

    @Override
    public void swipeRight() {
        // TODO Auto-generated method stub

    }

    @Override
    public void swipeLeft() {
        // TODO Auto-generated method stub

    }

    @Override
    public void swipeDown() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onText(CharSequence text) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRelease(int primaryCode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPress(int primaryCode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        long eventTime = System.currentTimeMillis();
        KeyEvent event = new KeyEvent(eventTime, eventTime,
                KeyEvent.ACTION_DOWN, primaryCode, 0, 0, 0, 0,
                KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE);

        mTargetActivity.dispatchKeyEvent(event);
    }

}
