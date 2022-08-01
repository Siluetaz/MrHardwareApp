package com.example.mrhardwareapp.adapters;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.mrhardwareapp.PerfilActivity;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.fragments.ComentariosPerfilFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogEliminarComentarioPerfil extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(requireActivity());
        dialogo.setTitle("Eliminarás tu Comentario")
                .setMessage("¿Estás seguro?")
                .setPositiveButton("No", (dialog, which) -> {
                })
                .setNegativeButton("Sí", (dialog, which) -> {
                    Bundle args = getArguments();
                    assert args != null;
                    int id = args.getInt("idComentario");
                    int position = args.getInt("position");
                    // String nombre = args.getString("nombre");
                    PerfilActivity perfilActivity = (PerfilActivity) getHost();
                    Call<Boolean> llamadaApi = ApiService.getApi().banearComentario(id);
                    llamadaApi.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                if (response.body()) {
                                    assert perfilActivity != null;
                                    ComentariosPerfilFragment commentFragment = (ComentariosPerfilFragment) perfilActivity.obtenerFragments().get(0);
                                    RVPerfilCommentAdapter adapter = commentFragment.recibirAdapter();
                                    adapter.update(position);
                                    commentFragment.mensajeVacio();
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

