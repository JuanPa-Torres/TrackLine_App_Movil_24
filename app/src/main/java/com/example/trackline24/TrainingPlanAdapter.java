package com.example.trackline24;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TrainingPlanAdapter extends ArrayAdapter<TrainingPlanObj> {

    public TrainingPlanAdapter(@NonNull Context context, List<TrainingPlanObj> plans) {
        super(context, 0, plans);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TrainingPlanObj plan = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.plan_list_item, parent, false);
        }

        TextView tvObjective = convertView.findViewById(R.id.tvObjective);
        TextView tvDates = convertView.findViewById(R.id.tvDates);
        TextView tvExperience = convertView.findViewById(R.id.tvExperience);


        tvObjective.setText("Objectivo: " + plan.getObjective());
        tvDates.setText("Fecha de inicio: " + convertDateFormat(plan.getStartDate()) + " - Fecha de fin: " + convertDateFormat(plan.getFinishDate()));
        tvExperience.setText("Nivel de experiencia: " + plan.getExperienceLevel());

        return convertView;
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
