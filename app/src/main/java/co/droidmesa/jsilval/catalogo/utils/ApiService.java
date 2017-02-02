package co.droidmesa.jsilval.catalogo.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import java.util.List;

import co.droidmesa.jsilval.catalogo.Categorias;
import co.droidmesa.jsilval.catalogo.R;
import co.droidmesa.jsilval.catalogo.constants.Constants;
import co.droidmesa.jsilval.catalogo.interfaces.ApiEndPointInterface;
import co.droidmesa.jsilval.catalogo.models.Catalogo;
import co.droidmesa.jsilval.catalogo.models.Entry;
import co.droidmesa.jsilval.catalogo.offlinemode.cache.DBHelper;
import co.droidmesa.jsilval.catalogo.offlinemode.cache.DBOperations;
import co.droidmesa.jsilval.catalogo.offlinemode.receiver.NetworkChangeReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jsilval on 29/01/17.
 */

public class ApiService {
//    public static List<Entry> list_entry = null;
//    private boolean isNetworkAvailable;
    private static ApiService service = null;

    private ApiService() {
    }

    public static ApiService getInstance() {
        if (service == null)
            service = new ApiService();
        return service;
    }

//    public boolean isNetworkAvailable() {
//        return isNetworkAvailable;
//    }

    public void sendRequest(Callback<Catalogo> callback) {
        View view = ((Activity) callback).findViewById(R.id.toolbar);
        receiveNetworkStatus((Context) callback, view);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPointInterface apiService = retrofit.create(ApiEndPointInterface.class);

        Call<Catalogo> call = apiService.getCatalogo();
        call.enqueue(callback);
    }

    public void createCache(List<Entry> list_entry, Context context) {
        DBOperations dbOperations = new DBOperations(context);
        int i = 1;
        ContentValues values = new ContentValues();
        for (Entry entry : list_entry) {
            values.put(DBHelper.C_ID, i++);
            values.put(DBHelper.C_NAME, entry.getImName().getLabel());
            values.put(DBHelper.C_SUMMARY, entry.getSummary().getLabel());
            values.put(DBHelper.C_PRICE, entry.getImPrice().getAttributes().getAmount());
            values.put(DBHelper.C_TITLE, entry.getTitle().getLabel());
            values.put(DBHelper.C_IMAGE50, entry.getImImage().get(0).getLabel());
            values.put(DBHelper.C_IMAGE75, entry.getImImage().get(1).getLabel());
            values.put(DBHelper.C_IMAGE100, entry.getImImage().get(2).getLabel());
            values.put(DBHelper.C_CATEGORY, entry.getCategory().getAttributes().getLabel());
            dbOperations.insertEntry(values, DBHelper.ENTRY_TABLE);
        }
    }

    private void receiveNetworkStatus(Context context, final View view) {
        IntentFilter filter = new IntentFilter(NetworkChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Categorias.isNetworkAvailable = intent.getBooleanExtra(NetworkChangeReceiver.IS_NETWORK_AVAILABLE, false);
                String networkStatus;

                if (Categorias.isNetworkAvailable) {
                    networkStatus = "connected";
                } else{
                    networkStatus =  "disconnected, using cache version";
                }
                Snackbar.make(view, "Network Status: " + networkStatus, Snackbar.LENGTH_LONG).show();
            }
        }, filter);
    }
}
