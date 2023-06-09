package com.cse482b.cvdtraining;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * DictionaryActivity provides a full dictionary screen with searchable and scrollable word-definition items.
 */
public class DictionaryActivity extends AppCompatActivity {

    /**
     * A list of word-definition items/pairs, i.e. [{Word, Definition}, ...]
     */
    private List<String[]> itemList;
    /**
     * Manages displaying the portion of the itemList that we filter using the search word.
     * @see androidx.recyclerview.widget.RecyclerView.Adapter
     */
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        // Set up the search input
        SearchView searchView = findViewById(R.id.searchView);
        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setAdjustViewBounds(true);

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        // Load the dictionary items from the dictionary.json
        itemList = new ArrayList<>();
        String jsonFilename = "dictionary";
        List<JSONObject> jsonObjects = GlobalMethods.parseJSONList(this, jsonFilename, "raw");

        for (JSONObject jsonObject : jsonObjects) {
            try {
                itemList.add(new String[]{(String) jsonObject.get("word"), (String) jsonObject.get("definition")});
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Set up the RecyclerView for displaying the dictionary items
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        itemAdapter = new ItemAdapter(this, itemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(itemAdapter);

        // Home and Help buttons
        ImageView home = findViewById(R.id.dict_home_button);
        Button help = findViewById(R.id.dict_help_button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DictionaryActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DictionaryActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Uses the itemAdapter to filter the RecyclerView to only show items in itemList where the word
     * contains the characters in the search string (ignoring case).
     * @param text the search string to filter by
     */
    private void filterList(String text) {
        List<String[]> filteredList = new ArrayList<>();
        for (String[] item : itemList)
            if (item[0].toLowerCase().contains(text.toLowerCase()))
                filteredList.add(item);

        if (!filteredList.isEmpty())
            itemAdapter.setFilteredList(filteredList);
    }
}

