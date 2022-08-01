package com.example.mrhardwareapp.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.mrhardwareapp.MainActivity;
import com.example.mrhardwareapp.PerfilActivity;
import com.example.mrhardwareapp.R;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.fragments.SetupsFragment;
import com.example.mrhardwareapp.models.Setup;
import com.example.mrhardwareapp.models.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogAgregarSetup extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        Usuario user = MainActivity.user;
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        PerfilActivity perfilActivity = (PerfilActivity) getHost();
        assert perfilActivity != null;
        SetupsFragment setupsFragment = (SetupsFragment) perfilActivity.obtenerFragments().get(1);
        View design = inflater.inflate(R.layout.dialog_design_add_setup, null);

        final EditText txtNombreSetup = design.findViewById(R.id.txtNombreSetup);
        builder.setView(design)
                .setPositiveButton("Añadir", (dialog, which) -> {
                    if (Setup.vacios(txtNombreSetup.getText().toString()).equals("")){
                        Toast.makeText(perfilActivity, "Nombre no válido", Toast.LENGTH_SHORT).show();
                    }else{
                        Call<Setup> llamadaGetSetup = ApiService.getApi().getSetupByName(txtNombreSetup.getText().toString());
                        llamadaGetSetup.enqueue(new Callback<Setup>() {
                            @Override
                            public void onResponse(@NonNull Call<Setup> call, @NonNull Response<Setup> response) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null) {
                                        if (response.body().getNombre().equals(txtNombreSetup.getText().toString())) {
                                            Toast.makeText(perfilActivity, "El nombre del setup ya existe", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Call<Boolean> llamadaAddSetup = ApiService.getApi().insertarSetup(user.getIdUsuario(), txtNombreSetup.getText().toString());
                                        llamadaAddSetup.enqueue(new Callback<Boolean>() {
                                            @Override
                                            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                                                if (response.isSuccessful()) {
                                                    assert response.body() != null;
                                                    if (response.body()){
                                                        setupsFragment.llenarSpinner(-1);
                                                    }else{
                                                        Toast.makeText(perfilActivity, "Nombre no válido", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                                                Log.e("Retrofit Error 2!!", t.getMessage());
                                            }
                                        });
                                    }
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<Setup> call, @NonNull Throwable t) {
                                Log.e("Retrofit Error 1!!", t.getMessage());
                            }
                        });


                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {

                });
        return builder.create();
    }

}
