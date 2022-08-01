package com.example.mrhardwareapp.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogAgregarProductoASetup extends DialogFragment {
    List<String> spinnerArray = new ArrayList<>();
    List<Setup> listaSetup = new ArrayList<>();
    Spinner spinnerSetupD;
    TextView tvRepetido;
    ImageView imgAddSetupProd;
    Setup setup;
    Producto prod;
    Usuario user;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
         user = MainActivity.user;
        Bundle args = getArguments();
        assert args != null;
        prod = (Producto) args.getSerializable("prod");
        int pos  =  args.getInt("pos");
        System.out.println(prod.getNombre());
        ProductoActivity productoActivity = (ProductoActivity) getHost();
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View design = inflater.inflate(R.layout.dialog_design_add_prod_setup, null);
        spinnerSetupD = design.findViewById(R.id.spinnerSetupD);
        imgAddSetupProd = design.findViewById(R.id.imgAddSetupProd);
        tvRepetido = design.findViewById(R.id.tvRepetido);
        llenarSpinner(pos);
        imgAddSetupProd.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            DialogAgregarSetupFromProd dialog = new DialogAgregarSetupFromProd();
            bundle.putSerializable("prod", prod);
            dialog.setArguments(bundle);
            dialog.show(((ProductoActivity) requireHost()).getSupportFragmentManager(), "AgregarSetup");
            dismiss();
        });

        builder.setView(design)
                .setPositiveButton("Añadir", (dialog, which) -> {
                    Call<Boolean> llamadaActualizarSetup = ApiService.getApi().insertarProdASetup(setup.getIdSetup(), prod.getIdProducto());
                    llamadaActualizarSetup.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(productoActivity, "Agregado correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                            Log.e("Retrofit Error!!", t.getMessage());
                        }
                    });
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                });
        return builder.create();
    }

    public class SpinnerSetupAdapter extends Activity implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            tvRepetido.setVisibility(View.GONE);
            //Obtener el setup especifico de la lista
            for (int i = 0; i < listaSetup.size(); i++) {
                if (listaSetup.get(i).getNombre().equals(spinnerSetupD.getSelectedItem().toString())) {
                    setup = listaSetup.get(i);
                    break;
                }
            }
            //Validación si se repite
            for (Producto productos : setup.getProducto_setup()) {
                if (productos.getIdProducto() == (prod.getIdProducto())) {
                    tvRepetido.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public void llenarSpinner(int pos) {
        Call<List<Setup>> llamadaLlenarSpinner = ApiService.getApi().getUserSetups(MainActivity.user.getIdUsuario());
        llamadaLlenarSpinner.enqueue(new Callback<List<Setup>>() {
            @Override
            public void onResponse(@NonNull Call<List<Setup>> call, @NonNull Response<List<Setup>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    //llenar Array
                    for (Setup setp : response.body()) {
                        spinnerArray.add(setp.getNombre());
                        listaSetup.add(setp);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerSetupD.setAdapter(adapter);
                        spinnerSetupD.setOnItemSelectedListener(new SpinnerSetupAdapter());
                        if (pos == -1) {
                            spinnerSetupD.setSelection(spinnerArray.size() - 1);
                        } else {
                            spinnerSetupD.setSelection(pos);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Setup>> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
    }
}
