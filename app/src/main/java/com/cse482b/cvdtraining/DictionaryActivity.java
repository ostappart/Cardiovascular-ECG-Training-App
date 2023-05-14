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
import java.util.Iterator;
import java.util.List;

public class DictionaryActivity extends AppCompatActivity {

    private SearchView searchView;
    private List<String[]> itemList;    // [{Word, Definition}, ...]
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] jsonFilename = {"dictionary"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        ImageView home = findViewById(R.id.dict_home_button);
        Button help = findViewById(R.id.dict_help_button);

        searchView = findViewById(R.id.searchView);
        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setAdjustViewBounds(true);

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        itemList = new ArrayList<>();
        List<JSONObject> jsonObjects = GlobalMethods.parseJSONList(this, jsonFilename, "raw");

        for (JSONObject jsonObject : jsonObjects) {
            try {
                itemList.add(new String[]{(String) jsonObject.get("word"), (String) jsonObject.get("definition")});
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        itemAdapter = new ItemAdapter(this, itemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(itemAdapter);


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

    private void filterList(String text) {
        List<String[]> filteredList = new ArrayList<>();
        for (String[] item : itemList) {
            if (item[0].toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (!filteredList.isEmpty())
            itemAdapter.setFilteredList(filteredList);
    }
}

