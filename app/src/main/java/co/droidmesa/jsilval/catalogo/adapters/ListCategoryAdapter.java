package co.droidmesa.jsilval.catalogo.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import co.droidmesa.jsilval.catalogo.Categorias;
import co.droidmesa.jsilval.catalogo.R;

/**
 * Created by jsilval on 29/01/17.
 */

public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.mViewHolder> implements View.OnClickListener{
    private ArrayList<String> listItems;
    private View.OnClickListener listener;

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
        holder.bindMessage(item, holder);
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

        public void bindMessage(String item, mViewHolder holder) {
            tvCategory.setText(item);
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            GradientDrawable bgShape = (GradientDrawable) holder.circle.getBackground();
            bgShape.setColor(color);
        }
    }
}
