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

import java.util.List;

public class RVProductosRelacionadosAdapter extends RecyclerView.Adapter<RVProductosRelacionadosAdapter.ViewHolder> {

    private final Context context;
    public static List<Producto> listaProd;
    private MyItemCLickListener myItemCLickListener;


    public void setMyItemCLickListener(MyItemCLickListener myItemCLickListener){
        this.myItemCLickListener = myItemCLickListener;
    }

    public RVProductosRelacionadosAdapter(Context context, List<Producto> listaProd){
        this.context = context;
        RVProductosRelacionadosAdapter.listaProd = listaProd;
    }

    public interface MyItemCLickListener {

        void OnClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProd;
        TextView tvNombreProd;
        TextView tvPrecioProd;
        TextView tvTiendaProd;

        public ViewHolder(@NonNull View view, RVProductosRelacionadosAdapter.MyItemCLickListener myItemCLickListener) {
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
    public RVProductosRelacionadosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_productos, parent, false);
        return new RVProductosRelacionadosAdapter.ViewHolder(view, this.myItemCLickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull RVProductosRelacionadosAdapter.ViewHolder holder, int position) {
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

}
