package com.example.menuandsettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tvLongPressMe;
    TextView tvToggleState;
    boolean toggleChangeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLongPressMe = findViewById(R.id.tvLongPressMe);
        tvToggleState = findViewById(R.id.tvToggleState);

        registerForContextMenu(tvLongPressMe);
    }

    @Override
    protected void onDestroy() {
        unregisterForContextMenu(tvLongPressMe);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean toggleState = sharedPreferences.getBoolean("toggle_state", false);

        String message = "Toggle State is " + (toggleState?  "On" : "Off");
        tvToggleState.setText(message);
    }

    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, tvLongPressMe);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.text_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_change_color:
                        changeColor();
                        break;
                }
                return false;
            }
        });

        popupMenu.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_help:
                Toast.makeText(this, "Help Selected", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_about:
                Toast.makeText(this, "About Selected", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Settings Selected", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflater = getMenuInflater();
        if(v == tvLongPressMe) {
            menuInflater.inflate(R.menu.text_menu, menu);
        }

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_change_color:
                changeColor();
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void changeColor() {
        toggleChangeColor = !toggleChangeColor;

        int colorId = toggleChangeColor ?
                android.R.color.holo_blue_dark : android.R.color.holo_green_dark;

        int color;

        if(Build.VERSION.SDK_INT >= 23) {
            color = getResources().getColor(colorId, getTheme());
        } else {
            color = getResources().getColor(colorId);
        }

        tvLongPressMe.setTextColor(color);
    }
}