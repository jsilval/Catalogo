package co.droidmesa.jsilval.catalogo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.ScaleAnimation;

import java.util.Random;

import co.droidmesa.jsilval.catalogo.R;
import co.droidmesa.jsilval.catalogo.constants.Constants;

import static co.droidmesa.jsilval.catalogo.constants.Constants.*;

/**
 * Created by jsilval on 1/02/17.
 * Clase que define metodos para configurar el aspecto visual de la aplicación
 */

public class SetUpActivity {

    /**
     * Determina la orientación de la pantalla segun el tipo de dispositivo
     * @param activity actividad actual
     * @return
     */
    public static boolean setOrientation(Activity activity) {
        if(activity.getResources().getBoolean(R.bool.portrait_only)){
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            return false;
        }
    }

    /**
     * Configura las transiciones de salida, entrada y reentrada de las actividades
     * @param activity  actividad.
     * @param name  nombre de la actividad.
     */
    public static void setupWindowAnimation(Activity activity, String name) {
        switch (name) {
            case CATEGORY_ACTIVITY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide slideTransition = new Slide();

                    slideTransition.setSlideEdge(Gravity.START); // Use START if using right - to - left locale
                    slideTransition.setDuration(activity.getResources().getInteger(R.integer.anim_duration_medium));

                    activity.getWindow().setReenterTransition(slideTransition);  // When MainActivity Re-enter the Screen
                    activity.getWindow().setExitTransition(slideTransition);     // When MainActivity Exits the Screen

                    // For overlap of Re Entering Activity - MainActivity.java and Exiting TransitionActivity.java
                    activity.getWindow().setAllowReturnTransitionOverlap(false);
                }
                break;
            case APPLIST_ACTIVITY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide enterTransition = new Slide();

                    enterTransition.setSlideEdge(Gravity.TOP);
                    enterTransition.setDuration(activity.getResources().getInteger(R.integer.anim_duration_very_long));

                    enterTransition.setInterpolator(new AnticipateOvershootInterpolator());
                    activity.getWindow().setEnterTransition(enterTransition);    // cuando la actividad entra
                    activity.getWindow().setReenterTransition(enterTransition);  // cuando la actividad reentra
                    activity.getWindow().setExitTransition(enterTransition);     // cuadno la actividad sale

                    // no superponer la animaciones
                    activity.getWindow().setAllowEnterTransitionOverlap(false);

                }
                break;
            case DETAIL_ACTIVITY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide slideTransition = new Slide();

                    slideTransition.setSlideEdge(Gravity.RIGHT);
                    slideTransition.setDuration(activity.getResources().getInteger(R.integer.anim_duration_medium));

                    activity.getWindow().setEnterTransition(slideTransition);     // cuando la actividad entra
                    activity.getWindow().setExitTransition(slideTransition);      // cuadno la actividad sale

                    // no superponer la animaciones
                    activity.getWindow().setAllowReturnTransitionOverlap(false);
                }
                break;
        }
    }

    /**
     * Define una animación de escalado para algunas imagenes de las vistas
     * @param viewToAnimate view que se desea animar.
     */
    public static void setAnimation(View viewToAnimate) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(501));// duracion aleatoria entre 0 y 501
            viewToAnimate.startAnimation(anim);
    }
}
