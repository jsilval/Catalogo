package co.droidmesa.jsilval.catalogo.interfaces;

import java.util.List;

import co.droidmesa.jsilval.catalogo.models.Catalogo;
import co.droidmesa.jsilval.catalogo.models.Entry;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by jsilval on 28/01/17.
 * interfaz para definir el metodo que se usara la libreria Retrofir 2.0 para obtener los resultados
 */

public interface ApiEndPointInterface {

    // metodo que retorna un objeto Catalogo
    @GET("us/rss/topfreeapplications/limit=20/json")
    Call<Catalogo> getCatalogo();
}
