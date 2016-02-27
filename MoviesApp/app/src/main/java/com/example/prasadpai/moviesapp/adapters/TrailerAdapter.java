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
import com.example.prasadpai.moviesapp.models.Trailer;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class TrailerAdapter extends BaseAdapter {

    private List<Trailer> trailers;
    public Context context;

    public TrailerAdapter(Context ctx, List<Trailer> trailers) {
        context = ctx;
        this.trailers = trailers;
    }


    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public Object getItem(int position) {
        return trailers.get(position);
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
            view = inflater.inflate(R.layout.trailer_list_item, null);
        }

        ViewHolder holder;
        holder = new ViewHolder(view);

        final Trailer trailer = trailers.get(position);

        holder.trailerTitle.setText(trailer.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callVideoPlayer(trailer.getKey());
            }
        });


        return view;
    }


    public static class ViewHolder {

        @InjectView(R.id.trailerTitle)
        TextView trailerTitle;
        public ViewHolder(View view){
            ButterKnife.inject(this, view);
        }

    }


    private void callVideoPlayer(String key)
    {
        String FILE_PATH = "https://www.youtube.com/watch?v=" + key;
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FILE_PATH)));

    }



}
