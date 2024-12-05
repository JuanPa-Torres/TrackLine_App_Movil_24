package com.example.trackline24;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddPlanFragment extends Fragment {
    Spinner spinner, spinnerAct;
    String nivelExperienciaSelect;
    TextView tvDateInicio, tvDateFin, tvSelectedDates, objective;
    CalendarView calendarView;
    Button calcular, guardar;
    boolean isSelectingStartDate = true;
    long selectedStartDate = -1;
    long selectedEndDate = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_plan, container, false);

        spinner = view.findViewById(R.id.nivelExperiencia);
        spinnerAct = view.findViewById(R.id.actividadSpn);
        calendarView = view.findViewById(R.id.calendarView);
        tvDateInicio = view.findViewById(R.id.tvDateInicio);
        tvDateFin = view.findViewById(R.id.tvDateFin);
        objective = view.findViewById(R.id.objetivo);
        tvSelectedDates = view.findViewById(R.id.tvSelectedDates);
        calcular = view.findViewById(R.id.button);
        guardar = view.findViewById(R.id.button2);
        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generarPlanEntrenamiento();
            }
        });

        guardar.setOnClickListener(v -> sendDataWithVolley());

        long currentTimeMillis = System.currentTimeMillis();
        calendarView.setMinDate(currentTimeMillis);

        String[] opciones = {"Principiante", "Medio", "Avanzado"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                opciones
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nivelExperienciaSelect = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                nivelExperienciaSelect = "Sin nivel de experiencia seleccionada";
            }
        });


        String[] opcionesAct = {"Carrera", "Ciclismo", "Natación"};
        ArrayAdapter<String> adapterAct = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                opcionesAct
        );
        adapterAct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAct.setAdapter(adapterAct);

        spinnerAct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nivelExperienciaSelect = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                nivelExperienciaSelect = "Sin nivel de experiencia seleccionada";
            }
        });

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            long selectedDate = calendar.getTimeInMillis();

            if (isSelectingStartDate) {
                selectedStartDate = selectedDate;
                tvDateInicio.setText(formatDate(selectedStartDate));
                isSelectingStartDate = false;
                tvSelectedDates.setText("Selecciona la fecha de fin");
            } else {
                selectedEndDate = selectedDate;

                // Calcular la diferencia en días
                long diffInMillis = selectedEndDate - selectedStartDate;
                long diffInDays = diffInMillis / (1000 * 60 * 60 * 24); // Convertir de milisegundos a días

                if (selectedEndDate < selectedStartDate) {
                    tvSelectedDates.setText("La fecha de fin no puede ser anterior a la fecha de inicio");
                } else if (diffInDays < 15) {
                    tvSelectedDates.setText("El rango de fechas debe ser de al menos 15 días");
                } else {
                    tvDateFin.setText(formatDate(selectedEndDate));
                    tvSelectedDates.setText("Fechas seleccionadas correctamente");
                }
                isSelectingStartDate = true; // Cambiar a seleccionar fecha de inicio
            }
        });

        return view;
    }

    private void generarPlanEntrenamiento() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaInicio = sdf.parse(tvDateInicio.getText().toString());
            Date fechaFin = sdf.parse(tvDateFin.getText().toString());
            double objetivoKm = Double.valueOf(objective.getText().toString().trim());
            String nivel = spinner.getSelectedItem().toString();

            List<String> planEntrenamiento = TrainingPlan.generarPlanEntrenamiento(objetivoKm, fechaInicio, fechaFin, nivel);
            StringBuilder planStringBuilder = new StringBuilder();

            for (String dia : planEntrenamiento) {
                planStringBuilder.append(dia).append("\n");
            }

            // Crear y mostrar el DialogFragment
            TrainingPlanDialogFragment dialogFragment = TrainingPlanDialogFragment.newInstance(planStringBuilder.toString());
            dialogFragment.show(getChildFragmentManager(), "TrainingPlanDialog");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Selecciona la fecha de inicio y la fecha de fin", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatDate(long dateInMillis) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date(dateInMillis));
    }

    private void sendDataWithVolley() {
        String url = "https://trackline.pythonanywhere.com/upload_plan";

        JSONObject jsonObject = new JSONObject();
        try {
            Integer idActivity;
            if (spinnerAct.getSelectedItem().toString().equals("Ciclismo")) {
                idActivity = 1;
            } else if (spinnerAct.getSelectedItem().toString().equals("Carrera")) {
                idActivity = 2;
            } else {
                idActivity = 3;
            }
            jsonObject.put("idActivity", idActivity);
            jsonObject.put("experienceLevel", spinner.getSelectedItem().toString());
            jsonObject.put("startDate", tvDateInicio.getText().toString().trim());
            jsonObject.put("finishDate", tvDateFin.getText().toString().trim());
            jsonObject.put("objective", objective.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    Toast.makeText(getContext(), "Plan registrado exitosamente", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(getContext(), "Error al subir tu plan de entrenamiento " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );
        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
    }
}
