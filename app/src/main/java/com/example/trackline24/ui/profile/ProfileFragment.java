package com.example.trackline24.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trackline24.LoginActivity;
import com.example.trackline24.R;
import com.example.trackline24.Sesion;
import com.example.trackline24.databinding.FragmentProfileBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private Button editButton;
    private TextView nombreUsuario, email, userName, actividadPreferida, altura, peso;
    String StrlastName1, StrlastName2;
    String dateBirth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        nombreUsuario = binding.nombreUsuario;
        email = binding.email;
        userName = binding.userName;
        actividadPreferida = binding.actividadPreferida;
        altura = binding.altura;
        peso = binding.peso;

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("user_id", "Sin ID");

        getUserInfo(idUser);

        editButton = binding.btnEdit;
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditModal(idUser);
            }
        });

        return root;
    }

    private void getUserInfo(String idUser) {
        String url = "https://trackline.pythonanywhere.com/infoUser/" + idUser;

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray infoUserArray = response.getJSONArray("infoUser");
                        if (infoUserArray.length() > 0) {
                            JSONObject infoUser = infoUserArray.getJSONObject(0);
                            nombreUsuario.setText(infoUser.getString("name") + " " +
                                    infoUser.getString("lastName1") + " " +
                                    infoUser.getString("lastName2"));
                            StrlastName1 = infoUser.getString("lastName1");
                            StrlastName2 = infoUser.getString("lastName2");
                            dateBirth = infoUser.getString("birthDate");
                        }

                        JSONArray userArray = response.getJSONArray("user");
                        if (userArray.length() > 0) {
                            JSONObject user = userArray.getJSONObject(0);
                            email.setText(user.getString("email"));
                            userName.setText(user.getString("userName"));
                        }

                        JSONArray physicalProfileArray = response.getJSONArray("physicalProfile");
                        if (physicalProfileArray.length() > 0) {
                            JSONObject physicalProfile = physicalProfileArray.getJSONObject(0);
                            actividadPreferida.setText(physicalProfile.getString("activity"));
                            altura.setText(String.valueOf(physicalProfile.getDouble("height")));
                            peso.setText(String.valueOf(physicalProfile.getInt("weight")));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
                });

        queue.add(jsonObjectRequest);
    }

    private void showEditModal(String idUser) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_user, null);
        builder.setView(dialogView);

        // Referencias a los campos
        EditText editName = dialogView.findViewById(R.id.editName);
        EditText editLastName1 = dialogView.findViewById(R.id.editLastName1);
        EditText editLastName2 = dialogView.findViewById(R.id.editLastName2);
        EditText editEmail = dialogView.findViewById(R.id.editEmail);
        EditText editBirthDate = dialogView.findViewById(R.id.editBirthDate);
        EditText editActivity = dialogView.findViewById(R.id.editActivity);
        EditText editHeight = dialogView.findViewById(R.id.editHeight);
        EditText editWeight = dialogView.findViewById(R.id.editWeight);
        EditText editUserName = dialogView.findViewById(R.id.editUserName);

        // Prellenar con los datos actuales del usuario
        editName.setText(nombreUsuario.getText().toString());
        editLastName1.setText(StrlastName1); // Agrega una referencia a lastName1 en tu layout o lógica.
        editLastName2.setText(StrlastName2); // Agrega una referencia a lastName2 en tu layout o lógica.
        editEmail.setText(email.getText().toString());
        editBirthDate.setText(dateBirth); // Agrega una referencia a birthDate en tu lógica.

// Prellenar con los datos actuales del physicalProfile
        editActivity.setText(actividadPreferida.getText().toString());
        editHeight.setText(altura.getText().toString());
        editWeight.setText(peso.getText().toString());

// Prellenar con los datos actuales del user
        editUserName.setText(userName.getText().toString());

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            try {
                JSONObject updatedData = new JSONObject();

                // Construir infoUser
                JSONArray infoUserArray = new JSONArray();
                JSONObject infoUser = new JSONObject();
                infoUser.put("idUser", idUser);
                infoUser.put("name", editName.getText().toString());
                infoUser.put("lastName1", editLastName1.getText().toString());
                infoUser.put("lastName2", editLastName2.getText().toString());
                infoUser.put("email", editEmail.getText().toString());
                infoUser.put("birthDate", editBirthDate.getText().toString());
                infoUser.put("profileImage", ""); // Si tienes un campo de imagen, agrégalo aquí.
                infoUserArray.put(infoUser);
                updatedData.put("infoUser", infoUserArray);

                // Construir physicalProfile
                JSONArray physicalProfileArray = new JSONArray();
                JSONObject physicalProfile = new JSONObject();
                physicalProfile.put("idUser", idUser);
                physicalProfile.put("activity", editActivity.getText().toString());
                physicalProfile.put("height", Double.parseDouble(editHeight.getText().toString()));
                physicalProfile.put("weight", Double.parseDouble(editWeight.getText().toString()));
                physicalProfileArray.put(physicalProfile);
                updatedData.put("physicalProfile", physicalProfileArray);

                // Construir user
                JSONArray userArray = new JSONArray();
                JSONObject user = new JSONObject();
                user.put("idUser", idUser);
                user.put("email", editEmail.getText().toString());
                user.put("isFirstLogin", false); // Cambia según tu lógica.
                user.put("userName", editUserName.getText().toString());
                userArray.put(user);
                updatedData.put("user", userArray);

                // Llamar al método para enviar los datos
                updateUserInfo(updatedData);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateUserInfo(JSONObject updatedData) {
        String url = "https://trackline.pythonanywhere.com/updateUser";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, updatedData,
                response -> {
                    Toast.makeText(getContext(), "Información actualizada con éxito", Toast.LENGTH_SHORT).show();
                    // Actualizar los datos en la pantalla
                    getUserInfo(updatedData.optString("idUser"));
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Error al actualizar la información", Toast.LENGTH_SHORT).show();
                });

        queue.add(jsonObjectRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
