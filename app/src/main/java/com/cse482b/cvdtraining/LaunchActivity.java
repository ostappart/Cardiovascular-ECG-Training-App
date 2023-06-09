package com.cse482b.cvdtraining;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * LaunchActivity is the first thing that runs. Decides whether to redirect to the WelcomeActivity
 * to get the users name if it's their first time or go straight to the home screen if we've seen them before.
 */
public class LaunchActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String name = GlobalMethods.getPreference(getApplicationContext(), "name", "");
        intent = new Intent(this, name.equals("") ? WelcomeActivity.class : HomeActivity.class);
        intent.putExtra("newUser", name.equals(""));
        startActivity(intent);
    }
}
