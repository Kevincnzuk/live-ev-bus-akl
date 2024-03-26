package com.github.kevincnzuk.aklliveevbus;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    private TextInputLayout tily;
    private TextInputEditText tiet;
    private RecyclerView rvResult;

    private double timestamp;
    private List<VehicleVO> vehicleVOList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.search_toolbar);
        tily = findViewById(R.id.search_bar_layout);
        tiet = findViewById(R.id.search_bar_edit_text);
        rvResult = findViewById(R.id.search_rv_result);

        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        timestamp = intent.getDoubleExtra("timestamp", 0);
        vehicleVOList = (List<VehicleVO>) intent.getSerializableExtra("datasets");

        toolbar.setSubtitle(String.format("Last updated: "
                + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .format(new Date(Math.round(timestamp * 1000)))));

        initSearch();
    }

    private void initSearch() {
        tiet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<VehicleVO> list = search_buses(s.toString().toUpperCase());

                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                rvResult.setLayoutManager(manager);
                BusAdapter adapter = new BusAdapter(SearchActivity.this, list);
                rvResult.setAdapter(adapter);

                if (!s.toString().isEmpty() && list.isEmpty()) {
                    tily.setError(getString(R.string.search_404));
                } else {
                    tily.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });
    }

    private List<VehicleVO> search_buses(String keyword) {
        List<VehicleVO> list = new ArrayList<>();

        if (!keyword.isEmpty()) {
            for (int i = 0; i < vehicleVOList.size(); i++) {
                VehicleVO vo = vehicleVOList.get(i);
                if (vo.getId().contains(keyword)) {
                    list.add(vo);
                } else if (vo.getLabel().contains(keyword)) {
                    list.add(vo);
                } else if (vo.getLicensePlate().contains(keyword)) {
                    list.add(vo);
                }
            }
        }

        return list;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}