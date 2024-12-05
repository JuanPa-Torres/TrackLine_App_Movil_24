package com.example.trackline24.ui.route;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trackline24.AddPlanFragment;
import com.example.trackline24.AddRouteFragment;
import com.example.trackline24.Event;
import com.example.trackline24.R;
import com.example.trackline24.Ruta;
import com.example.trackline24.RutaAdapter;
import com.example.trackline24.databinding.FragmentRouteBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RouteFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker startMarker;
    private Marker endMarker;
    Button addRoute;
    private FragmentRouteBinding binding;
    private ListView listViewRutas;
    private RutaAdapter rutaAdapter;
    private List<Ruta> rutas;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RouteViewModel routeViewModel =
                new ViewModelProvider(this).get(RouteViewModel.class);

        binding = FragmentRouteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        addRoute = root.findViewById(R.id.addRoute);
        addRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddRouteFragment addRouteFragment = new AddRouteFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, addRouteFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        rutas = new ArrayList<>();
        rutaAdapter = new RutaAdapter(getContext(),rutas);
        listViewRutas = root.findViewById(R.id.listViewRuta);
        listViewRutas.setAdapter(rutaAdapter);
        listViewRutas.setOnItemClickListener((parent, view, position, id) -> {
            Ruta rutaSeleccionada = rutas.get(position);
            mostrarRutaEnMapa(rutaSeleccionada);
        });

        obtenerRutas();
        return root;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng initialLocation = new LatLng(19.432608, -99.133209); // Ciudad de México
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10));
    }

    private void obtenerRutas() {
        String apiUrl = "https://trackline.pythonanywhere.com/get_routes";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            JSONArray routesArray = response.getJSONArray("routes");
                            List<Ruta> rutasList = new ArrayList<>();

                            for (int i = 0; i < routesArray.length(); i++) {
                                JSONObject routeObject = routesArray.getJSONObject(i);

                                String createdAt = routeObject.getString("createdAt");
                                String idRuta = routeObject.getString("idRuta");
                                double latFinish = routeObject.getDouble("latFinish");
                                double latStart = routeObject.getDouble("latStart");
                                double lonFinish = routeObject.getDouble("lonFinish");
                                double lonStart = routeObject.getDouble("lonStart");
                                String nombreRuta = routeObject.getString("nombreRuta");

                                Ruta ruta = new Ruta(
                                        createdAt, idRuta, latStart, lonStart, latFinish, lonFinish, nombreRuta
                                );
                                rutasList.add(ruta);
                            }

                            rutaAdapter.clear();
                            rutaAdapter.addAll(rutasList);
                            rutaAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "No se pudieron obtener rutas.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error procesando los datos.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error al obtener eventos", Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }

    private void mostrarRutaEnMapa(Ruta ruta) {
        if (mMap == null) return;

        if (startMarker != null) startMarker.remove();
        if (endMarker != null) endMarker.remove();

        // Agregar nuevas marcas
        LatLng inicio = new LatLng(ruta.getLatStart(), ruta.getLonStart());
        LatLng fin = new LatLng(ruta.getLatFinish(), ruta.getLonFinish());

        startMarker = mMap.addMarker(new MarkerOptions()
                .position(inicio)
                .title("Inicio: " + ruta.getNombreRuta()));

        endMarker = mMap.addMarker(new MarkerOptions()
                .position(fin)
                .title("Fin: " + ruta.getNombreRuta()));

        // Mover la cámara para mostrar ambas marcas
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(inicio)
                .include(fin)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

}