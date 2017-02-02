package co.droidmesa.jsilval.catalogo;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.droidmesa.jsilval.catalogo.adapters.ListCategoryAdapter;
import co.droidmesa.jsilval.catalogo.constants.Constants;
import co.droidmesa.jsilval.catalogo.models.Catalogo;
import co.droidmesa.jsilval.catalogo.models.Entry;
import co.droidmesa.jsilval.catalogo.offlinemode.cache.DBOperations;
import co.droidmesa.jsilval.catalogo.utils.ApiService;
import co.droidmesa.jsilval.catalogo.utils.RecyclerDecoration;
import co.droidmesa.jsilval.catalogo.utils.SetUpActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Vista que muestra las diferentes categorias
 */

public class Categorias extends AppCompatActivity implements View.OnClickListener, Callback<Catalogo> {
    private RecyclerView rvCategories;
    private List<Entry> list_entry;                 // lista de aplicaciones
    public static boolean isNetworkAvailable;       // inidcador del estado de la red
    public static boolean portrait;                 // inidicador de la orientacion del telefono o tablet
                                                    // usado para definir el layout de esta vista (menu estilo grid o menu vertical)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // determinar la orientacion de la actividad
        portrait = SetUpActivity.setOrientation(this);

        rvCategories = (RecyclerView)findViewById(R.id.rvCategory);
        rvCategories.addItemDecoration(new RecyclerDecoration(this));
        rvCategories.setHasFixedSize(true);

        // definir el estilo del menu
        if (portrait)
            rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        else
            rvCategories.setLayoutManager(new GridLayoutManager(this, 3));

        // enviar peticion al servidor
        ApiService.getInstance().sendRequest(this);
        // definir animaciones para esta actividad (para versiones >= android 21)
        SetUpActivity.setupWindowAnimation(this, Constants.CATEGORY_ACTIVITY);
    }

    /**
     * Obtener todas las categrorias, descartando las que esten duplicadas dentro de list_entry
     * @return lista de categorias disponible.
     */
    private ArrayList<String> getAllCategories() {
        Set<String> tmp_set =  new HashSet<>();
        for (Entry entry : list_entry)
            tmp_set.add(entry.getCategory().getAttributes().getLabel());

        return new ArrayList<>(tmp_set);
    }

    /**
     * Añadir la lista de categorias al adaptador que sera puesto dentro del recyclerview
     * @param set lista de categorias
     */
    private void addItemsToCategories(ArrayList<String> set) {
        ListCategoryAdapter mAdapter = new ListCategoryAdapter(set);
        rvCategories.setAdapter(mAdapter);
        mAdapter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        viewAppListActivity(v);
    }

    /**
     * Manejar la respuesta del servidor y obtener los atributos del JSON.
     * Añadir elementos a la lista.
     * @param call
     * @param response respuesta del servidor
     */
    @Override
    public void onResponse(Call<Catalogo> call, Response<Catalogo> response) {
        list_entry = response.body().getFeed().getEntry();
        ArrayList<String> categories = getAllCategories();
        addItemsToCategories(categories);
        ApiService.getInstance().createCache(list_entry, this);
    }

    /**
     * Usar el cache si no hay respuesta del servidor.
     * Añadir elementos a la lista.
     * @param call
     * @param t
     */
    @Override
    public void onFailure(Call<Catalogo> call, Throwable t) {
        Log.d("onFailure", t.toString());
        DBOperations dbOperations = new DBOperations(getBaseContext());
        list_entry = dbOperations.getEntryFromCache();

        ArrayList<String> categories = getAllCategories();
        addItemsToCategories(categories);
    }
    /**
     * Dispara la vista que muestra las aplicaciones que hacen parte de una categoria
     * @param v
     */
    private void viewAppListActivity(View v) {
        TextView tv = (TextView) v.findViewById(R.id.tvCategory);
        Intent i = new Intent(Categorias.this, AppList.class);
        i.putExtra(Constants.NAME_ITEM, tv.getText().toString());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(i, options.toBundle());
        } else {
            startActivity(i);
        }
    }

    /**
     * Sobre escribir la animacion  de salida de la aplicacion, para versiones menores a android 21
     * @param intent
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
    }
}
