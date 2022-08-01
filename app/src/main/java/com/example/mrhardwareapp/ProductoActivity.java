package com.example.mrhardwareapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.mrhardwareapp.adapters.DialogAgregarProductoASetup;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.fragments.ProductoCommentFragment;
import com.example.mrhardwareapp.fragments.ProductoRelacionadoFragment;
import com.example.mrhardwareapp.models.Detalle;
import com.example.mrhardwareapp.models.Producto;
import com.example.mrhardwareapp.models.Usuario;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoActivity extends AppCompatActivity {

    //Variables del layout
    TextView txtNombreProdSelec;
    TextView txtPrecioSelec;
    TextView txtTiendaSelec;
    TextView txtVistasSelec;
    TextView tvdetalleVacio;
    ImageView imageProdSelec;
    ImageView home;
    TextView titulo;
    Toolbar toolbarProd;
    Usuario user;
    Button btnVerProducto;
    ImageView btnAddToSetup;
    LinearLayout layoutContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        if (MainActivity.user != null) {
            user = MainActivity.user;
        }
        //Recibimos el producto enviado desde InicioClienteActivity o el mismo ProductoActivity
        Producto prod = (Producto) getIntent().getSerializableExtra("prod");


        //Llenamos las variables con sus respectivas vistas.
        txtNombreProdSelec = findViewById(R.id.txtNombreProdSelec);
        txtPrecioSelec = findViewById(R.id.txtPrecioSelec);
        tvdetalleVacio = findViewById(R.id.tvdetalleVacio);
        txtTiendaSelec = findViewById(R.id.txtTiendaSelec);
        txtVistasSelec = findViewById(R.id.txtVistasSelec);
        layoutContainer = findViewById(R.id.layoutContainer);
        imageProdSelec = findViewById(R.id.imageProdSelec);
        toolbarProd = findViewById(R.id.toolbarPerfil);
        home = findViewById(R.id.home);
        titulo = findViewById(R.id.titulo);
        btnVerProducto = findViewById(R.id.btnVerProducto);
        btnAddToSetup = findViewById(R.id.btnAddToSetup);

        //Designamos nuestra toolbar como la ActionBar que se usará
        setSupportActionBar(toolbarProd);

        //Este setOnClickListener redirige a InicioClienteActivity
        home.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), InicioClienteActivity.class);
            i.putExtra("user", user);
            startActivity(i);
        });
        //Rellenamos los TextView con los datos del producto
        txtNombreProdSelec.setText(prod.getNombre());
        txtPrecioSelec.setText(prod.getPrecioConSignos());
        txtTiendaSelec.setText(prod.getTiendas_idTiendas().getTienda());
        txtVistasSelec.setText(String.valueOf(prod.getVistas()));

        //Validación para mostrar correctamente las imagenes de los productos.
        if (prod.getLink().substring(0).equals("/")) {
            Glide.with(this).load("http:" + prod.getLink()).into(imageProdSelec);
        } else {
            Glide.with(this).load("http:/" + prod.getLink()).into(imageProdSelec);
        }

        //Este setOnClickListener redirige a la página web de la tienda oficial del producto
        //y añade una vista al producto.
        btnVerProducto.setOnClickListener(v -> {
            Call<Boolean> llamadaSumar = ApiService.getApi().sumarVista(prod.getVistas(), prod.getIdProducto());
            llamadaSumar.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                    if  (response.isSuccessful()){
                        //Sumamos una vista de manera visual
                        InicioClienteActivity.prod.setVistas(prod.getVistas()+1);
                        prod.setVistas(prod.getVistas()+1);
                        txtVistasSelec.setText(String.valueOf(prod.getVistas()));
                        //Estas lineas redirigen a la página oficial
                        Uri uri = Uri.parse(prod.getLinkTienda());
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {

                }
            });


        });
        //Esta llama nos trae los detalles del producto.
        Call<Detalle> detalleCall = ApiService.getApi().productoSelec(prod.getDetalle_idDetalle());
        detalleCall.enqueue(new Callback<Detalle>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Detalle> call, @NonNull Response<Detalle> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Detalle detalle = response.body();
                        //Creamos un arreglo de String para guardar los datos y poder recorrerlos
                        ArrayList<String> datos = new ArrayList<>();
                        datos.add(detalle.getCaracteristica());
                        datos.add(detalle.getCaracteristica2());
                        datos.add(detalle.getCaracteristica3());
                        datos.add(detalle.getCaracteristica4());
                        datos.add(detalle.getCaracteristica5());
                        datos.add(detalle.getCaracteristica6());
                        datos.add(detalle.getCaracteristica7());
                        datos.add(detalle.getCaracteristica8());
                        datos.add(detalle.getCaracteristica9());
                        datos.add(detalle.getCaracteristica10());
                        datos.add(detalle.getCaracteristica11());
                        datos.add(detalle.getCaracteristica12());
                        //Recorremos la lista
                        for (String dato : datos) {
                            //Creamos un LinearLayout que tendra dentro un TextView con los datos
                            //Se crea un LinearLayout por cada recorrido del for
                            LinearLayout layoutRow = new LinearLayout(getApplicationContext());
                            layoutRow.setOrientation(LinearLayout.HORIZONTAL);
                            layoutRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                            TextView tvNombreDetalle = new TextView(getApplicationContext());
                            tvNombreDetalle.setTextAppearance(getApplicationContext(), R.style.nombreDetalle);
                            //Este if valida que si lo que se recibe no es un dato válido, entonces lo indique
                            if (dato.equals("0") || dato.equals("''")) {
                                tvNombreDetalle.setText(getString(R.string.sinDetalle));
                                layoutRow.addView(tvNombreDetalle);
                            } else {
                                //Separamos el String para poder tener el nombre del detalle y el detalle mismo
                                String[] datoDividido = dato.split(",");
                                TextView tvDetalle = new TextView(getApplicationContext());
                                tvDetalle.setTextAppearance(getApplicationContext(), R.style.detalle);
                                tvDetalle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                                //Se rellena el TextView que contiene el detalle
                                //y se valida si este contien más de una coma.
                                tvDetalle.setText(" " + getString(R.string.detalle, datoDividido[1]));
                                if (datoDividido.length > 2) {
                                    tvDetalle.setText(" " + getString(R.string.detalle1, datoDividido[1], datoDividido[2]));
                                }
                                //Se rellena el TextView que contiene el nombre del detalle
                                tvNombreDetalle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                                tvNombreDetalle.setText(getString(R.string.nombreDetalle, datoDividido[0]));

                                layoutRow.addView(tvNombreDetalle);
                                layoutRow.addView(tvDetalle);

                            }
                            layoutContainer.addView(layoutRow);
                        }
                    } else {
                        //Si no existen detalles entonces se mostrará un TextView indicandolo
                        tvdetalleVacio.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<Detalle> call, @NonNull Throwable t) {
                Log.e("Retrofit Error!!", t.getMessage());
            }
        });
        //Este Bundle Contiene los objetos que se le enviarán a los fragments
        Bundle b = new Bundle();
        b.putSerializable("prod", prod);
        b.putSerializable("user", user);
        //Estos fragments fueron creados para que en esta clase no alla excesivo código
        ProductoCommentFragment comentariosFragment = new ProductoCommentFragment();
        ProductoRelacionadoFragment relacionadoFragment = new ProductoRelacionadoFragment();

        comentariosFragment.setArguments(b);

        relacionadoFragment.setArguments(b);

        //Añadimos los fragments a sus frameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameComment, comentariosFragment)
                .commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameProdRelacion, relacionadoFragment)
                .commit();

        btnAddToSetup.setOnClickListener(v -> {
            if (user != null){
                Bundle bundle = new Bundle();
                DialogAgregarProductoASetup dialog = new DialogAgregarProductoASetup();
                bundle.putSerializable("prod", prod);
                bundle.putInt("pos", 0);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "AddProdToSetup");

            }else{
                Toast.makeText(this, "Inicia sesión para editar tu setup", Toast.LENGTH_SHORT).show();
            }
    });
    }
    //Este método crea el menú de opciones.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.usuario_menu, menu);
        return true;
    }
    //Este método indica la accion de los items del menú de opciones.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Este item redirige al PerfilActivity si el usuario está logeado, si no lo está redirige al inicio de sesión.
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

    //Configuramos el onBackPressed() para finalizar la acividad actual y evitar un bucle
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}