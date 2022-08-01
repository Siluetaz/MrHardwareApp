package com.example.mrhardwareapp.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.mrhardwareapp.PerfilActivity;
import com.example.mrhardwareapp.R;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.fragments.ComentariosPerfilFragment;
import com.example.mrhardwareapp.models.Comentario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogEditarComentario extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        Bundle args = getArguments();
        assert args != null;
        Comentario comentario = (Comentario) args.getSerializable("comentario");
        int position =  args.getInt("position");

        PerfilActivity perfilActivity = (PerfilActivity) getHost();
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View design = inflater.inflate(R.layout.dialog_design_edit_comment, null);

        final EditText txtComentarioEditable = design.findViewById(R.id.txtComentarioEditable);

        txtComentarioEditable.setText(comentario.getComentario());

        builder.setView(design)
                .setPositiveButton("Editar", (dialog, which) -> {

                        comentario.setComentario(txtComentarioEditable.getText().toString());
                        Call<Boolean> llamadaApi = ApiService.getApi().editarComentario(comentario.getIdComentario(), comentario.getComentario());
                        llamadaApi.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                                if (response.isSuccessful()) {
                                    assert perfilActivity != null;
                                    ComentariosPerfilFragment comentariosPerfilFragment = (ComentariosPerfilFragment) perfilActivity.obtenerFragments().get(0);
                                    RVPerfilCommentAdapter adapter = comentariosPerfilFragment.recibirAdapter();
                                    adapter.updateC(position);
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {

                            }
                        });

                })
                .setNegativeButton("Cancelar", (dialog, which) -> {

                });
        return builder.create();
    }
}
