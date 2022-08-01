package com.example.mrhardwareapp.api;

import com.example.mrhardwareapp.models.Comentario;
import com.example.mrhardwareapp.models.Detalle;
import com.example.mrhardwareapp.models.Pais;
import com.example.mrhardwareapp.models.Producto;
import com.example.mrhardwareapp.models.Reportes;
import com.example.mrhardwareapp.models.Setup;
import com.example.mrhardwareapp.models.Usuario;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {

    @GET("autenticarApi.php")
    Call<Usuario> autenticar(@Query("user") String user, @Query("pass") String pass);

    //    Con esta llamada nos traemos todos los usuarios de la base de datos
    @GET("usuarioApi.php")
    Call<List<Usuario>> getAllUsers();

    @GET("usuarioApi.php")
    Call<List<Usuario>> validacionRegistro(@Query("emailUser") String emailUser, @Query("username") String username);

    //    Esta llamada nos traera todos los comentarios
    @GET("comentarioApi.php")
    Call<List<Comentario>> getAllComments();

    //    Esta llamada nos traera algunos comentarios especificos
    @GET("comentarioApi.php")
    Call<List<Comentario>> getAllComments(@Query("idComentario") int idComentario);

    //    Esta llamada con traera todos los reportes
    @GET("reporteApi.php")
    Call<List<Reportes>> getAllReports();

    //    Con esta llamada enviamos el id del usuario que banearemos
    @POST("usuarioApi.php")
    Call<Boolean> banear(@Query("id") int id);

    //    La etiqueta Multipart nos permite enviar los datos como un formulario (lo nos permite enviar una imagen)
    @Multipart
    @POST("usuarioApii.php")
    Call<String> actualizarUser(@Part("id") RequestBody id,
                                @Part("nombre") RequestBody nombre,
                                @Part("descripcion") RequestBody desc,
                                @Part("real") RequestBody real,
                                @Part MultipartBody.Part file);

    //    Con la etiqueta PUT enviamos los datos por body de la actualizacion de un usuario (en caso de que no edite su foto)
    @PUT("usuarioApii.php")
    Call<String> actualizarUserSinFoto(@Body Usuario user);

    //    Esta llamada envia el ID del comentario que se baneara
    @PUT("comentarioApi.php")
    Call<Boolean> banearComentario(@Query("idComentario") int idComentario);

    //    Esta llamada envia los datos del cliente que se registrara (enviando el: email, username y su contraseña)
    @PUT("usuarioApi.php")
    Call<Boolean> registrarCliente(@Query("email") String email, @Query("username") String username, @Query("password") String password);

    //    Esta llamada enviara los datos de un comentario
    @PUT("autenticarApi.php")
    Call<Boolean> enviarCorreo(@Query("comentario") String comentario, @Query("email") String email, @Query("nombreUsuario") String nombreUsuario);

    //    Con esta llamada nos traermos todos los productos
    @GET("productoApi.php")
    Call<List<Producto>> getAllProductos();

    //    Esta llamada con permite editar un comentario enviando la ID de este
    @PUT("comentarioApi.php")
    Call<Boolean> editarComentario(@Query("idComentario") int idComentario, @Query("comentario") String comentario);

    //    llamada que nos permite traernos un tipo de producto
    @GET("productoApi.php")
    Call<List<Producto>> filtrarProductosTipoProd(@Query("id_TipoProducto") int tipoProducto);

    //    llamada que nos trae los porductos ordenados de diferentes formas
    @GET("productoApi.php")
    Call<List<Producto>> filtrarProductosOrden(@Query("orden") String orden);

    //    llamada que nos trae un tipo de producto y al mismo tiempo ordenada
    @GET("productoApi.php")
    Call<List<Producto>> filtrarProductosTipoProdYOrden(@Query("id_TipoProducto") int tipoProducto, @Query("orden") String orden);

    @GET("productoApi.php")
    Call<List<Producto>> filtrarProdTiendas(@Query("idTienda") int idTienda);

    @GET("productoApi.php")
    Call<List<Producto>> filtrarProdTiendasYOrden(@Query("idTienda") int idTienda, @Query("orden") String orden);

    @GET("productoApi.php")
    Call<List<Producto>> filtrarProdTiendasYOrdenYTipoProd(@Query("idTienda") int idTienda, @Query("id_TipoProducto") int tipoProducto, @Query("orden") String orden);

    @GET("productoApi.php")
    Call<List<Producto>> filtrarProdTiendasYTipoProd(@Query("idTienda") int idTienda, @Query("id_TipoProducto") int tipoProducto);

    @GET("productoApi.php")
    Call<Producto> obtenerProductoById(@Query("idProducto") int idProducto);

    //    llamada que nos trae los detalles de el prodcuto con la ID que enviamos
    @GET("detalleApi.php")
    Call<Detalle> productoSelec(@Query("productoSelec") int productoSelec);

    //    llamada que trae los comentarios de el producto con la ID que enviamos
    @GET("comentarioApi.php")
    Call<List<Comentario>> getProductoComments(@Query("idProductoSelec") int idProductoSelec);

    //    Esta llamada por GET la aplicacmos a la URL llamada setupApi.php para traer los datos de los setup de la ID del usuario que le enviamos
    @GET("setupApi.php")
    Call<List<Setup>> getUserSetups(@Query("idUsuario") int idUsuario);

    @GET("setupApi.php")
    Call<Setup> getSetupByName(@Query("nombreSetup") String nombre);

    @GET("meGustaApi.php")
    Call<Boolean> getIsVoted(@Query("idUsuario") int idUsuario, @Query("idComentario") int idComentario);

    @POST("meGustaApi.php")
    Call<Boolean> darVotoPositivo(@Query("idComentario") int idComentario, @Query("idUsuario") int idUsuario);

    @PUT("meGustaApi.php")
    Call<Boolean> quitarVoto(@Query("idComentario") int idComentario, @Query("idUsuario") int idUsuario);

    //    Llamada que envia el comentario con la ID del producto comentado y el usuario que lo realizo
    @POST("comentarioApi.php")
    Call<Comentario> insertarComentario(@Query("comentario") String comentario, @Query("idProducto") int idProducto, @Query("idUsuario") int idUsuario);

    //    Llamada que envia la respuesta de un comentario con la ID del producto en el que se esta respondiendo y la ID del usuario que esta respondiendo y la ID del comentario que se esta respondiendo
    @POST("comentarioApi.php")
    Call<Comentario> insertarComentarioRespuesta(@Query("comentario") String comentario, @Query("idProducto") int idProducto, @Query("idUsuario") int idUsuario, @Query("idComentario") int idComentario, @Query("idComentarioPrincipal") int idComentarioPrincipal);

    //    Llamada que envia +1 vista en el producto que se encuentra seleccionado
    @POST("productoApi.php")
    Call<Boolean> sumarVista(@Query("sumaVista") int sumaVista, @Query("idProducto") int idProducto);

    @POST("setupApi.php")
    Call<Boolean> insertarSetup(@Query("idUsuario") int idUsuario, @Query("nombreSetup") String nombreSetup);

    @POST("setupApi.php")
    Call<Boolean> removerSetup(@Query("idSetup") int idSetup, @Query("idUsuarioToRemove") int idUsuario);

    @PUT("setupApi.php")
    Call<Boolean> insertarProdASetup(@Query("idSetup") int idSetup, @Query("idProducto") int idProducto);

    @PUT("setupApi.php")
    Call<Boolean> quitarProdASetup(@Query("idSetup") int idSetup, @Query("idProductoToRemove") int idProducto);

    @GET("paisApi.php")
    Call<List<Pais>> getAllPaises();

    @POST("reporteApi.php")
    Call<Boolean> insertarReporte(@Query("idUsuario") int idUsuario, @Query("idTipoReporte") int idTipoReporte, @Query("idComentario") int idComentario);

}
