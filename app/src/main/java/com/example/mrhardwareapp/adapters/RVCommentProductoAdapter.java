package com.example.mrhardwareapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mrhardwareapp.R;
import com.example.mrhardwareapp.api.ApiService;
import com.example.mrhardwareapp.models.Comentario;
import com.example.mrhardwareapp.models.Producto;
import com.example.mrhardwareapp.models.Setup;
import com.example.mrhardwareapp.models.Usuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RVCommentProductoAdapter extends RecyclerView.Adapter<RVCommentProductoAdapter.ViewHolder> {
    //Variables principales del Adapter
    private final Context context;
    public static List<Comentario> listaComentariosProd;
    public static List<Comentario> listaRespuestas;
    public static List<Comentario> listaRespuestaNueva = new ArrayList<>();
    public static int posRespuestaNueva;
    private MyItemCLickListener myItemCLickListener;
    Usuario user;
    Producto prod;


    public void setMyItemClickListener(MyItemCLickListener myItemCLickListener) {
        this.myItemCLickListener = myItemCLickListener;
    }

    //Constructor del Adapter
    public RVCommentProductoAdapter(Context context, List<Comentario> listaComentariosProd, List<Comentario> listaRespuestas, Usuario user, Producto prod) {
        this.context = context;
        RVCommentProductoAdapter.listaComentariosProd = listaComentariosProd;
        RVCommentProductoAdapter.listaRespuestas = listaRespuestas;
        this.user = user;
        this.prod = prod;
    }

    public interface MyItemCLickListener {

        void OnClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {
        //Designamos las variables dentro de layout
        ImageView votarPositivo;
        ImageView imgRespuesta;
        ImageView imgVer;
        TextView tvFechaComment;
        TextView tvUser;
        TextView tvComentario;
        TextView cantidadVotos;
        TextView tvVerRespuestas;
        TextView tvResponder;
        TextView tvEtiqueta;
        LinearLayout containerRespuesta;
        LinearLayout containerVerRespuestas;
        EditText txtRespuesta;
        Button btnResponder;
        CardView cardComentario;
        ImageView imgReport;

        public ViewHolder(@NonNull View view, RVCommentProductoAdapter.MyItemCLickListener myItemCLickListener) {
            super(view);

            //Llenamos las variables con sus respectivas vistas.
            votarPositivo = view.findViewById(R.id.votarPositivo);
            tvFechaComment = view.findViewById(R.id.tvFechaComment);
            imgRespuesta = view.findViewById(R.id.imgRespuesta);
            imgVer = view.findViewById(R.id.imgVer);
            tvUser = view.findViewById(R.id.tvUser);
            tvComentario = view.findViewById(R.id.tvComentario);
            tvVerRespuestas = view.findViewById(R.id.tvVerRespuestas);
            tvEtiqueta = view.findViewById(R.id.tvEtiqueta);
            cantidadVotos = view.findViewById(R.id.cantidadVotos);
            tvResponder = view.findViewById(R.id.tvResponder);
            containerRespuesta = view.findViewById(R.id.containerRespuesta);
            containerVerRespuestas = view.findViewById(R.id.containerVerRespuestas);
            txtRespuesta = view.findViewById(R.id.txtRespuesta);
            btnResponder = view.findViewById(R.id.btnResponder);
            cardComentario = view.findViewById(R.id.cardComentario);
            imgReport = view.findViewById(R.id.imgReport);
            imgReport.setOnClickListener(this::showpopupMenu);
            //Si clickeamos un comentario y este es demasiado extenso entonces se expande las lineas
            //que soporta el TextView
            view.setOnClickListener(v -> {
                int position = getAdapterPosition();
                myItemCLickListener.OnClick(position);
            });
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.reportar) {
                if (user != null) {
                    Bundle b = new Bundle();
                    DialogReportarComentario dialog = new DialogReportarComentario();
                    b.putSerializable("comentario", listaComentariosProd.get(getAdapterPosition()));
                    dialog.setArguments(b);
                    dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "reportarComentario");
                } else {
                    Toast.makeText(context, "Inica tu sesión para reportar", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        }

        private void showpopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_menu_comentario);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }
    }

    @NonNull
    @Override
    public RVCommentProductoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_comentarios_producto, parent, false);
        return new RVCommentProductoAdapter.ViewHolder(view, this.myItemCLickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RVCommentProductoAdapter.ViewHolder holder, int position) {
        //Comentario obtenido a través de la position.
        Comentario comentario = listaComentariosProd.get(position);
        //Se formatea la fecha para que esta se muestre correctamente
        SimpleDateFormat formater = new SimpleDateFormat("dd/mm/yyyy");
        //Rellenamos el comentario con sus datos
        holder.tvFechaComment.setText(formater.format(comentario.getFecha()));
        holder.tvUser.setText(comentario.getId_Usuario().getUsername());
        holder.tvComentario.setText(comentario.getComentario());
        holder.cantidadVotos.setText(String.valueOf(comentario.getVotoPositivo()));

        //Se realiza una validación, este if lo que hace es validar si el comentario es una respuesta o no
        //si es una respuesta se le añadirá el diseño correspondiente

        if (comentario.getIdComentario_Principal() > 0) {
            holder.imgRespuesta.setVisibility(View.VISIBLE);
            holder.containerVerRespuestas.setVisibility(View.GONE);
            for (Comentario com : listaComentariosProd) {
                if (com.getIdComentario() == comentario.getId_Comentario_Respuesta()) {
                    holder.tvEtiqueta.setText("@" + com.getId_Usuario().getUsername());
                    break;
                }
            }
            String text = holder.tvEtiqueta.getText().toString() + "  " + holder.tvComentario.getText().toString();
            Spannable spannablee = new SpannableString(text);
            spannablee.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.blue)), 0, holder.tvEtiqueta.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvComentario.setText(spannablee, TextView.BufferType.SPANNABLE);
        } else {
            holder.imgRespuesta.setVisibility(View.GONE);
            //Se recorre la lista de respuestas para comparar sus idComentarioPrincipal con el id del
            //comentario del position, si existe alguna respuesta que cumpla la condición del if
            //se le añadirá containerRespuesta el cual se encarga de mostrar y ocultar las respuestas
            for (Comentario respuestas : listaRespuestas) {
                if (comentario.getIdComentario() == respuestas.getIdComentario_Principal()) {
                    holder.containerVerRespuestas.setVisibility(View.VISIBLE);
                    break;
                }
            }
            if (listaComentariosProd.get(listaComentariosProd.size() - 1).getIdComentario() != comentario.getIdComentario()) {
                if (listaComentariosProd.get(position + 1).getIdComentario_Principal() > 0) {
                    holder.tvVerRespuestas.setText("Ocultar Respuestas");
                    holder.imgVer.setImageResource(R.drawable.upward_arrow);
                } else {
                    holder.tvVerRespuestas.setText("Ver Respuestas");
                    holder.imgVer.setImageResource(R.drawable.downward_arrow);
                }
            } else {
                holder.tvVerRespuestas.setText("Ver Respuestas");
                holder.imgVer.setImageResource(R.drawable.downward_arrow);
            }
        }

        //Si nadie ha iniciado sesión entonces esta validación no se realiza
        if (user != null) {
            //Llamada que devuelve un true si el comentario ha sido votado por el usuario.
            Call<Boolean> isVoted = ApiService.getApi().getIsVoted(user.getIdUsuario(), comentario.getIdComentario());
            isVoted.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body()) {
                            //Si el comentario ha sido votado se le agruega un a imagen acorde a la situación
                            //y un contentDescription que indica que ha sido votado, este contentDescription se utilizará
                            //para validar
                            holder.votarPositivo.setImageResource(R.drawable.arrow_up__1_);
                            holder.votarPositivo.setContentDescription("Votado");
                        } else {
                            //Si no ha sido votado se le añaden parámetros acorde
                            holder.votarPositivo.setImageResource(R.drawable.arrow_up);
                            holder.votarPositivo.setContentDescription("noVotado");

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {

                }
            });
        }
        //Este setOnClickListener añade un voto al comentario o lo quita si esque ya está votado
        holder.votarPositivo.setOnClickListener(v -> {
            //Solo puede responder si ha iniciado sesión,
            if (user != null) {
                //Validación para saber si el comentario ha sido votado o no.
                if (holder.votarPositivo.getContentDescription().toString().equals("noVotado")) {
                    //Esta llamada añade un voto al comentario
                    Call<Boolean> votar = ApiService.getApi().darVotoPositivo(comentario.getIdComentario(), user.getIdUsuario());
                    votar.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                            if (response.isSuccessful()) {

                                //Se cambia el contentDescription y la imagen
                                holder.votarPositivo.setImageResource(R.drawable.arrow_up__1_);
                                holder.votarPositivo.setContentDescription("Votado");

                                //Se suma un voto de forma visual para que la acción se vea reflejada inmediatamente
                                int sumarVoto = Integer.parseInt(holder.cantidadVotos.getText().toString());
                                holder.cantidadVotos.setText(String.valueOf(sumarVoto + 1));
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                        }
                    });
                } else {
                    //Esta llamada quita un voto del comentario
                    Call<Boolean> quitarVoto = ApiService.getApi().quitarVoto(comentario.getIdComentario(), user.getIdUsuario());
                    quitarVoto.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                            if (response.isSuccessful()) {
                                //Se cambia el contentDescription y la imagen
                                holder.votarPositivo.setImageResource(R.drawable.arrow_up);
                                holder.votarPositivo.setContentDescription("noVotado");
                                //Se resta un voto de forma visual para que la acción se vea reflejada inmediatamente
                                int restarVoto = Integer.parseInt(holder.cantidadVotos.getText().toString());
                                holder.cantidadVotos.setText(String.valueOf(restarVoto - 1));
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                        }
                    });
                }
            } else {
                Toast.makeText(context.getApplicationContext(), "Debes iniciar sesión para realizar esta acción", Toast.LENGTH_SHORT).show();
            }
        });
        //Este setOnClickListener abre containerRespuesta, que es un LinearLayout que contiene
        //un EditText y un Button
        holder.tvResponder.setOnClickListener(v -> {
            if (holder.containerRespuesta.getVisibility() == View.VISIBLE) {
                holder.containerRespuesta.setVisibility(View.GONE);
            } else {
                holder.containerRespuesta.setVisibility(View.VISIBLE);
                holder.txtRespuesta.requestFocus();
                ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        });

        //La accion de este Button hace una Call para insertar una respuesta a un comentario en la base de datos.
        holder.btnResponder.setOnClickListener(v -> {
            //Solo puede responder si ha iniciado sesión,
            if (user != null) {
                if (Setup.vacios(holder.txtRespuesta.getText().toString()).equals("")) {
                    Toast.makeText(context.getApplicationContext(), "El campo está vacío", Toast.LENGTH_SHORT).show();
                } else {
                    listaRespuestaNueva = new ArrayList<>();
                    //Se obtiene el idComentarioPrincial si es que se responde a una respuesta, si no es asi
                    //es porque este comentario es un comentario principal, entonces obtenemos su id
                    int idComentarioPrincipal = comentario.getIdComentario_Principal();
                    if (idComentarioPrincipal == 0) {
                        idComentarioPrincipal = comentario.getIdComentario();
                    }
                    //Se hace la llamada
                    Call<Comentario> comentarioCall = ApiService.getApi().insertarComentarioRespuesta(holder.txtRespuesta.getText().toString(), prod.getIdProducto(), user.getIdUsuario(), comentario.getIdComentario(), idComentarioPrincipal);
                    comentarioCall.enqueue(new Callback<Comentario>() {
                        @Override
                        public void onResponse(@NonNull Call<Comentario> call, @NonNull Response<Comentario> response) {
                            if (response.isSuccessful()) {
                                //si la llamada se cummple se formatea el campo donde se comenta y se esconde el containerRespuesta.
                                holder.txtRespuesta.setText("");
                                holder.containerRespuesta.setVisibility(View.GONE);
                                listaRespuestas.add(response.body());
                                listaComentariosProd.add(holder.getAdapterPosition() + 1, response.body());
                                notifyItemInserted(holder.getAdapterPosition() + 1);
                                listaRespuestaNueva.add(response.body());
                                posRespuestaNueva = holder.getAdapterPosition() + 1;
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Comentario> call, @NonNull Throwable t) {
                        }
                    });
                }
            } else {
                Toast.makeText(context.getApplicationContext(), "Debes iniciar tu sesión para responder", Toast.LENGTH_SHORT).show();
            }
        });
        //Esta accion muestra las respuestas a los comentarios si estos están ocultos y los oculta si estan mostrandose
        holder.containerVerRespuestas.setOnClickListener(v -> {
            if (listaRespuestaNueva.size() > 0) {
                listaComentariosProd.remove(posRespuestaNueva);
                notifyItemRemoved(posRespuestaNueva);
                listaRespuestaNueva.remove(0);
            }
            //Si el TextView al que se le hace el setOnClickListener tiene este texto entonces mostrará las respuestas
            if (String.valueOf(holder.tvVerRespuestas.getText()).equals("Ver Respuestas")) {
                holder.tvVerRespuestas.setText("Ocultar Respuestas");
                holder.imgVer.setImageResource(R.drawable.upward_arrow);
                //Se cambia el texto y la imagen del TextView que realiza la acción
                //Las respuestas se muestran insertandolas en la lista principal, es decir, litaComentariosProd
                for (Comentario respuesta : listaRespuestas) {
                    if (respuesta.getIdComentario_Principal() == comentario.getIdComentario()) {
                        listaComentariosProd.add(position + 1, respuesta);
                        notifyItemInserted(position + 1);
                    }

                }
                //Si están mostrandose las respuestas, se ocultarán.
            } else if (String.valueOf(holder.tvVerRespuestas.getText()).equals("Ocultar Respuestas")) {

                holder.tvVerRespuestas.setText("Ver Respuestas");
                holder.imgVer.setImageResource(R.drawable.downward_arrow);
                //Se recorre la lista Principal
                for (int j = 0; j < listaComentariosProd.size(); j++) {
                    Comentario comentario1 = listaComentariosProd.get(j);
                    //Si el idComentarioPrincipal del objeto de la lista es el mismo que el
                    //objeto actual(el obtenido a través del position), entonces lo removerá,
                    //esta comparación es para que oculte solo las respuestas del comentario actual.
                    if (comentario1.getIdComentario_Principal() == comentario.getIdComentario()) {
                        listaComentariosProd.remove(j);
                        notifyItemRemoved(j);
                        //Si no quitamos una posición, dejará algunas respuestas debido a
                        //que los indices de la lista disminuyen al remover un item de la lista.
                        j--;

                    }
                }
            }

        });
    }

    //Metodo para obtener el tamaño de la listaPrincipal
    @Override
    public int getItemCount() {
        return listaComentariosProd.size();
    }

    //Metodo para actualizar el recycler view
    public void update() {
        notifyDataSetChanged();
    }

}
