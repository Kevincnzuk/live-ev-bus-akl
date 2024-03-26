package com.github.kevincnzuk.aklliveevbus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //private SearchBar searchBar;
    //private SearchView searchView;
    //private RecyclerView searchResultRV;
    private MaterialToolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private double timestamp = 0.0;
    private List<VehicleVO> vehicleVOList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // searchBar = findViewById(R.id.main_search_bar);
        // searchView = findViewById(R.id.main_search_view);
        // searchResultRV = findViewById(R.id.main_search_rv);
        toolbar = findViewById(R.id.main_toolbar);
        swipeRefreshLayout = findViewById(R.id.main_swipe_refresh_layout);
        recyclerView = findViewById(R.id.main_recycler_view);

        // setSupportActionBar(searchBar);
        setSupportActionBar(toolbar);

        swipeRefreshLayout.setOnRefreshListener(this::getDataFromPortalOkHttp);

        if (!Utils.isConnectedAvailableNetwork(this)) {
            // If no internet connection, do not pull api, remind user first or JSONException.
            Snackbar.make(swipeRefreshLayout, "You have no internet connection.", Snackbar.LENGTH_LONG)
                    .setAction("Open settings", v -> {
                        ComponentName cm = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                        Intent intent = new Intent("/");
                        intent.setComponent(cm);
                        intent.setAction("android.intent.action.VIEW");
                        startActivity(intent);
                    }).show();
        } else if (SPHelper.getInstance(this).get(SPHelper.PRIMARY_KEY, "").equals("")) {
            // If primary key is empty, do not pull api, remind user first or exception.
            Snackbar.make(swipeRefreshLayout, "You have not set up a key.", Snackbar.LENGTH_LONG)
                    .setAction("Set up", v -> startActivity(
                            new Intent(MainActivity.this, SettingsActivity.class))
                    ).show();
        } else {
            // Safe to do so.
            getDataFromPortalOkHttp();
        }
    }

    /* private void initSearch() {
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        searchResultRV.setLayoutManager(manager);
        BusAdapter adapter = new BusAdapter(this, search_buses(searchView.getText().toString()));
        searchResultRV.setAdapter(adapter);
    } */

    private List<VehicleVO> search_buses(String keyword) {
        List<VehicleVO> list = new ArrayList<>();

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

        return list;
    }

    private void getDataFromPortalOkHttp() {
        new Thread(() -> {
            StringBuilder fleets = new StringBuilder();

            List<BusVO> busVOList = new ArrayList<>();

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://raw.githubusercontent.com/Kevincnzuk/live-ev-bus-akl/master/datasets/akl_ev.json")
                    .build();

            try {
                Response response = client.newCall(request).execute();

                JSONArray array = new JSONArray(response.body().string());

                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    BusVO vo = new BusVO();
                    vo.setId(object.getString("id"));
                    vo.setFleet(object.getString("fleet"));
                    vo.setEnergy(object.getString("energy"));

                    busVOList.add(vo);
                }

                busVOList.sort((o1, o2) -> {
                    String bus1 = o1.getId();
                    String bus2 = o2.getId();
                    return bus1.compareTo(bus2);
                });

                Log.d(TAG, "updateAklEv: Complete");
            } catch (Exception e) {
                Log.e(TAG, "updateAklEv: OkHttp: ", e);
            }

            List<String> stringList = new ArrayList<>();
            for (int i = 0; i < busVOList.size(); i++) {
                stringList.add(busVOList.get(i).getId());
            }

            stringList.sort(Comparator.naturalOrder());
            for (int i = 0; i < stringList.size(); i++) {
                fleets.append(stringList.get(i)).append(",");
            }

            String json = "";
            String url = "https://api.at.govt.nz/realtime/legacy/vehiclelocations?vehicleid=";

            OkHttpClient client1 = new OkHttpClient();

            Request request1 = new Request.Builder()
                    .url(url + fleets)
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Ocp-Apim-Subscription-Key",
                            SPHelper.getInstance(this).get(SPHelper.PRIMARY_KEY, ""))
                    .build();

            try {
                Response response1 = client1.newCall(request1).execute();

                json = response1.body().string();

                Log.d(TAG, "getDataFromPortalOkHttp: Response received");
            } catch (Exception e) {
                Log.e(TAG, "getDataFromPortalOkHttp: ", e);
            }

           showResponse(json, busVOList);
        }).start();
    }

    private void showResponse(final String response, List<BusVO> busVOList) {
        runOnUiThread(() -> {
            try {
                timestamp = new JSONObject(response).getJSONObject("response")
                        .getJSONObject("header").getDouble("timestamp");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            List<VehicleVO> list = new ArrayList<>();
            try {
                list = ApiJsonProcessor.getApiDatasets(response);
            } catch (Exception e) {
                Snackbar
                        .make(swipeRefreshLayout, "Data source currently unavailable.", Snackbar.LENGTH_LONG)
                        .setAction("Why?", v -> {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");

                            Uri content_url = Uri.parse("https://github.com/Kevincnzuk/live-ev-bus-akl/blob/master/no_datasets.md");
                            intent.setData(content_url);

                            startActivity(intent);
                        }).show();
            }
            if (!list.isEmpty()) {
                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(manager);
                BusAdapter adapter = new BusAdapter(this, list);
                recyclerView.setAdapter(adapter);

                vehicleVOList = list;

                Snackbar.make(swipeRefreshLayout, "Realtime has been updated.", Snackbar.LENGTH_LONG).show();
                toolbar.setSubtitle(String.format("Last updated: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                        .format(new Date(Math.round(timestamp * 1000)))));
                swipeRefreshLayout.setRefreshing(false);

                //initSearch();
            } else {
                Snackbar make = Snackbar.make(swipeRefreshLayout, "Hmm, seems like no bus is running.", Snackbar.LENGTH_LONG);
                make.setAction("Why?", v -> {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");

                    Uri content_url = Uri.parse("https://github.com/Kevincnzuk/live-ev-bus-akl/blob/master/no_bus_running.md");
                    intent.setData(content_url);

                    startActivity(intent);
                });
                make.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_main_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (item.getItemId() == R.id.menu_main_map) {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("timestamp", timestamp);
            intent.putExtra("datasets", (Serializable) vehicleVOList);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_main_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("timestamp", timestamp);
            intent.putExtra("datasets", (Serializable) vehicleVOList);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}