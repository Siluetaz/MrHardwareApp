package com.example.mrhardwareapp.adapters;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mrhardwareapp.R;
import com.example.mrhardwareapp.models.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RVAdminUserAdapter extends RecyclerView.Adapter<RVAdminUserAdapter.ViewHolder> {
    private final Context context;
    public static List<Usuario> listaUser;
    private MyItemClickListener myItemClickListener;
    public static List<Usuario> listaOriginal;


    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public RVAdminUserAdapter(Context context, List<Usuario> listaUser) {
        this.context = context;
        listaOriginal = new ArrayList<>();
        RVAdminUserAdapter.listaUser = listaUser;
        listaOriginal.addAll(listaUser);
    }

    public interface MyItemClickListener {
        void OnClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUser;
        TextView tvDatos;
        ImageView imageUser;
        ImageView imageButton;
        Context context;

        public ViewHolder(@NonNull View view, RVAdminUserAdapter.MyItemClickListener myItemClickListener) {
            super(view);
            tvUser = view.findViewById(R.id.tvUser);
            tvDatos = view.findViewById(R.id.tvDatos);
            imageUser = view.findViewById(R.id.imageUser);
            imageButton = view.findViewById(R.id.imageButton);
            context = view.getContext();
            imageButton.setOnClickListener(v -> {
                DialogBanearUsuario dialogBanearUsuario = new DialogBanearUsuario();
                Bundle args = new Bundle();
                args.putInt("idUsuario", listaUser.get(getAdapterPosition()).getIdUsuario());
                args.putInt("position", getAdapterPosition());
                //args.putString("nombre", listaUser.get(getAdapterPosition()).getUsername());
                dialogBanearUsuario.setArguments(args);
                dialogBanearUsuario.show(((AppCompatActivity) context).getSupportFragmentManager(), "salir");
            });

            view.setOnClickListener(v -> {
                int position = getAdapterPosition();
                myItemClickListener.OnClick(position);
            });
        }
    }

    @NonNull
    @Override
    public RVAdminUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, parent, false);
        return new RVAdminUserAdapter.ViewHolder(view, this.myItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdminUserAdapter.ViewHolder holder, int position) {
        Usuario user = listaUser.get(position);
        holder.tvUser.setText(user.getUsername());
        holder.tvDatos.setText(user.getDescripcion());
        Glide.with(context.getApplicationContext()).load(user.getAvatar()).apply(RequestOptions.circleCropTransform()).into(holder.imageUser);
    }


    @Override
    public int getItemCount() {
        return listaUser.size();
    }

    public void update(int position, int position1) {
        listaUser.remove(position);
        listaOriginal.remove(position1);
        notifyItemRemoved(position);

    }

    public void filtrado(String txtbuscar) {
        int longitud = txtbuscar.length();
        if (longitud == 0) {
            listaUser.clear();
            listaUser.addAll(listaOriginal);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<Usuario> collecion = listaUser.stream().filter(i -> i.getUsername()
                        .toLowerCase().contains(txtbuscar.toLowerCase())).collect(Collectors.toList());
                listaUser.clear();
                listaUser.addAll(collecion);
            } else {
                for (Usuario c : listaOriginal) {
                    if (c.getNombre().toLowerCase().contains(txtbuscar.toLowerCase())) {
                        listaUser.add(c);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}