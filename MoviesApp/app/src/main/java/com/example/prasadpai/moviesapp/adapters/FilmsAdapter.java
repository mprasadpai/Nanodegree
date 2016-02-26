package com.example.prasadpai.moviesapp.adapters;

/**
 * Created by prasadpai on 25/02/16.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.prasadpai.moviesapp.activities.DetailedActivity;
import com.example.prasadpai.moviesapp.models.Film;
import com.example.prasadpai.moviesapp.R;


import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class FilmsAdapter extends BaseAdapter {

    private List<Film> films;
    public Context context;

    public FilmsAdapter(Context ctx, List<Film> filmsArray) {
        context = ctx;
        films = filmsArray;
    }


    @Override
    public int getCount() {
        return films.size();
    }

    @Override
    public Object getItem(int position) {
        return films.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = null;

        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.film_list_item, null);
        }

        ViewHolder holder;
        holder = new ViewHolder(view);

        final Film film = films.get(position);

        Glide.with(context).load("https://image.tmdb.org/t/p/w185" + film.getPosterPath()).into(holder.posterimage);

        holder.posterimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDetailedActivity(film);
            }
        });


        return view;
    }


    public static class ViewHolder {

        @InjectView(R.id.filmPosterImage)
        ImageView posterimage;
        public ViewHolder(View view){
            ButterKnife.inject(this, view);
        }

    }


    private void callDetailedActivity(Film data)
    {
        Intent intent = new Intent(context, DetailedActivity.class).putExtra(Intent.EXTRA_TEXT, data);
        context.startActivity(intent);
    }



}
