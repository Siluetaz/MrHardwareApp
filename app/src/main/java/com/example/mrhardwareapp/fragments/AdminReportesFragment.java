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
import com.example.mrhardwareapp.adapters.RVAdminReportAdapter;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.models.Reportes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminReportesFragment extends Fragment implements SearchView.OnQueryTextListener {

    SearchView txtbuscarR;
    RecyclerView recyclerReports;
    RVAdminReportAdapter adapter;
    TextView mensajeVacio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycler_reports, container, false);
        recyclerReports = view.findViewById(R.id.recyclerReports);
        mensajeVacio = view.findViewById(R.id.mensajeVacio);
        txtbuscarR = view.findViewById(R.id.txtbuscarR);

        Call<List<Reportes>> listaReports = ApiService.getApi().getAllReports();
        listaReports.enqueue(new Callback<List<Reportes>>() {
            @Override
            public void onResponse(@NonNull Call<List<Reportes>> call, @NonNull Response<List<Reportes>> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        for (int j = 1; j < response.body().size(); ) {
                            Reportes reporte = response.body().get(j);
                            if (reporte.getIdReporte() > 1 && reporte.getId_Comentario().getIdComentario() == response.body().get(j - 1).getId_Comentario().getIdComentario()) {
                                response.body().remove(j);
                            } else {
                                j++;
                            }
                        }
                    }

                    adapter = new RVAdminReportAdapter(getContext(), response.body());

                    recyclerReports.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerReports.setHasFixedSize(true);
                    recyclerReports.setAdapter(adapter);
                    adapter.setMyItemClickListener(position -> {

                    });
                    mensajeVacio();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Reportes>> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
        txtbuscarR.setOnQueryTextListener(this);
        return view;
    }

    public RVAdminReportAdapter update() {
        return adapter;
    }

    public void mensajeVacio(){
        if (recyclerReports.getAdapter() != null) {
            if (recyclerReports.getAdapter().getItemCount() == 0) {
                mensajeVacio.setVisibility(View.VISIBLE);
            } else if (recyclerReports.getAdapter().getItemCount() > 0) {
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
