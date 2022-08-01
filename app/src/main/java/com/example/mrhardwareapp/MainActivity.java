package com.example.mrhardwareapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.models.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button btnIniciarSesion;
    TextView tvRegistrar;
    EditText txtUser;
    EditText txtPass;
    public static Usuario user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        txtUser = findViewById(R.id.txtUser);
        txtPass = findViewById(R.id.txtPass);
        tvRegistrar = findViewById(R.id.tvRegistrar);
        String mensajeRegistro = getIntent().getStringExtra("mensajeRegistro");
        if (mensajeRegistro != null && mensajeRegistro.equals("Confirmacion")){
            Toast.makeText(this, "Autentifica tu cuenta antes de iniciar sesión", Toast.LENGTH_LONG).show();
        }
//        Al precionar el boton si se encuentran vacios los campos mostrara un toast de que debe rellenar los campos para ingresar
//        encaso de ingresar los datos correctamente y si se ecuentre registrado, abra iniciado su sesion
        btnIniciarSesion.setOnClickListener(v -> {
            if (txtUser.getText().toString().equals("") || txtPass.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Debes rellenar los campos para ingresar", Toast.LENGTH_SHORT).show();
            } else {
                Call<Usuario> llamadaUsuarioApi = ApiService.getApi().autenticar(txtUser.getText().toString(), txtPass.getText().toString());
                llamadaUsuarioApi.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                        if (response.isSuccessful()) {
                            user = response.body();
                            if (user != null) {
                                if (txtUser.getText().toString().equals(user.getUsername())&&txtPass.getText().toString().equals(user.getPassword())) {
                                    switch (user.getId_TipoUsuario()) {
                                        case 1:
                                            Intent i = new Intent(getApplicationContext(), AdministrarActivity.class);
                                            startActivity(i);
                                            finish();
                                            break;
                                        case 2:
                                            Intent i1 = new Intent(getApplicationContext(), InicioClienteActivity.class);
                                            assert user != null;
                                            if (user.getConfirmacion() == 1) {
                                                startActivity(i1);
                                                finish();

                                            } else {
                                                Toast.makeText(MainActivity.this, "Tu cuenta aún no está autentificada", Toast.LENGTH_SHORT).show();
                                            }
                                            break;

                                    }
                                }else{
                                    Toast.makeText(MainActivity.this, "Usuario y/o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {

                        Toast.makeText(getApplicationContext(), "Error al comunicar con el servidor", Toast.LENGTH_SHORT).show();
                        Log.e("Error Retrofit", t.getMessage());
                    }
                });


            }
        });
        tvRegistrar.setOnClickListener(v -> {
            Intent i = new Intent(this, RegistrarseActivity.class);
            startActivity(i);
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, InicioClienteActivity.class);
        startActivity(i);
        finish();
    }
}