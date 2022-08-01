package com.example.mrhardwareapp.adapters;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.mrhardwareapp.MainActivity;

public class DialogSalir extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(requireActivity());

        dialogo.setMessage("¿Estás seguro?")
                .setTitle("Vas a cerrar tu sesión")
                .setPositiveButton("No", (dialogInterface, i) -> {
                })
                .setNegativeButton("Sí", (dialog, which) -> {
                    MainActivity.user = null;
                    Intent i = new Intent(getContext(), MainActivity.class);
                    startActivity(i);
                    requireActivity().finish();
                });

        return dialogo.create();
    }
}
