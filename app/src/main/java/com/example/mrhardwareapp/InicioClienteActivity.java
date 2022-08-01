package com.example.mrhardwareapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mrhardwareapp.adapters.RVProductosAdapter;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.models.Producto;
import com.example.mrhardwareapp.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioClienteActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, PopupMenu.OnMenuItemClickListener {

    SearchView txtbuscarProd;
    Toolbar toolbar;
    RecyclerView recyclerProductos;
    RVProductosAdapter adapter;
    TextView mensajeVacio;
    ImageView imgFiltrar;
    ImageView home;
    TextView titulo;
    TextView todas;
    TextView spDigital;
    TextView pcFactory;
    PopupMenu popupMenu;
    Usuario user;
    String tipoLista;
    String tipoOrden;
    int tipoTipoProd;
    public static Producto prod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_cliente);
        if (MainActivity.user != null) {
            user = MainActivity.user;
        }

        recyclerProductos = findViewById(R.id.recyclerProductos);
        mensajeVacio = findViewById(R.id.mensajeVacio);
        txtbuscarProd = findViewById(R.id.txtbuscarProd);
        toolbar = findViewById(R.id.toolbarPerfil);
        imgFiltrar = findViewById(R.id.imgFiltrar);
        home = findViewById(R.id.home);
        titulo = findViewById(R.id.titulo);
        todas = findViewById(R.id.todas);
        spDigital = findViewById(R.id.spDigital);
        pcFactory = findViewById(R.id.pcFactory);

        //Método para llamar al RVProductos sin filtros
        listaLimpia();

        setSupportActionBar(toolbar);
