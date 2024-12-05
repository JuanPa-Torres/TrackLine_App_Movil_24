package com.example.trackline24;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class UbicacionEventoFragment extends Fragment implements OnMapReadyCallback {

    private LatLng startMarker, endMarker;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ubicacion_evento, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        Bundle bndl = getArguments();
        Log.d("Marcas", ""+bndl.getString("latitudeFinish")+"_______"+bndl.getString("longitudeFinish"));

        if (bndl != null) {
            double latitudeFinish = Double.parseDouble(bndl.getString("latitudeFinish"));
            double longitudeFinish = Double.parseDouble(bndl.getString("longitudeFinish"));
            double latitudeStart = Double.parseDouble(bndl.getString("latitudeStart"));
            double longitudeStart = Double.parseDouble(bndl.getString("longitudeStart"));
            startMarker = new LatLng(latitudeStart, longitudeStart);
            endMarker = new LatLng(latitudeFinish,longitudeFinish);
        }
        return v;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (startMarker != null && endMarker != null) {
            mMap.addMarker(new MarkerOptions()
                    .position(startMarker)
                    .title("Inicio"));

            mMap.addMarker(new MarkerOptions()
                    .position(endMarker)
                    .title("Destino"));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startMarker, 15));
        }
    }
}