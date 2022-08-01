package com.example.mrhardwareapp.adapters;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mrhardwareapp.R;
import com.example.mrhardwareapp.models.Comentario;

import java.text.SimpleDateFormat;
import java.util.List;

public class RVPerfilCommentAdapter extends RecyclerView.Adapter<RVPerfilCommentAdapter.ViewHolder> {
    private final Context context;
    public static List<Comentario> listaComentariosPerfil;
    private MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public RVPerfilCommentAdapter(Context context, List<Comentario> listaComentarios) {
        this.context = context;
        RVPerfilCommentAdapter.listaComentariosPerfil = listaComentarios;
    }

    public interface MyItemClickListener {
        void OnClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        private static final String TAG = "MyViewHolder";
        TextView tvUser;
        TextView tvComentario;
        TextView tvFechaComment;
        ImageView imageComment;
        ImageView imageMenu;
        Context context;

        public ViewHolder(@NonNull View view, RVPerfilCommentAdapter.MyItemClickListener myItemClickListener) {
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
            popupMenu.inflate(R.menu.popup_menu_perfil);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.eliminar) {
                DialogEliminarComentarioPerfil dialogo = new DialogEliminarComentarioPerfil();
                Bundle args = new Bundle();
                args.putInt("idComentario", listaComentariosPerfil.get(getAdapterPosition()).getIdComentario());
                dialogo.setArguments(args);
                dialogo.show(((AppCompatActivity) context).getSupportFragmentManager(), "salir");

                return true;
            } else if (item.getItemId() == R.id.editar) {
                DialogEditarComentario dialog = new DialogEditarComentario();
                Bundle args =  new Bundle();
                args.putSerializable("comentario", listaComentariosPerfil.get(getAdapterPosition()));
                args.putInt("position", getAdapterPosition());
                dialog.setArguments(args);
                dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "salir");
            }
            return false;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_comentarios, parent, false);
        return new RVPerfilCommentAdapter.ViewHolder(view, this.myItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comentario comentario = listaComentariosPerfil.get(position);

        SimpleDateFormat formater = new SimpleDateFormat("dd/mm/yyyy");
        holder.tvFechaComment.setText(formater.format(comentario.getFecha()));
        holder.tvUser.setText(comentario.getId_Usuario().getUsername());
        holder.tvComentario.setText(comentario.getComentario());

    }


    @Override
    public int getItemCount() {
        return listaComentariosPerfil.size();
    }

    public void update(int position) {
        listaComentariosPerfil.remove(position);
        notifyItemRemoved(position);
    }
    public void updateC(int position) {
        notifyItemChanged(position);

    }


}