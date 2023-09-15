package com.github.kevincnzuk.aklliveevbus;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiJsonProcessor {

    private static final String TAG = "ApiJsonProcessor";
    
    public static List<VehicleVO> getApiDatasets(String json) {
        Log.d(TAG, "getApiDatasets: length of json: " + json.length());

        List<VehicleVO> datasets = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(json);

            JSONObject response = root.getJSONObject("response");

            JSONArray entity = response.getJSONArray("entity");

            for (int i = 0; i < entity.length(); i++) {
                JSONObject object = entity.getJSONObject(i).getJSONObject("vehicle");

                if (!object.isNull("trip") && !object.isNull("vehicle") && !object.isNull("position")) {
                    JSONObject trip = object.getJSONObject("trip");

                    String tripId = JsonUtils.checkNameValidityThenReturnString(trip, "trip_id");
                    String startTime = JsonUtils.checkNameValidityThenReturnString(trip, "start_time");
                    String startDate = JsonUtils.checkNameValidityThenReturnString(trip, "start_date");
                    String routeId = JsonUtils.checkNameValidityThenReturnString(trip, "route_id");
                    if (routeId != null && routeId.indexOf("-") > 0) routeId = routeId.substring(0, routeId.indexOf("-"));

                    JSONObject vehicle = object.getJSONObject("vehicle");

                    String id = JsonUtils.checkNameValidityThenReturnString(vehicle, "id");
                    String label = JsonUtils.checkNameValidityThenReturnString(vehicle, "label");
                    String licensePlate = JsonUtils.checkNameValidityThenReturnString(vehicle, "license_plate");

                    JSONObject position = object.getJSONObject("position");

                    double latitude = JsonUtils.checkNameValidityThenReturnDouble(position, "latitude");
                    double longitude = JsonUtils.checkNameValidityThenReturnDouble(position, "longitude");

                    VehicleVO vo = new VehicleVO();
                    vo.setId(id);
                    vo.setLabel(label);
                    vo.setLicensePlate(licensePlate);
                    vo.setTripId(tripId);
                    vo.setStartTime(startTime);
                    vo.setStartDate(startDate);
                    vo.setRouteId(routeId);
                    vo.setLatitude(latitude);
                    vo.setLongitude(longitude);
                    vo.setFullJson(entity.getJSONObject(i).toString());

                    datasets.add(vo);
                }
            }

            Log.d(TAG, "getApiDatasets: datasets.length = " + datasets.size());
        } catch (JSONException e) {
            Log.e(TAG, "getApiDatasets: ", e);
        }

        return datasets;
    }
}
