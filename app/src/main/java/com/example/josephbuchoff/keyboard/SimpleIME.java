package com.example.josephbuchoff.keyboard;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.*;
import android.inputmethodservice.InputMethodService;
import android.media.AudioManager;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;
import android.widget.TextView;

public class SimpleIME extends InputMethodService implements OnKeyboardActionListener {
    // Instance fields
    private KeyboardView kv;
    private Keyboard keyboard;
    private boolean caps = false;
    private boolean encoding = false;
    TextView tv;

    // Instance fields related to encryption
    private Message message;
    private String password = "coolbeans";

    // Methods to override

    @Override
    public View onCreateInputView() {
        final View root = getLayoutInflater().inflate(R.layout.keyboard, null, false);

        tv = (TextView) root.findViewById(R.id.tvKeyboard);
        tv.setText("");

        kv = (KeyboardView) root.findViewById(R.id.keyboard);

        keyboard = new Keyboard(this, R.xml.querty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);

        message = new Message(password);

        return root;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);

        String tvText;

        switch (primaryCode) {
            case 999: // If the user presses the decode key (sends 999)
                String text = (String) ic.getSelectedText(0);
                message.setEnc(text);
                if (text != null) {
                    encoding = true;
                    decodeText();
                    break;
                }

                text = (String) ic.getTextBeforeCursor(9999, 0);  // Get the text in the field
                if (text == null) break; // If there's nothing in the input field, ignore this

                message.setEnc(text);    // Set the encoded text of our Message field to the text we just pulled

                switchText();

                break;
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);

                tvText = tv.getText().toString();
                int tvTextLength = tvText.length();
                if (tvTextLength > 1)   // If there's more that one character
                    tv.setText(tvText.substring(0, tvText.length() - 1));  // Set the text to everything but the last character
                else {                  // Otherwise set it to an empty string
                    tv.setText("");
                }
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

                String encChar = Character.toString(message.encodeChar(code));
                String charStr = Character.toString(code);
                tvText = (String) tv.getText();

                if (encoding) {
                    ic.commitText(encChar, 1);      // Commit the encoded version of the character to the input field
                    tv.setText(tvText + charStr);   // Commit the decoded version to the text view
                } else {
                    ic.commitText(charStr, 1);      // Commit the decoded version of the character to the input field
                    tv.setText(tvText + encChar);   // Commit the encoded version to the text view
                }
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
    private void playClick(int keyCode) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
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
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    // Switch the text field and the text view
    private void switchText() {
        InputConnection ic = getCurrentInputConnection();

        String switchtvText = (String) tv.getText();
        String switchfieldText = (String) ic.getTextBeforeCursor(9999, 0);

        ic.deleteSurroundingText(9999, 9999);
        ic.commitText(switchtvText, switchtvText.length());
        tv.setText(switchfieldText);

        encoding = !encoding;
    }

    // Decode the text that's selected
    private void decodeText()
    {
        InputConnection ic = getCurrentInputConnection();

        String tvText = (String) message.decode("coolbeans");
        String fieldText = (String) message.toString();

        ic.deleteSurroundingText(9999, 9999);
        ic.commitText(fieldText, fieldText.length());
        tv.setText(tvText);
    }

}