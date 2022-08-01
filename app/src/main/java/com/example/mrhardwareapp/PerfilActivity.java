package com.example.mrhardwareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mrhardwareapp.adapters.DialogEditarDatos;
import com.example.mrhardwareapp.adapters.DialogSalir;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.fragments.ComentariosPerfilFragment;
import com.example.mrhardwareapp.fragments.SetupsFragment;
import com.example.mrhardwareapp.models.Usuario;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {

    //Variables del layout
    TextView txtUsername;
    TextView txtDescripcion;
    TextView txtNombre;
    TextView txtInsta;
    TextView txtEmail;
    TextView likes;
    Toolbar toolbarPerfil;
    ImageView userAvatar;
    TabLayout tabLayout;
    ViewPager2 viewPagerPerfil;
    FragmentStateAdapter pageAdapter;
    ArrayList<Fragment> fragments = new ArrayList<>();
    ImageView home;
    ImageView editarDatos;
    //Obtenemos el usuario logeado
    Usuario user = MainActivity.user;
    //Url que nos permite mostrar las imagenes
    String URL = ApiService.IP + "build/imagenesUsuarios/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        userAvatar = findViewById(R.id.userAvatar);
        //Llamada que actualiza el usuario, esta llamada se hizo para actualizar la imagen,
        //ya que no puede ser actualizada de forma visual o inmediata.
        Call<Usuario> llamadaUsuarioApi = ApiService.getApi().autenticar(user.getUsername(), user.getPassword());
        llamadaUsuarioApi.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    user.setAvatar(response.body().getAvatar());
                    Glide.with(getApplicationContext()).load(URL+response.body().getAvatar())
                            .apply(RequestOptions.circleCropTransform()).into(userAvatar);
                    mostrarDatos();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {

            }
        });

        //Llenamos las variables con sus respectivas vistas.
        txtUsername = findViewById(R.id.txtUsername);
        likes = findViewById(R.id.likes);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtNombre = findViewById(R.id.txtNombre);
        txtInsta = findViewById(R.id.txtInsta);
        txtEmail = findViewById(R.id.txtEmail);
        toolbarPerfil = findViewById(R.id.toolbarPerfil);
        viewPagerPerfil = findViewById(R.id.viewPagerPerfil);
        home = findViewById(R.id.home);
        editarDatos = findViewById(R.id.editarDatos);
        tabLayout = findViewById(R.id.tabLayoutPerfil);
        setSupportActionBar(toolbarPerfil);
        txtUsername.setText(user.getUsername());
        likes.setText(String.valueOf(user.getMeGusta()));
        //Se añaden los fragments al ViewPager
        fragments.add(new ComentariosPerfilFragment());
        fragments.add(new SetupsFragment());
        //Le integramos el ScreenSlidePagerAdapter al ViewPAger
        pageAdapter = new ScreenSlidePagerAdapter(this);
        viewPagerPerfil.setAdapter(pageAdapter);
        //Este metodo muestra la cantidad de fragments dentro del ViewPager
        new TabLayoutMediator(tabLayout, viewPagerPerfil,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Comentarios");
                            break;
                        case 1:
                            tab.setText("Setups");
                            break;
                    }
                }
        ).attach();
        //Este setOnClickListener redirige a InicioClienteActivity
        home.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), InicioClienteActivity.class);
            i.putExtra("user", user);
            startActivity(i);
        });
        //Este setOnClickListener abre un dialog el cual se encarga de editar los datos del usuario.
        editarDatos.setOnClickListener(v -> {
            Bundle b = new Bundle();
            DialogEditarDatos dialogEditarDatos = new DialogEditarDatos();
            b.putSerializable("user", user);
            dialogEditarDatos.setArguments(b);
            dialogEditarDatos.show(this.getSupportFragmentManager(), "editarDatos");

        });

            Glide.with(getApplicationContext()).load(URL + user.getAvatar())
                    .apply(RequestOptions.circleCropTransform()).into(userAvatar);

        mostrarDatos();
    }
    //Este método crea el menú de opciones.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.perfil_menu, menu);
        return true;
    }

    //Este método indica la accion de los items del menú de opciones.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Este item abre un dialog el cual se encarga de cerrar la sesión del usuario.
        if (item.getItemId() == R.id.salir) {
            DialogSalir dialogo = new DialogSalir();
            dialogo.show(this.getSupportFragmentManager(), "salir");
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    //Adapter del ViewPager el cual encargado del slide
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);

        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }
    //Método para que retorna los fragmentos, se utiliza en otras clases.
    public ArrayList<Fragment> obtenerFragments() {
        return fragments;
    }

    public void actualizarFragments() {
        int position = viewPagerPerfil.getCurrentItem();
        fragments.remove(0);
        fragments.remove(0);
        fragments.add(new ComentariosPerfilFragment());
        fragments.add(new SetupsFragment());
        pageAdapter = new ScreenSlidePagerAdapter(this);
        viewPagerPerfil.setPageTransformer(new AdministrarActivity.ZoomOutPageTransformer());
        viewPagerPerfil.setAdapter(pageAdapter);
        viewPagerPerfil.setCurrentItem(position);
    }
    //Método que muestra los datos del usuario, si el usuario no tiene este dato entonces oculta el TextView
    public void mostrarDatos() {
        if (user.getDescripcion().equals("")) {
            txtDescripcion.setVisibility(View.GONE);
        } else {
            txtDescripcion.setText(user.getDescripcion());
        }
        if (user.getNombre().equals("")) {
            txtNombre.setVisibility(View.GONE);
        } else {
            txtNombre.setText(user.getNombre());
        }
        if (user.getInstagram().equals("")) {
            txtInsta.setVisibility(View.GONE);
        } else {
            txtInsta.setText(user.getInstagram());
        }
        if (user.getEmail().equals("")) {
            txtEmail.setVisibility(View.GONE);
        } else {
            txtEmail.setText(user.getEmail());
        }
    }

}