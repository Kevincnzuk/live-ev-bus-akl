package com.github.kevincnzuk.aklliveevbus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextInputEditText etPrimaryKey;
    private TextInputEditText etSecondaryKey;
    private MaterialButton btnKeyWhere;
    private MaterialButton btnAboutApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.settings_toolbar);
        etPrimaryKey = findViewById(R.id.settings_et_primary_key);
        etSecondaryKey = findViewById(R.id.settings_et_secondary_key);
        btnKeyWhere = findViewById(R.id.settings_btn_key_where);
        btnAboutApp = findViewById(R.id.settings_btn_about_app);

        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        initComponents();
    }

    private void initComponents() {
        etPrimaryKey.setText(SPHelper.getInstance(this).get(SPHelper.PRIMARY_KEY, ""));
        etSecondaryKey.setText(SPHelper.getInstance(this).get(SPHelper.SECONDARY_KEY, ""));

        btnKeyWhere.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");

            Uri content_url = Uri.parse("https://github.com/Kevincnzuk/live-ev-bus-akl#get-an-api-key");
            intent.setData(content_url);

            startActivity(intent);
        });

        btnAboutApp.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");

            Uri content_url = Uri.parse("https://github.com/Kevincnzuk/live-ev-bus-akl");
            intent.setData(content_url);

            startActivity(intent);
        });
    }

    private void save() {
        SPHelper.getInstance(this).save(SPHelper.PRIMARY_KEY, etPrimaryKey.getText().toString());
        SPHelper.getInstance(this).save(SPHelper.SECONDARY_KEY, etSecondaryKey.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_settings_save) {
            save();
            Snackbar.make(toolbar, "Changes saved!", Snackbar.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}