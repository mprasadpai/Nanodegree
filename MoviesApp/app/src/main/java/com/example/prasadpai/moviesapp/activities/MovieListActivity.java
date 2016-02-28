package com.example.prasadpai.moviesapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prasadpai.moviesapp.R;

import com.example.prasadpai.moviesapp.adapters.FilmsAdapter;
import com.example.prasadpai.moviesapp.commands.GetMoviesCommand;
import com.example.prasadpai.moviesapp.fragments.MovieDetailFragment;
import com.example.prasadpai.moviesapp.models.Movie;
import com.example.prasadpai.moviesapp.network.AsyncCommand;
import com.example.prasadpai.moviesapp.network.CommandExecutionError;
import com.example.prasadpai.moviesapp.contentprovider.MovieDataSource;

import java.util.ArrayList;
import java.util.List;


public class MovieListActivity extends AppCompatActivity {


    private boolean mTwoPane;
    static private List<Movie> movies;
    private static String now_sorted_by = null;
    private String sort_by;
    private View recyclerView;
    private GridView filmsGridView;
    private FilmsAdapter filmsAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movieslistactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            fillMovieList();
            return true;
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        recyclerView = findViewById(R.id.item_list);
        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }else {

            filmsGridView = (GridView) findViewById(R.id.filmsGridView);
            mTwoPane = false;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(!isNetworkAvailable())
        {
            Toast.makeText(MovieListActivity.this, R.string.no_netwrok_avialbale,Toast.LENGTH_SHORT).show();
            sort_by = "favourite";
            fillMovieList();
        }
        else {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sort_by = sharedPreferences.getString(getString(R.string.pref_sort_key), "popularity.desc");

            if (now_sorted_by == null || sort_by.compareTo(now_sorted_by) != 0) {
                fillMovieList();
            } else {

                if(movies !=null && movies.size()>0) {
                    checkForTablet();
                }else
                {
                    fillMovieList();
                }

            }
        }

    }


    private void fillMovieList()
    {

        switch(sort_by)
        {
            case "popularity.desc":
                now_sorted_by = "popularity.desc";
                updateMovies();
                break;
            case "vote_average.desc":
                now_sorted_by ="vote_average.desc";
                updateMovies();
                break;
            case "favourite":
                now_sorted_by = "favourite";
                MovieDataSource movieDataSource =  new MovieDataSource(MovieListActivity.this);
                movieDataSource.open();
                movies = movieDataSource.getAllFilms(getBaseContext());
                movieDataSource.close();

                if(movies !=null && movies.size()>0) {
                    checkForTablet();
                }else
                {
                    Toast.makeText(MovieListActivity.this, "No favourites, change settings", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }


    private void checkForTablet()
    {
        if(mTwoPane)
        {
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView);

        }else {
            filmsAdapter = new FilmsAdapter(this, movies);
            filmsGridView.setAdapter(filmsAdapter);
        }
    }

    private void updateMovies() {

        movies =new ArrayList<>();

        final ProgressDialog progressDialog = ProgressDialog.show(this, "Please Wait", "Fetching Movies");
        GetMoviesCommand getMoviesCommand = new GetMoviesCommand(sort_by);
        getMoviesCommand.execute(null, new AsyncCommand.Callback() {
            @Override
            public <T> void success(int statusCode, T result) {
                movies = (ArrayList<Movie>) result;
                progressDialog.dismiss();
                checkForTablet();
            }

            @Override
            public void failure(int statusCode, String message, CommandExecutionError error) {
                progressDialog.dismiss();
                Toast.makeText(MovieListActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(movies));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Movie> mValues;

        public SimpleItemRecyclerViewAdapter(List<Movie> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mContentView.setText(mValues.get(position).getTitle());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putSerializable(Intent.EXTRA_TEXT, holder.mItem);
                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(Intent.EXTRA_TEXT, holder.mItem);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mContentView;
            public Movie mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }
}
