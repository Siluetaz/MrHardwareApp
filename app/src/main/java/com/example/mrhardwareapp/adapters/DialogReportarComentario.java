package com.example.mrhardwareapp.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.mrhardwareapp.MainActivity;
import com.example.mrhardwareapp.ProductoActivity;
import com.example.mrhardwareapp.R;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.models.Comentario;
import com.example.mrhardwareapp.models.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogReportarComentario extends DialogFragment {
    int idTipoReporte = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        Usuario user = MainActivity.user;
        Bundle b = getArguments();
        assert b != null;
        Comentario comentario = (Comentario) b.getSerializable("comentario");
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        ProductoActivity productoActivity = (ProductoActivity) getHost();
        assert productoActivity != null;
        View design = inflater.inflate(R.layout.dialog_design_report_comment, null);

        final RadioGroup tipoReporte = design.findViewById(R.id.tipoReporte);
        builder.setView(design)
                .setPositiveButton("Reportar", (dialog, which) -> {
                    onRadioButtonClicked(tipoReporte);
                    if (idTipoReporte == 0) {
                        Toast.makeText(productoActivity, "Motivo no sleccionado", Toast.LENGTH_SHORT).show();
                    }else {
                        Call<Boolean> llamadaReportar = ApiService.getApi().insertarReporte(user.getIdUsuario(), idTipoReporte, comentario.getIdComentario());
                        llamadaReportar.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                                if (response.isSuccessful()) {
                                    assert response.body() != null;
                                    if (response.body()) {
                                        Toast.makeText(productoActivity, "Reporte enviado", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(productoActivity, "Error al enviar reporte", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                                Log.e("Retrofit Error 2!!", t.getMessage());
                            }
                        });

                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {

                });
        return builder.create();
    }

    public void onRadioButtonClicked(RadioGroup tipoProd) {

        switch (tipoProd.getCheckedRadioButtonId()) {
            case R.id.inapropiado:
                idTipoReporte = 1;
                System.out.println("A" + idTipoReporte);
                break;
            case R.id.ofensivo:
                idTipoReporte = 2;
                System.out.println("B" + idTipoReporte);
                break;
            case R.id.spam:
                idTipoReporte = 3;
                System.out.println("C" + idTipoReporte);
                break;
            case R.id.otro:
                idTipoReporte = 4;
                System.out.println("D" + idTipoReporte);
                break;
        }
    }
}