//        Search que permite bucar el producto por su nombre(o una porsion de el)
        txtbuscarProd.setOnQueryTextListener(this);
        titulo.setText(R.string.inicio);

        popupMenu = new PopupMenu(this, imgFiltrar);
        popupMenu.inflate(R.menu.popup_menu_filtrar);
        popupMenu.setOnMenuItemClickListener(this);
        imgFiltrar.setOnClickListener(v -> {
            popupMenu.show();

        });

        todas.setOnClickListener(v -> {
            colorFiltroTienda(todas);
            switch (tipoLista) {
                case "limpia":
                case "tienda":
                    listaLimpia();
                    break;
                case "tiendaYProd":
                case "tipoProd":
                    filtrarTipoProd(tipoTipoProd);
                    break;
                case "tiendaYProdYOrden":
                case "tipoProdYOrden":
                    filtrarTipoProdYOrden(tipoTipoProd, tipoOrden);
                    break;
                case "tiendaYOrden":
                case "orden":
                    filtrarOrdenProd(tipoOrden);
                    break;
            }
        });
        spDigital.setOnClickListener(v -> {
            colorFiltroTienda(spDigital);
            switch (tipoLista) {
                case "limpia":
                case "tienda":
                    filtrarPorTienda(1);
                    break;
                case "tiendaYProd":
                case "tipoProd":
                    filtrarPorTiendaYTipoProd(1, tipoTipoProd);
                    break;
                case "tiendaYProdYOrden":
                case "tipoProdYOrden":
                    filtrarPorTiendaYTipoProdYOrden(1, tipoTipoProd, tipoOrden);
                    break;
                case "tiendaYOrden":
                case "orden":
                    filtrarPorTiendaYOrden(1, tipoOrden);
                    break;
            }
        });
        pcFactory.setOnClickListener(v -> {
            colorFiltroTienda(pcFactory);
            switch (tipoLista) {
                case "limpia":
                case "tienda":
                    filtrarPorTienda(2);
                    break;
                case "tiendaYProd":
                case "tipoProd":
                    filtrarPorTiendaYTipoProd(2, tipoTipoProd);
                    break;
                case "tiendaYProdYOrden":
                case "tipoProdYOrden":
                    filtrarPorTiendaYTipoProdYOrden(2, tipoTipoProd, tipoOrden);
                    break;
                case "tiendaYOrden":
                case "orden":
                    filtrarPorTiendaYOrden(2, tipoOrden);
                    break;
            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.usuario_menu, menu);
        return true;
    }

    //    Como la aplicacion muestra esta activity de inicio si, el usuario no tiene su sesion iniciada,
//    lo redirijira a al MainActivity para que inicie su sesion o se registre
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.perfil) {

            Intent i;
            if (user != null) {
                i = new Intent(this, PerfilActivity.class);
                i.putExtra("user", user);
            } else {
                i = new Intent(this, MainActivity.class);
            }
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (adapter != null)
            adapter.filtrado(newText);
        mensajeVacio();
        return false;
    }

    public void mensajeVacio() {
        if (recyclerProductos.getAdapter() != null) {
            if (recyclerProductos.getAdapter().getItemCount() == 0) {
                mensajeVacio.setVisibility(View.VISIBLE);
            } else if (recyclerProductos.getAdapter().getItemCount() > 0) {
                mensajeVacio.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    //    Metodo que Selecciona el filtro
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getGroupId() == R.id.hardware) {
            switch (tipoLista) {
                case "limpia":
                case "tipoProd":
                    filtrarTipoProd(Integer.parseInt(String.valueOf(item.getTitleCondensed())));
                    break;
                case "tiendaYProd":
                case "tienda":
                    filtrarPorTiendaYTipoProd(RVProductosAdapter.listaProd.get(0).getTiendas_idTiendas().getIdTiendas(), Integer.parseInt(String.valueOf(item.getTitleCondensed())));
                    break;
                case "tipoProdYOrden":
                case "orden":
                    filtrarTipoProdYOrden(Integer.parseInt(String.valueOf(item.getTitleCondensed())), tipoOrden);
                    break;
                case "tiendaYProdYOrden":
                case "tiendaYOrden":
                    filtrarPorTiendaYTipoProdYOrden(RVProductosAdapter.listaProd.get(0).getTiendas_idTiendas().getIdTiendas(), Integer.parseInt(String.valueOf(item.getTitleCondensed())), tipoOrden);
                    break;
            }
            item.setChecked(true);
            return true;
        } else if (item.getGroupId() == R.id.periferico) {
            switch (tipoLista) {
                case "limpia":
                case "tipoProd":
                    filtrarTipoProd(Integer.parseInt(String.valueOf(item.getTitleCondensed())));
                    break;
                case "tiendaYProd":
                case "tienda":
                    filtrarPorTiendaYTipoProd(RVProductosAdapter.listaProd.get(0).getTiendas_idTiendas().getIdTiendas(), Integer.parseInt(String.valueOf(item.getTitleCondensed())));
                    break;
                case "tipoProdYOrden":
                case "orden":
                    filtrarTipoProdYOrden(Integer.parseInt(String.valueOf(item.getTitleCondensed())), tipoOrden);
                    break;
                case "tiendaYProdYOrden":
                case "tiendaYOrden":
                    filtrarPorTiendaYTipoProdYOrden(RVProductosAdapter.listaProd.get(0).getTiendas_idTiendas().getIdTiendas(), Integer.parseInt(String.valueOf(item.getTitleCondensed())), tipoOrden);
                    break;
            }
            item.setChecked(true);
            return true;
        } else if (item.getGroupId() == R.id.AscDesc) {
            switch (tipoLista) {
                case "limpia":
                case "orden":
                    filtrarOrdenProd(String.valueOf(item.getTitleCondensed()));
                    break;
                case "tiendaYOrden":
                case "tienda":
                    filtrarPorTiendaYOrden(RVProductosAdapter.listaProd.get(0).getTiendas_idTiendas().getIdTiendas(), String.valueOf(item.getTitleCondensed()));
                    break;
                case "tiendaYProdYOrden":
                case "tiendaYProd":
                    filtrarPorTiendaYTipoProdYOrden(RVProductosAdapter.listaProd.get(0).getTiendas_idTiendas().getIdTiendas(), RVProductosAdapter.listaProd.get(0).getId_TipoProducto(), String.valueOf(item.getTitleCondensed()));
                    break;
                case "tipoProdYOrden":
                case "tipoProd":
                    filtrarTipoProdYOrden(RVProductosAdapter.listaProd.get(0).getId_TipoProducto(), String.valueOf(item.getTitleCondensed()));
                    break;
            }
            item.setChecked(true);
            return true;

        } else if (item.getItemId() == R.id.limpiar) {
            listaLimpia();
            colorFiltroTienda(todas);
            popupMenu = new PopupMenu(this, imgFiltrar);
            popupMenu.inflate(R.menu.popup_menu_filtrar);
            popupMenu.setOnMenuItemClickListener(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //    Metodo que permite filtrar los productos y ordenarlos
    public void filtrarTipoProdYOrden(int tipoProd, String orden) {
        Call<List<Producto>> listaProdFiltrada = ApiService.getApi().filtrarProductosTipoProdYOrden(tipoProd, orden);
        listaProdFiltrada.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Producto>> call, @NonNull Response<List<Producto>> response) {
                if (response.isSuccessful()) {
                    adapter = new RVProductosAdapter(getApplicationContext(), response.body());
                    recyclerProductos.setAdapter(adapter);
                    recyclerProductos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    recyclerProductos.setHasFixedSize(true);
                    adapter.setMyItemCLickListener(position -> {
                        Intent i = new Intent(getApplicationContext(), ProductoActivity.class);
                        prod = RVProductosAdapter.listaProd.get(position);
                        i.putExtra("prod", prod);
                        startActivity(i);
                    });
                    mensajeVacio();
                    tipoLista = "tipoProdYOrden";
                    tipoOrden = orden;
                    tipoTipoProd = tipoProd;
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Producto>> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
    }

    //    Metodo que permite filtar los productos por tipo
    public void filtrarTipoProd(int tipoProd) {
        Call<List<Producto>> listaProdFiltrada = ApiService.getApi().filtrarProductosTipoProd(tipoProd);
        listaProdFiltrada.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Producto>> call, @NonNull Response<List<Producto>> response) {
                if (response.isSuccessful()) {
                    adapter = new RVProductosAdapter(getApplicationContext(), response.body());
                    recyclerProductos.setAdapter(adapter);
                    recyclerProductos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    recyclerProductos.setHasFixedSize(true);
                    adapter.setMyItemCLickListener(position -> {
                        Intent i = new Intent(getApplicationContext(), ProductoActivity.class);
                        prod = RVProductosAdapter.listaProd.get(position);
                        i.putExtra("prod", prod);
                        startActivity(i);
                    });
                    mensajeVacio();
                    tipoLista = "tipoProd";
                    tipoTipoProd = tipoProd;
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Producto>> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
    }

    //    Metodo que permite odenar los prodcutos
    public void filtrarOrdenProd(String orden) {
        Call<List<Producto>> listaProdFiltrada = ApiService.getApi().filtrarProductosOrden(orden);
        listaProdFiltrada.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Producto>> call, @NonNull Response<List<Producto>> response) {
                if (response.isSuccessful()) {
                    adapter = new RVProductosAdapter(getApplicationContext(), response.body());
                    recyclerProductos.setAdapter(adapter);
                    recyclerProductos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    recyclerProductos.setHasFixedSize(true);
                    adapter.setMyItemCLickListener(position -> {
                        Intent i = new Intent(getApplicationContext(), ProductoActivity.class);
                        prod = RVProductosAdapter.listaProd.get(position);
                        i.putExtra("prod", prod);
                        startActivity(i);
                    });
                    mensajeVacio();
                    tipoLista = "orden";
                    tipoOrden = orden;
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Producto>> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
    }

    //    Metodo que permite odenar los productos por tienda
    public void filtrarPorTienda(int idTienda) {
        Call<List<Producto>> listaProdFiltrada = ApiService.getApi().filtrarProdTiendas(idTienda);
        listaProdFiltrada.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Producto>> call, @NonNull Response<List<Producto>> response) {
                if (response.isSuccessful()) {
                    adapter = new RVProductosAdapter(getApplicationContext(), response.body());
                    recyclerProductos.setAdapter(adapter);
                    recyclerProductos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    recyclerProductos.setHasFixedSize(true);
                    adapter.setMyItemCLickListener(position -> {
                        Intent i = new Intent(getApplicationContext(), ProductoActivity.class);
                        prod = RVProductosAdapter.listaProd.get(position);
                        i.putExtra("prod", prod);
                        startActivity(i);
                    });
                    mensajeVacio();
                    tipoLista = "tienda";
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Producto>> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
    }

    public void filtrarPorTiendaYOrden(int idTienda, String orden) {
        Call<List<Producto>> listaProdFiltrada = ApiService.getApi().filtrarProdTiendasYOrden(idTienda, orden);
        listaProdFiltrada.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Producto>> call, @NonNull Response<List<Producto>> response) {
                if (response.isSuccessful()) {
                    adapter = new RVProductosAdapter(getApplicationContext(), response.body());
                    recyclerProductos.setAdapter(adapter);
                    recyclerProductos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    recyclerProductos.setHasFixedSize(true);
                    adapter.setMyItemCLickListener(position -> {
                        Intent i = new Intent(getApplicationContext(), ProductoActivity.class);
                        prod = RVProductosAdapter.listaProd.get(position);
                        i.putExtra("prod", prod);
                        startActivity(i);
                    });
                    mensajeVacio();
                    tipoLista = "tiendaYOrden";
                    tipoOrden = orden;
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Producto>> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
    }

    public void filtrarPorTiendaYTipoProd(int idTienda, int tipoProd) {
        Call<List<Producto>> listaProdFiltrada = ApiService.getApi().filtrarProdTiendasYTipoProd(idTienda, tipoProd);
        listaProdFiltrada.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Producto>> call, @NonNull Response<List<Producto>> response) {
                if (response.isSuccessful()) {
                    adapter = new RVProductosAdapter(getApplicationContext(), response.body());
                    recyclerProductos.setAdapter(adapter);
                    recyclerProductos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    recyclerProductos.setHasFixedSize(true);
                    adapter.setMyItemCLickListener(position -> {
                        Intent i = new Intent(getApplicationContext(), ProductoActivity.class);
                        prod = RVProductosAdapter.listaProd.get(position);
                        i.putExtra("prod", prod);
                        startActivity(i);
                    });
                    mensajeVacio();
                    tipoLista = "tiendaYProd";
                    tipoTipoProd = tipoProd;
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Producto>> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
    }

    public void filtrarPorTiendaYTipoProdYOrden(int idTienda, int tipoProd, String orden) {
        Call<List<Producto>> listaProdFiltrada = ApiService.getApi().filtrarProdTiendasYOrdenYTipoProd(idTienda, tipoProd, orden);
        listaProdFiltrada.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Producto>> call, @NonNull Response<List<Producto>> response) {
                if (response.isSuccessful()) {
                    adapter = new RVProductosAdapter(getApplicationContext(), response.body());
                    recyclerProductos.setAdapter(adapter);
                    recyclerProductos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    recyclerProductos.setHasFixedSize(true);
                    adapter.setMyItemCLickListener(position -> {
                        Intent i = new Intent(getApplicationContext(), ProductoActivity.class);
                        prod = RVProductosAdapter.listaProd.get(position);
                        i.putExtra("prod", prod);
                        startActivity(i);
                    });
                    mensajeVacio();
                    tipoLista = "tiendaYProdYOrden";
                    tipoOrden = orden;
                    tipoTipoProd = tipoProd;
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Producto>> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
    }


    //    Metodo que permite eliminar los filtros
    public void listaLimpia() {
        Call<List<Producto>> listaProd = ApiService.getApi().getAllProductos();
        listaProd.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Producto>> call, @NonNull Response<List<Producto>> response) {
                if (response.isSuccessful()) {
                    adapter = new RVProductosAdapter(getApplicationContext(), response.body());
                    recyclerProductos.setAdapter(adapter);
                    recyclerProductos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    recyclerProductos.setHasFixedSize(true);
                    adapter.setMyItemCLickListener(position -> {
                        Intent i = new Intent(getApplicationContext(), ProductoActivity.class);
                        prod = RVProductosAdapter.listaProd.get(position);
                        i.putExtra("prod", prod);
                        startActivity(i);
                    });
                    mensajeVacio();
                    tipoLista = "limpia";
                }else{
                    tipoLista = "fallo";
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Producto>> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
    }

    public void colorFiltroTienda(TextView tv) {
        if (tv == todas) {
            todas.setBackgroundColor(getResources().getColor(R.color.colorPrincipal));
            spDigital.setBackgroundColor(getResources().getColor(R.color.white));
            pcFactory.setBackgroundColor(getResources().getColor(R.color.white));
            todas.setTextColor(getResources().getColor(R.color.white));
            spDigital.setTextColor(getResources().getColor(R.color.black));
            pcFactory.setTextColor(getResources().getColor(R.color.black));
        } else if (tv == spDigital) {
            todas.setBackgroundColor(getResources().getColor(R.color.white));
            spDigital.setBackgroundColor(getResources().getColor(R.color.colorPrincipal));
            spDigital.setTextColor(getResources().getColor(R.color.white));
            todas.setTextColor(getResources().getColor(R.color.black));
            pcFactory.setTextColor(getResources().getColor(R.color.black));
            pcFactory.setBackgroundColor(getResources().getColor(R.color.white));
        } else {
            todas.setBackgroundColor(getResources().getColor(R.color.white));
            spDigital.setBackgroundColor(getResources().getColor(R.color.white));
            pcFactory.setBackgroundColor(getResources().getColor(R.color.colorPrincipal));
            pcFactory.setTextColor(getResources().getColor(R.color.white));
            todas.setTextColor(getResources().getColor(R.color.black));
            spDigital.setTextColor(getResources().getColor(R.color.black));
        }
    }
}