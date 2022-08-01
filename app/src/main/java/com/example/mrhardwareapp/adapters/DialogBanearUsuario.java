package com.example.mrhardwareapp.adapters;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.mrhardwareapp.AdministrarActivity;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.fragments.AdminUsuariosFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogBanearUsuario extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(requireActivity());
        dialogo.setTitle("Banearás a un usuario")
                .setMessage("¿Estás seguro?")
                .setPositiveButton("No", (dialog, which) -> {
                })
                .setNegativeButton("Sí", (dialog, which) -> {
                    Bundle args = getArguments();
                    assert args != null;
                    int id = args.getInt("idUsuario");
                    int position = args.getInt("position");
                    AdministrarActivity adminActivity = (AdministrarActivity) getHost();
                    // String nombre = args.getString("nombre");
                    Call<Boolean> llamadaUsuarioApi = ApiService.getApi().banear(id);
                    llamadaUsuarioApi.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                if (response.body()) {
                                    assert adminActivity != null;
                                    AdminUsuariosFragment userFragment = (AdminUsuariosFragment) adminActivity.obtenerFragments().get(0);
                                    RVAdminUserAdapter adapter = userFragment.update();

                                    for (int i = 0; i < RVAdminUserAdapter.listaOriginal.size(); i++) {
                                        if (RVAdminUserAdapter.listaOriginal.get(i).getIdUsuario() == id) {
                                            adapter.update(position, i);
                                            userFragment.mensajeVacio();
                                            break;
                                        }
                                    }
                                    userFragment.mensajeVacio();
                                } else {
                                    Toast.makeText(requireActivity().getApplicationContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                            Log.e("Retrofit Error!!", t.getMessage());
                        }
                    });
                });

        return dialogo.create();
    }
}
