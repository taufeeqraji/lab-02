package com.example.listcity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView cityList;
    private ArrayAdapter<String> cityAdapter;
    private ArrayList<String> dataList;

    private Button addCityBtn;
    private Button deleteCityBtn;

    private int selectedIndex = -1; // -1 means nothing selected

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cityList = findViewById(R.id.city_list);
        addCityBtn = findViewById(R.id.btn_add_city);
        deleteCityBtn = findViewById(R.id.btn_delete_city);

        String[] cities = {"Edmonton", "Vancouver", "Moscow", "Sydney", "Berlin", "Tokyo"};
        dataList = new ArrayList<>(Arrays.asList(cities));

        cityAdapter = new ArrayAdapter<>(this, R.layout.content, R.id.content_view, dataList);
        cityList.setAdapter(cityAdapter);
        cityList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Tap to select a city
        cityList.setOnItemClickListener((parent, view, position, id) -> {
            selectedIndex = position;
            cityList.setItemChecked(position, true);
        });

        addCityBtn.setOnClickListener(v -> showAddCityDialog());

        deleteCityBtn.setOnClickListener(v -> deleteSelectedCity());
    }

    private void showAddCityDialog() {
        final EditText input = new EditText(this);
        input.setHint("Enter city name");

        new AlertDialog.Builder(this)
                .setTitle("Add City")
                .setView(input)
                .setPositiveButton("CONFIRM", (dialog, which) -> {
                    String cityName = input.getText().toString().trim();

                    if (TextUtils.isEmpty(cityName)) {
                        Toast.makeText(this, "City name cannot be empty.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Optional: prevent duplicates (case-insensitive)
                    for (String c : dataList) {
                        if (c.equalsIgnoreCase(cityName)) {
                            Toast.makeText(this, "That city is already in the list.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    dataList.add(cityName);
                    cityAdapter.notifyDataSetChanged();

                    // auto-scroll to the new city
                    cityList.smoothScrollToPosition(dataList.size() - 1);
                })
                .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteSelectedCity() {
        if (selectedIndex < 0 || selectedIndex >= dataList.size()) {
            Toast.makeText(this, "Tap a city first to select it.", Toast.LENGTH_SHORT).show();
            return;
        }

        dataList.remove(selectedIndex);
        cityAdapter.notifyDataSetChanged();

        // clear selection
        cityList.clearChoices();
        selectedIndex = -1;
    }
}
