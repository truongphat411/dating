package com.application.dating.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.application.dating.R;
import com.application.dating.model.File_Image_Male;
import com.application.dating.model.Profile;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private List<Profile> items;
    Context context;
    public CardStackAdapter(Context context,List<Profile> items) {
        this.items = items;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // holder.setData(items.get(position));
        holder.name.setText(items.get(position).getTen());
        holder.age.setText(String.valueOf(items.get(position).getTuoi()));
        String path = "http://192.168.1.8:8086";
        Glide.with(context).load(path+items.get(position).getDuongdan()).placeholder(R.drawable.monkey).into(holder.image);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, age;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            name = itemView.findViewById(R.id.item_name);
            age = itemView.findViewById(R.id.item_age);
        }
    }
    public List<Profile> getItems() {
        return items;
    }
    public void setItems(List<Profile> items) {
        this.items = items;
    }
}
