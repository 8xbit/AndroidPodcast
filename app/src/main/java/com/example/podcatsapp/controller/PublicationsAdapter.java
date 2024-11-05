package com.example.podcatsapp.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.podcatsapp.R;
import com.example.podcatsapp.model.Publication;
import com.example.podcatsapp.view.PlayerActivity;

import java.util.List;

public class PublicationsAdapter extends RecyclerView.Adapter<PublicationsAdapter.ViewHolder> {
    private List<Publication> mPublications;
    private Context context;

    public PublicationsAdapter(Context context, List<Publication> publications) {
        this.context = context;
        this.mPublications = publications;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pubTitle;
        public TextView pubDescription;
        public ImageView pubImage;

        public ViewHolder(View itemView) {
            super(itemView);
            pubTitle = itemView.findViewById(R.id.item_tv_title);
            pubDescription = itemView.findViewById(R.id.item_tv_description);
            pubImage = itemView.findViewById(R.id.item_iv_image);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View pubView = inflater.inflate(R.layout.item_pub, parent, false);
        return new ViewHolder(pubView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Publication publication = mPublications.get(position);

        holder.pubTitle.setText(publication.getTitle());
        holder.pubDescription.setText(publication.getDescription());

        // Al hacer clic en la imagen de la publicación
        holder.pubImage.setOnClickListener(v -> {
            // Crea el Intent para abrir PlayerActivity
            Intent intent = new Intent(context, PlayerActivity.class);
            // Pasa la publicación completa como parámetro (usando Serializable)
            intent.putExtra("publication", publication);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mPublications.size();
    }
}
