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
import com.example.mrhardwareapp.fragments.AdminCommentFragment;
import com.example.mrhardwareapp.fragments.AdminReportesFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogBanearComentario extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(requireActivity());
        dialogo.setTitle("Banearás un Comentario")
                .setMessage("¿Estás seguro?")
                .setPositiveButton("No", (dialog, which) -> {
                })
                .setNegativeButton("Sí", (dialog, which) -> {
                    Bundle args = getArguments();
                    assert args != null;
                    int id = args.getInt("idComentario");
                    int destino = args.getInt("destino");
                    int position = args.getInt("position");
                    // String nombre = args.getString("nombre");
                    AdministrarActivity adminActivity = (AdministrarActivity) getHost();
                    Call<Boolean> llamadaApi = ApiService.getApi().banearComentario(id);
                    llamadaApi.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                if (response.body()) {
                                    assert adminActivity != null;
                                    switch (destino) {
                                        case 1:
                                            AdminCommentFragment commentFragment = (AdminCommentFragment) adminActivity.obtenerFragments().get(1);
                                            RVAdminCommentAdapter adapterComment = commentFragment.update();
                                            for (int i = 0; i < RVAdminCommentAdapter.listaOriginal.size(); i++) {
                                                if (RVAdminCommentAdapter.listaOriginal.get(i).getIdComentario() == id) {
                                                    adapterComment.update(position, i);
                                                    commentFragment.mensajeVacio();
                                                    break;
                                                }
                                            }
                                            break;
                                        case 2:
                                            AdminReportesFragment reportFragment = (AdminReportesFragment) adminActivity.obtenerFragments().get(2);
                                            RVAdminReportAdapter adapterReport = reportFragment.update();
                                            for (int i = 0; i < RVAdminReportAdapter.listaOriginal.size(); i++) {
                                                if (RVAdminReportAdapter.listaOriginal.get(i).getId_Comentario().getIdComentario() == id) {
                                                    adapterReport.update(position, i);
                                                    reportFragment.mensajeVacio();
                                                    break;

                                                }
                                            }
                                            break;
                                    }
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

