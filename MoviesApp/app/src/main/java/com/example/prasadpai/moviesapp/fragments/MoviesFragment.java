package com.example.prasadpai.moviesapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.prasadpai.moviesapp.R;
import com.example.prasadpai.moviesapp.activities.SettingsActivity;
import com.example.prasadpai.moviesapp.adapters.FilmsAdapter;
import com.example.prasadpai.moviesapp.models.Film;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by prasadpai on 25/02/16.
 */
public class MoviesFragment extends Fragment {


    public static final String TAG = "MoviesFragment";
    private static String MOVIES_API_KEY = "2712ea4b7e211da7acc201b40a38e71c";

    private String sort_by;

    //The views binded with library Butterknife
    @InjectView(R.id.filmsGridView)GridView filmsGridView;

    private FilmsAdapter filmsAdapter;
    private List<Film> films;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sort_by = sharedPreferences.getString(getString(R.string.pref_sort_key), getString(R.string.sort_by_label_popularity));
        updateMovies();
    }


    private void updateMovies() {

        FetchMoviesTask weatherTask = new FetchMoviesTask();
        weatherTask.execute(sort_by);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.inject(this, rootView);




        return rootView;
    }


    public class FetchMoviesTask extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            populateGridView();
        }

        @Override
        protected Void doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJSONStr = null;

            try {

                final String FORECAST_BASE_URL =
                        "https://api.themoviedb.org/3/movie/upcoming?";

                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, MOVIES_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                movieJSONStr = buffer.toString();
                getFilmsFrom(movieJSONStr);
                return null;

            } catch (IOException e) {
                Log.e("MoviesFragment", "Error ", e);
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("MoviesFragment", "Error closing stream", e);
                    }
                }
            }

        }

    }


    private void getFilmsFrom(String jsonData) {

        films = new ArrayList<>();

        JSONObject jsonResponse = null;

        try {
            jsonResponse = new JSONObject(jsonData);


            JSONArray results = jsonResponse.getJSONArray("results");

            for(int i=0; i<results.length(); i++)
            {
                Film film = new Film();
                film.setTitle(results.getJSONObject(i).getString("original_title"));
                film.setPosterPath(results.getJSONObject(i).getString("poster_path"));
                film.setOverview(results.getJSONObject(i).getString("overview"));
                film.setVote(results.getJSONObject(i).getLong("vote_average"));
                film.setPopularity(results.getJSONObject(i).getLong("popularity"));
                film.setRelease_date(results.getJSONObject(i).getString("release_date"));
                films.add(film);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateGridView() {


        if(sort_by.compareTo(getString(R.string.sort_by_label_popularity))==0)
        {

            Collections.sort(films, new Comparator<Film>() {
                public int compare(Film emp1, Film emp2) {
                    if (emp1.getPopularity() > emp2.getPopularity()) {
                        return -1;
                    }
                    if (emp1.getPopularity() < emp2.getPopularity()) {
                        return 1;
                    }
                    return 0;
                }
            });

        }


        else {

            Collections.sort(films, new Comparator<Film>() {
                public int compare(Film emp1, Film emp2) {
                    if (emp1.getVote() > emp2.getVote()) {
                        return -1;
                    }
                    if (emp1.getVote() < emp2.getVote()) {
                        return 1;
                    }
                    return 0;
                }
            });
        }


        filmsAdapter = new FilmsAdapter(getActivity(), films);
        filmsGridView.setAdapter(filmsAdapter);

    }

}
