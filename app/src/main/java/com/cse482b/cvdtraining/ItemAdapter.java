package com.cse482b.cvdtraining;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<String[]> itemList;
    private Context context;

    public ItemAdapter(Context context, List<String[]> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setFilteredList(List<String[]> filteredList) {
        this.itemList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dict_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.word.setText(itemList.get(position)[0]);
        holder.def.setText(itemList.get(position)[1]);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView word;
        TextView def;

        public ViewHolder(View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.dictItem_word);
            def = itemView.findViewById(R.id.dictItem_def);

        }
    }
}

//import static androidx.core.content.ContextCompat.startActivity;
//
//import android.app.ActionBar;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.ColorMatrix;
//import android.graphics.ColorMatrixColorFilter;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.constraintlayout.widget.ConstraintLayout;
//
//import java.util.List;
//
//public class ItemAdapter extends ArrayAdapter<String> {
//    private Context mContext;
//    private List<String[]> mItemList;
//
//    public ItemAdapter(Context context, List<String[]> itemList) {
//        super(context, 0);
//        mContext = context;
//        mItemList = itemList;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        TextView word, def;
//        if (convertView == null) {
//            // Inflate a new layout for the row
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.dict_item, parent, false);
//        }
//        word = convertView.findViewById(R.id.dictItem_word);
//        def = convertView.findViewById(R.id.dictItem_def);
//        word.setText(mItemList.get(position)[0]);
//        def.setText(mItemList.get(position)[1]);
//
//        return convertView;
//    }
//}

