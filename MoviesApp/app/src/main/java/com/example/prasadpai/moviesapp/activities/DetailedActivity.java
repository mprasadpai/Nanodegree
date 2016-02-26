package com.example.prasadpai.moviesapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.prasadpai.moviesapp.R;
import com.example.prasadpai.moviesapp.models.Film;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailedActivity extends ActionBarActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.app_name);

        ab.show();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, FilmsActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    public static class DetailFragment extends Fragment {

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

        private Film movie;

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
                vote.setText(String.valueOf(movie.getVote()));
                popularity.setText(String.valueOf(movie.getPopularity()));
                releasedate.setText(movie.getRelease_date());

                Glide.with(getContext()).load("https://image.tmdb.org/t/p/w185" + movie.getPosterPath()).into(poster);


            }

            return rootView;
        }
    }
}
