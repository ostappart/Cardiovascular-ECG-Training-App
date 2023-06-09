package com.cse482b.cvdtraining;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.AlignmentSpan;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * WelcomeActivity is shown to the user the first time they open the application, it welcomes them,
 * prompts for their name which we store so we can greet them on subsequent visits (plus this might
 * be useful if notifications are ever implemented).
 */
public class WelcomeActivity extends AppCompatActivity {

    /**
     * Allow the user to enter text.
     */
    private EditText mEditText;
    /**
     * Display the text nicely.
     */
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mEditText = findViewById(R.id.welcome_editText);
        Button mButton = findViewById(R.id.welcome_button);
        mTextView = findViewById(R.id.welcome_textName);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEditText.getText().toString();
                if (name.length() == 0)
                    return;

                String text = "Welcome, " + name + "!";
                Spannable centeredText = new SpannableString(text);
                centeredText.setSpan(
                        new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0, text.length() - 1,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE
                );
                Toast.makeText(getApplicationContext(), centeredText, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                startActivity(intent);
                GlobalMethods.setPreference(getApplicationContext(), "name", name);
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextView.setText(s.toString());
                float size = mTextView.getTextSize();
                size = size / 3;
                mEditText.setTextSize(size);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
    }
}

