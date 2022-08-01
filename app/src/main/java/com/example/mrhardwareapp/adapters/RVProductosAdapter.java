package com.example.mrhardwareapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mrhardwareapp.R;
import com.example.mrhardwareapp.models.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RVProductosAdapter extends RecyclerView.Adapter<RVProductosAdapter.ViewHolder> {

    private final Context context;
    public static List<Producto> listaProd;
    private MyItemCLickListener myItemCLickListener;
    private static List<Producto> listaOriginal;

    public void setMyItemCLickListener(MyItemCLickListener myItemCLickListener){
        this.myItemCLickListener = myItemCLickListener;
    }

    public RVProductosAdapter(Context context, List<Producto> listaProd){
        this.context = context;
        RVProductosAdapter.listaProd = listaProd;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listaProd);
    }

    public interface MyItemCLickListener {

        void OnClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProd;
        TextView tvNombreProd;
        TextView tvPrecioProd;
        TextView tvTiendaProd;

        public ViewHolder(@NonNull View view, RVProductosAdapter.MyItemCLickListener myItemCLickListener) {
            super(view);
            imageProd = view.findViewById(R.id.imageProd);
            tvNombreProd = view.findViewById(R.id.tvNombreProd);
            tvPrecioProd = view.findViewById(R.id.tvPrecioProd);
            tvTiendaProd = view.findViewById(R.id.tvTiendaProd);

            view.setOnClickListener(v -> {
                int position = getAdapterPosition();

                myItemCLickListener.OnClick(position);
            });
        }
    }

    @NonNull
    @Override
    public RVProductosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_productos, parent, false);
        return new RVProductosAdapter.ViewHolder(view, this.myItemCLickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull RVProductosAdapter.ViewHolder holder, int position) {
        Producto prod = listaProd.get(position);
        holder.tvNombreProd.setText(prod.getNombre());
        holder.tvPrecioProd.setText(prod.getPrecioConSignos());
        holder.tvTiendaProd.setText(prod.getTiendas_idTiendas().getTienda());
        if(prod.getLink().substring(0).equals("/") ){
            Glide.with(context).load("http:"+prod.getLink()).into(holder.imageProd);
        }else{
            Glide.with(context).load("http:/"+prod.getLink()).into(holder.imageProd);
        }
    }

    @Override
    public int getItemCount() {
        return listaProd.size();
    }

    public void update(int position, int position1){
        listaProd.remove(position);
        listaOriginal.remove(position1);
        notifyItemRemoved(position);
    }

    public void filtrado(String txtbuscarprod){
        int longitud = txtbuscarprod.length();
        if (longitud == 0) {
            listaProd.clear();
            listaProd.addAll(listaOriginal);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N){
                List<Producto> collecion = listaProd.stream().filter(i -> i.getNombre().toLowerCase()
                        .contains(txtbuscarprod.toLowerCase())).collect(Collectors.toList());
                listaProd.clear();
                listaProd.addAll(collecion);
            } else {
                for (Producto c: listaOriginal){
                    if (c.getNombre().toLowerCase().contains(txtbuscarprod.toLowerCase()));
                    listaProd.add(c);
                }
            }
        }
        notifyDataSetChanged();

    }

    public void update(){
        notifyDataSetChanged();
    }
}
