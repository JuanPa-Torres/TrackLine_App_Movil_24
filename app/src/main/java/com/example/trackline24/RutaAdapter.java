package com.example.trackline24;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class RutaAdapter extends ArrayAdapter<Ruta> {
    TextView nombreRuta;

    public RutaAdapter(@NonNull Context context, List<Ruta> eventos) {
        super(context, 0, eventos);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ruta ruta = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ruta_list_item, parent, false);
        }
        nombreRuta = convertView.findViewById(R.id.textView);
        nombreRuta.setText(ruta.getNombreRuta().toUpperCase());
        return convertView;
    }
}

