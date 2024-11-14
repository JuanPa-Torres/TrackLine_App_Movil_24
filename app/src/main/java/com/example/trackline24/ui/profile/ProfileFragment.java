package com.example.trackline24.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.trackline24.LoginActivity;
import com.example.trackline24.R;
import com.example.trackline24.Sesion;
import com.example.trackline24.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private Button logoutButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProfile;
        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        logoutButton = binding.btnLogOut;
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sesion sesion = new Sesion(getContext());
                sesion.clearSession();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
