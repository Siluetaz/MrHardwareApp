package com.example.mrhardwareapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mrhardwareapp.MainActivity;
import com.example.mrhardwareapp.R;
import com.example.mrhardwareapp.adapters.RVPerfilCommentAdapter;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.models.Comentario;
import com.example.mrhardwareapp.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComentariosPerfilFragment extends Fragment{

    RecyclerView recyclerComments;
    RVPerfilCommentAdapter adapter;
    TextView mensajeVacio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comentarios, container, false);

        recyclerComments = view.findViewById(R.id.recyclerComments);
        mensajeVacio = view.findViewById(R.id.mensajeVacio);
        Usuario user = MainActivity.user;
        Call<List<Comentario>> listaComment = ApiService.getApi().getAllComments(user.getIdUsuario());
        listaComment.enqueue(new Callback<List<Comentario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comentario>> call, @NonNull Response<List<Comentario>> response) {
                if (response.isSuccessful()) {
                    adapter = new RVPerfilCommentAdapter(getContext(), response.body());
                    recyclerComments.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerComments.setHasFixedSize(true);
                    adapter.setMyItemClickListener(position -> {
                    });
                    recyclerComments.setAdapter(adapter);
                    mensajeVacio();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Comentario>> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
        return view;
    }

    public RVPerfilCommentAdapter recibirAdapter() {
        return adapter;
    }

    public void mensajeVacio(){
        if (recyclerComments.getAdapter() != null) {
            if (recyclerComments.getAdapter().getItemCount() == 0) {
                mensajeVacio.setVisibility(View.VISIBLE);
            } else if (recyclerComments.getAdapter().getItemCount() > 0) {
                mensajeVacio.setVisibility(View.GONE);
            }
        }
    }
}