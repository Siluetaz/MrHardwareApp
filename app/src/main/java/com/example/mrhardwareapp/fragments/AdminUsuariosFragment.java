package com.example.mrhardwareapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mrhardwareapp.R;

import com.example.mrhardwareapp.adapters.RVAdminUserAdapter;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUsuariosFragment extends Fragment implements SearchView.OnQueryTextListener {

    SearchView txtbuscar;
    RecyclerView recyclerUsers;
    RVAdminUserAdapter adapter;
    TextView mensajeVacio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycler_usuarios, container, false);
        recyclerUsers = view.findViewById(R.id.recyclerUsers);
        txtbuscar = view.findViewById(R.id.txtbuscar);
        mensajeVacio = view.findViewById(R.id.mensajeVacio);

        Call<List<Usuario>> listaUser = ApiService.getApi().getAllUsers();
        listaUser.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Usuario>> call, @NonNull Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    System.out.println(response.body());
                    adapter = new RVAdminUserAdapter(getContext(), response.body());
                    recyclerUsers.setAdapter(adapter);
                    recyclerUsers.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerUsers.setHasFixedSize(true);
                    adapter.setMyItemClickListener(position -> {
                    });
                    mensajeVacio();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Usuario>> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
        txtbuscar.setOnQueryTextListener(this);
        return view;
    }

    public RVAdminUserAdapter update() {
        return adapter;
    }

    public void mensajeVacio() {
        if (recyclerUsers.getAdapter() != null) {
            if (recyclerUsers.getAdapter().getItemCount() == 0) {
                mensajeVacio.setVisibility(View.VISIBLE);
            } else if (recyclerUsers.getAdapter().getItemCount() > 0) {
                mensajeVacio.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.filtrado(s);
        mensajeVacio();
        return false;
    }
}