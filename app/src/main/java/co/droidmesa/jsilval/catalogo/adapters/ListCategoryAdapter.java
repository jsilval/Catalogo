package co.droidmesa.jsilval.catalogo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import co.droidmesa.jsilval.catalogo.Categorias;
import co.droidmesa.jsilval.catalogo.R;

/**
 * Created by jsilval on 29/01/17.
 * Adaptador personalizado para la lista de categorias disponibles
 */
public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.mViewHolder> implements View.OnClickListener{
    private ArrayList<String> listItems;
    private View.OnClickListener listener;
    private int lastPosition = -1;

    public ListCategoryAdapter(ArrayList<String> listItems) {
        this.listItems = listItems;
    }

    @Override
    public ListCategoryAdapter.mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (Categorias.portrait)
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category_tablet, parent, false);

        itemView.setOnClickListener(this);

        return new mViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListCategoryAdapter.mViewHolder holder, int position) {
        String item = listItems.get(position);
        holder.bindCategory(item, holder);
        setAnimation(holder.tvCategory, position);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onClick(v);
    }

    public class mViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategory;
        private View circle;

        public mViewHolder(View itemView) {
            super(itemView);

            tvCategory = (TextView)itemView.findViewById(R.id.tvCategory);
            circle = itemView.findViewById(R.id.imgColor);
        }

        public void bindCategory(String item, mViewHolder holder) {
            tvCategory.setText(item);
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            GradientDrawable bgShape = (GradientDrawable) holder.circle.getBackground();
            bgShape.setColor(color);
        }
    }

    /**
     * Animacion para los item de la lista, se animan si no han sido mostrados anteriormente
     * @param viewToAnimate
     * @param position
     */
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation((Context) listener, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
