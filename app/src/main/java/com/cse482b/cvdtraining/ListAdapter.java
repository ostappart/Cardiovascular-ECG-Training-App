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
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

/**
 * The adapter that enables going through a list of items handled by different activities.
 */
public class ListAdapter extends ArrayAdapter<String> {
    private final Context mContext;
    private final List<String> mItemList;
    private final List<String> mActivityList;
    private List<String> mfragmentList;
    private List<String> mquestionCategoryList;
    private final boolean mForModule;

    public ListAdapter(Context context, List<String> itemList, List<String> activityList) {
        super(context, 0, itemList);
        mContext = context;
        mItemList = itemList;
        mActivityList = activityList;
        mForModule = false;
    }

    public ListAdapter(Context context, List<String> itemList, List<String> activityList,
                       List<String> fragmentList, List<String> questionCategoryList) {
        super(context, 0, itemList);
        mContext = context;
        mItemList = itemList;
        mActivityList = activityList;
        mfragmentList = fragmentList;
        mquestionCategoryList = questionCategoryList;
        mForModule = true;
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
            textView.setTag(moduleName);
        }

        String activityName = mActivityList.get(position);
        if (!activityName.equals("")) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Class<?> classActivityName = Class.forName(getContext().getPackageName() + "." + activityName);
                        Intent intent = new Intent(getContext(), classActivityName);
                        intent.putExtra("module_name", moduleName);
                        if (mForModule) { // add extra module data
                            intent.putExtra("module_fragments", mfragmentList.get(position));
                            if (position + 1 < mItemList.size())
                                intent.putExtra("module_unlock", mItemList.get(position + 1));
                            GlobalMethods.setPracticeQuestionCategory(getContext(), mquestionCategoryList.get(position));
                            String completionKey = moduleName + "-completion";
                            String value = GlobalMethods.getPreference(mContext, completionKey, "");
                            if (value.equals("UNLOCKED") || value.equals("COMPLETED")) {
                                startActivity(getContext(), intent, null);
                            }
                        } else startActivity(getContext(), intent, null);
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
                moduleCheck.setImageResource(R.drawable.cvd_lock);
                break;
            case "":
                if (position == 0) {
                    moduleCheck.setImageResource(R.drawable.cvd_unlock);
                } else {
                    moduleCheck.setImageResource(R.drawable.cvd_lock);
                }
                GlobalMethods.setPreference(mContext, completionKey, position == 0 ? "UNLOCKED" : "LOCKED");
                break;
        }

        return convertView;
    }
}

