package com.example.josephbuchoff.keyboard;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.*;
import android.inputmethodservice.InputMethodService;
import android.media.AudioManager;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;
import android.R.id.*;

public class SimpleIME extends InputMethodService implements OnKeyboardActionListener
{
    // Instance fields
    private KeyboardView kv;
    private Keyboard keyboard;
    private boolean caps = false;

    // Instance fields related to encryption
    private Message message;
    private String password;

    // Methods to override

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.querty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);

        if (message == null) {
            message = new Message();
        }

        return kv;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);

        switch(primaryCode) {
            case 0: // If the user presses the decode key (sends a null character, Unicode 0)
                String enc = (String) ic.getSelectedText(0);  // Get the selected text
                if (enc == null) break; // If there's nothing selected, ignore this

                message.setEnc(enc);    // Set the encoded text of our Message field to the text we just pulled
                ic.performContextMenuAction(16908320);  // The ID for cut is 16908320 - Cut the selected text
                ic.commitText(message.decode("coolbeans"),1);   // Commit the decoded text just after the cursor
                break;
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && caps) {
                    code = Character.toUpperCase(code);
                }
                ic.commitText(Character.toString(message.append(String.valueOf(code))), 1); // Commit the encoded version of the character to the text field
        }
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }

    // Original methods
    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            //case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }
}