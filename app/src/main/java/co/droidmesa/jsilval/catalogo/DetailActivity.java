package co.droidmesa.jsilval.catalogo;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.droidmesa.jsilval.catalogo.constants.Constants;
import co.droidmesa.jsilval.catalogo.models.Catalogo;
import co.droidmesa.jsilval.catalogo.models.Entry;
import co.droidmesa.jsilval.catalogo.offlinemode.cache.DBOperations;
import co.droidmesa.jsilval.catalogo.utils.ApiService;
import co.droidmesa.jsilval.catalogo.utils.SetUpActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity que muestra el detalle de cada item seleccionado de una lista
 */
public class DetailActivity extends AppCompatActivity implements  Callback<Catalogo> {
    private List<Entry> list_entry;     // lista de aplicaciones
    private String app_name;            // nombre de la app que mostrara los detalles
    TextView tvTitle, tvSummary, tvPrice, tvCategory;
    ImageView imgApp;
    ProgressBar progressBar;
    Entry app = null;                   // para guardar la app que corresponde a la seleccionada para mostrar detalles.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // determinar la orientacion de la actividad
        SetUpActivity.setOrientation(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Downloading...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        app_name = getIntent().getExtras().getString(Constants.NAME_ITEM);
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvSummary = (TextView)findViewById(R.id.tvSummary);
        tvPrice = (TextView)findViewById(R.id.tvPriceDetail);
        tvCategory = (TextView)findViewById(R.id.tvCategory);
        imgApp = (ImageView) findViewById(R.id.imgApp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // enviar peticion al servidor
        ApiService.getInstance().sendRequest(this);
        // definir animaciones para esta actividad (para versiones >= android 21)
        SetUpActivity.setupWindowAnimation(this, Constants.DETAIL_ACTIVITY);
    }

    /**
     * Obtener los detalles de la aplicacion seleccionada
     * @param name
     */
    private void getDetailsApps(String name) {
        for (Entry entry : list_entry) {
            if (entry.getImName().getLabel().equals(name)) {
                app = entry;
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
        return true;
    }

    // Sobreescribir la animacion cuando se ejecuta el metodo finish() solo cuando la version de android es < 21
    @Override
    public void finish() {
        super.finish();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    }


    /**
     * Manejar la respuesta del servidor, obtener los datos necesarios para mostrar en los detalles de la aplicacion
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call<Catalogo> call, Response<Catalogo> response) {
        list_entry = response.body().getFeed().getEntry();
        getDetailsApps(app_name);
        if (app != null) {
            tvTitle.setText(app.getTitle().getLabel());
            tvPrice.setText("Price: " + app.getImPrice().getAttributes().getAmount() + " " + app.getImPrice().getAttributes().getCurrency());
            tvSummary.setText(app.getSummary().getLabel());
            tvCategory.setText(app.getCategory().getAttributes().getLabel());

            // Cargar la imagen dentro del imageview usando la libreria Picasso
            Picasso.with(this).load(app.getImImage().get(2).getLabel()).into(imgApp, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                    SetUpActivity.setAnimation(imgApp);
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    /**
     * Usar la cache para mostrar los detalles de la aplicacion si el servidor no responde o no hay conexion a internet
     * @param call
     * @param t
     */
    @Override
    public void onFailure(Call<Catalogo> call, Throwable t) {
        Log.d("onFailure", t.toString());
        DBOperations dbOperations = new DBOperations(getBaseContext());
        list_entry = dbOperations.getEntryFromCache();
        Log.d("OlO", String.valueOf(list_entry.get(0).getImImage().size()));

        getDetailsApps(app_name);

        if (app != null) {
            tvTitle.setText(app.getTitle().getLabel());
            tvPrice.setText("Price: " + app.getImPrice().getAttributes().getAmount() + " " + app.getImPrice().getAttributes().getCurrency());
            tvSummary.setText(app.getSummary().getLabel());
            tvCategory.setText(app.getCategory().getAttributes().getLabel());

            Picasso.with(this).load(app.getImImage().get(2).getLabel()).networkPolicy(NetworkPolicy.OFFLINE).into(imgApp, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                    SetUpActivity.setAnimation(imgApp);
                }

                @Override
                public void onError() {
                    Picasso.with(getBaseContext()).load(app.getImImage().get(2).getLabel()).into(imgApp, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                            SetUpActivity.setAnimation(imgApp);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
            });
        }
    }
}
