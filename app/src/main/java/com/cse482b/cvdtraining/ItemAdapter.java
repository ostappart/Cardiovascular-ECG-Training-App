package com.cse482b.cvdtraining;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * ItemAdapter uses a RecyclerView to display a list of items for the dictionary.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<String[]> itemList;
    private final Context context;

    public ItemAdapter(Context context, List<String[]> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(List<String[]> filteredList) {
        this.itemList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

