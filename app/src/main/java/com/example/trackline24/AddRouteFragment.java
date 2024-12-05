package com.example.trackline24;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class AddRouteFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Button btnSaveRoute;
    private Marker startMarker;
    private Marker endMarker;
    TextView nombreRuta;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_route, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        nombreRuta = view.findViewById(R.id.nombreRuta);
        btnSaveRoute = view.findViewById(R.id.button3);
        btnSaveRoute.setOnClickListener(v -> saveRoute());

        return view;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng initialLocation = new LatLng(19.432608, -99.133209); // Ciudad de México
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10));

        // Configurar clics en el mapa para agregar marcadores
        mMap.setOnMapClickListener(latLng -> {
            if (startMarker == null) {
                // Agregar marcador de inicio
                startMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Inicio de la ruta").draggable(true));
            } else if (endMarker == null) {
                // Agregar marcador de fin
                endMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Fin de la ruta").draggable(true));
            } else {
                Toast.makeText(getContext(), "Solo puedes agregar dos marcadores", Toast.LENGTH_SHORT).show();
            }
        });

        // Habilitar arrastre de marcadores
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {}

            @Override
            public void onMarkerDrag(@NonNull Marker marker) {}

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                marker.setPosition(marker.getPosition());
            }
        });
    }

    private void saveRoute() {
        if (startMarker == null || endMarker == null) {
            Toast.makeText(getContext(), "Por favor, selecciona ambos puntos de la ruta", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener datos de los marcadores
        LatLng startLatLng = startMarker.getPosition();
        LatLng endLatLng = endMarker.getPosition();
        String idRuta = UUID.randomUUID().toString(); // Generar un ID único
        String routeName = nombreRuta.getText().toString().trim(); // Nombre de la ruta

        // Crear JSON con los datos
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latStart", startLatLng.latitude);
            jsonObject.put("lonStart", startLatLng.longitude);
            jsonObject.put("latFinish", endLatLng.latitude);
            jsonObject.put("lonFinish", endLatLng.longitude);
            jsonObject.put("idRuta", idRuta);
            jsonObject.put("nombreRuta", routeName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Enviar los datos con Volley
        String url = "https://trackline.pythonanywhere.com/save_route";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> Toast.makeText(getContext(), "Ruta guardada exitosamente", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(getContext(), "Error al guardar la ruta: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
    }
}