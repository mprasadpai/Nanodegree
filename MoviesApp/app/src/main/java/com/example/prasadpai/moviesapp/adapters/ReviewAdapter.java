package com.example.prasadpai.moviesapp.adapters;

/**
 * Created by prasadpai on 25/02/16.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.prasadpai.moviesapp.R;
import com.example.prasadpai.moviesapp.models.Reviews;
import com.example.prasadpai.moviesapp.models.Trailer;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ReviewAdapter extends BaseAdapter {

    private List<Reviews> reviews;
    public Context context;

    public ReviewAdapter(Context ctx, List<Reviews> reviews) {
        context = ctx;
        this.reviews = reviews;
    }


    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
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
            view = inflater.inflate(R.layout.review_list_item, null);
        }

        ViewHolder holder;
        holder = new ViewHolder(view);

        final Reviews trailer = reviews.get(position);

        holder.reviewDescription.setText(trailer.getContent());
        holder.reviewAuthor.setText("Reviewed By: " + trailer.getAuthor());


        return view;
    }


    public static class ViewHolder {

        @InjectView(R.id.reviewDescription)
        TextView reviewDescription;

        @InjectView(R.id.reviewAuthor)
        TextView reviewAuthor;

        public ViewHolder(View view){
            ButterKnife.inject(this, view);
        }

    }



}
