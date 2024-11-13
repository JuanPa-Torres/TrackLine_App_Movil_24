package com.example.trackline24.ui.route;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.trackline24.databinding.FragmentRouteBinding;

public class RouteFragment extends Fragment {

    private FragmentRouteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RouteViewModel routeViewModel =
                new ViewModelProvider(this).get(RouteViewModel.class);

        binding = FragmentRouteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textRoute;
        routeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}