package co.droidmesa.jsilval.catalogo.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import co.droidmesa.jsilval.catalogo.R;

/**
 * Created by jsilval on 1/02/17.
 */

public class RecyclerDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public RecyclerDecoration(Context context) {
        margin = context.getResources().getDimensionPixelSize(R.dimen.item_margin);
    }

    @Override
    public void getItemOffsets(
            Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(margin, margin, margin, margin);
    }
}
