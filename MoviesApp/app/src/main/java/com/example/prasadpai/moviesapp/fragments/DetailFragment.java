package com.example.prasadpai.moviesapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prasadpai.moviesapp.R;
import com.example.prasadpai.moviesapp.adapters.TrailerAdapter;
import com.example.prasadpai.moviesapp.commands.GetTrailersCommand;
import com.example.prasadpai.moviesapp.models.Film;
import com.example.prasadpai.moviesapp.models.Trailer;
import com.example.prasadpai.moviesapp.network.AsyncCommand;
import com.example.prasadpai.moviesapp.network.CommandExecutionError;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by prasadpai on 26/02/16.
 */
public class DetailFragment extends Fragment {

    @InjectView(R.id.movie_name)
    TextView movie_name;

    @InjectView(R.id.vote)
    TextView vote;

    @InjectView(R.id.overview)
    TextView overview;

    @InjectView(R.id.popularity)
    TextView popularity;

    @InjectView(R.id.releasedate)
    TextView releasedate;

    @InjectView(R.id.poster)
    ImageView poster;

    @InjectView(R.id.trailerList)
    ListView trailerList;

    private Film movie;
    private TrailerAdapter trailerAdapter;
    private List<Trailer> trailers;

    public DetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detailed, container, false);

        // The detail Activity called via intent.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            movie = (Film)intent.getSerializableExtra(Intent.EXTRA_TEXT);

            ButterKnife.inject(this, rootView);
            movie_name.setText(movie.getTitle());
            overview.setText(movie.getOverview());
            vote.setText(String.valueOf(movie.getVote_average()));
            popularity.setText(String.valueOf(movie.getPopularity()));
            releasedate.setText(movie.getRelease_date());

            Glide.with(getContext()).load("https://image.tmdb.org/t/p/w185" + movie.getPoster_path()).into(poster);
            updateTrailer();

        }

        return rootView;
    }


    private void updateTrailer() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Please Wait", "Fetching trailers");
        GetTrailersCommand getTrailersCommand = new GetTrailersCommand(movie.getId());
        getTrailersCommand.execute(null, new AsyncCommand.Callback() {
            @Override
            public <T> void success(int statusCode, T result) {
                trailers = (ArrayList<Trailer>) result;
                progressDialog.dismiss();
                populateGridView();
            }

            @Override
            public void failure(int statusCode, String message, CommandExecutionError error) {

                progressDialog.dismiss();
                Toast.makeText(getContext(), "Server call failed", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void populateGridView() {

        trailerAdapter = new TrailerAdapter(getActivity(), trailers);
        trailerList.setAdapter(trailerAdapter);

    }
}
