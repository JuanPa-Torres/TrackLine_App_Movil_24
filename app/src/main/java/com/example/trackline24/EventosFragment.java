package com.example.trackline24;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventosFragment extends Fragment {
    Button btnAgregarEvento;
    private ListView listViewEvento;
    private EventoAdapter eventoAdapter;
    private List<Event> eventos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_eventos, container, false);
        btnAgregarEvento = rootView.findViewById(R.id.btnAgregarEvento);
        eventos = new ArrayList<>();
        eventoAdapter = new EventoAdapter(getContext(), eventos, requireActivity().getSupportFragmentManager());
        listViewEvento = rootView.findViewById(R.id.listViewEvento);
        listViewEvento.setAdapter(eventoAdapter);
        btnAgregarEvento.setOnClickListener(v -> replaceFragment(new AddEventFragment()));

        obtenerEventos();
        return rootView;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void obtenerEventos() {
        String apiUrl = "https://trackline.pythonanywhere.com/get_events";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    try {
                        JSONArray events = response.getJSONArray("events");
                        List<Event> eventos = new ArrayList<>();

                        for (int i = 0; i < events.length(); i++) {
                            JSONObject event = events.getJSONObject(i);

                            String imageUrl = event.getString("imageUrl");
                            String name = event.getString("name");
                            String nameOrganizer = event.getString("nameOrganizer");
                            String award = event.getString("award");
                            String dateStartRegistry = event.getString("dateStartRegistry");
                            String eventDate = event.getString("eventDate");
                            String facebookLink = event.getString("facebookLink");
                            String idActivity = event.getString("idActivity");
                            String idEvent = event.getString("idEvent");
                            String latitudeFinish = event.getString("latitudeFinish");
                            String longitudeFinish = event.getString("longitudeFinish");
                            String latitudeStart = event.getString("latitudeStart");
                            String longitudeStart = event.getString("longitudeStart");
                            String objective = event.getString("objective");
                            String type = event.getString("type");
                            String whatsappLink = event.getString("whatsappLink");

                            Event nuevoEvento = new Event(award,dateStartRegistry,eventDate,facebookLink,idActivity,idEvent,imageUrl,
                                    latitudeFinish,latitudeStart,longitudeFinish,longitudeStart,name,nameOrganizer,objective,type,whatsappLink);
                            eventos.add(nuevoEvento);
                        }
                        eventoAdapter.clear();
                        eventoAdapter.addAll(eventos);
                        eventoAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error al obtener eventos", Toast.LENGTH_SHORT).show();
                }
        );
        Volley.newRequestQueue(requireContext()).add(request);
    }
}