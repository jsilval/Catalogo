package co.droidmesa.jsilval.catalogo.interfaces;

import java.util.List;

import co.droidmesa.jsilval.catalogo.models.Catalogo;
import co.droidmesa.jsilval.catalogo.models.Entry;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by jsilval on 28/01/17.
 */

public interface ApiEndPointInterface {

    @GET("us/rss/topfreeapplications/limit=20/json")
    Call<Catalogo> getCatalogo();
}
