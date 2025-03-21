package com.example.foodreelappassignment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodreelappassignment.adapter.MyAdapter;
import com.example.foodreelappassignment.model.FoodData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;  // 🔹 ADD THIS IMPORT
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    private MyAdapter adapter;  // Declare at class level


    private ArrayList<FoodData> dataArrayList = new ArrayList<>();

    private FusedLocationProviderClient fusedLocationClient;
    private RequestQueue requestQueue;
    private static final int LOCATION_PERMISSION_REQUEST = 100;

    private static final int SEARCH_RADIUS = 10000; // 10 km radius

    public static ArrayList<FoodData> foodList = new ArrayList<>();

    private String dummyVideoURL[] = {"https://media.istockphoto.com/id/1940149072/video/beef-burger-ingredients-falling-and-landing-in-the-bun.mp4?s=mp4-640x640-is&k=20&c=YPQ_f0NJnxubW0iyngc5ebTzoadjQ6jajd9HmFxGYZ4=",
            "https://assets.mixkit.co/videos/44001/44001-720.mp4",
            "https://cdn.pixabay.com/video/2025/02/16/258562_large.mp4",
            "https://media.istockphoto.com/id/1833231083/video/super-slow-motion-of-rotating-salad.mp4?s=mp4-640x640-is&k=20&c=-1G4VJ9kZe0BKUSdSUGHvzoO95vfIixmPwDBU9TPp78=",
            "https://media.istockphoto.com/id/1406767577/video/sharing-food.mp4?s=mp4-640x640-is&k=20&c=q2EtB2sG3Y2nsfK0QrICw8ukeasoajld0FhN9fnQ-qY=",
            "https://cdn.pixabay.com/video/2017/08/29/11617-231571913_large.mp4",
            "https://cdn.pixabay.com/video/2024/02/11/200157-912127896_large.mp4"


    };


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        // 🔹 Initialize Location Client Here
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // Request Location Permission
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        } else {
            getCurrentLocation();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return true; // Enable vertical scrolling
            }
        };

        //loadData();

        getCurrentLocation();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Resume the first visible video
                    playVisibleVideo();
                }
            }
        });

        // Set smooth scrolling behavior
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        recyclerView.setAdapter(adapter);

        return view;
    }

    private void playVisibleVideo() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager != null) {
            int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

            // Stop all videos first
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                if (holder instanceof MyAdapter.ViewHolder) {
                    ((MyAdapter.ViewHolder) holder).exoPlayer.setPlayWhenReady(false);
                }
            }

            // Play only the first visible video
            if (firstVisibleItem != RecyclerView.NO_POSITION) {
                MyAdapter.ViewHolder holder = (MyAdapter.ViewHolder)  recyclerView.findViewHolderForAdapterPosition(firstVisibleItem);
                if (holder != null && holder.exoPlayer != null) {
                    holder.exoPlayer.setPlayWhenReady(true);
                }
            }
        }
    }

    // Convert Latitude & Longitude to Address
    private String getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                return address.getAddressLine(0); // Full Address
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Address not found";
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        if (fusedLocationClient != null) {
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Location location = task.getResult();
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();

                                fetchNearbyRestaurants(latitude,longitude);
                                // 🔹 Get Address from LatLng
                                String address = getAddress(latitude, longitude);

                                // 🔹 Display Location Details
                                Log.d("Location : ","Latitude: " + latitude + "\nLongitude: " + longitude + "\nAddress: " + address);
                                //Toast.makeText(getActivity(), "Latitude: " + latitude + "\nLongitude: " + longitude + "\nAddress: " + address, Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(getActivity(), "Failed to get location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "FusedLocationProviderClient is null", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Ensures `onViewRecycled()` is triggered
        }
    }

    public  void fetchNearbyRestaurants(double latitude, double longitude) {
        foodList = new ArrayList<>();

        String url = "https://overpass-api.de/api/interpreter?data=[out:json];node(around:10000," +  latitude + "," + longitude + ")[amenity=restaurant];out;";

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray elements = response.getJSONArray("elements");

                            int count = 0;

                            for (int i = 0; i < elements.length(); i++) {
                                JSONObject place = elements.getJSONObject(i);
                                String name = "Unnamed Restaurant";
                                String location = "Unknown Location";
                                String url = "";

                                if (place.has("tags")) {
                                    JSONObject tags = place.getJSONObject("tags");
                                    if (tags.has("name")) {
                                        name = tags.getString("name");
                                    }
                                    if (tags.has("addr:city")) {
                                        location = tags.getString("addr:city");
                                    }
                                }

                                // setting dummy video url

                                if(count<7)
                                {
                                    url = dummyVideoURL[count];
                                    count++;
                                }
                                else
                                {
                                    count=0;
                                    url = dummyVideoURL[count];
                                }

                                // Add fetched data to foodList
                                foodList.add(new FoodData(name, location, url));
                            }

                            adapter = new MyAdapter(getContext(),foodList);

                            recyclerView.setAdapter(adapter);

                            // ✅ Notify Adapter (if using RecyclerView)
                            updateRecyclerView();

                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Error parsing data", Toast.LENGTH_SHORT).show();
                            Log.e("JSON_ERROR", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "API request failed", Toast.LENGTH_SHORT).show();
                        Log.e("API_ERROR", "Error: " + error.getMessage());
                    }
                });

        requestQueue.add(request);

    }


    private void updateRecyclerView() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }



}