package com.example.trackline24;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;

import java.util.List;

public class EventoAdapter extends ArrayAdapter<Event> {
    private FragmentManager fragmentManager;
    public EventoAdapter(Context context, List<Event> eventos, FragmentManager fragmentManager) {
        super(context, 0, eventos);
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event evento = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_list_item, parent, false);
        }

        TextView organizer = convertView.findViewById(R.id.tvEventSubtitle);
        TextView name = convertView.findViewById(R.id.tvEventTitle);
        ImageView imageView = convertView.findViewById(R.id.eventImage);
        Button btnDetalles = convertView.findViewById(R.id.btnConsultar);

        btnDetalles.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("name", evento.getName());
            bundle.putString("nameOrganizer", evento.getNameOrganizer());
            bundle.putString("imageUrl", evento.getImageUrl());
            bundle.putString("award", evento.getAward());
            bundle.putString("dateStartRegistry", evento.getDateStartRegistry());
            bundle.putString("eventDate", evento.getEventDate());
            bundle.putString("facebookLink", evento.getFacebookLink());
            bundle.putString("idActivity", evento.getIdActivity());
            bundle.putString("idEvent", evento.getIdEvent());
            bundle.putString("latitudeFinish", evento.getLatitudeFinish());
            bundle.putString("longitudeFinish", evento.getLongitudeFinish());
            bundle.putString("longitudeStart", evento.getLongitudeStart());
            bundle.putString("latitudeStart", evento.getLatitudeStart());
            bundle.putString("objectiveobjective", evento.getObjective());
            bundle.putString("type", evento.getType());
            bundle.putString("whatsappLink", evento.getWhatsappLink());


            ConsultarEventoFragment consultarEventoFragment = new ConsultarEventoFragment();
            replaceFragment(consultarEventoFragment, bundle);
        });
        organizer.setText(evento.getNameOrganizer());
        name.setText(evento.getName());
        Log.d("Mensaje", "Ai va la renderizada ponte buzo");
        Picasso.get().load(evento.getImageUrl()).error(R.drawable.agenda).into(imageView);
        Log.d("Eventos", "name: " + evento.getName() + "nombreOrganizador: "+ evento.getNameOrganizer()+ "url:" + evento.getImageUrl());
        return convertView;
    }

    private void replaceFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentManager fragmentManager2 = fragmentManager;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
