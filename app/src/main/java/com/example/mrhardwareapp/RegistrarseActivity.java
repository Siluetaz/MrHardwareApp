package com.example.mrhardwareapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarseActivity extends AppCompatActivity {
    Button btnRegistrarse;
    EditText txtUser;
    EditText txtPass;
    EditText txtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        txtUser = findViewById(R.id.txtUser);
        txtPass = findViewById(R.id.txtPass);
        txtEmail = findViewById(R.id.txtEmail);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        //Este setOnClickListener realiza la acción del registro
        btnRegistrarse.setOnClickListener(view -> {
            //Validación para asegurarse de que los campos no estén vacíos
            if (txtUser.getText().toString().equals("") || txtPass.getText().toString().equals("") || txtEmail.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                Call<List<Usuario>> llamadaValidacion = ApiService.getApi().validacionRegistro(txtEmail.getText().toString(), txtUser.getText().toString());
                llamadaValidacion.enqueue(new Callback<List<Usuario>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Usuario>> call, @NonNull Response<List<Usuario>> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().size() == 0) {
                                //Llamada que inserta en la base de datos al cliente, y envia un correo al mismo para realizar la confirmación
                                Call<Boolean> llamadaUsuario = ApiService.getApi().registrarCliente(txtEmail.getText().toString(), txtUser.getText().toString(), txtPass.getText().toString());
                                llamadaUsuario.enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse
                                            (@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                                        if (response.isSuccessful()) {
                                            //redirigimos al inicio de sesión;
                                            Toast.makeText(RegistrarseActivity.this, "Espere...", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                            i.putExtra("mensajeRegistro", "Confirmacion");
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error al crear el usuario", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                                        Toast.makeText(getApplicationContext(), "Error al comunicar con el servidor", Toast.LENGTH_SHORT).show();
                                        Log.e("Error Retrofit", t.getMessage());
                                    }
                                });
                            } else {
                                Usuario usuarioExistente = response.body().get(0);
                                if (txtUser.getText().toString().equals(usuarioExistente.getUsername())) {
                                    Toast.makeText(RegistrarseActivity.this, "El Nombre de Usuario ya existe", Toast.LENGTH_SHORT).show();
                                } else if (txtEmail.getText().toString().equals(usuarioExistente.getEmail())) {
                                    Toast.makeText(RegistrarseActivity.this, "El Email ya existe", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Usuario>> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error al comunicar con el servidor", Toast.LENGTH_SHORT).show();
                        Log.e("Error Retrofit", t.getMessage());
                    }
                });

            }
        });
    }

}