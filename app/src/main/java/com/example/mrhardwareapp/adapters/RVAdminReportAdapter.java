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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mrhardwareapp.R;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.models.Reportes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RVAdminReportAdapter extends RecyclerView.Adapter<RVAdminReportAdapter.ViewHolder> {
    private final Context context;
    public static List<Reportes> listaReportes;
    private MyItemClickListener myItemClickListener;
    public static List<Reportes> listaOriginal;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public RVAdminReportAdapter(Context context, List<Reportes> listaReportes) {
        this.context = context;
        listaOriginal = new ArrayList<>();
        RVAdminReportAdapter.listaReportes = listaReportes;
        listaOriginal.addAll(listaReportes);
    }

    public interface MyItemClickListener {
        void OnClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        TextView tvUser;
        TextView tvDatos;
        ImageView imageUser;
        ImageView imageMenu;
        Context context;
        ImageView iconReport;
        TextView nReport;

        public ViewHolder(@NonNull View view, RVAdminReportAdapter.MyItemClickListener myItemClickListener) {
            super(view);
            tvUser = view.findViewById(R.id.tvUser);
            tvDatos = view.findViewById(R.id.tvDatos);
            imageUser = view.findViewById(R.id.imageUser);
            imageMenu = view.findViewById(R.id.imageMenu);
            iconReport = view.findViewById(R.id.iconReport);
            nReport = view.findViewById(R.id.nReport);
            context = view.getContext();
            imageMenu.setOnClickListener(this);

            view.setOnClickListener(v -> {
                int position = getAdapterPosition();
                myItemClickListener.OnClick(position);
                if (tvDatos.getMaxLines() == 1) {
                    tvDatos.setMaxLines(10);
                } else if (tvDatos.getMaxLines() == 10) {
                    tvDatos.setMaxLines(1);
                }
            });
        }

        @Override
        public void onClick(View view) {
            showpopupMenu(view);
        }

        private void showpopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(this);
            //popupMenu.getMenu().getItem(2).setVisible(true);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.banear) {
                DialogBanearComentario dialogo = new DialogBanearComentario();
                Bundle args = new Bundle();
                args.putInt("idComentario", listaReportes.get(getAdapterPosition()).getId_Comentario().getIdComentario());
                args.putInt("destino", 2);
                args.putInt("position", getAdapterPosition());
                dialogo.setArguments(args);
                dialogo.show(((AppCompatActivity) context).getSupportFragmentManager(), "salir");
                return true;
            } else if (item.getItemId() == R.id.advertencia) {
                Call<Boolean> llamadaApi = ApiService.getApi().enviarCorreo(listaReportes.get(getAdapterPosition()).getId_Comentario().getComentario(), listaReportes.get(getAdapterPosition()).getId_Comentario().getId_Usuario().getEmail(), listaReportes.get(getAdapterPosition()).getId_Usuario().getUsername());
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
                return true;
            }
            return false;
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_reportes, parent, false);
        return new RVAdminReportAdapter.ViewHolder(view, this.myItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reportes reportes = listaReportes.get(position);

        holder.tvUser.setText(reportes.getId_Comentario().getId_Usuario().getUsername());
        holder.tvDatos.setText(reportes.getId_Comentario().getComentario());
        holder.nReport.setText(String.valueOf(reportes.getId_Comentario().getCantidad_Reportes()));
    }


    @Override
    public int getItemCount() {
        return listaReportes.size();
    }

    public void update(int position, int position1) {
        listaReportes.remove(position);
        listaOriginal.remove(position1);
        notifyItemRemoved(position);
    }

    public void filtrado(String txtbuscarR) {
        int longitud = txtbuscarR.length();
        if (longitud == 0) {
            listaReportes.clear();
            listaReportes.addAll(listaOriginal);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Reportes> collecion = listaReportes.stream().filter(i -> i.getId_Comentario().getId_Usuario().getUsername()
                        .toLowerCase().contains(txtbuscarR.toLowerCase())).collect(Collectors.toList());
                listaReportes.clear();
                listaReportes.addAll(collecion);
            } else {
                for (Reportes c : listaOriginal) {
                    if (c.getId_Usuario().getUsername().toLowerCase().contains(txtbuscarR.toLowerCase())) {
                        listaReportes.add(c);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
