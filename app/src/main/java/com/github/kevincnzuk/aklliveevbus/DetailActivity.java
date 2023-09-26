package com.github.kevincnzuk.aklliveevbus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";

    private MaterialToolbar toolbar;
    private RecyclerView rvBusImages;
    private MaterialTextView tvMapContributors;
    private MapView mapView;
    private WebView wvFleetLists;
    private MaterialTextView tvFullJson;

    private VehicleVO vehicleVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String json = intent.getStringExtra("json");

        vehicleVO = phaseJsonFromUpper(json);

        toolbar = findViewById(R.id.detail_toolbar);
        rvBusImages = findViewById(R.id.detail_bus_images);
        tvMapContributors = findViewById(R.id.detail_map_tv_contributor);
        mapView = findViewById(R.id.detail_map_view);
        wvFleetLists = findViewById(R.id.detail_web_view);
        tvFullJson = findViewById(R.id.detail_full_json);

        toolbar.setTitle(vehicleVO.getLabel());
        toolbar.setSubtitle(vehicleVO.getId() + " / " + vehicleVO.getLicensePlate());
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        tvMapContributors.setMovementMethod(LinkMovementMethod.getInstance());

        initBusImages();
        initWebView(vehicleVO.getLicensePlate());
        tvFullJson.setText(json);

        // Initialize MapView data
        Configuration.getInstance().load(this, SPHelper.getInstance(this).getPref());
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        GeoPoint position = new GeoPoint(-36.8484, 174.7621);

        try {
            JSONObject object = new JSONObject(json)
                    .getJSONObject("vehicle")
                    .getJSONObject("position");
            position.setLatitude(object.getDouble("latitude"));
            position.setLongitude(object.getDouble("longitude"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        IMapController mapController = mapView.getController();
        mapController.setZoom(19.0);
        mapController.setCenter(position);

        Marker marker = new Marker(mapView);
        marker.setPosition(position);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker.setIcon(getResources().getDrawable(R.drawable.directions_bus_black_24dp, getTheme()));
        mapView.getOverlays().add(marker);
    }

    private void initBusImages() {
        List<String> list = new ArrayList<>();
        list.add("https://raw.githubusercontent.com/Kevincnzuk/akl-live-ev-bus/main/datasets/images/csr6127glev1/20230615-_DSC0063.jpg");
        list.add("https://raw.githubusercontent.com/Kevincnzuk/akl-live-ev-bus/main/datasets/images/csr6127glev1/20230615-_DSC0067.jpg");
        list.add("https://raw.githubusercontent.com/Kevincnzuk/akl-live-ev-bus/main/datasets/images/csr6127glev1/20230615-_DSC0063.jpg");
        list.add("https://raw.githubusercontent.com/Kevincnzuk/akl-live-ev-bus/main/datasets/images/csr6127glev1/20230615-_DSC0067.jpg");

        CarouselLayoutManager manager = new CarouselLayoutManager();
        rvBusImages.setLayoutManager(manager);
        ImageAdapter adapter = new ImageAdapter(list);
        rvBusImages.setAdapter(adapter);
    }

    private void initWebView(String licensePlate) {
        wvFleetLists.getSettings().setJavaScriptEnabled(true);
        wvFleetLists.getSettings().setBuiltInZoomControls(true);
        wvFleetLists.getSettings().setUseWideViewPort(true);
        wvFleetLists.getSettings().setSupportZoom(true);
        wvFleetLists.setWebViewClient(new WebViewClient());
        wvFleetLists.setInitialScale(1);
        wvFleetLists.loadUrl("https://fleetlists.busaustralia.com/indbusdata.php?nz=ON&requesttype=rego&reqtype=" + licensePlate);
    }

    private VehicleVO phaseJsonFromUpper(String json) {
        VehicleVO vo = new VehicleVO();

        try {
            JSONObject object = new JSONObject(json).getJSONObject("vehicle");

            if (!object.isNull("trip")) {
                JSONObject trip = object.getJSONObject("trip");

                String tripId = trip.getString("trip_id");
                String startTime = trip.getString("start_time");
                String startDate = trip.getString("start_date");
                String routeId = trip.getString("route_id");
                routeId = routeId.substring(0, routeId.indexOf("-"));

                JSONObject vehicle = object.getJSONObject("vehicle");

                String id = vehicle.getString("id");
                String label = vehicle.getString("label");
                String licensePlate = vehicle.getString("license_plate");

                vo.setId(id);
                vo.setLabel(label);
                vo.setLicensePlate(licensePlate);
                vo.setTripId(tripId);
                vo.setStartTime(startTime);
                vo.setStartDate(startDate);
                vo.setRouteId(routeId);
            }
        } catch (JSONException e) {
            Log.e(TAG, "phaseJsonFromUpper: ", e);
        }
        return vo;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}