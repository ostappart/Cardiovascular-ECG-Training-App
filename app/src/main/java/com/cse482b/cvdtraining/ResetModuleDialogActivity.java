package com.cse482b.cvdtraining;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ResetModuleDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog dialog = createDialog();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        TextView message = (TextView) dialog.findViewById(android.R.id.message);
        int titleId = getResources().getIdentifier( "alertTitle", "id", "com.cse482b.cvdtraining");
        if (titleId > 0) {
            TextView dialogTitle = (TextView) dialog.findViewById(titleId);
            if (dialogTitle != null) {
                dialogTitle.setTextSize(getResources().getDimension(com.intuit.ssp.R.dimen._9ssp));
                message.setTextSize(getResources().getDimension(com.intuit.ssp.R.dimen._8ssp));
            }
        }
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?")
                .setMessage("All modules but the first will be locked.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (String key : GlobalMethods.getPreferencesKeys(getApplicationContext())) {
                            if (key.contains("-completion"))
                                GlobalMethods.setPreference(getApplicationContext(), key, "");
                        }
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        return dialog;
    }
}


