package com.example.trackline24.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.trackline24.databinding.FragmentRouteBinding;
import com.example.trackline24.ui.route.RouteViewModel;

public class ProfileFragment extends Fragment {

    private FragmentRouteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel ProfileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentRouteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textRoute;
        ProfileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}