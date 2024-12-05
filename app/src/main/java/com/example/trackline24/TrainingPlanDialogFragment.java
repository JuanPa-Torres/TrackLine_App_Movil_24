package com.example.trackline24;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TrainingPlanDialogFragment extends DialogFragment {
    private static final String ARG_PLAN = "plan";
    Button cerrar;

    public static TrainingPlanDialogFragment newInstance(String plan) {
        TrainingPlanDialogFragment fragment = new TrainingPlanDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAN, plan);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_plan_dialog, container, false);
        TextView tvPlan = view.findViewById(R.id.tvPlan);
        cerrar = view.findViewById(R.id.btnClose);

        cerrar.setOnClickListener(v -> dismiss());

        if (getArguments() != null) {
            String plan = getArguments().getString(ARG_PLAN);
            tvPlan.setText(plan);
        }

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}