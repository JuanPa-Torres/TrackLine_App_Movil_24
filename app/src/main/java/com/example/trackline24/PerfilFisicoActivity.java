package com.example.trackline24;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PerfilFisicoActivity extends AppCompatActivity {
    String seleccionado;
    EditText altura, peso;
    Button btnEnviar;
    RequestQueue requestQueue;
    String url = "https://trackline.pythonanywhere.com/registrarPerfilFisico";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil_fisico);

        altura = findViewById(R.id.altura);
        peso = findViewById(R.id.peso);
        btnEnviar = findViewById(R.id.btnEnviar);
        requestQueue = Volley.newRequestQueue(this);
        Spinner spinner = findViewById(R.id.spinnerSeleccion);

        String[] opciones = {"Carrera", "Natación", "Ciclismo", "Caminata", "Trail", "Ciclismo de ruta", "Ciclismo de montaña"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                opciones
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleccionado = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                seleccionado = "Sin actividad preferida seleccionada";
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarPerfilFisico();
            }
        });
    }

    public void enviarPerfilFisico () {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("user_id", "Sin ID");
        Float Altura = Float.parseFloat(altura.getText().toString().trim());
        Float Peso = Float.parseFloat(peso.getText().toString().trim());

        if (Altura.isNaN() || Peso.isNaN()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("altura", Altura);
            jsonBody.put("peso", Peso);
            jsonBody.put("actividad", seleccionado );
            jsonBody.put("idUser", idUser );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Toast.makeText(this, "Tu perfil físico se registró exitosamente", Toast.LENGTH_SHORT).show();
                            finish();  // Cierra la actividad de registro de información
                            Intent intent = new Intent(PerfilFisicoActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "Error en el registro: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error en la solicitud", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

}