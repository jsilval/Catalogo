package co.droidmesa.jsilval.catalogo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import co.droidmesa.jsilval.catalogo.R;
import co.droidmesa.jsilval.catalogo.models.Entry;
import co.droidmesa.jsilval.catalogo.utils.SetUpActivity;

/**
 * Created by jsilval on 29/01/17.
 * Adaptador personalizado para la lista de aplicaciones disponibles
 */
public class ListAppAdapter extends RecyclerView.Adapter<ListAppAdapter.mViewHolder> implements View.OnClickListener{
    private ArrayList<Entry> listItems;
    private View.OnClickListener listener;
    private Context context;

    public ListAppAdapter(ArrayList<Entry> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ListAppAdapter.mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_app, parent, false);
        itemView.setOnClickListener(this);

        return new ListAppAdapter.mViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListAppAdapter.mViewHolder holder, int position) {
        Entry item = listItems.get(position);
        holder.bindApp(item);
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
        private TextView tvTitle;
        private TextView tvPrice;
        private ImageView imgApp;
        private ProgressBar pbImgLoading;

        public mViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            imgApp = (ImageView) itemView.findViewById(R.id.imgApp);
            pbImgLoading = (ProgressBar) itemView.findViewById(R.id.pbImgLoading);
        }

        public void bindApp(final Entry item) {
            tvTitle.setText(item.getImName().getLabel());
            tvPrice.setText("Price: " + item.getImPrice().getAttributes().getAmount() + " " + item.getImPrice().getAttributes().getCurrency());
            Picasso.with(context).load(item.getImImage().get(0).getLabel()).networkPolicy(NetworkPolicy.OFFLINE).into(imgApp, new Callback() {
                @Override
                public void onSuccess() {
                    pbImgLoading.setVisibility(View.GONE);
                    SetUpActivity.setAnimation(imgApp);
                }

                @Override
                public void onError() {
                    Picasso.with(context).load(item.getImImage().get(0).getLabel()).into(imgApp, new Callback() {
                        @Override
                        public void onSuccess() {
                            pbImgLoading.setVisibility(View.GONE);
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
