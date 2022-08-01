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
import com.example.mrhardwareapp.ProductoActivity;
import com.example.mrhardwareapp.R;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.models.Producto;
import com.example.mrhardwareapp.models.Setup;
import com.example.mrhardwareapp.models.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogAgregarSetupFromProd extends DialogFragment {
    ProductoActivity productoActivity;
    Producto prod;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        Bundle b = getArguments();
        assert b != null;
        prod = (Producto) b.getSerializable("prod");
        productoActivity = (ProductoActivity) getHost();
        assert productoActivity != null;
        Usuario user = MainActivity.user;

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View design = inflater.inflate(R.layout.dialog_design_add_setup, null);

        final EditText txtNombreSetup = design.findViewById(R.id.txtNombreSetup);


        builder.setView(design)
                .setPositiveButton("Añadir", (dialog, which) -> {
                    if (Setup.vacios(txtNombreSetup.getText().toString()).equals("")) {
                        Toast.makeText(productoActivity, "Nombre no válido", Toast.LENGTH_SHORT).show();
                        retornarA(0);
                    } else {
                        Call<Setup> llamadaGetSetup = ApiService.getApi().getSetupByName(txtNombreSetup.getText().toString());
                        llamadaGetSetup.enqueue(new Callback<Setup>() {
                            @Override
                            public void onResponse(@NonNull Call<Setup> call, @NonNull Response<Setup> response) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null) {
                                        if (response.body().getNombre().equals(txtNombreSetup.getText().toString())) {
                                            Toast.makeText(productoActivity, "El nombre del setup ya existe", Toast.LENGTH_SHORT).show();
                                            retornarA(0);
                                        }
                                    } else {
                                        Call<Boolean> llamadaAddSetup = ApiService.getApi().insertarSetup(user.getIdUsuario(), txtNombreSetup.getText().toString());
                                        llamadaAddSetup.enqueue(new Callback<Boolean>() {
                                            @Override
                                            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                                                if (response.isSuccessful()) {
                                                    assert response.body() != null;
                                                    if (response.body()) {
                                                        retornarA(-1);
                                                    } else {
                                                        Toast.makeText(productoActivity, "Nombre no válido", Toast.LENGTH_SHORT).show();
                                                        retornarA(0);
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
                    retornarA(0);
                });
        return builder.create();
    }

    public void retornarA(int pos) {
        Bundle bundle = new Bundle();
        DialogAgregarProductoASetup dialogo = new DialogAgregarProductoASetup();
        bundle.putSerializable("prod", prod);
        bundle.putInt("pos", pos);
        dialogo.setArguments(bundle);
        dialogo.show(productoActivity.getSupportFragmentManager(), "AddProdToSetup");
    }
}
