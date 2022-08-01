package com.example.mrhardwareapp.adapters;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mrhardwareapp.MainActivity;
import com.example.mrhardwareapp.PerfilActivity;
import com.example.mrhardwareapp.R;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.models.Pais;
import com.example.mrhardwareapp.models.Usuario;
import com.example.mrhardwareapp.util.RealPathUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogEditarDatos extends DialogFragment {

    final int GALLERY_INTENT = 1;
    File file;
    ImageView imgUser;
//    Este String nos permite darle la ruta en la que se encuentra la imagen del usuario
    String URL = ApiService.IP + "build/imagenesUsuarios/";
    List<String> spinner;
    Spinner spinnerPais;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle args = getArguments();
        assert args != null;
//        Nos traer el objeto usuario que se logreo
        Usuario user = MainActivity.user;
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View design = inflater.inflate(R.layout.dialog_design_edit_datos, null);


        final TextView tvCambiarFoto = design.findViewById(R.id.tvCambiarFoto);
        final EditText txtEditarUsername = design.findViewById(R.id.txtEditarUsername);
        final EditText txtEditarDescripcion = design.findViewById(R.id.txtEditarDescripcion);
        final EditText txtEditarNombre = design.findViewById(R.id.txtEditarNombre);
    /*    spinnerPais = design.findViewById(R.id.spinnerEditarPais);*/
        imgUser = design.findViewById(R.id.imgUser);
        Glide.with(requireContext()).load(URL + user.getAvatar()).apply(RequestOptions.circleCropTransform())
                .error(imgUser).placeholder(R.drawable.freddy).into(imgUser);

//        Al precionar el TextView nos abrira la galeria (para lo que deberemos dar los respectivos permisos) y nos informara en el log si tenemos los permisos o no
        tvCambiarFoto.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permissionCheck = ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    Log.i("Mensaje", "onCreateDialog: No se tiene permiso para leer.");
                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
                } else {
                    Log.i("Mensaje", "Se tiene permiso para leer!");
                }
            }

            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(i, GALLERY_INTENT);

        });
//        Capturamos los detos que ingreso el usuario
        txtEditarUsername.setText(user.getUsername());
        txtEditarDescripcion.setText(user.getDescripcion());
        txtEditarNombre.setText(user.getNombre());


        builder.setView(design)
                .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Call<String> llamadaApi = null;
//                        entrata en este if si NO se encuentra ninguna imagen seleccionada para enviar
                        if (file == null) {
                            user.setUsername(txtEditarUsername.getText().toString());
                            user.setDescripcion(txtEditarDescripcion.getText().toString());
                            user.setNombre(txtEditarNombre.getText().toString());
//                            llenamos la llamada qeu este caso no contendra la imagen
                            llamadaApi = ApiService.getApi().actualizarUserSinFoto(user);

                        } else {
//                            De esta forma solo puede seleccionar una imagen de la galeria
                            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
//                            De esta forma se guarda la imagen en la variable image para enviarla por la llamada con la etiqueta @Multipart
                            MultipartBody.Part image = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

//                            En esta llamada enviamos toda la informacion que querramos actualizar + la imagen
                            llamadaApi = ApiService.getApi().actualizarUser(
                                    createPartFromString(String.valueOf(user.getIdUsuario())),
                                    createPartFromString(txtEditarUsername.getText().toString()),
                                    createPartFromString(txtEditarDescripcion.getText().toString()),
                                    createPartFromString(txtEditarNombre.getText().toString()),
                                    image);
                            user.setUsername(txtEditarUsername.getText().toString());
                            user.setDescripcion(txtEditarDescripcion.getText().toString());
                            user.setNombre(txtEditarNombre.getText().toString());

                        }

                        llamadaApi.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                Log.e("Retrofit Error!!", t.getMessage());
                            }
                        });
//                        Nos vuelve a traer el avatar del usuario que edito su perfil
                        Call<Usuario> llamadaUsuarioApi = ApiService.getApi().autenticar(user.getUsername(), user.getPassword());
                        llamadaUsuarioApi.enqueue(new Callback<Usuario>() {
                            @Override
                            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                                if (response.isSuccessful()) {
                                    Usuario user1 = response.body();
                                    assert user1 != null;
                                    user.setAvatar(user1.getAvatar());
                                }
                            }

                            @Override
                            public void onFailure(Call<Usuario> call, Throwable t) {

                            }
                        });
//                        En caso presionar el boton editar nos enviara al perfil activity
                        Intent i = new Intent(getContext(), PerfilActivity.class);
                        startActivity(i);
                        requireActivity().finish();

                    }
                })
//                Al precionar el boton cancelar en el Dialog nos enviara al perfilActivity
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Objects.requireNonNull(DialogEditarDatos.this.getDialog()).cancel();
                        Intent i = new Intent(getContext(), PerfilActivity.class);
                        startActivity(i);
                        requireActivity().finish();
                    }

                });
        return builder.create();
    }

    public class SpinnerSetupAdapter extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Call<List<Pais>> listapaises = ApiService.getApi().getAllPaises();
            listapaises.enqueue(new Callback<List<Pais>>() {
                @Override
                public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {

                }

                @Override
                public void onFailure(Call<List<Pais>> call, Throwable t) {

                }
            });
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

        @Override
        public void onClick(View view) {

        }
    }

//    Metodo que permite abrir la galeria de fotos para selecionar alguna
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            file = new File(RealPathUtil.getRealPath(getContext(), uri));
            if (file == null)
                file = new File(uri.getPath());

            imgUser.setImageURI(uri);
        }
    }

//    Metodo que permite enviar los datos como String para que la etiqueta @Part los acepte
    private RequestBody createPartFromString(String token) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, token);
    }

    public void llenarSpinnerPais(int pos) {
        spinner = new ArrayList<>();
        Call<List<Pais>> listapais = ApiService.getApi().getAllPaises();
        listapais.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                assert response.body() != null;
                if (response.body().size() > 0){
                    for (Pais pais : response.body()){
                        spinner.add(pais.getNombre());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPais.setAdapter(adapter);
                    spinnerPais.setOnClickListener(new SpinnerSetupAdapter());
                }
            }

            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {

            }
        });
    }

}