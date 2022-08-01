package com.example.mrhardwareapp.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.mrhardwareapp.MainActivity;
import com.example.mrhardwareapp.PerfilActivity;
import com.example.mrhardwareapp.R;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.fragments.SetupsFragment;
import com.example.mrhardwareapp.models.Producto;
import com.example.mrhardwareapp.models.Setup;
import com.example.mrhardwareapp.models.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogQuitarProdSetup extends DialogFragment {

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        Usuario user = MainActivity.user;
        Bundle args = getArguments();
        assert args != null;
        Producto prod = (Producto) args.getSerializable("prod");
        Setup setup = (Setup) args.getSerializable("setup");
        int pos =args.getInt("pos");
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        PerfilActivity perfilActivity = (PerfilActivity) getHost();
        assert perfilActivity != null;
        SetupsFragment setupsFragment = (SetupsFragment) perfilActivity.obtenerFragments().get(1);
        View design = inflater.inflate(R.layout.dialog_design_remove_setup, null);
        final TextView titulo = design.findViewById(R.id.tvTitulo);
        titulo.setText("Eliminarás un producto del setup: " + "\"" + setup.getNombre() + "\"");
        builder.setView(design)
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    Call<Boolean> llamadaRemoveSetup = ApiService.getApi().quitarProdASetup(setup.getIdSetup(), prod.getIdProducto());
                    llamadaRemoveSetup.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                if (response.body()) {
                                    setupsFragment.llenarSpinner(pos);
                                    Toast.makeText(perfilActivity, "Producto quitado", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(perfilActivity, "El producto ya no existe", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                            Log.e("Retrofit Error 1!!", t.getMessage());
                        }

                    });
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {

                });
        return builder.create();
    }
}
