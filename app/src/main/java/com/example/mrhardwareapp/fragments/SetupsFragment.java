package com.example.mrhardwareapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.mrhardwareapp.MainActivity;
import com.example.mrhardwareapp.ProductoActivity;
import com.example.mrhardwareapp.R;
import com.example.mrhardwareapp.adapters.DialogAgregarSetup;
import com.example.mrhardwareapp.adapters.DialogQuitarProdSetup;
import com.example.mrhardwareapp.adapters.DialogQuitarSetup;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.models.Producto;
import com.example.mrhardwareapp.models.Setup;
import com.example.mrhardwareapp.models.Usuario;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetupsFragment extends Fragment {

    Spinner spinnerSetup;
    TextView tvNameProce;
    TextView tvPriceProce;
    TextView tvNameGrafica;
    TextView tvPriceGrafica;
    TextView tvNameMother;
    TextView tvPriceMother;
    TextView tvNameRam;
    TextView tvPriceRam;
    TextView tvNameAlmace;
    TextView tvPriceAlmace;
    TextView tvNameGabinete;
    TextView tvPriceGabinete;
    TextView tvNamePower;
    TextView tvPricePower;
    TextView tvNameVentilador;
    TextView tvPriceVentilador;
    TextView tvNameMonitor;
    TextView tvPriceMonitor;
    ScrollView containerSetup;
    Usuario user = MainActivity.user;
    List<String> spinnerArray;
    LinearLayout mensajeVacio;
    TextView msjVacioAddSetup;
    TextView tvSumaTotal;
    CardView containerProcesador;
    CardView containerGrafica;
    CardView containerMother;
    CardView containerRam;
    CardView containerAlmace;
    CardView containerGabinete;
    CardView containerPower;
    CardView containerVentilador;
    CardView containerMonitor;
    ImageView imgAddSetup;
    ImageView imgRemoverSetup;
    ImageView imgRemoverProce;
    ImageView imgRemoverMother;
    ImageView imgRemoverGrafica;
    ImageView imgRemoverRam;
    ImageView imgRemoverAlmace;
    ImageView imgRemoverGabinete;
    ImageView imgRemoverPower;
    ImageView imgRemoverVentilador;
    ImageView imgRemoverMonitor;
    List<Producto> listaProductos;
    List<Setup> listaSetup;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setups, container, false);

        tvNameProce = view.findViewById(R.id.tvNameProce);
        tvPriceProce = view.findViewById(R.id.tvPriceProce);
        tvNameGrafica = view.findViewById(R.id.tvNameGrafica);
        tvPriceGrafica = view.findViewById(R.id.tvPriceGrafica);
        tvNameMother = view.findViewById(R.id.tvNameMother);
        tvPriceMother = view.findViewById(R.id.tvPriceMother);
        tvNameRam = view.findViewById(R.id.tvNameRam);
        tvPriceRam = view.findViewById(R.id.tvPriceRam);
        tvNameAlmace = view.findViewById(R.id.tvNameAlmace);
        tvPriceAlmace = view.findViewById(R.id.tvPriceAlmace);
        tvNameGabinete = view.findViewById(R.id.tvNameGabinete);
        tvPriceGabinete = view.findViewById(R.id.tvPriceGabinete);
        tvNamePower = view.findViewById(R.id.tvNamePower);
        tvPricePower = view.findViewById(R.id.tvPricePower);
        tvNameVentilador = view.findViewById(R.id.tvNameVentilador);
        tvPriceVentilador = view.findViewById(R.id.tvPriceVentilador);
        tvNameMonitor = view.findViewById(R.id.tvNameMonitor);
        tvPriceMonitor = view.findViewById(R.id.tvPriceMonitor);
        mensajeVacio = view.findViewById(R.id.mensajeVacio);
        containerSetup = view.findViewById(R.id.containerSetup);
        spinnerSetup = view.findViewById(R.id.spinnerSetup);
        msjVacioAddSetup = view.findViewById(R.id.msjVacioAddSetup);
        tvSumaTotal = view.findViewById(R.id.tvSumaTotal);
        containerProcesador = view.findViewById(R.id.containerProcesador);
        containerMother = view.findViewById(R.id.containerMother);
        containerGrafica = view.findViewById(R.id.containerGrafica);
        containerRam = view.findViewById(R.id.containerRam);
        containerAlmace = view.findViewById(R.id.containerAlmace);
        containerGabinete = view.findViewById(R.id.containerGabinete);
        containerPower = view.findViewById(R.id.containerPower);
        containerVentilador = view.findViewById(R.id.containerVentilador);
        containerMonitor = view.findViewById(R.id.containerMonitor);
        imgAddSetup = view.findViewById(R.id.imgAddSetup);
        imgRemoverSetup = view.findViewById(R.id.imgRemoverSetup);
        imgRemoverProce = view.findViewById(R.id.imgRemoverProce);
        imgRemoverMother = view.findViewById(R.id.imgRemoverMother);
        imgRemoverGrafica = view.findViewById(R.id.imgRemoverGrafica);
        imgRemoverRam = view.findViewById(R.id.imgRemoverRam);
        imgRemoverAlmace = view.findViewById(R.id.imgRemoverAlmace);
        imgRemoverGabinete = view.findViewById(R.id.imgRemoverGabinete);
        imgRemoverPower = view.findViewById(R.id.imgRemoverPower);
        imgRemoverVentilador = view.findViewById(R.id.imgRemoverVentilador);
        imgRemoverMonitor = view.findViewById(R.id.imgRemoverMonitor);

        llenarSpinner(0);

        msjVacioAddSetup.setOnClickListener(v -> {
            DialogAgregarSetup dialog = new DialogAgregarSetup();
            dialog.show(requireActivity().getSupportFragmentManager(), "addSetup");
        });
        imgAddSetup.setOnClickListener(v -> {
            DialogAgregarSetup dialog = new DialogAgregarSetup();
            dialog.show(requireActivity().getSupportFragmentManager(), "addSetup");
        });
        imgRemoverSetup.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putSerializable("setup", obtenerSetup());
            DialogQuitarSetup dialog = new DialogQuitarSetup();
            dialog.setArguments(b);
            dialog.show(requireActivity().getSupportFragmentManager(), "quitarSetup");
        });
        containerProcesador.setOnClickListener(v -> {
            if (obtenerProducto(1) != null) {
                Intent i = new Intent(getContext(), ProductoActivity.class);
                i.putExtra("prod", obtenerProducto(1));
                startActivity(i);
            }
        });
        containerGrafica.setOnClickListener(v -> {
            if (obtenerProducto(2) != null) {
                Intent i = new Intent(getContext(), ProductoActivity.class);
                i.putExtra("prod", obtenerProducto(2));
                startActivity(i);
            }
        });
        containerMother.setOnClickListener(v -> {
            if (obtenerProducto(3) != null) {
                Intent i = new Intent(getContext(), ProductoActivity.class);
                i.putExtra("prod", obtenerProducto(3));
                startActivity(i);
            }
        });
        containerRam.setOnClickListener(v -> {
            if (obtenerProducto(4) != null) {
                Intent i = new Intent(getContext(), ProductoActivity.class);
                i.putExtra("prod", obtenerProducto(4));
                startActivity(i);
            }
        });
        containerAlmace.setOnClickListener(v -> {
            if (obtenerProducto(5) != null) {
                Intent i = new Intent(getContext(), ProductoActivity.class);
                i.putExtra("prod", obtenerProducto(5));
                startActivity(i);
            }
        });
        containerGabinete.setOnClickListener(v -> {
            if (obtenerProducto(6) != null) {
                Intent i = new Intent(getContext(), ProductoActivity.class);
                i.putExtra("prod", obtenerProducto(6));
                startActivity(i);
            }
        });
        containerPower.setOnClickListener(v -> {
            if (obtenerProducto(7) != null) {
                Intent i = new Intent(getContext(), ProductoActivity.class);
                i.putExtra("prod", obtenerProducto(7));
                startActivity(i);
            }
        });
        containerVentilador.setOnClickListener(v -> {
            if (obtenerProducto(8) != null) {
                Intent i = new Intent(getContext(), ProductoActivity.class);
                i.putExtra("prod", obtenerProducto(8));
                startActivity(i);
            }
        });
        containerMonitor.setOnClickListener(v -> {
            if (obtenerProducto(9) != null) {
                Intent i = new Intent(getContext(), ProductoActivity.class);
                i.putExtra("prod", obtenerProducto(9));
                startActivity(i);
            }
        });
        imgRemoverProce.setOnClickListener(v -> {
            Bundle b = new Bundle();
            DialogQuitarProdSetup dialog = new DialogQuitarProdSetup();
            b.putSerializable("prod", obtenerProducto(1));
            b.putSerializable("setup", obtenerSetup());
            b.putInt("pos", spinnerSetup.getSelectedItemPosition());
            dialog.setArguments(b);
            dialog.show(requireActivity().getSupportFragmentManager(), "removerProceDeSetup");
        });
        imgRemoverGrafica.setOnClickListener(v -> {
            Bundle b = new Bundle();
            DialogQuitarProdSetup dialog = new DialogQuitarProdSetup();
            b.putSerializable("prod", obtenerProducto(2));
            b.putSerializable("setup", obtenerSetup());
            b.putInt("pos", spinnerSetup.getSelectedItemPosition());
            dialog.setArguments(b);
            dialog.show(requireActivity().getSupportFragmentManager(), "removerGraficaDeSetup");
        });
        imgRemoverMother.setOnClickListener(v -> {
            Bundle b = new Bundle();
            DialogQuitarProdSetup dialog = new DialogQuitarProdSetup();
            b.putSerializable("prod", obtenerProducto(3));
            b.putSerializable("setup", obtenerSetup());
            b.putInt("pos", spinnerSetup.getSelectedItemPosition());
            dialog.setArguments(b);
            dialog.show(requireActivity().getSupportFragmentManager(), "removerMotherDeSetup");
        });
        imgRemoverRam.setOnClickListener(v -> {
            Bundle b = new Bundle();
            DialogQuitarProdSetup dialog = new DialogQuitarProdSetup();
            b.putSerializable("prod", obtenerProducto(4));
            b.putSerializable("setup", obtenerSetup());
            b.putInt("pos", spinnerSetup.getSelectedItemPosition());
            dialog.setArguments(b);
            dialog.show(requireActivity().getSupportFragmentManager(), "removerRamDeSetup");
        });
        imgRemoverAlmace.setOnClickListener(v -> {
            Bundle b = new Bundle();
            DialogQuitarProdSetup dialog = new DialogQuitarProdSetup();
            b.putSerializable("prod", obtenerProducto(5));
            b.putSerializable("setup", obtenerSetup());
            b.putInt("pos", spinnerSetup.getSelectedItemPosition());
            dialog.setArguments(b);
            dialog.show(requireActivity().getSupportFragmentManager(), "removerAlmaceDeSetup");
        });
        imgRemoverGabinete.setOnClickListener(v -> {
            Bundle b = new Bundle();
            DialogQuitarProdSetup dialog = new DialogQuitarProdSetup();
            b.putSerializable("prod", obtenerProducto(6));
            b.putSerializable("setup", obtenerSetup());
            b.putInt("pos", spinnerSetup.getSelectedItemPosition());
            dialog.setArguments(b);
            dialog.show(requireActivity().getSupportFragmentManager(), "removerGabineteDeSetup");
        });
        imgRemoverPower.setOnClickListener(v -> {
            Bundle b = new Bundle();
            DialogQuitarProdSetup dialog = new DialogQuitarProdSetup();
            b.putSerializable("prod", obtenerProducto(7));
            b.putSerializable("setup", obtenerSetup());
            b.putInt("pos", spinnerSetup.getSelectedItemPosition());
            dialog.setArguments(b);
            dialog.show(requireActivity().getSupportFragmentManager(), "removerPowerDeSetup");
        });
        imgRemoverVentilador.setOnClickListener(v -> {
            Bundle b = new Bundle();
            DialogQuitarProdSetup dialog = new DialogQuitarProdSetup();
            b.putSerializable("prod", obtenerProducto(8));
            b.putSerializable("setup", obtenerSetup());
            b.putInt("pos", spinnerSetup.getSelectedItemPosition());
            dialog.setArguments(b);
            dialog.show(requireActivity().getSupportFragmentManager(), "removerVentiladorDeSetup");
        });
        imgRemoverMonitor.setOnClickListener(v -> {
            Bundle b = new Bundle();
            DialogQuitarProdSetup dialog = new DialogQuitarProdSetup();
            b.putSerializable("prod", obtenerProducto(9));
            b.putSerializable("setup", obtenerSetup());
            b.putInt("pos", spinnerSetup.getSelectedItemPosition());
            dialog.setArguments(b);
            dialog.show(requireActivity().getSupportFragmentManager(), "removerMonitorDeSetup");
        });
        return view;
    }

    public class SpinnerSetupAdapter extends Activity implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Call<List<Setup>> listaSetup = ApiService.getApi().getUserSetups(user.getIdUsuario());
            listaSetup.enqueue(new Callback<List<Setup>>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<List<Setup>> call, @NonNull Response<List<Setup>> response) {
                    if (response.isSuccessful()) {
                        int suma = 0;
                        tvNameProce.setText("Sin Procesador");
                        tvPriceProce.setText("$0");
                        tvNameGrafica.setText("Sin Grafica");
                        tvPriceGrafica.setText("$0");
                        tvNameMother.setText("Sin Placa Madre");
                        tvPriceMother.setText("$0");
                        tvNameRam.setText("Sin Ram");
                        tvPriceRam.setText("$0");
                        tvNameAlmace.setText("Sin Almacenamiento");
                        tvPriceAlmace.setText("$0");
                        tvNameGabinete.setText("Sin Gabinete");
                        tvPriceGabinete.setText("$0");
                        tvNamePower.setText("Sin Fuente de Poder");
                        tvPricePower.setText("$0");
                        tvNameVentilador.setText("Sin Ventilador");
                        tvPriceVentilador.setText("$0");
                        tvNameMonitor.setText("Sin Monitor");
                        tvPriceMonitor.setText("$0");
                        imgRemoverProce.setVisibility(View.GONE);
                        imgRemoverGrafica.setVisibility(View.GONE);
                        imgRemoverMother.setVisibility(View.GONE);
                        imgRemoverRam.setVisibility(View.GONE);
                        imgRemoverAlmace.setVisibility(View.GONE);
                        imgRemoverGabinete.setVisibility(View.GONE);
                        imgRemoverPower.setVisibility(View.GONE);
                        imgRemoverVentilador.setVisibility(View.GONE);
                        imgRemoverMonitor.setVisibility(View.GONE);
                        String selected = spinnerSetup.getSelectedItem().toString();
                        listaProductos = new ArrayList<>();
                        for (int i = 0; i < spinnerArray.size(); i++) {
                            if (selected.equals(spinnerArray.get(i))) {
                                assert response.body() != null;
                                if (response.body().size() > 0) {
                                    Setup setp = response.body().get(i);
                                    if (setp.getProducto_setup().size() > 0) {
                                        for (Producto prodSetp : setp.getProducto_setup()) {
                                            listaProductos.add(prodSetp);
                                            switch (prodSetp.getId_TipoProducto()) {
                                                case 1:
                                                    imgRemoverProce.setVisibility(View.VISIBLE);
                                                    tvNameProce.setText(prodSetp.getNombre());
                                                    tvPriceProce.setText(ponerSignos(prodSetp.getPrecio()));
                                                    suma = suma + prodSetp.getPrecio();
                                                    break;
                                                case 2:
                                                    imgRemoverGrafica.setVisibility(View.VISIBLE);
                                                    tvNameGrafica.setText(prodSetp.getNombre());
                                                    tvPriceGrafica.setText(ponerSignos(prodSetp.getPrecio()));
                                                    suma = suma + prodSetp.getPrecio();
                                                    break;
                                                case 3:
                                                    imgRemoverMother.setVisibility(View.VISIBLE);
                                                    tvNameMother.setText(prodSetp.getNombre());
                                                    tvPriceMother.setText(ponerSignos(prodSetp.getPrecio()));
                                                    suma = suma + prodSetp.getPrecio();
                                                    break;
                                                case 4:
                                                    imgRemoverRam.setVisibility(View.VISIBLE);
                                                    tvNameRam.setText(prodSetp.getNombre());
                                                    tvPriceRam.setText(ponerSignos(prodSetp.getPrecio()));
                                                    suma = suma + prodSetp.getPrecio();
                                                    break;
                                                case 5:
                                                    imgRemoverAlmace.setVisibility(View.VISIBLE);
                                                    tvNameAlmace.setText(prodSetp.getNombre());
                                                    tvPriceAlmace.setText(ponerSignos(prodSetp.getPrecio()));
                                                    suma = suma + prodSetp.getPrecio();
                                                    break;
                                                case 6:
                                                    imgRemoverGabinete.setVisibility(View.VISIBLE);
                                                    tvNameGabinete.setText(prodSetp.getNombre());
                                                    tvPriceGabinete.setText(ponerSignos(prodSetp.getPrecio()));
                                                    suma = suma + prodSetp.getPrecio();
                                                    break;
                                                case 7:
                                                    imgRemoverPower.setVisibility(View.VISIBLE);
                                                    tvNamePower.setText(prodSetp.getNombre());
                                                    tvPricePower.setText(ponerSignos(prodSetp.getPrecio()));
                                                    suma = suma + prodSetp.getPrecio();
                                                    break;
                                                case 8:
                                                    imgRemoverVentilador.setVisibility(View.VISIBLE);
                                                    tvNameVentilador.setText(prodSetp.getNombre());
                                                    tvPriceVentilador.setText(ponerSignos(prodSetp.getPrecio()));
                                                    suma = suma + prodSetp.getPrecio();
                                                    break;
                                                case 9:
                                                    imgRemoverMonitor.setVisibility(View.VISIBLE);
                                                    tvNameMonitor.setText(prodSetp.getNombre());
                                                    tvPriceMonitor.setText(ponerSignos(prodSetp.getPrecio()));
                                                    suma = suma + prodSetp.getPrecio();
                                                    break;
                                            }
                                        }

                                    }
                                }

                            }
                        }
                        tvSumaTotal.setText(ponerSignos(suma));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Setup>> call, @NonNull Throwable t) {
                    Log.e("Retrofit Error!!", t.getMessage());
                }
            });
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public void llenarSpinner(int pos) {
        spinnerArray = new ArrayList<>();
        listaSetup = new ArrayList<>();
        Call<List<Setup>> llamadaLlenarSetup = ApiService.getApi().getUserSetups(user.getIdUsuario());
        llamadaLlenarSetup.enqueue(new Callback<List<Setup>>() {
            @Override
            public void onResponse(@NonNull Call<List<Setup>> call, @NonNull Response<List<Setup>> response) {
                if (response.isSuccessful()) {
                    //
                    assert response.body() != null;
                    if (response.body().size() > 0) {
                        for (Setup setup : response.body()) {
                            spinnerArray.add(setup.getNombre());
                            listaSetup.add(setup);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerSetup.setAdapter(adapter);
                        spinnerSetup.setOnItemSelectedListener(new SpinnerSetupAdapter());
                        if (pos == -1) {
                            spinnerSetup.setSelection(spinnerArray.size() - 1);
                        } else {
                            spinnerSetup.setSelection(pos);
                        }
                        mensajeVacio.setVisibility(View.GONE);
                        containerSetup.setVisibility(View.VISIBLE);
                    } else {
                        mensajeVacio.setVisibility(View.VISIBLE);
                        containerSetup.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Setup>> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
    }

    public String ponerSignos(int precio) {
        String precioS = String.valueOf(precio);
        int pos = 4;
        StringBuilder stringBuilder = new StringBuilder(precioS);
        ArrayList<String> array = new ArrayList<>();
        for (int i = precioS.length(); i > 0; i--) {
            array.add(precioS.substring(i));
            if (array.size() == pos) {
                stringBuilder.insert(i, ".");
                pos = pos + 3;
            }
        }
        return "$" + stringBuilder;
    }

    public Producto obtenerProducto(int tipoProd) {
        for (int i = 0; i < listaProductos.size(); i++) {
            if (listaProductos.get(i).getId_TipoProducto() == tipoProd) {
                return listaProductos.get(i);
            }
        }
        return null;
    }

    public Setup obtenerSetup() {
        String nombreSetup = spinnerSetup.getSelectedItem().toString();
        for (int i = 0; i < listaSetup.size(); i++) {
            if (listaSetup.get(i).getNombre().equals(nombreSetup)) {
                return listaSetup.get(i);
            }
        }
        return null;
    }

}
