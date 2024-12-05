package com.example.trackline24;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Random;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistroActivity extends AppCompatActivity {

    private EditText userName, email, pass;
    private Button registerButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        userName = findViewById(R.id.etUserName);
        email = findViewById(R.id.etEmail);
        pass = findViewById(R.id.etPassword);
        registerButton = findViewById(R.id.btnRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
    }

    private void registerUser() {

        Random random = new Random();
        String UserName = userName.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String Password = pass.getText().toString().trim();
        String idUsuario =  "" + random.nextInt(10000);

        if (UserName.isEmpty() || Email.isEmpty()|| Password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://trackline.pythonanywhere.com/registrar";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("idUsuario", idUsuario );
            jsonBody.put("userName", UserName);
            jsonBody.put("email", Email);
            jsonBody.put("password", Password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            finish();
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