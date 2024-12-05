package com.example.trackline24;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddEventFragment extends Fragment implements OnMapReadyCallback {
    private TextView tvDate, tvDate2;
    private ImageView ivDatePicker, ivDatePicker2;
    private Marker startMarker, endMarker;
    private GoogleMap mMap;
    private Button btnLimpiarMarcas, btnEnviar;
    private ImageView img;
    private String imageBase64;
    private EditText etAward, etEventName, etOrganizer, etType, etFacebookLink, etWhatsappLink, etActividad;
    LatLng startMark, endMark;
    double distance;
    private CheckBox cbMen;
    private CheckBox cbWomen;
    private CheckBox cbChildren;
    ArrayList<String> selectedValues;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_event, container, false);

        etAward = rootView.findViewById(R.id.etAward);
        etEventName = rootView.findViewById(R.id.etEventName);
        etOrganizer = rootView.findViewById(R.id.etOrganizer);
        etType = rootView.findViewById(R.id.etType);
        etFacebookLink = rootView.findViewById(R.id.etFacebookLink);
        etWhatsappLink = rootView.findViewById(R.id.etWhatsappLink);
        etActividad = rootView.findViewById(R.id.etActividad);

        tvDate = rootView.findViewById(R.id.tvDate);
        tvDate2 = rootView.findViewById(R.id.tvDate2);
        ivDatePicker = rootView.findViewById(R.id.ivDatePicker);
        ivDatePicker.setOnClickListener(v -> openDatePicker(tvDate));
        ivDatePicker2 = rootView.findViewById(R.id.ivDatePicker2);
        ivDatePicker2.setOnClickListener(v -> openDatePicker(tvDate2));
        btnLimpiarMarcas = rootView.findViewById(R.id.btnLimpiarMarcas);
        btnLimpiarMarcas.setOnClickListener(v -> limpiarMarcas());
        img = rootView.findViewById(R.id.image);
        cbMen = rootView.findViewById(R.id.cbMen);
        cbWomen = rootView.findViewById(R.id.cbWomen);
        cbChildren = rootView.findViewById(R.id.cbChildren);

        img.setOnClickListener(v -> seleccionarImagen(1));

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnEnviar = rootView.findViewById(R.id.btnSubmit);
        btnEnviar.setOnClickListener(v -> sendDataWithVolley(imageBase64));

        return rootView;
    }

    private void openDatePicker(TextView tv) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    tv.setText(date);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng initialLocation = new LatLng(19.432608, -99.133209); // Ciudad de México
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10));

        mMap.setOnMapClickListener(latLng -> handleMapClick(latLng));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                // Evento al iniciar el arrastre
                System.out.println("Iniciando arrastre del marcador...");
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // Evento mientras se arrastra el marcador (opcional)
                System.out.println("Arrastrando marcador a: " + marker.getPosition());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // Evento al finalizar el arrastre
                System.out.println("Marcador movido a: " + marker.getPosition());
            }
        });
    }

    private void handleMapClick(LatLng latLng) {
        if (startMarker == null) {
            // Crear el marcador de inicio si no existe
            startMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Inicio del recorrido")
                    .draggable(true));
        } else if (endMarker == null) {
            endMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Fin del recorrido")
                    .draggable(true));
        } else {
            Toast.makeText(getContext(), "Solo puedes agregar la marca de inicio y de fin", Toast.LENGTH_LONG).show();
        }
        if (startMarker != null && endMarker != null) {
             startMark = startMarker.getPosition();
             endMark = endMarker.getPosition();

            distance = calculateDistance(startMark, endMark);
            Toast.makeText(getContext(), "Distancia calculada: " + String.format("%.2f", distance) + " Km", Toast.LENGTH_LONG).show();
        }
    }

    public double calculateDistance(LatLng start, LatLng end) {
        final int R = 6371; // Radio de la Tierra en kilómetros

        double latDistance = Math.toRadians(end.latitude - start.latitude);
        double lonDistance = Math.toRadians(end.longitude - start.longitude);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(start.latitude)) * Math.cos(Math.toRadians(end.latitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    private void limpiarMarcas() {
        if (startMarker != null) {
            startMarker.remove(); // Eliminar el marcador de inicio
            startMarker = null; // Restablecer la referencia
        }
        if (endMarker != null) {
            endMarker.remove(); // Eliminar el marcador de fin
            endMarker = null; // Restablecer la referencia
        }
        Toast.makeText(getContext(), "Marcadores limpiados", Toast.LENGTH_SHORT).show();
    }

    private void seleccionarImagen(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
                img.setImageURI(uri);
                imageBase64 = encodeImageToBase64(uri);
                Log.d("ImagenBase64",imageBase64);
        }
    }

    private String encodeImageToBase64(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream); // Compresión en formato JPEG
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendDataWithVolley(String imageBase64) {
        String url = "https://trackline.pythonanywhere.com/upload_event";
        validateCheckBoxes();

        JSONObject jsonObject = new JSONObject();
        try {
            String uniqueID = UUID.randomUUID().toString();
            jsonObject.put("award", etAward.getText().toString().trim());
            jsonObject.put("dateStartRegistry", tvDate.getText().toString().trim());
            jsonObject.put("eventDate", tvDate2.getText().toString().trim());
            jsonObject.put("facebookLink", etFacebookLink.getText().toString().trim());
            jsonObject.put("idActivity", etActividad.getText().toString().trim());
            jsonObject.put("idEvent", uniqueID);
            jsonObject.put("image", imageBase64);
            jsonObject.put("category", selectedValues);
            jsonObject.put("latitudeFinish", ""+endMark.latitude);
            jsonObject.put("latitudeStart", ""+startMark.latitude);
            jsonObject.put("longitudeFinish", ""+endMark.longitude);
            jsonObject.put("longitudeStart", ""+startMark.longitude);
            jsonObject.put("name", etEventName.getText().toString().trim());
            jsonObject.put("nameOrganizer", etOrganizer.getText().toString().trim());
            jsonObject.put("objective", etActividad.getText().toString().trim()+" - " + distance + "Km");
            Log.d("Distancia", etActividad.getText().toString().trim()+" - " + distance + "Km");
            jsonObject.put("type", "Trail");
            jsonObject.put("whatsappLink", etWhatsappLink.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    Toast.makeText(getContext(), "Evento subido exitosamente", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(getContext(), "Error al subir evento: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
        Log.d("ObjetoJSON", String.valueOf(jsonObject));
    }

    public void validateCheckBoxes() {
        selectedValues = new ArrayList<>();

        if (cbMen.isChecked()) {
            selectedValues.add(cbMen.getText().toString());
        }
        if (cbWomen.isChecked()) {
            selectedValues.add(cbWomen.getText().toString());
        }
        if (cbChildren.isChecked()) {
            selectedValues.add(cbChildren.getText().toString());
        }
    }

}