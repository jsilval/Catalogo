package co.droidmesa.jsilval.catalogo;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.droidmesa.jsilval.catalogo.adapters.ListAppAdapter;
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

public class AppList extends AppCompatActivity implements View.OnClickListener, Callback<Catalogo> {
    RecyclerView rvListApp;
    private List<Entry> list_entry;
    private static String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SetUpActivity.setOrientation(this);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvListApp = (RecyclerView)findViewById(R.id.rvListApp);
        rvListApp.addItemDecoration(new RecyclerDecoration(this));
        rvListApp.setHasFixedSize(true);
        rvListApp.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if (getIntent().getExtras() != null)
            category = getIntent().getExtras().getString(Constants.NAME_ITEM);
        getSupportActionBar().setTitle(category);

        ApiService.getInstance().sendRequest(this);

        SetUpActivity.setupWindowAnimation(this, Constants.APPLIST_ACTIVITY);
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

    @Override
    public void finish() {
        super.finish();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    }

    public void viewDetailActivity(View v) {
        Intent i = new Intent(AppList.this, DetailActivity.class);
        TextView tv = (TextView)v.findViewById(R.id.tvTitle);
        i.putExtra(Constants.NAME_ITEM, tv.getText().toString());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(i, options.toBundle());
        } else {
            startActivity(i);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
    }

    private ArrayList<Entry> getAppsInCategory(String category) {
        ArrayList<Entry> apps = new ArrayList<>();
        Log.d("asd", Integer.toString(list_entry.size()));

        for (Entry entry : list_entry) {
            if (entry.getCategory().getAttributes().getLabel().equals(category)) {
                apps.add(entry);
            }
        }
        return apps;
    }

    private void addItemsToAppsList(ArrayList<Entry> set) {
        ListAppAdapter mAdapter = new ListAppAdapter(set, this);
        rvListApp.setAdapter(mAdapter);
        mAdapter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        viewDetailActivity(v);
    }

    @Override
    public void onResponse(Call<Catalogo> call, Response<Catalogo> response) {
        list_entry = response.body().getFeed().getEntry();
        ArrayList<Entry> apps = getAppsInCategory(category);
        addItemsToAppsList(apps);
    }

    @Override
    public void onFailure(Call<Catalogo> call, Throwable t) {
        Log.d("onFailure", t.toString());
        DBOperations dbOperations = new DBOperations(getBaseContext());
        list_entry = dbOperations.getEntryFromCache();
        ArrayList<Entry> apps = getAppsInCategory(category);
        addItemsToAppsList(apps);
    }
}
