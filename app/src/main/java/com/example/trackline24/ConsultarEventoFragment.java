package com.example.trackline24;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class ConsultarEventoFragment extends Fragment {

    public ConsultarEventoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_consultar_evento, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("name");
            String nameOrganizer = bundle.getString("nameOrganizer");
            String imageUrl = bundle.getString("imageUrl");
            String award = bundle.getString("award");
            String dateStartRegistry = bundle.getString("dateStartRegistry");
            String eventDate = bundle.getString("eventDate");
            String facebookLink = bundle.getString("facebookLink");
            String idActivity = bundle.getString("idActivity");
            String idEvent = bundle.getString("idEvent");
            String latitudeFinish = bundle.getString("latitudeFinish");
            String longitudeFinish = bundle.getString("longitudeFinish");
            String latitudeStart = bundle.getString("latitudeStart");
            String longitudeStart = bundle.getString("longitudeStart");
            String objective = bundle.getString("objective");
            String type = bundle.getString("type");
            String whatsappLink = bundle.getString("whatsappLink");

            Button btnConsultarUbicación = rootView.findViewById(R.id.btnConsultarUbicación);
            TextView tvName = rootView.findViewById(R.id.tvEventName);
            TextView tvOrganizer = rootView.findViewById(R.id.tvEventTitle);
            ImageView ivEventImage = rootView.findViewById(R.id.imageView);
            TextView tvActivity = rootView.findViewById(R.id.tvEventActivity);
            TextView tvAward = rootView.findViewById(R.id.premio);
            TextView tvDateStartRegistry = rootView.findViewById(R.id.convocatoria);
            TextView tvEventDate = rootView.findViewById(R.id.fechaEvento);
            Button btnFacebookLink = rootView.findViewById(R.id.btnFacebok);
            TextView tvObjective = rootView.findViewById(R.id.objetivoEvento);
            TextView tvType = rootView.findViewById(R.id.tvEventType);
            Button tvWhatsappLink = rootView.findViewById(R.id.btnWhatsapp);

            tvAward.setText(award);
            tvDateStartRegistry.setText(dateStartRegistry);
            tvEventDate.setText(eventDate);
            tvObjective.setText(objective);
            tvType.setText(type);
            tvActivity.setText(idActivity);
            tvName.setText(name);
            tvOrganizer.setText(nameOrganizer);
            btnConsultarUbicación.setOnClickListener(v -> {
                Bundle bndl = new Bundle();
                bndl.putString("latitudeStart", latitudeStart);
                bndl.putString("longitudeStart", longitudeStart);
                bndl.putString("latitudeFinish", latitudeFinish);
                bndl.putString("longitudeFinish", longitudeFinish);

                replaceFragment(new UbicacionEventoFragment(), bndl);
            });

            Picasso.get().load(imageUrl).error(R.drawable.agenda).into(ivEventImage);
        }
        return rootView;
    }

    private void replaceFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}