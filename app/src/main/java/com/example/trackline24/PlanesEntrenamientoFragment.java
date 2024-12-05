package com.example.trackline24;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlanesEntrenamientoFragment extends Fragment {
    private ListView listViewPlans;
    private List<TrainingPlanObj> trainingPlans;
    private TrainingPlanAdapter adapter;
    Button addPlan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planes_entrenamiento, container, false);
        addPlan = view.findViewById(R.id.addPlan);
        addPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPlanFragment addPlanFragment = new AddPlanFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, addPlanFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        listViewPlans = view.findViewById(R.id.listViewPlans);
        trainingPlans = new ArrayList<>();
        adapter = new TrainingPlanAdapter(getContext(), trainingPlans);
        listViewPlans.setAdapter(adapter);
        listViewPlans.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TrainingPlanObj selectedPlan = adapter.getItem(i);
                SimpleDateFormat desiredFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date dateInicio, dateFin;
                try {
                    dateInicio = desiredFormat.parse(convertDateFormat(selectedPlan.getStartDate()));
                    dateFin = desiredFormat.parse(convertDateFormat(selectedPlan.getFinishDate()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                generarPlanEntrenamiento(dateInicio, dateFin, Double.valueOf(selectedPlan.getObjective()),selectedPlan.getExperienceLevel());
            }
        });
        fetchTrainingPlans();

        return view;
    }

    private void fetchTrainingPlans() {
        String url = "https://trackline.pythonanywhere.com/get_plans";
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray routes = response.getJSONArray("routes");
                            for (int i = 0; i < routes.length(); i++) {
                                JSONObject obj = routes.getJSONObject(i);
                                TrainingPlanObj plan = new TrainingPlanObj(
                                        obj.getString("experienceLevel"),
                                        obj.getString("finishDate"),
                                        obj.getInt("idActivity"),
                                        obj.optInt("idTrainingPlan", 0),
                                        obj.getString("objective"),
                                        obj.getString("startDate")
                                );
                                trainingPlans.add(plan);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void generarPlanEntrenamiento(Date inicio, Date fin, double objetivo, String nivelExp) {
        try {
            Date fechaInicio = inicio;
            Date fechaFin = fin;
            double objetivoKm = objetivo;
            String nivel = nivelExp;

            List<String> planEntrenamiento = TrainingPlan.generarPlanEntrenamiento(objetivoKm, fechaInicio, fechaFin, nivel);
            StringBuilder planStringBuilder = new StringBuilder();

            for (String dia : planEntrenamiento) {
                planStringBuilder.append(dia).append("\n");
            }

            TrainingPlanDialogFragment dialogFragment = TrainingPlanDialogFragment.newInstance(planStringBuilder.toString());
            dialogFragment.show(getChildFragmentManager(), "TrainingPlanDialog");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String convertDateFormat(String dateString) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        SimpleDateFormat desiredFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = originalFormat.parse(dateString);
            return desiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
