package com.github.kevincnzuk.aklliveevbus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textview.MaterialTextView;
import com.permissionx.guolindev.PermissionX;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";

    private MaterialToolbar toolbar;
    private MapView mapView;
    private FrameLayout flBottomSheet;
    private MaterialTextView tvMapContributors;

    private double timestamp;
    private List<VehicleVO> vehicleVOList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        timestamp = intent.getDoubleExtra("timestamp", 0);
        vehicleVOList = (List<VehicleVO>) intent.getSerializableExtra("datasets");

        toolbar = findViewById(R.id.map_toolbar);
        mapView = findViewById(R.id.map_osm_map);
        flBottomSheet = findViewById(R.id.map_bottom_sheet);
        tvMapContributors = findViewById(R.id.map_map_tv_contributor);

        toolbar.setSubtitle(String.format("Last updated: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .format(new Date(Math.round(timestamp * 1000)))));
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        Configuration.getInstance().load(this, SPHelper.getInstance(this).getPref());
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(false);

        IMapController mapController = mapView.getController();
        mapController.setZoom(16.0);
        mapController.setCenter(new GeoPoint(-36.8484, 174.7621));

        initPermissionX();

        initMapOverlays(vehicleVOList);

        BottomSheetBehavior behavior = BottomSheetBehavior.from(flBottomSheet);;
        behavior.setPeekHeight(194);
        behavior.setHideable(false);

        // Reference to https://stackoverflow.com/questions/2734270/how-to-make-links-in-a-textview-clickable
        tvMapContributors.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initPermissionX() {
        PermissionX.init(this)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "These permissions are required to use map function.", "I understand"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "You will need to manually allow the permission through System Setting.", "I understand"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (!allGranted) {
                        Toast.makeText(MapActivity.this, "Missing permissions!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initMapOverlays (List<VehicleVO> list) {
        // Show my location
        MyLocationNewOverlay locationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        locationNewOverlay.enableMyLocation();
        mapView.getOverlays().add(locationNewOverlay);

        // Show buses on map with label
        List<IGeoPoint> points = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            VehicleVO vo = list.get(i);

            points.add(new LabelledGeoPoint(vo.getLatitude(), vo.getLongitude(),
                    vo.getRouteId() + " / " + vo.getLabel()));
        }

        SimplePointTheme theme = new SimplePointTheme(points, true);

        Paint text = new Paint();
        text.setStyle(Paint.Style.FILL);
        text.setColor(Color.parseColor("#2c384c"));
        text.setTextAlign(Paint.Align.LEFT);
        text.setTextSize(64);
        text.setAntiAlias(true);

        Paint point = new Paint();
        point.setStyle(Paint.Style.FILL);
        point.setColor(Color.parseColor("#7ca117"));
        point.setStrokeCap(Paint.Cap.ROUND);
        point.setAntiAlias(true);

        SimpleFastPointOverlayOptions options = SimpleFastPointOverlayOptions.getDefaultStyle()
                .setAlgorithm(SimpleFastPointOverlayOptions.RenderingAlgorithm.MAXIMUM_OPTIMIZATION)
                .setRadius(24)
                .setCellSize(16)
                .setTextStyle(text)
                .setPointStyle(point)
                .setIsClickable(true);

        SimpleFastPointOverlay overlay = new SimpleFastPointOverlay(theme, options);

        overlay.setOnClickListener((points1, point1) -> {
            Intent intent = new Intent(MapActivity.this, DetailActivity.class);
            intent.putExtra("json", list.get(point1).getFullJson());
            startActivity(intent);
        });

        mapView.getOverlays().add(overlay);

        // 实现点击车辆进入详情页：https://github.com/osmdroid/osmdroid/wiki/Markers,-Lines-and-Polygons-(Java)

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}