package com.cse482b.cvdtraining;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class ListAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> mItemList;
    private List<String> mActivityList;
    private boolean mForModule;

    public ListAdapter(Context context, List<String> itemList, List<String> activityList, boolean forModule) {
        super(context, 0, itemList);
        mContext = context;
        mItemList = itemList;
        mActivityList = activityList;
        mForModule = forModule;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            // Inflate a new layout for the row
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        }
        // Set the text of the TextView to the item at the current position
        String moduleName = mItemList.get(position);
        textView = convertView.findViewById(R.id.module_button);
        if (moduleName != null) {
            textView.setText(moduleName);
        }

        String activityName = mActivityList.get(position);
        if (!activityName.equals("")) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Class<?> classActivityName = Class.forName(getContext().getPackageName() + "." + activityName);
                        Intent intent = new Intent(getContext(), classActivityName);
                        startActivity(getContext(), intent, null);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        ImageView moduleCheck = convertView.findViewById(R.id.module_check);
        // remove ImageView for non-module lists
        if (!mForModule) {
            ((ViewGroup) convertView).removeView(moduleCheck);

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
            params.width = ActionBar.LayoutParams.MATCH_PARENT;
//            params.setMargins(30, 30, 30, 30);
//            int padding = 16;
//            textView.setPadding(padding, 0, padding, 0);
            textView.setLayoutParams(params);

            return convertView;
        }

        String completionKey = moduleName + "-completion";
        String value = GlobalMethods.getPreference(mContext, completionKey, "");
        switch (value) {
            case "COMPLETED":
                moduleCheck.setImageResource(R.drawable.cvd_check);
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(1);
                ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
                moduleCheck.setColorFilter(cf);
                moduleCheck.setImageAlpha(255);
                break;
            case "UNLOCKED":
                moduleCheck.setImageResource(R.drawable.cvd_unlock);
                break;
            case "LOCKED":
            case "":
                if (value.equals(""))
                    GlobalMethods.setPreference(mContext, completionKey, position == 0 ? "UNLOCKED" : "LOCKED");
                moduleCheck.setImageResource(R.drawable.cvd_lock);
                break;
        }

        return convertView;
    }
}

