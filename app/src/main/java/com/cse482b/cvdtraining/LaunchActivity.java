package com.cse482b.cvdtraining;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
