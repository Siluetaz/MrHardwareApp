package com.example.mrhardwareapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mrhardwareapp.R;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.models.Comentario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RVAdminCommentAdapter extends RecyclerView.Adapter<RVAdminCommentAdapter.ViewHolder> {
    private final Context context;
    public static List<Comentario> listaComentarios;
    private MyItemClickListener myItemClickListener;
    public static List<Comentario> listaOriginal;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public RVAdminCommentAdapter(Context context, List<Comentario> listaComentarios) {
        this.context = context;
        listaOriginal = new ArrayList<>();
        RVAdminCommentAdapter.listaComentarios = listaComentarios;
        listaOriginal.addAll(listaComentarios);
    }

    public interface MyItemClickListener {
        void OnClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        private static final String TAG = "MyViewHolder";
        TextView tvUser;
        TextView tvComentario;
        ImageView imageComment;
        ImageView imageMenu;
        Context context;
        TextView tvFechaComment;

        public ViewHolder(@NonNull View view, RVAdminCommentAdapter.MyItemClickListener myItemClickListener) {
            super(view);
            tvUser = view.findViewById(R.id.tvUser);
            tvComentario = view.findViewById(R.id.tvComentario);
            tvFechaComment = view.findViewById(R.id.tvFechaComment);
            imageComment = view.findViewById(R.id.imageComment);
            context = view.getContext();
            imageMenu = view.findViewById(R.id.imageMenu);
            imageMenu.setOnClickListener(this);

            view.setOnClickListener(v -> {
                int position = getAdapterPosition();
                myItemClickListener.OnClick(position);
                if (tvComentario.getMaxLines() == 1) {
                    tvComentario.setMaxLines(10);
                } else if (tvComentario.getMaxLines() == 10) {
                    tvComentario.setMaxLines(1);
                }
            });
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick" + getAdapterPosition());
            showpopupMenu(view);
        }

        private void showpopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.banear) {
                DialogBanearComentario dialogo = new DialogBanearComentario();
                Bundle args = new Bundle();
                args.putInt("idComentario", listaComentarios.get(getAdapterPosition()).getIdComentario());
                args.putInt("destino", 1);
                args.putInt("position", getAdapterPosition());
                dialogo.setArguments(args);
                dialogo.show(((AppCompatActivity) context).getSupportFragmentManager(), "salir");
                return true;
            } else if (item.getItemId() == R.id.advertencia) {
                Call<Boolean> llamadaApi = ApiService.getApi().enviarCorreo(listaComentarios.get(getAdapterPosition()).getComentario(), listaComentarios.get(getAdapterPosition()).getId_Usuario().getEmail(), listaComentarios.get(getAdapterPosition()).getId_Usuario().getUsername());
                llamadaApi.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context.getApplicationContext(), "Se ha enviado la advertencia", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure
                            (@NonNull Call<Boolean> call, @NonNull Throwable t) {
                        Log.e("Retrofit Error!!", t.getMessage());
                    }
                });
            }
            return false;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_comentarios, parent, false);
        return new RVAdminCommentAdapter.ViewHolder(view, this.myItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comentario comentario = listaComentarios.get(position);

        SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
        holder.tvFechaComment.setText(format.format(comentario.getFecha()));
        holder.tvUser.setText(comentario.getId_Usuario().getUsername());
        holder.tvComentario.setText(comentario.getComentario());

    }


    @Override
    public int getItemCount() {
        return listaComentarios.size();
    }

    public void update(int position, int position1) {
        listaComentarios.remove(position);
        listaOriginal.remove(position1);
        notifyItemRemoved(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filtrado(String txtbuscarC) {
        int longitud = txtbuscarC.length();
        if (longitud == 0) {
            listaComentarios.clear();
            listaComentarios.addAll(listaOriginal);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Comentario> collecion = listaComentarios.stream().filter(i -> i.getId_Usuario().getUsername().toLowerCase()
                        .contains(txtbuscarC.toLowerCase())).collect(Collectors.toList());
                listaComentarios.clear();
                listaComentarios.addAll(collecion);
            } else {
                for (Comentario c : listaOriginal) {
                    if (c.getId_Usuario().getUsername().toLowerCase().contains(txtbuscarC.toLowerCase())) {
                        listaComentarios.add(c);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}