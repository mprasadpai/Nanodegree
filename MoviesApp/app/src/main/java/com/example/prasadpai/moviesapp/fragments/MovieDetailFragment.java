package com.example.prasadpai.moviesapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prasadpai.moviesapp.R;
import com.example.prasadpai.moviesapp.activities.MovieDetailActivity;
import com.example.prasadpai.moviesapp.activities.MovieListActivity;
import com.example.prasadpai.moviesapp.adapters.ReviewAdapter;
import com.example.prasadpai.moviesapp.adapters.TrailerAdapter;
import com.example.prasadpai.moviesapp.commands.GetReviewsCommand;
import com.example.prasadpai.moviesapp.commands.GetTrailersCommand;
import com.example.prasadpai.moviesapp.models.Movie;
import com.example.prasadpai.moviesapp.models.Reviews;
import com.example.prasadpai.moviesapp.models.Trailer;
import com.example.prasadpai.moviesapp.network.AsyncCommand;
import com.example.prasadpai.moviesapp.network.CommandExecutionError;
import com.example.prasadpai.moviesapp.contentprovider.MovieDataSource;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MovieDetailFragment extends Fragment {

    private MovieDataSource movieDataSource;

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

    @InjectView(R.id.reviewList)
    ListView reviewList;

    @InjectView(R.id.favIcon)
    ImageView favImage;

    private Movie movie;
    private TrailerAdapter trailerAdapter;
    private List<Reviews> reviews;
    private ReviewAdapter reviewAdapter;
    private List<Trailer> trailers;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesdetailfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share) {
            shraeTrailer(item);
            return true;
        }
        if (id == android.R.id.home) {
            getActivity().navigateUpTo(new Intent(getActivity(), MovieListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shraeTrailer(MenuItem menuItem)
    {

        if(trailers!=null && trailers.size()>0) {

            ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);;
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            String FILE_PATH = TrailerAdapter.YOUTUBE_URL_PREFIX + trailers.get(0).getKey();
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    FILE_PATH);
            if (shareActionProvider != null ) {
                shareActionProvider.setShareIntent(shareIntent);
            }
        }

    }


    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(Intent.EXTRA_TEXT)) {

            movieDataSource = new MovieDataSource(getContext());
            movieDataSource.open();
            movie = (Movie) getArguments().getSerializable(Intent.EXTRA_TEXT);
            checkIfMovieisFav();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detailed, container, false);


        ButterKnife.inject(this, rootView);

        movie_name.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        vote.setText(String.valueOf(movie.getVote_average()));
        popularity.setText(String.valueOf(movie.getPopularity()));
        releasedate.setText(movie.getRelease_date());

        if (movie.isFav()) {
            favImage.setImageResource(android.R.drawable.star_on);
        } else {
            favImage.setImageResource(android.R.drawable.star_off);
        }


        favImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (movie.isFav()) {
                    movieDataSource.deleteFilm(getContext(), movie.getId());
                    favImage.setImageResource(android.R.drawable.star_off);


                } else {

                    favImage.setImageResource(android.R.drawable.star_on);
                    movieDataSource.insertFilm(getContext(), movie.getId(), movie.getTitle(), movie.getPoster_path(), movie.getVote_average(), movie.getRelease_date(), movie.getPopularity(), movie.getOverview());
                }


                movie.setIsFav(!movie.isFav());
            }
        });

        Glide.with(getContext()).load("https://image.tmdb.org/t/p/w185" + movie.getPoster_path()).into(poster);
        updateTrailer();
        updateReviews();


        return rootView;
    }

    private void checkIfMovieisFav() {
        List<Movie> favList = movieDataSource.getAllFilms(getContext());
        for(int i=0; i<favList.size(); i++)
        {
            if(favList.get(i).getId() == movie.getId())
            {
                movie.setIsFav(true);
                break;
            }
        }
    }


    private void updateTrailer() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Please Wait", "Fetching trailers");
        GetTrailersCommand getTrailersCommand = new GetTrailersCommand(movie.getId());
        getTrailersCommand.execute(null, new AsyncCommand.Callback() {
            @Override
            public <T> void success(int statusCode, T result) {
                trailers = (ArrayList<Trailer>) result;
                progressDialog.dismiss();
                populateTrailersView();
            }

            @Override
            public void failure(int statusCode, String message, CommandExecutionError error) {

                progressDialog.dismiss();
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

            }
        });

    }


    private void updateReviews() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Please Wait", "Fetching reviews");
        GetReviewsCommand getReviewsCommand = new GetReviewsCommand(movie.getId());
        getReviewsCommand.execute(null, new AsyncCommand.Callback() {
            @Override
            public <T> void success(int statusCode, T result) {
                reviews = (ArrayList<Reviews>) result;
                progressDialog.dismiss();
                populateReviewsView();
            }

            @Override
            public void failure(int statusCode, String message, CommandExecutionError error) {

                progressDialog.dismiss();
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

            }
        });

    }

    private void populateTrailersView() {
        trailerAdapter = new TrailerAdapter(getActivity(), trailers);
        trailerList.setAdapter(trailerAdapter);
    }

    private void populateReviewsView() {
        reviewAdapter = new ReviewAdapter(getActivity(), reviews);
        reviewList.setAdapter(reviewAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        movieDataSource.close();
    }



}
