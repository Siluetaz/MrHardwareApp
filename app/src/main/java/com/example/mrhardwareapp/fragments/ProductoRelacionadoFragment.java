package com.example.mrhardwareapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mrhardwareapp.ProductoActivity;
import com.example.mrhardwareapp.R;
import com.example.mrhardwareapp.adapters.RVProductosRelacionadosAdapter;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.models.Producto;
import com.example.mrhardwareapp.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoRelacionadoFragment extends Fragment {

    RecyclerView recyclerProdRelacion;
    RVProductosRelacionadosAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_producto_relacionado, container, false);

        recyclerProdRelacion = view.findViewById(R.id.recyclerProdRelacion);

        assert getArguments() != null;
        Producto prod = (Producto) getArguments().getSerializable("prod");
        Usuario user = (Usuario) getArguments().getSerializable("user");

        Call<List<Producto>> listaProdFiltrada = ApiService.getApi().filtrarProductosTipoProd(prod.getId_TipoProducto());
        listaProdFiltrada.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Producto>> call, @NonNull Response<List<Producto>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    for (Producto producto : response.body()){
                        if (producto.getIdProducto() == prod.getIdProducto()){
                            response.body().remove(producto);
                            break;
                        }
                    }
                    adapter = new RVProductosRelacionadosAdapter(getContext(), response.body());
                    recyclerProdRelacion.setAdapter(adapter);
                    recyclerProdRelacion.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
                    recyclerProdRelacion.setHasFixedSize(true);
                    adapter.setMyItemCLickListener(position -> {
                        Intent i = new Intent(getContext(), ProductoActivity.class);
                        i.putExtra("user", user);
                        i.putExtra("prod", RVProductosRelacionadosAdapter.listaProd.get(position));
                        startActivity(i);
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Producto>> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
        return view;
    }
}