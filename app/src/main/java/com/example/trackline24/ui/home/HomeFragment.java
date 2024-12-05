package com.example.trackline24.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.trackline24.DesafiosFragment;
import com.example.trackline24.EventosFragment;
import com.example.trackline24.PlanesEntrenamientoFragment;
import com.example.trackline24.R;
import com.example.trackline24.databinding.FragmentHomeBinding;
import com.example.trackline24.ui.route.RouteFragment;

public class HomeFragment extends Fragment {
    Button btnEvento, btnPlan, btnDesafio;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btnEvento = root.findViewById(R.id.eventoCardButton);
        btnPlan = root.findViewById(R.id.planCardButton);
        btnEvento.setOnClickListener(v -> replaceFragment(new EventosFragment()));
        btnPlan.setOnClickListener(v -> replaceFragment(new RouteFragment()));
        return root;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        btnEvento.setEnabled(false);
        btnPlan.setEnabled(false);
        binding = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        btnEvento.setEnabled(false);
        btnPlan.setEnabled(false);
    }

}