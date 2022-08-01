package com.example.mrhardwareapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mrhardwareapp.R;
import com.example.mrhardwareapp.adapters.RVCommentProductoAdapter;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.models.Comentario;
import com.example.mrhardwareapp.models.Producto;
import com.example.mrhardwareapp.models.Usuario;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductoCommentFragment extends Fragment {

    //Variables del layout
    RecyclerView recyclerProdComment;
    RVCommentProductoAdapter adapter;
    LinearLayout verTodos;
    TextView tvMinimizar;
    Usuario user;
    EditText txtComentarioN;
    Button btnComentar;
    Producto prod;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producto_comment, container, false);
        assert getArguments() != null;
        //Obtenemos el producto enviado desde el ProductoActivity
        prod = (Producto) getArguments().getSerializable("prod");
        //Validacion creada para saber si hay un usuario logeado
        if (getArguments().getSerializable("user") != null) {
            user = (Usuario) getArguments().getSerializable("user");
        }

        //Llenamos las variables con sus respectivas vistas.
        recyclerProdComment = view.findViewById(R.id.recyclerProdComment);
        verTodos = view.findViewById(R.id.verTodos);
        tvMinimizar = view.findViewById(R.id.tvMinimizar);
        btnComentar = view.findViewById(R.id.btnComentar);
        txtComentarioN = view.findViewById(R.id.txtComentarioN);

        comentariosMinimizado(prod.getIdProducto());
        verTodos.setOnClickListener(v -> {
            if (tvMinimizar.getText().toString().equals("Ver todos los comentarios")) {
                tvMinimizar.setText("Minimizar comentarios");
                todosLosComentarios(prod.getIdProducto());
            } else if (tvMinimizar.getText().toString().equals("Minimizar comentarios")) {
                tvMinimizar.setText("Ver todos los comentarios");
                comentariosMinimizado(prod.getIdProducto());
            }
        });
        btnComentar.setOnClickListener(v -> {
            if (user != null) {
                if (txtComentarioN.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "El campo está vacío", Toast.LENGTH_SHORT).show();
                } else {
                    Call<Comentario> comentarioCall = ApiService.getApi().insertarComentario(txtComentarioN.getText().toString(), prod.getIdProducto(), user.getIdUsuario());
                    comentarioCall.enqueue(new Callback<Comentario>() {
                        @Override
                        public void onResponse(@NonNull Call<Comentario> call, @NonNull Response<Comentario> response) {
                            if (response.isSuccessful()) {
                                txtComentarioN.setText("");
                                comentariosMinimizado(prod.getIdProducto());

                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Comentario> call, @NonNull Throwable t) {
                        }
                    });
                }

            } else {
                Toast.makeText(getContext(), "Debes iniciar tu sesión para comentar", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void comentariosMinimizado(int idProducto) {
        Call<List<Comentario>> listaComment = ApiService.getApi().getProductoComments(idProducto);
        listaComment.enqueue(new Callback<List<Comentario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comentario>> call, @NonNull Response<List<Comentario>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<Comentario> listaComentario = new ArrayList<>();
                    List<Comentario> listaRespuestas = new ArrayList<>();
                    List<Comentario> listaComentario1 = new ArrayList<>();
                    for (Comentario comentario : response.body()) {
                        if (comentario.getId_Comentario_Respuesta() == 0) {
                            listaComentario.add(comentario);
                        } else {
                            listaRespuestas.add(comentario);
                        }
                    }
                    if (listaComentario.size() > 2) {
                        for (int i = 0; i < listaComentario.size(); i++) {
                            listaComentario1.add(listaComentario.get(i));
                            if (i == 2) {
                                break;
                            }
                        }
                    } else if (listaComentario.size() == 0) {
                        tvMinimizar.setText("No hay comentarios, sé el primero");
                    } else {
                        tvMinimizar.setText("No hay más comentarios");
                        listaComentario1.addAll(listaComentario);
                    }
                    adapter = new RVCommentProductoAdapter(getContext(), listaComentario1, listaRespuestas, user, prod);
                    recyclerProdComment.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerProdComment.setHasFixedSize(true);
                    adapter.setMyItemClickListener(position -> {

                    });
                    recyclerProdComment.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Comentario>> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
    }

    public void todosLosComentarios(int idProducto) {
        Call<List<Comentario>> listaComment = ApiService.getApi().getProductoComments(idProducto);
        listaComment.enqueue(new Callback<List<Comentario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comentario>> call, @NonNull Response<List<Comentario>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<Comentario> listaComentario = new ArrayList<>();
                    List<Comentario> listaRespuestas = new ArrayList<>();
                    for (Comentario comentario : response.body()) {
                        if (comentario.getId_Comentario_Respuesta() == 0) {
                            listaComentario.add(comentario);
                        } else {
                            listaRespuestas.add(comentario);
                        }
                    }

                    adapter = new RVCommentProductoAdapter(getContext(), listaComentario, listaRespuestas, user, prod);
                    recyclerProdComment.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerProdComment.setHasFixedSize(true);
                    adapter.setMyItemClickListener(position -> {
                    });
                    recyclerProdComment.setAdapter(adapter);
                    if (response.body().size() == 0) {
                        tvMinimizar.setText("No hay comentarios, sé el primero");
                    } else {
                        tvMinimizar.setText("Minimizar comentarios");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Comentario>> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
    }

}