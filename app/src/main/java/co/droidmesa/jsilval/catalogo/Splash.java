package co.droidmesa.jsilval.catalogo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import co.droidmesa.jsilval.catalogo.utils.SetUpActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Splash extends AppCompatActivity {

    private static final int UI_ANIMATION_DELAY = 300;
    private static final int DELAY = 3000;
    private final Handler mHideHandler = new Handler();
    private View mContentView;

    // Runnable que ocultara la barra de estado y navegacion
    private final Runnable mHideSystemUI = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private final Runnable mFinishRunnable = new Runnable() {
        @Override
        public void run() {
           fadeTransition();
        }
    };

    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SetUpActivity.setOrientation(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = true;
        mContentView = findViewById(R.id.flSplash);
        initAnimation();
        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        mHideHandler.postDelayed(mFinishRunnable, DELAY);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Ocultar UI despues que la activity haya sido creada.
        delayedHide(10);
    }

    /**
     * Ocultar o mostrar
     */
    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    /**
     * Agendar el runnabe que ocultara las barra de estado y navegacion
     */
    private void hide() {
        mVisible = false;
        mHideHandler.postDelayed(mHideSystemUI, UI_ANIMATION_DELAY);
    }

    /**
     * Mostrar las barras estado y navegacion
     */
    @SuppressLint("InlinedApi")
    private void show() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Cancelar cualquier llamada anterior de mHideSystemUI
        // y nuevamnete  ocultar la barra de estado y navegacion
        mHideHandler.removeCallbacks(mHideSystemUI);
        mHideHandler.postDelayed(mHideSystemUI, UI_ANIMATION_DELAY);
    }

    /**
     * Cancela cualquier agendada que tenga el runnable mHideRunnable
     * y agenda una llamada a al metodo hide() con un retardo en milisegundos.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    /**
     * Pasar a la sigiente actividad.
     */
    public void fadeTransition() {
        Intent i = new Intent(Splash.this, Categorias.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
        finish();
    }


    private void initAnimation() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        mContentView.clearAnimation();
        mContentView.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        TextView tv = (TextView) findViewById(R.id.tvSplash);
        tv.clearAnimation();
        tv.startAnimation(anim);
    }
}
