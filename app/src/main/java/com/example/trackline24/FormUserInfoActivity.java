package com.example.trackline24;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class FormUserInfoActivity extends AppCompatActivity {

    private EditText nombre, apPaterno, apMaterno, fechaNacimiento;
    private Button btnInfoUsr;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_user_info);

        nombre = findViewById(R.id.nombre);
        apPaterno = findViewById(R.id.apellidoPaterno);
        apMaterno = findViewById(R.id.apellidoMaterno);
        fechaNacimiento = findViewById(R.id.fechaNacimiento);
        btnInfoUsr =  findViewById(R.id.btnInfoUsuario);
        requestQueue = Volley.newRequestQueue(this);

        btnInfoUsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarInfo();
            }
        });
    }

    public void registrarInfo(){
        String Nombre = nombre.getText().toString().trim();
        String ApPaterno = apPaterno.getText().toString().trim();
        String ApMaterno = apMaterno.getText().toString().trim();
        String FechaNacimiento = fechaNacimiento.getText().toString().trim();

        if (Nombre.isEmpty() || ApPaterno.isEmpty()|| ApMaterno.isEmpty() || FechaNacimiento.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://trackline.pythonanywhere.com/registrarInfo";

        JSONObject jsonBody = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
            String idUser = sharedPreferences.getString("user_id", "Sin ID");
            jsonBody.put("birthDate", FechaNacimiento);
            jsonBody.put("name", Nombre);
            jsonBody.put("lastName1", ApPaterno);
            jsonBody.put("lastName2", ApMaterno );
            jsonBody.put("profileImage", "Image.jpg" );
            jsonBody.put("idUser", idUser );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            finish();  // Cierra la actividad de registro de información
                            Intent intent = new Intent(FormUserInfoActivity.this, PerfilFisicoActivity.class);
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